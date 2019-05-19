#include "fellesfunksjoner.h"
// ****************************************************************************





/* Denne funksjonen lager en jobb struct.
 *
 * Return:
 *      job struct
 */
job *createJob(unsigned char jobtype, unsigned int length, char text[]) {
    job *j = malloc(sizeof(job) + length + 1);
    j->jobinfo = jobtype;
    j->length = length;
    memcpy(j->text, text, length + 1);

    return j;
}

// ****************************************************************************





/* Denne funksjonen tar porten brukeren skriver inn i terminalen, og
 * feilsjekker den. Den leser inn porten som en string, og konverterer den til
 * en unsigned short som deretter brukes videre i programmet.
 *
 * Input:
 *      char* port_as_string: er det brukeren skrev inn i terminalen hentet
 *                            fra tidligere funksjon
 *      unsigned short* port: den koverterte verdien som blir brukt videre
 *
 * Return:
 * 1 hvis noe feiler, og 0 hvis alt gaar som det skal.
 */
int get_port(char* port_as_string, unsigned short* port) {
    char* endptr;
    ret = strtol(port_as_string, &endptr, 10);
    if(ret > 65535) {
        printf("OVERFLOW\n");
        return 1;
    }

    if(endptr == port_as_string && ret == 0) {
        printf("[port]-argumentet maa vaere en integer!\n");
        return 1;
    }

    *port = (unsigned short) ret;
    return 0;
}

// ****************************************************************************





/* Denne funksjonen tar hensyn til om brukeren har tastet CTRL-C.
 * Setter den globale variabelen interrupt_received = 1.
 *
 * Return:
 *      void
 */
void interrupt_handler(int signal) {
    printf("\nMottok signal %d. Avslutter...\n", signal);
    printf("Avsluttet.\n");
    interrupt_received = 1;
}

// ****************************************************************************





/* Denne funksjonen brukes hvis debug modus er paa. Den printer ut prosessens
 * pid, samt en melding.
 *
 * Input:
 *      char* print: er meldingen som skal printes ut i printen.
 *
 * Return:
 *      void
 */
void debugPrint(char* print) {
    if(debugMode) {
        printf(">>>%d<<< %s\n", getpid(), print);
    }
    else {
        return;
    }
}
