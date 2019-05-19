#include "fellesfunksjoner.h"
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/wait.h>

// Dette er brukt til aa printe ut enten roedt eller groent slik at man skal
// kunne se forskjellen mellom barneprosessene i terminalen.
#define NORMALFARGE "\x1b[0m"
#define ROEDFARGE "\x1b[31m"
#define GROENNFARGE "\x1b[32m"
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
        printf("BRUK: %s [server-adr] [port] (\"debug\")\n", argv[0]);
        return 1;
    }
    if(argc == 4) {
        if(strcmp(argv[3], "debug") == 0) {
            printf("\n>>> %d <<< CLIENTEN STARTER I DEBUG MODUS\n", getpid());
            printf("\n>>> %d <<< KOBLET TIL PORT: %s\n", getpid(), argv[2]);
            debugMode = 1;
        }
        else {
            debugMode = 0;
        }
    }
    return 0;
}

// ****************************************************************************





/*
 *
 */
void childrenProcess(int readFrom, FILE* stream, int barnetall) {
    while(1) {
        unsigned char jobinfo;
        unsigned int length;
        debugPrint("Barneprosess leser jobbinfo");
        ret = read(readFrom, &jobinfo, sizeof(char));
        if(ret == -1) {
            if(errno == EINTR) {
                break;
            }
            perror("read");
            break;
        }
        if(jobinfo == 224) {
            break;
        }

        debugPrint("Barneprosess leser tekstlengde");
        ret = read(readFrom, &length, sizeof(int));
        if(ret == -1) {
            perror("read");
            break;
        }
        char text[length + 1];

        debugPrint("Barneprosess leser tekst");
        ret = read(readFrom, text, length + 1);
        if(ret == -1) {
            perror("read");
            break;
        }

        if(barnetall == 1) {
            fprintf(stream, "%s%s%s\n", GROENNFARGE, text, NORMALFARGE);
        }
        else if(barnetall == 2) {
            fprintf(stream, "%s%s%s\n", ROEDFARGE, text, NORMALFARGE);
        }

        fflush(stdout);
    }
}

// ****************************************************************************





/* Denne funksjonen blir kalt paa av serverConnection(). Denne tar hensyn til
 * kommunikasjonen fra server til client. Her lages job structen som serveren
 * sender, og sender de videre til de respektive barneprosessene som skal
 * printe de ut ut ifra hvilken jobbtype det er. Her er det et par sjekker som
 * tar hensyn til hva serveren har sendt. Hvis jobbinfoen er 230, vil det si
 * at serveren er terminert med CTRL-C. Dette foerer til at barneprosessene
 * faar beskjed om aa terminere, samt at clienten generelt faar vite at
 * serveren er avsluttet. Naar da brukeren taster inn en ny kommando, vil
 * clienten ogsaa avslutte fordi serveren er avsluttet. En annen sjekk ser om
 * jobbtypen er 224 (Q). Hvis ja, saa skjer akkurat det samme som om serveren
 * har brukt CTRL-C. Altsaa at clienten terminerer og alle barneprosesser
 * terminerer. Ellers er det sjekker om read kraesjer. Her er det brukt
 * ntohl() for tekstlengde i tilfelle en hendelse hvor kommunikasjonen er
 * mellom big-endian og little-endian. Ellers, hvis alt gaar som det skal,
 * lages det en struct av jobben sendt fra server, og denne videresendes til
 * den barneprosessen som skal gjennomfoere utifra om jobbtypen er E eller O.
 *
 * Input:
 *      int sock:       verdien til socketen sendt fra tidligere funksjoner
 *      int fd_out[]:   verdien til foerste pipen sendt fra tidligere
                        funksjoner
 *      int fd_error[]: verdien til andre pipen sendt fra tidligere funksjoner
 *
 * Return:
 * Denne funksjonen returnerer en int, enten 0, 1 eller -1.  Hvis checksum ikke
 * stemmer, returneres -1 og de tidligere funksjonene reagerer ut fra det.
 * Hvis checksum ikke stemmer, hoppes jobben over.
 * Hvis serveren enten ikke har flere jobber, er korrupt, eller end of file,
 * returneres 1. Da avsluttes clienten riktig.
 * 0 returneres hvis alt gaar som det skal og programmet skal fortsette.
 */
int readFromServer(int sock, int fd_out[], int fd_error[]) {
    unsigned char jobinfo;
    unsigned int length;
    debugPrint("Reader jobbinfo fra server");
    ret = read(sock, &jobinfo, sizeof(char));
    if(ret == -1) {
        perror("read");
        int toServer = htons(65004);
        unsigned char q = 224;
        debugPrint("Writer til foerste barneprosess for aa avslutte");
        write(fd_out[1], &q, sizeof(char));
        debugPrint("Writer til andre barneprosess for aa avslutte");
        write(fd_error[1], &q, sizeof(char));
        debugPrint("Writer til server om at client avslutter");
        write(sock, &toServer, sizeof(int));
        return 1;
    }
    if(jobinfo == 230) {
        unsigned char q = 224;
        debugPrint("Writer til foerste barneprosess for aa avslutte");
        write(fd_out[1], &q, sizeof(char));
        debugPrint("Writer til andre barneprosess for aa avslutte");
        write(fd_error[1], &q, sizeof(char));
        sleep(1);
        printf("\n\nServeren er avsluttet.\n");
        return 1;
    }
    if(jobinfo == 224) {
        unsigned char q = 224;
        debugPrint("Writer til foerste barneprosess for aa avslutte");
        write(fd_out[1], &q, sizeof(char));
        debugPrint("Writer til andre barneprosess for aa avslutte");
        write(fd_error[1], &q, sizeof(char));
        sleep(1);
        printf("\n\nServer er ferdig.\n");
        return 1;
    }

    ret = read(sock, &length, sizeof(int));
    if(ret == -1) {
        perror("read");
        int toServer = htons(65004);
        unsigned char q = 224;
        debugPrint("Writer til foerste barneprosess for aa avslutte");
        write(fd_out[1], &q, sizeof(char));
        debugPrint("Writer til andre barneprosess for aa avslutte");
        write(fd_error[1], &q, sizeof(char));
        debugPrint("Writer til server om at client avslutter");
        write(sock, &toServer, sizeof(int));
        return 1;
    }

    length = ntohl(length);

    char text[length + 1];
    debugPrint("Reader teksten fra server");
    ret = read(sock, text, length + 1);
    if(ret == -1) {
        perror("read");
        int toServer = htons(65004);
        unsigned char q = 224;
        debugPrint("Writer til foerste barneprosess for aa avslutte");
        write(fd_out[1], &q, sizeof(char));
        debugPrint("Writer til andre barneprosess for aa avslutte");
        write(fd_error[1], &q, sizeof(char));
        debugPrint("Writer til server om at client avslutter");
        write(sock, &toServer, sizeof(int));
        return 1;
    }


    unsigned int i;
    unsigned long sum = 0;
    for(i = 0; i < length; i++) {
        sum += (int)text[i];
    }
    int checksum = sum % 32;

    job *j = createJob(jobinfo, length, text);

    if(checksum != (jobinfo & ((int)pow(2, 5) - 1))) {
        free(j);
        return -1;
    }
    else{
        if((j->jobinfo >= 32)) {
            debugPrint("Writer jobben til barneprosess");
            write(fd_out[1], j, sizeof(job) + j->length + 1);
            free(j);
        }
        else if((j->jobinfo < 32)) {
            debugPrint("Writer jobben til barneprosess");
            write(fd_error[1], j, sizeof(job) + j->length + 1);
            free(j);
        }
        return 0;
    }
}

// ****************************************************************************





/* Denne funksjonen tar hensyn til kommunikasjonen fra client til serveren,
 * og blir tilkalt fra mainmenu(). Den tar inn fire parametere forklart under.
 * Reaksjonen paa denne funksjonen er avhengig av hva brukeren taster inn i
 * mainmenu() funksjonen. Hele denne funksjonen er én stor if-else, som
 * gir beskjed til server om henting av jobber, igjen avhengig av om brukeren
 * ville ha én jobb, X antall jobber, alle jobber eller om brukeren velger
 * avslutt. I hvert tilfelle gis det et kall til readFromServer(). Ut fra
 * returverdien til dette kallet blir det sjekket om checksum stemmer og om det
 * er flere jobber igjen. Feilsjekking skjer som foelge av det, og Funksjonen
 * vil gi beskjed tilbake til server om evt. terminering osv.
 * I tillegg sjekkes ogsaa riktig brukerinput hvis brukeren ville ha X antall
 * jobber. I og med at toServer variabelen (kommandoen som sendes til server for
 * forstaaelse av hvilken jobb clienten vil hente) er en int, har jeg valgt aa
 * bruke htons() for aa ta hensyn til eventuelle big-endian til little-endian
 * hendelser. I og med at jeg har brukt short, har jeg satt en grense til at
 * brukeren ikke kan skrive mer enn 65000 jobber aa hente i X antall jobber.
 * Ellers er det beskrevet i protokoll.txt hvordan jeg har brukt de fire
 * bytene vi har til disposisjon.
 *
 * Input:
 *      int sock:       socketveriden som blir sendt med
 *      int input:      brukerinputen som blir sendt med fra mainmenu()
 *      int fd_out[]:   foerste pipe sendt fra tidligere funskjoner
 *      int fd_error[]: andre pipe sendt fra tidligere funskjoner
 *
 * Return:
 * Funksjonen returnerer en int verdi. Enten 0 eller 1 avhengig av om serveren
 * skal fortsette, eller avslutte. Hvis funksjonen returnerer 0, vil
 * mainmenu() avslutte og serveren terminere som foelge av det. Hvis
 * funksjonen returnerer 1, har alt gaatt bra og den fortsetter.
 */
int serverConnection(int sock, int input, int fd_out[], int fd_error[]) {
    int toServer;

    if(input == 1) {
        toServer = htons(65001);
        printf("Henter én jobb...\n\n");
        debugPrint("Writer til server for aa hente én jobb");
        write(sock, &toServer, sizeof(int));
        ret = readFromServer(sock, fd_out, fd_error);

        if(ret == -1) {
            printf("* CHECKSUM STEMMER IKKE! HOPPER OVER JOBB. *\n");
        }
        else if(ret == 1) {
            toServer = htons(65003);
            printf("Avslutter...\n");
            printf("Avsluttet.\n");
            debugPrint("Writer til sever om at client avslutter");
            write(sock, &toServer, sizeof(int));
            return 0;
        }
        return 1;
    }
    else if(input == 2) {
        char input2[64] = { 0 };
        int check = 1;

        while(check == 1) {
            printf("Hvor mange jobber vil du hente?\n");
            printf("Antall: ");
            fgets(input2, sizeof(input2), stdin);

            if(interrupt_received) {
                debugPrint("CTRL-C er brukt");
                toServer = htons(65003);
                unsigned char q = 224;
             debugPrint("Writer foerste barneprosess om at den skal avslutte");
                write(fd_out[1], &q, sizeof(char));
           debugPrint("Writer til andre barneprosess om at den skal avslutte");
                write(fd_error[1], &q, sizeof(char));
                debugPrint("Writer til server om at client avslutter");
                write(sock, &toServer, sizeof(int));
                return 0;
            }

            int i, length;
            length = strlen(input2);
            for (i = 0; i < length; i++) {
                if(!isdigit(input2[i])) {
                    printf("* SKRIV INN ET TALL! *\n");
                    break;
                }
                else if(atoi(input2) >= 65000) {
                    printf("* SKRIV INN ET MINDRE ANTALL! *\n");
                    break;
                }
                else if(atoi(input2) < 1) {
                    printf("* SKRIV INN ET TALL OVER 0! *\n");
                    break;
                }
                else {
                    int amount = atoi(input2);
                    printf("Henter %d jobber...\n\n", amount);
                    toServer = htons(amount);
                    if(debugMode) {
                        printf(">>>%d<<< Writer til server for aa hente %d"
                               " jobber\n", getpid(), amount);
                    }
                    write(sock, &toServer, sizeof(int));

                    for(i = 0; i < amount; i++) {
                        ret = readFromServer(sock, fd_out, fd_error);
                        if(ret == 1) {
                            toServer = htons(65003);
                            sleep(1);
                            printf("Avslutter...\n");
                            printf("Avsluttet.\n");
                        debugPrint("Writer til server om at client avslutter");
                            write(sock, &toServer, sizeof(int));
                            return 0;
                        }
                        else if(ret == -1) {
                            printf("* CHECKSUM STEMMER IKKE! HOPPER OVER"
                                   " JOBB. *\n");
                        }
                    }
                    check = 0;
                }
            }
        }
        return 1;
    }
    else if(input == 3) {
        toServer = htons(65002);
        printf("Henter alle jobbene...\n\n");
        debugPrint("Writer til server for aa hente alle jobber");
        write(sock, &toServer, sizeof(int));

        while(1) {
            ret = readFromServer(sock, fd_out, fd_error);
            if(ret == 1) {
                toServer = htons(65003);
                sleep(1);
                printf("Avslutter...\n");
                printf("Avsluttet.\n");
                debugPrint("Writer til server om at client avslutter");
                write(sock, &toServer, sizeof(int));
                return 0;
            }
            else if(ret == -1) {
                printf("* CHECKSUM STEMMER IKKE! HOPPER OVER JOBB. *\n");
            }
        }
        return 1;
    }
    else if(input == 4) {
        toServer = htons(65003);
        printf("Avslutter...\n");
        unsigned char q = 224;
        debugPrint("Writer til foerste barneprosess om at den skal avslutte");
        write(fd_out[1], &q, sizeof(char));
        debugPrint("Writer til andre barneprosess om at den skal avslutte");
        write(fd_error[1], &q, sizeof(char));
        debugPrint("Writer til server om at client avslutter");
        write(sock, &toServer, sizeof(int));
        printf("Avsluttet.\n");
        return 0;
    }
    else {
        printf("* NOE GIKK GALT! *\n");
        toServer = htons(65004);
        printf("Avslutter pga. en feil...\n");
        unsigned char q = 224;
        debugPrint("Writer til foerste barneprosess om at den skal avslutte");
        write(fd_out[1], &q, sizeof(char));
        debugPrint("Writer til andre barneprosess om at den skal avslutte");
        write(fd_error[1], &q, sizeof(char));
        debugPrint("Writer til server om at client avslutter");
        write(sock, &toServer, sizeof(int));
        printf("Avsluttet.\n");
        return 0;
    }
}

// ****************************************************************************





/* Dette er kommandoloekke funksjonen min. Denne blir tilkalt i startClient(),
 * og er en void funksjon. Funksjonen tar med tre parametere forklart under.
 * Funksjonen her gir brukeren fire valgmuligheter som du kan se i koden, og
 * gaar i en while-loekke til det breakes eller returneres.
 * Ut i fra det brukeren skriver inn, reagerer programmet som foelge av det.
 * Her sjekkes ogsaa om brukeren bruker CTRL-C. Hvis CTRL-C er brukt,
 * avsluttes clienten og samtidig gir beskjed til server om termineringen.
 * Ellers sjekkes brukerinput og kallene videre paa serverConnection() blir et
 * resultat av det.
 *
 * Input:
 *      int sock:       socket parameteren sendt fra startClient()
 *      int fd_out[]:   ene pipen sendt fra startClient()
 *      int fd_error[]: andre pipen sendt fra startClient()
 *
 * Return:
 *      void
 */
void mainmenu(int sock, int fd_out[], int fd_error[]) {
    while(1) {
        sleep(1);
        char input[64] = { 0 };
        printf("\n\nTrykk paa den tasten du vil gjennomfoere:\n");
        printf("[1] Hent én jobb fra serveren\n");
        printf("[2] Hent X antall jobber fra serveren\n");
        printf("[3] Hent alle jobber fra serveren\n");
        printf("[4] Avslutt programmet\n\n");
        printf("Valg: ");
        fgets(input, sizeof(input), stdin);

        if(interrupt_received) {
            debugPrint("CTRL-C er brukt");
            int toServer = htons(65003);
            unsigned char q = 224;
           debugPrint("Writer til barneprosesser om at de skal avslutte");
            write(fd_out[1], &q, sizeof(char));
            write(fd_error[1], &q, sizeof(char));
           debugPrint("Writer til server om at client avslutter");
            write(sock, &toServer, sizeof(int));
            return;
        }
        else {
            if(strlen(input) == 2) {
                if(input[0] == '1') {
                    ret = serverConnection(sock, 1, fd_out, fd_error);
                    if(ret == 0) {
                        return;
                    }
                }
                else if(input[0] == '2') {
                    ret = serverConnection(sock, 2, fd_out, fd_error);
                    if(ret == 0) {
                        return;
                    }
                }
                else if(input[0] == '3') {
                    ret = serverConnection(sock, 3, fd_out, fd_error);
                    if(ret == 0) {
                        return;
                    }
                }
                else if(input[0] == '4') {
                    ret = serverConnection(sock, 4, fd_out, fd_error);
                    return;
                }
                else {
                    printf("* SKRIV INN ET TALL MELLOM 1 OG 4! *\n");
                }
            }
            else {
                printf("* TAST INN RIKTIG! *\n");
            }
        }
    }
}

// ****************************************************************************





/* Denne funksjonen er veldig lang, men det er fordi det er her hele client
 * starter. Altsaa det er her alt initialiserers og lages, som bl.a pipes,
 * socket, structer, barneprosesser og foreldreprosess, samt hvordan de
 * lukkes. I tillegg feilsjekkes alt der det kan oppstaa feil.
 * Denne funksjonen starter barneprosessene med fork, og samtidig er
 * foreldreprosessen. Altsaa er i mitt tilfelle client.c foreldreprosessen,
 * som lager barneprosesser. Disse kommuniserer med hverandre via pipes.
 * Ellers; kobler clienten seg seg paa serveren med socket og connect.
 *
 * REF. kommentar 1. og 2. nede i funksjonen:
 *      Kilde: googlet meg frem til loesning om aa konvertere en adresse til
 *             en IP-adresse (f.eks localhost om til 127.0.0.1 osv).
 * Ellers er mye av koden selvskrevet, med hjelp fra forelesninger og plenum,
 * samt medstudenter.
 *
 *
 * Input:
 *      char* argv[]: array av argumentene brukeren skrev inn i terminal
 *
 * Return:
 * Verdien er en int avhengig av om det som kjoeres er riktig eller feil.
 * Det returneres 0 tilbake til main hvis sjekkene mine finner en feil, og 1
 * hvis alt har gatt riktig.
 */
int startClient(char* argv[]) {
    int fd_out[2];
    int fd_error[2];
    pid_t cpidOut;
    pid_t cpidError;

    debugPrint("Oppretter foerste pipe");
    if(pipe(fd_out) == -1) {
        perror("pipe");
        return 0;
    }
    debugPrint("Oppretter andre pipe");
    if(pipe(fd_error) == -1) {
        perror("pipe");
        return 0;
    }
    debugPrint("Oppretter foerste barneprosess");
    cpidOut = fork();

    if(cpidOut == -1) {
        perror("fork");
        return 0;
    }

    if(cpidOut != 0) {
        debugPrint("Oppretter andre barneprosess");
        cpidError = fork();
        if(cpidError == -1) {
            perror("fork");
            return 0;
        }

        if(cpidError != 0) {
            int sock, ret;
            unsigned short port;
            struct addrinfo hints;
            struct addrinfo *result, *rp;
            struct in_addr *addr;
            // 1. START (se dokumentasjon)
            char ipstring[INET_ADDRSTRLEN];
            // 1. SLUTT (se dokumentasjon)

            memset(&hints, 0, sizeof(struct addrinfo));
            hints.ai_family = AF_INET;
            hints.ai_socktype = SOCK_STREAM;
            hints.ai_flags = 0;
            hints.ai_protocol = IPPROTO_TCP;

            debugPrint("Starter tilkobling");
            ret = getaddrinfo(argv[1], argv[2], &hints, &result);
            if(ret) {
                const char* s = gai_strerror(ret);
                printf("getaddrinfo: %s\n", s);
                return 0;
            }

            sock = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
            if(sock == -1) {
                perror("socket init");
                return 0;
            }

            ret = get_port(argv[2], &port);
            if(ret) {
                printf("[port]-argumentet maa vaere en integer!\n");
                debugPrint("Closer socket");
                close(sock);
                return 0;
            }

            printf("Kobler til server...\n");
            for(rp = result; rp != NULL; rp = rp->ai_next) {
                ret = connect(sock, rp->ai_addr, rp->ai_addrlen);
                if(!ret) {
                    break;
                }
            }

            if(rp == NULL) {
                fprintf(stderr, "Ingen adresse lyktes!\n");
                debugPrint("Closer socket");
                close(sock);
                unsigned char q = 224;
                debugPrint("Writer til foerste barneprosess for aa avslutte");
                write(fd_out[1], &q, sizeof(char));
                debugPrint("Writer til andre barneprosess for aa avslutte");
                write(fd_error[1], &q, sizeof(char));
                freeaddrinfo(result);
                return 0;
            }

            // 2. START (se dokumentasjon)
            struct sockaddr_in *ipv = (struct sockaddr_in *)rp->ai_addr;
            addr = &(ipv->sin_addr);
            if(debugMode) {
                printf(">>>%d<<< GJOER OM \"%s\" TIL IP ADRESSE\n",
                        getpid(), argv[1]);
            }
            inet_ntop(rp->ai_family, addr, ipstring, sizeof(ipstring));
            if(debugMode) {
                printf(">>>%d<<< KOBLET TIL IP: %s\n", getpid(), ipstring);
            }
            // 2. SLUTT (se dokumentasjon)

            freeaddrinfo(result);
            debugPrint("Tilkoblet");
            printf("Tilkoblet.\n");

            mainmenu(sock, fd_out, fd_error);
            waitpid(cpidOut, NULL, 0);
            waitpid(cpidError, NULL, 0);
            close(fd_out[1]);
            close(fd_error[1]);
            close(fd_out[0]);
            close(fd_error[0]);
            debugPrint("Lukker alle pipes");
            close(sock);
            debugPrint("Lukker socket");
        }
        else {
            close(fd_error[1]);
            close(fd_out[1]);
            close(fd_out[0]);
            childrenProcess(fd_error[0], stderr, 2);
            debugPrint("Barneprosess ferdig");
            close(fd_error[0]);
            debugPrint("Lukker socket");
        }
    }
    else {
        close(fd_out[1]);
        close(fd_error[1]);
        close(fd_error[0]);
        childrenProcess(fd_out[0], stdout, 1);
        debugPrint("Barneprosess ferdig");
        close(fd_out[0]);
        debugPrint("Lukker pipes");
    }

    return 1;
}
// ****************************************************************************





/* Dette er main-funksjonen. Sjekker usage, deklarerer CTRL-C og starter
 * startClient() funksjonen.
 *
 * Return:
 * Hvis programmet avsluttes riktig, returneres EXIT_SUCCESS, hvis ikke
 * returneres EXIT_FAILURE. Dette er avhengig av funksjonen startClient(),
 * eller om usage() er brukt riktig/feil.
 */
int main(int argc, char* argv[]) {
    interrupt_received = 0;

    if(usage(argc, argv)) {
        debugPrint("EXIT SUCCESS");
        return EXIT_SUCCESS;
    }

    struct sigaction sa;
    memset(&sa, 0, sizeof(struct sigaction));
    sa.sa_handler = &interrupt_handler;
    sigaction(SIGINT, &sa, NULL);

    debugPrint("Starter client");
    ret = startClient(argv);
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
