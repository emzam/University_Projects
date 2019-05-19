# Navn:		    Oblig3
# Oppgave:	    Implementere sprintf funksjonen i C manuelt ved hjelp av
#               assemblerkode.
# C-signatur: 	int sprinter(unsigned char *res, unsigned char *format, ...);
# Registre:	    %ECX - Antall bytes skrevet (telleren)
#			    %EDX - Format-pekeren (hva som kommer etter %)
#			    %EBX - Resultat-pekeren (variabelen som blir skrevet til)
# 			    Bruker videre %EAX, %ESI og %EDI som ekstra bruksregistre.


        .globl sprinter

sprinter:
        pushl   %ebp            # Normalt
        movl    %esp, %ebp      # Ogsaa normalt
        pushl   %esi            # Push verdien av %ESI til stacken
        pushl   %edi            # Push verdien av %EDI til stacken
        movl    $0, %ecx        # %ECX = teller for byte
        movl    8(%ebp), %ebx   # Flytte resultat-peker til %EBX
        movl    12(%ebp), %edx  # Flytte format-peker til %EDX
        addl    $16, %ebp       # (%EBP) er naa foerste variabel argumentet

loekke:
        movb    (%edx), %al     # Flytter foerste karakter i format-pointer til %AL
        cmpb    $37, %al        # Sammenligner med ASCII sin "%"
        je      format_sjekk    # Hvis "%", saa hopper den til format_sjekk
        cmpb    $0, %al         # Sammenligner med ASCII sin "0"
        je      ferdig          # Hvis "0", saa hopper den til ferdig
        movb    %al, (%ebx)     # Flytt karakteren fra format-pekeren til resultat-pekeren
        jmp     ink_pekere      # Inkrementerer pekerne, og fortsetter loekken

format_sjekk:
        incl    %edx            # Oek formatpeker til neste karakter(etter %)
        movb    (%edx), %al     # Flytt karakteren etter % til %AL
        cmpb    $99, %al        # Sammenligner med ASCII "c"
        je      format_char     # Hvis det er "c", hopp til format_char
        cmpb    $100, %al       # Sammenligner med ASCII "d"
        je      format_tall     # Hvis det er "d", hopp til format_tall
        cmpb    $117, %al       # Sammenligner med ASCII "u"
        je      format_utall    # Hvis det er "u", hopp til format_tall
        cmpb    $115, %al       # Sammenligner med ASCII "s"
        je      format_string   # Hvis det er "s", hopp til format_string
        cmpb    $120, %al       # Sammenligner med ASCII "x"
        je      format_hex      # Hvis det er "x", hopp til format_hex
        cmpb    $35, %al        # Sammenligner med ASCII "#"
        je      format_hash     # Hvis det er "#", hopp til format_hash
        cmpb    $37, %al        # Sammenligner med ASCII "%"
        je      format_prosent  # Hvis det er "%", hopp til format_prosent
        movb    $63, (%ebx)     # For alt annet, sett inn et "?"
        jmp     ink_pekere      # Inkrementerer pekere, og fortsetter loekken

format_char:
        movb    (%ebp), %al     # Flytt argument karakteren til %AL
        movb    %al, (%ebx)     # Skriv karakteren til resultat-peker
        addl    $4, %ebp        # (%EBP) peker naa paa neste argument
        jmp     ink_pekere      # Inkrementerer pekere, og fortsetter loekken

format_utall:
        pushl   %edx            # Push format-peker paa stacken
        movl    $0, %esi        # %ESI er naa teller
        movl    (%ebp), %eax    # Flytt tallet til %EAX
        jmp     ikke_negativ    # Hopp til ikke_negativ

format_tall:
        pushl   %edx            # Push format-peker paa stacken
        movl    $0, %esi        # %ESI er naa teller
        movl    (%ebp), %eax    # Flytt tallet til %EAX
        cmp     $0, %eax        # Sjekker om tallet er negativt
        jns     ikke_negativ    # Hvis ikke tallet er negativ, ikke legg til "-"
        movb    $45, (%ebx)     # Legger til "-" til resultat om tallet er negativt
        negl    %eax            # Subtraher verdien
        incl    %ecx            # Inkrementer byte-teller
        incl    %ebx            # Inkrementer resultat-peker

ikke_negativ:
        movl    $10, %edi       # Putter 10 inn i %EDI for divisjon
        movl    $0, %edx        # Setter %EDX til 0 for divisjon
        divl    %edi            # Dividerer %EAX med 10, rest gaar inn i %EDX
        addl    $48, %edx       # Legger til 48 til rest, for a faa ASCII verdi
        push    %edx            # Pusher tallet til stacken
        incl    %esi            # Inkrementerer teller
        cmp     $0, %eax        # Sjekker om jeg har naadd 0
        jne     ikke_negativ    # Hvis ikke, fortsett

skriv_tall:
        popl	%edx  			# Popper msb til %edx
        movb	%dl, (%ebx) 	# Legger til karakteren i resultat-pekeren
        incl	%ecx 			# Inkrementer teller
        incl	%ebx			# Inkrementer result-peker
        decl 	%esi 			# Reduserer teller, Zeroflag blir satt om %esi er 0
        jnz		skriv_tall     	# Hvis telleren ikke er 0, legg til flere
        popl	%edx 			# Popper og gjenoppretter %edx
        incl 	%edx 			# Inkrement format-peker
        addl 	$4, %ebp 		# (%EBP) er naa neste argument
        jmp 	loekke		    # Fortsett loekken

format_string:
        movl    (%ebp), %eax    # Flytt argument-tekst adressen til %EAX
        movb    (%eax), %al     # Flytt foerste karakter av stringen til %AL
        cmpb    $0, %al         # Sjekk etter null-byte (\0)
        je      string_ferdig   # Hvis null-byte er funnet, er teksten lest. Hopp til string_ferdig
        movb    %al, (%ebx)     # Skriv karakteren til resultat-peker
        incl    %ecx            # Inkrementer pekere
        incl    %ebx            # ------||------
        incl    (%ebp)          # ------||------
        jmp     format_string   # Les neste karakter i stringen

string_ferdig:
        addl    $4, %ebp        # (%EBP) er naa neste argument
        incl    %edx            # Inkrementer format-pekeren
        jmp     loekke          # Fortsett loekken

format_prosent:
        movb    $37, (%ebx)     # Legger "%" inn i resultat-pekeren
        jmp     ink_pekere      # Inkrementerer pekere og fortsetter loekken

format_hash:
        incl    %edx            # Oek formatpeker til neste karakter(etter #)
        movb    (%edx), %al     # Flytt karakteren etter # til %AL
        cmpb    $120, %al       # Sammenligner med ASCII "x"
        je      format_hash2    # Hvis ja, hopp til format_hash2
        jmp     loekke          # Ellers tilbake til loekke

format_hash2:
        movb    $48, (%ebx)     # Legger til 0 inn i resultat-peker
        incl    %ebx            # Inkrementerer resultat-peker
        incl    %ecx            # Inkrementerer teller
        movb    $120, (%ebx)    # Legger til "x" inn i resultat-peker
        incl    %ebx            # Inkrementerer resultat-peker
        incl    %ecx            # Inkrementerer teller
        jmp     format_hex      # Skriver saa hexatallet via hex-metoden

format_hex:
        pushl   %edx            # Pusher format-pekeren paa stacken
        movl    $0, %esi        # %ESI er naa teller
        movl    (%ebp), %eax    # Flytt argumentet til %EAX

hexabytes:
        movl    $16, %edi       # Legger til 16(hex) i %EDI for divisjon
        movl    $0, %edx        # Setter %edx til 0 for divisjon
        divl    %edi            # Dividerer %eax med 16
        cmp     $9, %edx        # Sjekker om rest er stoerre enn 9
        ja      over_ti         # Hvis ja, hopp til over_ti
        addl    $48, %edx       # Legger til 48 (0) til rest
        push    %edx            # Pusher tallet til stacken
        incl    %esi            # Inkrementer teller
        jmp     sjekknull       # Sjekk om vi har naadd 0

over_ti:
        addl    $87, %edx       # Legger til 87 (a for 10) til rest
        push    %edx            # Pusher tallet til stacken
        incl    %esi            # Inkrementerer teller

sjekknull:
        cmp     $0, %eax        # Sjekker om det er 0
        jne     hexabytes       # Hvis ja, hopp til hexabytes og fortsett
        jmp     skriv_tall      # Skriver tallene til resultat-pekeren

ink_pekere:
        incl    %ecx            # Inkrementerer telleren
        incl    %edx            # Inkrementerer format-peker
        incl    %ebx            # Inkrementerer resultat-peker
        jmp     loekke          # Fortsetter loekken

ferdig:
        movb    $0, (%ebx)      # Nullterminerer resultat-pekeren
        movl    %ecx, %eax      # Flytter antall bytes skrevet til #eax for returverdi
        popl    %edi            # Gjenoppretter %edi
        popl    %esi            # Gjenoppretter %esi
        popl    %ebp            # Gjenoppretter %ebp
        ret                     # Returnerer antall bytes skrevet til resultat-peker
