README FOR Å HJELPE RETTER

- KOMPILERING AV PROGRAM
    Kompilering skjer ved kall på make. Evt. make clean, så make. Dette
    kompilerer alle filer.

- KJØRING AV PROGRAM
    For å kjøre server med en eksempel, brukes: ./server alice.job 1060
        Hvis du vil ha debugmodus, brukes: ./server alice.job 1060 debug

    For å kjøre client med eksempel, brukes: ./client localhost 1060
        Hvis du vil ha debugmodus, brukes: ./client localhost 1060 debug

    * Clienten kan kjøres med bruk av IP-adresser på tallformat, samt på
      url-format. Dvs. at hvis du er koblet til to forskjellige uio-pcer, kan
      du starte server på den ene, og starte client på den andre med f.eks
      nordur.ifi.uio.no (avhengig av hvilken pc du er på da selvfølgelig).

- HEADER FILER
    Jeg bruker en header fil.
    Navnene er fellesfunksjoner.c og fellesfunksjoner.h
