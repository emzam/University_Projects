#include <stdio.h>

// a)
struct datetime {
    int timer, minutter, sekunder;
    int dato, maaned, aarstall;
};

// b)
void init_datetime(struct datetime *d, int timer, int minutter, int sekunder,
                                       int dato, int maaned, int aarstall) {
    (*d).timer = timer;
    (*d).minutter = minutter;
    (*d).sekunder = sekunder;
    (*d).dato = dato;
    (*d).maaned = maaned;
    (*d).aarstall = aarstall;

    printf("Du har laget tiden og datoen: %d:%d:%d, %d.%d.%d\n", timer,
                                   minutter, sekunder, dato, maaned, aarstall);
}


// c)
void datetime_set_date(struct datetime* dt, int dato, int maaned,
                                                      int aarstall) {
    (*dt).dato = dato;
    (*dt).maaned = maaned;
    (*dt).aarstall = aarstall;
}

void datetime_set_time(struct datetime* dt, int timer, int minutter,
                                                       int sekunder) {
    (*dt).timer = timer;
    (*dt).minutter = minutter;
    (*dt).sekunder = sekunder;
}


// d)
struct timerange {
    struct datetime en;
    struct datetime to;
};


// e)
int main(void) {
    struct timerange timeRange;
    struct datetime startTime = timeRange.en;
    struct datetime endTime = timeRange.to;

    init_datetime(&startTime, 18, 9, 14, 4, 9, 2017);
    init_datetime(&endTime, 23, 59, 59, 31, 12, 2017);

    printf("Endret tiden fra: %d\n", startTime.timer);
    datetime_set_time(&startTime, 1, 13, 59);
    printf("Til: %d\n", startTime.timer);

    printf("Endret datoen fra: %d\n", startTime.aarstall);
    datetime_set_date(&startTime, 31, 12, 2020);
    printf("Til: %d\n", startTime.aarstall);

}
