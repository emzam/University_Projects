#include "fellesfunksjoner.h"
// ****************************************************************************





/* Denne funksjonen sjekker om brukeren har tastet inn riktig i terminal.
 * Den tar imot en int, og en char peker slik som i main funksjonen.
 *
 * Input:
 *      argc:   antallet av argumenter brukeren har skrevet inn i terminal
 *      argv[]: array av argumenter du kjoerer
 *
 * Return:
 * Hvis brukeren skriver for faa argumenter, eller for mange, returneres 1.
 * Printes da til bruker om hvordan terminalen skal brukes.
 * Hvis brukeren skriver riktig, returneres 0. Samtidig sjekkes om bruker
 * vil starte i debug mode eller ikke (valgfritt; enten skrive debug etter port
 * for aa starte client i debugmodus, eller skrive ingenting etter port for aa
 * starte client uten debugmodus).
 */
int usage(int argc, char* argv[]) {
    if(argc < 3 || argc > 4) {
        printf("BRUK: %s [fil] [port] (\"debug\")\n", argv[0]);
        return 1;
    }
    if(argc == 4) {
        if(strcmp(argv[3], "debug") == 0) {
            printf(">>> %d <<< SERVEREN STARTER I DEBUG MODUS\n", getpid());
            printf(">>> %d <<< KOBLET TIL PORT: %s\n", getpid(), argv[2]);
            debugMode = 1;
        }
        else {
            debugMode = 0;
        }
    }
    return 0;
}

// ****************************************************************************





/* Denne funksjonen blir kalt paa av clientConnection(). Her lages job structen
 * som skal sendes til client. Det leses fra filen, og jobbtybe, checksum,
 * jobbinfo og teksten blir lagret i structen. Her sjekkes det for om filen er
 * korrupt eller om det er end of file.
 *
 * Input:
 *      FILE *file: filen det leses fra, som blir videresendt fra tidligere
                    funksjoner
 *
 * Return:
 * Denne funksjonen returnerer en job struct, eller NULL. Hvis alt gaar bra,
 * returneres structen. Hvis NULL returneres, betyr det at en eller annen feil
 * har hendt.
 */
job *readFile(FILE *file) {
    debugPrint("Leser jobbtype fra fil");
    char jobtype = (unsigned char) fgetc(file);
    if(jobtype != 'E' && jobtype != 'O' && jobtype != EOF) {
        printf("\nKorrupt fil.\n");
        return NULL;
    }

    if(jobtype == EOF) {
        return NULL;
    }

    unsigned int length;

    debugPrint("Leser length fra fil");
    ret = fread(&length, sizeof(int), 1, file);
    if(ret == 0) {
        perror("read");
        return NULL;
    }

    char text[length + 1];
    debugPrint("Leser teksten fra fil");
    ret = fread(text, sizeof(char), length, file);
    if(ret == 0) {
        perror("read");
        return NULL;
    }
    text[length] = '\0';

    unsigned int i;
    unsigned long sum = 0;
    for(i = 0; i < length; i++) {
        sum += (int)text[i];
    }

    int checksum = sum % 32;
    unsigned char jobinfo = 0;
    if(jobtype == 'O') {
        jobinfo = 0;
    }
    else if(jobtype == 'E') {
        jobinfo = 1 << 5;
    }
    else if(jobtype == 'Q') {
        jobinfo = 7 << 5;
    }

    jobinfo += checksum;

    return createJob(jobinfo, length, text);
}

// ****************************************************************************





/* Denne funksjonen lager en "tom" jobb med jobbtype lik Q. Denne structen
 * lages hvis det enten ikke er flere jobber igjen, eller man er paa slutten
 * av filen, eller om filen er korrupt. Dette gir beskjed til client om at
 * den skal avslutte.
 *
 * Return:
 *      job struct
 */
job *createEmptyJob() {
    job *j = malloc(sizeof(job));
    j->jobinfo = 224;
    j->length = 0;

    return j;
}

// ****************************************************************************





/* Denne funksjonen tar hensyn til kommunikasjonen fra server til client,
 * og blir tilkalt fra startServer(). Den tar inn tre parametere forklart under.
 * Reaksjonen paa denne funksjonen er avhengig av hva brukeren taster inn fra
 * clienten. Hele denne funksjonen er én stor if-else, som
 * gir sender jobber til client, igjen avhengig av om brukeren fra client
 * ville ha én jobb, X antall jobber, alle jobber eller om brukeren velger
 * avslutt. I hvert tilfelle gis det et kall til readFile(). Ut fra
 * structen til det dette kallet returnerer blir det sjekket om det er flere
 * jobber igjen. Feilsjekking skjer som foelge av det, og funksjonen
 * vil gi beskjed til server om evt. ingen flere jobber, eller korrupt fil osv.
 * Ellers er det beskrevet i protokoll.txt hvordan jeg har brukt de fire
 * bytene vi har til disposisjon (fra client til server kommunikasjon).
 *
 * Input:
 *      int sock:   socketveriden som blir sendt med fra tidligere funksjoner
 *      FILE *file: filen som blir sendt med fra tidligere funksjoner
 *      int buffer: verdien fra brukerinput sendt fra client. Hentet fra
                    startServer()
 *
 * Return:
 * Funksjonen returnerer en int verdi. Enten -1, 0 eller 1 avhengig av om
 * serveren skal fortsette, eller avslutte. Hvis funksjonen returnerer -1,
 * betyr det at clienten terminerer pga. en feil. Hvis 0 returneres, betyr det
 * at clienten terminerer riktig. Hvis 1 returneres, fortsetter programmet.
 */
int clientConnection(int sock, FILE *file, int buffer) {
    if(interrupt_received) {
        debugPrint("CTRL-C brukt");
        unsigned char q = 230;
        debugPrint("Writer til client om at server avslutter");
        write(sock, &q, sizeof(char));
        return -1;
    }

    if(buffer == 65001) {
        printf("Clienten vil hente én jobb.\n");
        job *j = readFile(file);

        if(j == NULL) {
            job *k = createEmptyJob();
            printf("Ferdig.\n");
            debugPrint("Writer til client om at server er ferdig");
            write(sock, k, sizeof(job));
            free(k);
        }
        else {
            printf("Sender...\n");
            j->length = htonl(j->length);
            debugPrint("Writer en jobb til client");
            write(sock, j, sizeof(job) + ntohl(j->length) + 1);
            free(j);
            printf("Sendt.\n");
        }
    }
    else if(buffer > 1 && buffer <= 65000) {
        printf("Clienten vil hente %d jobber.\n", buffer);
        printf("Sender...\n");

        int i;
        for(i = 0; i < buffer; i++) {
            if(interrupt_received) {
                debugPrint("CTRL-C brukt");
                unsigned char q = 230;
                debugPrint("Writer til client om at server avslutter");
                write(sock, &q, sizeof(char));
                return -1;
            }

            job *j = readFile(file);
            if(j == NULL) {
                job *k = createEmptyJob();
                printf("Ferdig.\n");
                debugPrint("Writer til client om at server er ferdig");
                write(sock, k, sizeof(job));
                free(k);
                break;
            }
            j->length = htonl(j->length);
            debugPrint("Writer en jobb til client");
            write(sock, j, sizeof(job) + ntohl(j->length) + 1);
            free(j);
            printf("Sendt.\n");
        }
    }
    else if(buffer == 65002) {
        printf("Clienten vil hente alle jobber.\n");
        printf("Sender...\n");

        while(1) {
            if(interrupt_received) {
                debugPrint("CTRL-C brukt");
                unsigned char q = 230;
                debugPrint("Writer til client om at server avslutter");
                write(sock, &q, sizeof(char));
                return -1;
            }

            job *j = readFile(file);
            if(j == NULL) {
                job *k = createEmptyJob();
                printf("Ferdig.\n");
                debugPrint("Writer til client om at server er ferdig");
                write(sock, k, sizeof(job));
                free(k);
                break;
            }
            j->length = htonl(j->length);
            debugPrint("Writer en jobb til client");
            write(sock, j, sizeof(job) + ntohl(j->length) + 1);
            free(j);
            printf("Sendt.\n");
        }
    }
    else if(buffer == 65003) {
        printf("Clienten terminerer normalt.\n");
        printf("Serveren avslutter...\n");
        printf("Avsluttet.\n");
        return 0;
    }
    else if (buffer == 65004) {
        printf("Clienten terminerer paa grunn av en feil.\n");
        printf("Serveren avslutter...\n");
        printf("Avsluttet.\n");
        return -1;
    }
    else {
        printf("* NOE GIKK GALT! *\n");
        return -1;
    }

    return 1;
}
// ****************************************************************************





/* Denne funksjonen starter hele serveren. Altsaa det er her alt initialiserers
 * og lages, som bl.a socket, structer, samt hvordan de lukkes.
 * I tillegg feilsjekkes alt der det kan oppstaa feil.
 * Serveren startes ved hjelp av kall paa socket, bind og listen, og aksepterer
 * tilkobling fra klienten med accept. Jeg har tatt hensyn til at serveren kun
 * kan haandtere én client om gangen, og avvise tilkoblingsforsoek fra andre.
 * I tillegg har jeg en while-loekke neders som reader det clienten sender.
 * Verdien til denne readen blir videresendt til clientConnection(). Loekken
 * breakes ut i fra returverdien til clientConnection(). Det er ogsaa tastet
 * hensyn til om brukeren bruker CTRL-C.
 * I og med at variabelen (kommandoen som sendes til server for
 * forstaaelse av hvilken jobb serveren skal sende) er en int, har jeg valgt aa
 * bruke ntohs() for aa ta hensyn til eventuelle big-endian til little-endian
 * hendelser.
 *
 * Kilder: selvskrevet, med hjelp fra forelesninger og plenum,
 * samt medstudenter.
 *
 * Input:
 *      char* argv[]: array av argumentene brukeren skrev inn i terminal
 *      FILE* file:   filen som blir lest fra videresendt fra main
 *
 * Return:
 * Verdien er en int avhengig av om det som kjoeres er riktig eller feil.
 * Det returneres 0 tilbake til main hvis sjekkene mine finner en feil, og 1
 * hvis alt har gatt riktig.
 */
int startServer(char* argv[], FILE* file) {
    struct sockaddr_in serveraddr, clientaddr;
    socklen_t clientaddrlen = sizeof(clientaddr);
    int request_sock, sock;
    unsigned short port;
    ret = get_port(argv[2], &port);
    if(ret == 1) {
        perror("get_port");
        debugPrint("Closer fil");
        fclose(file);
        return 0;
    }

    debugPrint("Starter tilkobling");
    request_sock = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    if(ret == -1) {
        perror("socket");
        debugPrint("Closer request_sock");
        close(request_sock);
        debugPrint("Closer fil");
        fclose(file);
        return 0;
    }

    memset(&serveraddr, 0, sizeof(serveraddr));
    serveraddr.sin_family = AF_INET;
    serveraddr.sin_addr.s_addr = INADDR_ANY;
    serveraddr.sin_port = htons(port);

    int y = 1;
    ret = setsockopt(request_sock, SOL_SOCKET, SO_REUSEADDR, &y, sizeof(int));
    if(ret == -1) {
        perror("setsockopt");
        debugPrint("Closer request_sock");
        close(request_sock);
        debugPrint("Closer fil");
        fclose(file);
        return 0;
    }

    ret = bind(request_sock, (struct sockaddr *)&serveraddr,
                              sizeof(serveraddr));
    if(ret == -1) {
        perror("bind");
        debugPrint("Closer request_sock");
        close(request_sock);
        debugPrint("Closer fil");
        fclose(file);
        return 0;
    }

    ret = listen(request_sock, 1);
    if(ret == -1) {
        perror("listen");
        debugPrint("Closer request_sock");
        close(request_sock);
        debugPrint("Closer fil");
        fclose(file);
        return 0;
    }

    printf("Server er paa.\n");
    printf("Venter paa klient...\n");

    sock = accept(request_sock, (struct sockaddr *)&clientaddr,
    &clientaddrlen);

    if(sock == -1) {
        if(errno == EINTR) {
            return -1;
        }

        perror("accept");
        debugPrint("Closer request_sock");
        close(request_sock);
        debugPrint("Closer fil");
        fclose(file);
        return 0;
    }
    else {
        debugPrint("Closer request_sock");
        close(request_sock);
        printf("Klient er koblet til.\n");
        printf("Venter paa en request...\n");
    }
    debugPrint("Tilkoblet");

    while(1) {
        int buffer;
        ret = read(sock, &buffer, sizeof(buffer));

        if(interrupt_received) {
            unsigned char q = 230;
            debugPrint("Writer til client om at server avslutter");
            write(sock, &q, sizeof(char));
            break;
        }
        if(ret == -1) {
            perror("read");
            unsigned char q = 230;
            debugPrint("Writer til client om at server avslutter");
            write(sock, &q, sizeof(char));
            return 0;
        }

        buffer = ntohs(buffer);
        ret = clientConnection(sock, file, buffer);

        if(ret == 0) {
            break;
        }
        if(ret == -1) {
            break;
        }
    }

    debugPrint("Closer fil");
    fclose(file);
    debugPrint("Closer socket");
    close(sock);
    return 1;
}
// ****************************************************************************





/* Dette er main-funksjonen. Sjekker usage, deklarerer CTRL-C og starter
 * startServer() funksjonen. Her aapnes ogsaa filen som det skal leses fra.
 *
 * Return:
 * Hvis programmet avsluttes riktig, returneres EXIT_SUCCESS, hvis ikke
 * returneres EXIT_FAILURE. Dette er avhengig av funksjonen startServer(),
 * eller om usage() er brukt riktig/feil.
 */
int main(int argc, char* argv[]) {
    interrupt_received = 0;

    if(usage(argc, argv)) {
        debugPrint("EXIT SUCCESS");
        return EXIT_SUCCESS;
    }

    FILE *file = fopen(argv[1], "rb");
    debugPrint("Aapner fil");
    if(file == NULL) {
        printf("Error naar fil skulle aapnes.\n");
        debugPrint("Closer fil");
        fclose(file);
        debugPrint("EXIT FAILURE");
        return EXIT_FAILURE;
    }

    struct sigaction sa;
    memset(&sa, 0, sizeof(struct sigaction));
    sa.sa_handler = &interrupt_handler;
    sigaction(SIGINT, &sa, NULL);

    ret = startServer(argv, file);
    if(ret == 0) {
        debugPrint("EXIT FAILURE");
        return EXIT_FAILURE;
    }
    else {
        debugPrint("EXIT SUCCESS");
        return EXIT_SUCCESS;
    }
}
// ****************************************************************************
