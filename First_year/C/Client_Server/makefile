CC=gcc
CFLAGS=-Wall -Wextra -Wpedantic -std=gnu99 -g
ALLEFILER = client server

all: $(ALLEFILER)

%.o:%.c
	$(CC) $^ -c -o $@ $(CFLAGS)

server: server.o fellesfunksjoner.o
	$(CC) $(CFLAGS) $^ -o $@

client: client.o fellesfunksjoner.o
	$(CC) $(CFLAGS) $^ -o $@

clean:
	rm -f $(ALLEFILER) *.o
