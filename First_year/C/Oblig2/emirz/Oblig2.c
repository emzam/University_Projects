#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <math.h>


// Struct er basically det samme som en klasse eller naermere et objekt i java.
struct router {
    unsigned char ID;
    unsigned char flagg;
    unsigned char model_length;
    char* model;
};

// ****************************************************************************
/* Dette gjoer slik at jeg slipper aa skrive struct router hele tiden.
Kan heller skrive router bare. */
typedef struct router router;

// Lager router structer.
router *createRouter(unsigned char ID, unsigned char flagg,
                     unsigned char model_length, char* model) {

    router *r = malloc(sizeof(router));
    r->ID = ID;
    r->flagg = flagg;
    r->model_length = model_length;
    r->model = model;

    return r;
}

// ****************************************************************************
/* toBinary() metoden tar inn et tall, samt en char array og gjoer om til et
binaert tall. Returnerer oppdatert char*. */
void toBinary(int num, char* arr) {
    int pos = 7;
    int i;

	for(i = 0; i < 8; i++) {
		char c = num & (1 << pos) ? '1' : '0';
        arr[i] = c;
		pos--;
	}
    arr[8] = 0;
}

// ****************************************************************************
// Globale variabler.
static router *routerArray[256];
int router_amount;
FILE *file;
FILE *file2;
char* filename;
// Metode for aa lese fra fil.
void readFile(char* file_name) {
    filename = file_name;
    file = fopen(filename, "rb");

    if(file == NULL) {
        printf("Error naar fil skulle aapnes.\n");
        exit(1);
    }

    router_amount = fgetc(file);
    fgetc(file); // For aa fjerne de unoedvendige bytene i foerste for antall.
    fgetc(file);
    fgetc(file);
    fgetc(file);

    int i;
    for(i = 0; i < router_amount; i++) {
        unsigned char ID = (unsigned char)fgetc(file);
        unsigned char flagg = (unsigned char)fgetc(file);
        unsigned char model_length = (unsigned char)fgetc(file);

        char* model = malloc(sizeof(char) * model_length);
        model = fgets(model, model_length, file);
        fgetc(file);

        routerArray[ID] = createRouter(ID, flagg, model_length, model);
    }

    fclose(file);
}

// ****************************************************************************
// Refresher antall rutere som finnes i databasen.
void refreshRouterAmount() {
    int counter = 0;
    int i;

    for(i = 0; i < 256; i++) {
        if(routerArray[i] != NULL) {
            counter++;
        }
    }

    router_amount = counter;
}

// ****************************************************************************
// Metode som soeker paa en ID og printer ut info.
void infoID(int id) {
    char buffer[9];
    toBinary(routerArray[id]->flagg, buffer);

    printf("----------------------------------------\n");
    printf("ID:              %d\n", routerArray[id]->ID);
    printf("Flagg:           %s (Desimal: %d)\n", buffer,
                                                  routerArray[id]->flagg);
    printf("Model length:    %d\n", routerArray[id]->model_length - 1);
    printf("Model/produsent: %s\n", routerArray[id]->model);
    printf("----------------------------------------\n");

    return;
}

// ****************************************************************************
// Printer alle ruterne og deres tilsvarende info.
void printAll() {
    int i;
    for(i = 0; i<256; i++) {
        if(routerArray[i] != NULL) {
            char buffer[9];
            toBinary(routerArray[i]->flagg, buffer);

            printf("----------------------------------------\n");
            printf("ID:              %d\n", routerArray[i]->ID);
            printf("Flagg:           %s (Desimal: %d)\n", buffer,
            routerArray[i]->flagg);
            printf("Model length:    %d\n", routerArray[i]->model_length - 1);
            printf("Model/produsent: %s\n", routerArray[i]->model);
            printf("----------------------------------------\n");
        }
    }
}

// ****************************************************************************
// Printer ut info for én ID.
void printRouterInfo(int i) {
    char buffer[9];
    toBinary(routerArray[i]->flagg, buffer);

    printf("\nFlagget til ID: %d, er: %s. Dvs:\n", routerArray[i]->ID, buffer);
    printf("Bit-pos. (LSB)  | Egenskap    | Forklaring    | (1) Ja (0) Nei\n");
    printf("----------------|-------------|---------------|---------------\n");
    printf("       0        | Aktiv       | I bruk?       |       %c      \n",
                                                            buffer[7]);
    printf("       1        | Traadloes   | Traadloes?    |       %c      \n",
                                                            buffer[6]);
    printf("       2        | 5GHz        | Stoetter 5GHz?|       %c      \n",
                                                            buffer[5]);
    printf("       3        | Ubrukt      | Ny?           |       %c      \n",
                                                            buffer[4]);
printf("      4-7       | Endringsnr: | %c%c%c%c          |               \n",
buffer[0], buffer[1], buffer[2], buffer[3]);
    printf("\n");
}

// ****************************************************************************
// Endrer flagget for en ID.
void changeFlagg(int i) {
    printRouterInfo(i);
    char buffer[9];
    toBinary(routerArray[i]->flagg, buffer);

    char input[64];
    int test = 0;

    while(1) {
        if(test == 0) {
            printf("Hva vil du endre?\n");
            printf("[1] Aktiv\n");
            printf("[2] Traadloes\n");
            printf("[3] 5GHz\n");
            printf("[4] Ubrukt\n");
            printf("[5] Print ut ruterinfo\n");
            printf("[6] Tilbake til hovedmeny\n");
            printf("\nValg: ");
            test++;
        }
        else {
            printf("Valg: ");
        }
        scanf("%s", input);


        if(routerArray[i]-> flagg >= 240 && !(strcmp(input, "6") == 0)
                                         && !(strcmp(input, "5") == 0)) {
           printf("\n* ENDRINGSNUMMERET ER MAX. DU KAN IKKE ENDRE MER! *\n\n");
        }
        else if(strcmp(input, "1") == 0) {
            if(buffer[7] == '1') {
                routerArray[i]->flagg = routerArray[i]->flagg & 254;
                // AND'er med 254. (11111110)
                routerArray[i]->flagg = routerArray[i]->flagg + (int)pow(2,4);
                // Legger til 16. (10000000)
                // Tilsvarende nedover metoden.
                toBinary(routerArray[i]->flagg, buffer);
            }
            else {
                routerArray[i]->flagg = routerArray[i]->flagg | (int)pow(2,0);
                routerArray[i]->flagg = routerArray[i]->flagg + (int)pow(2,4);
                toBinary(routerArray[i]->flagg, buffer);
            }
            printf("\nEndret!\n");
        }
        else if(strcmp(input, "2") == 0) {
            if(buffer[6] == '1') {
                routerArray[i]->flagg = routerArray[i]->flagg & 253;
                routerArray[i]->flagg = routerArray[i]->flagg + pow(2,4);
                toBinary(routerArray[i]->flagg, buffer);
            }
            else {
                routerArray[i]->flagg = routerArray[i]->flagg | (int)pow(2,1);
                routerArray[i]->flagg = routerArray[i]->flagg + (int)pow(2,4);
                toBinary(routerArray[i]->flagg, buffer);
            }
            printf("\nEndret!\n");
        }
        else if(strcmp(input, "3") == 0) {
            if(buffer[5] == '1') {
                routerArray[i]->flagg = routerArray[i]->flagg & 251;
                routerArray[i]->flagg = routerArray[i]->flagg + (int)pow(2,4);
                toBinary(routerArray[i]->flagg, buffer);
            }
            else {
                routerArray[i]->flagg = routerArray[i]->flagg | (int)pow(2,2);
                routerArray[i]->flagg = routerArray[i]->flagg + (int)pow(2,4);
                toBinary(routerArray[i]->flagg, buffer);
            }
            printf("\nEndret!\n");
        }
        else if(strcmp(input, "4") == 0) {
            if(buffer[4] == '1') {
                routerArray[i]->flagg = routerArray[i]->flagg & 247;
                routerArray[i]->flagg = routerArray[i]->flagg + (int)pow(2,4);
                toBinary(routerArray[i]->flagg, buffer);
                printf("\nEndret!\n");
            }
            else {
                printf("\n* KAN IKKE ENDRE FRA BRUKT TIL UBRUKT! *\n");
            }
        }
        else if(strcmp(input, "5") == 0) {
            printRouterInfo(i);
        }
        else if(strcmp(input, "6") == 0) {
            printf("\n");
            return;
        }
        else {
            printf("* SKRIV INN RIKTIG VALG! *\n");
        }
    }
}

// ****************************************************************************
// Endrer modellnavn for en gitt ruter.
void changeModel(int i) {
    char* buffer = malloc(64);
    unsigned char length;

    printf("\nID: %d, har modell/produsent: %s\n", routerArray[i]->ID,
                                                 routerArray[i]->model);

    if(routerArray[i]-> flagg >= 240) {
        printf("\n* ENDRINGSNUMMERET ER MAX. DU KAN IKKE ENDRE MER! *\n\n");
        return;
    }

    printf("\nHva vil du endre det til?\n");
    printf("Produsent/modell: ");


    if(routerArray[i]->model) {
        free(routerArray[i]->model);
    }

    /* De to linjene under "clearer" paa en maate stdin, slik at jeg kan bruke
    fgets til aa lese inn fra bruker. Med Scanf saa vil den ikke registrere
    mellomrom. */
    char c;
    while((c = getchar()) != '\n' && c != EOF);
    fgets(buffer, 256, stdin);
    buffer[strlen(buffer) - 1] = '\0';
    routerArray[i]->model = buffer;

    length = strlen(buffer) + 1;
    routerArray[i]->model_length = length;

    printf("Endret til: %s\n\n", routerArray[i]->model);
    routerArray[i]->flagg = routerArray[i]->flagg + 16;



    char mn[64];
    while(1) {
        printf("[y] for hovedmeny: ");
        scanf("%s", mn);

        if(strcmp(mn, "y") == 0) {
            printf("\n");
            return;
        }
        else {
            printf("* FEIL TAST! *\n");
        }
    }
}

// ****************************************************************************
// Legger til en ny ruter i databasen.
void addRouter() {
    char input[64];
    unsigned char id;
    unsigned char length;
    char* name = malloc(64);
    int t = 0;
    unsigned char flagg = 0;

    while(1) {
        printf("ID: ");
        scanf("%s", input);

        int length = strlen(input);
        int i;
        for(i = 0; i < length; i++) {
            if(!isdigit(input[i])) {
                printf("* SKRIV INN ET TALL! *\n");
            }

            t = atoi(input);
            break;
        }

        if(routerArray[t] != NULL) {
            printf("En ruter med den ID'en finnes.\n");

            while(1) {
                printf("\nVil du erstatte den? [y/n]: ");
                scanf("%s", input);

                if(strcmp(input, "y") == 0) {
                    id = t;
                    printf("Erstattet.\n");
                    break;
                }
                else if(strcmp(input, "n") == 0) {
                    printf("Da faar ruteren en tilfeldig generert ID.\n");
                    int j;
                    for(j = 0; j < 256; j++) {
                        if(routerArray[j] == NULL) {
                            id = j;
                            printf("ID'en ble: %d\n", j);
                            break;
                        }
                    }
                    break;
                }
                else {
                    printf("* TAST INN RIKTIG! *\n");
                }
            }
        }
        else {
            id = t;
            printf("ID er satt.\n");
        }

        break;
    }

    printf("\nProdusent/model: ");
    char c;
    while((c = getchar()) != '\n' && c != EOF);
    name = fgets(name, 256, stdin);
    name[strlen(name) - 1] = '\0';

    length = strlen(name) + 1;
    printf("Navn gitt.\n\n");

    while(1) {
        printf("Er ruteren i bruk? [y/n]: ");
        scanf("%s", input);

        if(strcmp(input, "y") == 0) {
            flagg += 1;
            break;
        }
        else if(strcmp(input, "n") == 0) {
            break;
        }
        else {
            printf("* TAST INN RIKTIG! *\n");
        }
    }

    while(1) {
        printf("Er ruteren traadloes? [y/n]: ");
        scanf("%s", input);

        if(strcmp(input, "y") == 0) {
            flagg += 2;
            break;
        }
        else if(strcmp(input, "n") == 0) {
            break;
        }
        else {
            printf("* TAST INN RIKTIG! *\n");
        }
    }

    while(1) {
        printf("Stoetter ruteren 5GHz? [y/n]: ");
        scanf("%s", input);

        if(strcmp(input, "y") == 0) {
            flagg += 4;
            break;
        }
        else if(strcmp(input, "n") == 0) {
            break;
        }
        else {
            printf("* TAST INN RIKTIG! *\n");
        }
    }

    while(1) {
        printf("Er ruteren ny? [y/n]: ");
        scanf("%s", input);

        if(strcmp(input, "y") == 0) {
            flagg += 8;
            break;
        }
        else if(strcmp(input, "n") == 0) {
            break;
        }
        else {
            printf("* TAST INN RIKTIG! *\n");
        }
    }

    routerArray[id] = createRouter(id, flagg, length, name);
    refreshRouterAmount();
}

// ****************************************************************************
// Sletter en ruter fra databasen.
void deleteRouter(int i) {
    free(routerArray[i]->model);
    free(routerArray[i]);
    routerArray[i] = NULL;
    printf("\nRuteren med ID: %d er naa slettet.\n\n", i);
    refreshRouterAmount();
}

// ****************************************************************************
// Metode for aa avslutte programmet. Her free'es alt i tillegg.
void endProgram() {
    int i;
    for(i = 0; i < 256; i++) {
        if(routerArray[i] != NULL) {
            free(routerArray[i]->model);
            free(routerArray[i]);
        }
    }

    printf("\nProgrammet avsluttes.\n");
    exit(0);
}

// ****************************************************************************
// Metode for aa skrive/erstatte en fil med ny data.
void writeFile() {
    file2 = fopen(filename, "wb");
    refreshRouterAmount();

    unsigned char b = '\n';
    fwrite(&router_amount, sizeof(int), 1, file2);
    fwrite(&b, sizeof(char), 1, file2);

    int i;
    for(i = 0; i < 256; i++) {
        if(routerArray[i] != NULL) {
            fwrite(&routerArray[i]->ID, sizeof(char), 1, file2);
            fwrite(&routerArray[i]->flagg, sizeof(char), 1, file2);
            fwrite(&routerArray[i]->model_length, sizeof(char), 1, file2);
            fwrite(routerArray[i]->model, sizeof(char),
                                      routerArray[i]->model_length - 1, file2);
            fwrite(&b, sizeof(char), 1, file2);
        }
    }

    fclose(file2);
}

// ****************************************************************************
// Egen metode for ordreloekken.
void appMenu() {
    char input[64];
    int length, i;
    int test = 0;

    while(1) {
        if(test == 0) {
            printf("Trykk paa den tasten du vil gjennomfoere:\n");
            printf("[1] Printe info om ruter gitt en ID\n");
            printf("[2] Print alle ruterne\n");
            printf("[3] Endre flagg for en ID\n");
            printf("[4] Endre produsent/modell for en ID\n");
            printf("[5] Legge inn en ny ruter\n");
            printf("[6] Slette en ruter fra databasen\n");
            printf("[7] Print hovedmenyen paa nytt\n");
            printf("[8] Avslutte programmet\n\n");
            printf("[HOVEDMENY] Valg: ");
            test++;
        }
        else {
            printf("[HOVEDMENY] Valg: ");
        }
        scanf("%s", input);

        if(strcmp(input, "1") == 0) {
            int t;
            int check = 1;
            printf("\nHvilken ID vil du printe info om?\n");

            while(check == 1) {
                printf("ID: ");
                scanf("%s", input);

                length = strlen(input);
                for(i = 0; i < length; i++) {
                    if(!isdigit(input[i])) {
                        printf("* SKRIV INN ET TALL! *\n");
                        break;
                    }
                    else if(routerArray[atoi(input)] == NULL) {
                        printf("* ID FINNES IKKE! *\n");
                        break;
                    }
                    else {
                        t = atoi(input);
                        infoID(t);
                        check = 0;
                        break;
                    }
                }
            }
        }
        else if(strcmp(input, "2") == 0) {
            printAll();
        }
        else if(strcmp(input, "3") == 0) {
            int check = 1;
            int t;

            printf("Hvilken ID vil du endre flagget til?\n");

            while(check == 1) {
                printf("ID: ");
                scanf("%s", input);

                length = strlen(input);
                for(i = 0; i < length; i++) {
                    if(!isdigit(input[i])) {
                        printf("* SKRIV INN ET TALL! *\n");
                        break;
                    }
                    else if(routerArray[atoi(input)] == NULL) {
                        printf("* ID FINNES IKKE! *\n");
                        break;
                    }
                    else {
                        t = atoi(input);
                        changeFlagg(t);
                        check = 0;
                        break;
                    }
                }
            }
        }
        else if(strcmp(input, "4") == 0) {
            int check = 1;
            int t;

            printf("Hvilken ID vil du endre produsent/modell til?\n");

            while(check == 1) {
                printf("ID: ");
                scanf("%s", input);

                length = strlen(input);;
                for(i = 0; i < length; i++) {
                    if(!isdigit(input[i])) {
                        printf("* SKRIV INN ET TALL! *\n");
                        break;
                    }
                    else if(routerArray[atoi(input)] == NULL) {
                        printf("* ID FINNES IKKE! *\n");
                        break;
                    }
                    else {
                        t = atoi(input);
                        changeModel(t);
                        check = 0;
                        test = 0;
                        break;
                    }
                }
            }
        }
        else if(strcmp(input, "5") == 0) {
            printf("\n");
            addRouter();
        }
        else if(strcmp(input, "6") == 0) {
            int check = 1;
            int t;

            printf("Hvilken ID vil du slette?\n");

            while(check == 1) {
                printf("ID: ");
                scanf("%s", input);

                length = strlen(input);;
                for(i = 0; i < length; i++) {
                    if(!isdigit(input[i])) {
                        printf("* SKRIV INN ET TALL! *\n");
                        break;
                    }
                    else if(routerArray[atoi(input)] == NULL) {
                        printf("* ID FINNES IKKE! *\n");
                        break;
                    }
                    else {
                        t = atoi(input);
                        deleteRouter(t);
                        check = 0;
                        break;
                    }
                }
            }
        }
        else if(strcmp(input, "7") == 0) {
            test = 0;
        }
        else if(strcmp(input, "8") == 0) {
            writeFile();
            endProgram();
        }
        else {
            printf("* TAST INN RIKTIG! *\n");
        }
    }
}



// ****************************************************************************
// Main metoden.
int main(int argc, char *argv[]) {
    if(argc < 2) {
        printf("* SKRIV ET FILNAVN! *\n");
    }
    else {
        readFile(argv[1]);
        appMenu();
    }
}
