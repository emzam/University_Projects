#           %ebx = variabelpeker
#           %edx = resultatpeker
#           %ecx = counter?

        .globl strcombine

strcombine:
        pushl   %ebp
        movl    %esp, %ebp

        movb    $0, %ecx            # Teller for n variabelen
        movl    8(%ebp), %edx       # Setter foerste var. til resultatpeker
        movl    12(%ebp), %ebx      # Setter andre var. til variabelpeker
        addl    $16, %ebp           # Setter basepointer paa andre variabel

mainloop:
        movb    (%ebx), %al         # Flytter peker til al
        cmpb    $42, %al            # Sammenligner med *
        je      stjerne
        cmpb    $0, %al             # Sammenligner med 0 for 0 byte
        je      ferdig

        movb    %al, %ebx           # putter al inn i ebx igjen
        jmp     ink_pekere

stjerne:
        incb    %cl                 # Oeker cl, som er n
        movb    (%ebp), %al
        cmpb    $0, %al
        je      sferdig

        movb    %al, (%edx)         # Putter den inn i edx
        incl    (%ebp)              # Oeker ebp til neste karakter
        incl    %edx
        jmp     stjerne

sferdig:
        addl    $4, (%ebp)
        incl    %ebx
        jmp     mainloop

ink_pekere:
        incl    %ebx                # Oeker alle nedover
        incl    %edx
        jmp     mainloop

ferdig:
        movb    $0, (%ebx)
        movb    %cl, %eax
        popl    %edi
        popl    %ebp

        ret
