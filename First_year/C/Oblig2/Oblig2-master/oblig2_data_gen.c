#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

char generate_flags(int active, int wireless, int fiveghz, char change);
char rand_router_name(char *str);

int main(int argc, char* argv[]) {

    int random_IDs = 0;
    char used_IDs[256] = { 0 };

    if(argc < 2) {
        printf("USAGE: %s <lines to write> [random IDs (add something as third arg to enable)]\n", argv[0]);
        return 0;
    } else if(argc == 3) {
        random_IDs = 1;
    }

    char filename[64] = { 0 };
    sprintf(filename, "oblig2_%lu.dat", time(NULL));

    FILE* f = fopen(filename, "w");
    int lines_to_make = strtol(argv[1], 0, 10);

    srand(time(NULL));

    fwrite(&lines_to_make, sizeof(int), 1, f);
    fwrite("\n", 1, 1, f);
    for(int i = 0; i < lines_to_make; i++) {
        char line_to_write[256] = { 0 };

        if(random_IDs) {
            int ID;
            do {
                ID = rand()%256;
            } while(used_IDs[ID]);
            used_IDs[ID] = 1;
            line_to_write[0] = ID;
        } else {
            line_to_write[0] = i;
        }

        line_to_write[1] = generate_flags(rand()%2, rand()%2, rand()%2, rand()%16);
        line_to_write[2] = rand_router_name(&line_to_write[3]);
        fwrite(line_to_write, line_to_write[2] + 3, 1, f);
    }
    fclose(f);
}

char generate_flags(int active, int wireless, int fiveghz, char change) {
    char c = 0;

    if(active)   c |= 1 << 0;
    if(wireless) c |= 1 << 1;
    if(fiveghz)  c |= 1 << 2;
    if(change)   c |= change << 4;

    return c;
}

char rand_router_name(char *str) {
    char* producers[] = { "Cisco", "Juniper", "D-Link", "Linksys", "Netgear", "ZyXEL" };
    char* charset = "abcdefghijklmnopqrstuvwxyz";
    int len = 0;

    int producer = rand() % 6;
    strcpy(str, producers[producer]);
    len += strlen(producers[producer]);

    str[len++] = ' ';
    str[len++] = charset[rand() % strlen(charset)];
    str[len++] = '1' + rand() % 9;

    for (int i = 2; i < rand() % 8; i++) {
        str[len++] = '0';
    }
    if(rand() % 2) str[len++] = charset[rand() % strlen(charset)];

    str[len++] = '\n';
    return (char)len;
}
