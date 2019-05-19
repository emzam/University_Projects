#ifndef __FELLESFUNKSJONER_H
#define __FELLESFUNKSJONER_H

#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <ctype.h>
#include <math.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <signal.h>
#include <errno.h>

// Globale variabler
int debugMode;
int ret;
int interrupt_received;
typedef struct job job;

struct job {
    unsigned char jobinfo;
    unsigned int length;
    char text[];
}__attribute__((packed));

job *createJob(unsigned char jobtype, unsigned int length, char text[]);
int get_port(char* port_as_string, unsigned short* port);
void interrupt_handler(int signal);
void debugPrint(char* print);

#endif
