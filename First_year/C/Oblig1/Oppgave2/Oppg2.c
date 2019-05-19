#include <stdio.h>
#include <ctype.h>
#include <string.h>
#include <stdlib.h>

// b) Summerer characterene i strengen
int stringsum(char* s) {
    int i = 0;
    int sum = 0;

    while(s[i]) {
        int verdi = s[i];

        if(islower(s[i])) {
            verdi = verdi - 96;
        }
        else if(isupper(s[i])) {
            verdi = verdi - 64;
        }
        else {
            return -1;
        }

        sum += verdi;
        i++;
    }
    return sum;
}


// c) Sjekker om c finnes i *s, og finner avstanden mellom de gangene c finnes
int distance_between(char* s, char c) {
    int lengde = strlen(s);
    int foerste;
    int andre;
    int avstand;

    int i;
    for(i = 0; i < lengde; i++) {
        if(s[i] == c) {
            foerste = i;

            int j;
            for(j = (i+1); j < lengde; j++) {
                if(s[j] == c) {
                    andre = j;
                    avstand = (andre - foerste);
                    return avstand;
                }
            }
        }
    }
    return -1;
}


// d) Lager en ny string av karakterene mellom karakteren c som blir sendt inn.
char* string_between(char* s, char c) {
    int lengde = distance_between(s, c);
    char* ny = malloc(lengde);
    int i, j;

    if(lengde == -1) {
        return NULL;
    }

    for (i = 0; i < (int)strlen(s); i++) {
        if(s[i] == c) {
            i++;
            for (j = 0; j < (lengde - 1); j++) {
                ny[j] = s[i];
                i++;
            }
            return ny;
        }
    }

    return NULL;
}


// e) Split-oppgaven.
char** split(char* s) {
    int slengde = strlen(s);
    char** dobbeltpeker = (char **)(malloc(slengde * sizeof(char*)));
    char* enkeltpeker = (char *)(malloc(slengde * sizeof(char*)));
    int i = 0;
    int j = 0;
    int k = 0;

    while(i < slengde) {
        if(!isspace(s[i])) {
            enkeltpeker[j] = s[i];
            j++;
            i++;
        }
        else {
            enkeltpeker[j] = '\0';
            dobbeltpeker[k] = enkeltpeker;
            enkeltpeker = (char *)(malloc(slengde * sizeof(char*)));
            k++;
            i++;
            j = 0;
        }
    }
    dobbeltpeker[k] = enkeltpeker;
    dobbeltpeker[k+1] = NULL;

    return dobbeltpeker;
}


// g)
void stringsum2(char* s, int* res) {
    *res = stringsum(s);
}
