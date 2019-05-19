        .globl oppg8

oppg8:
        pushl   %ebp
        movl    %esp, %ebp

        pushl   %edi            # UNDER
        pushl   %esi            # Siden registret skal brukes, maa det pushes
                                # HUSK AA POPPE DEN TIL SLUTT
        movl    8(%ebp), %ebx   # Setter inn variabel inn i ebx (a[0])

while:
        movb    $2, %cl         # Lagrer variabelen ix i cl
        movb    (%ebx), %dl     # a[i] er naa i b (unsigned char)
        incl    %ebx            # i++, naa er det a[1]

        cmpb    $0, %dl         # Sammenlinger dl (b'en vaar) med 0
        je      return1         # Hopper til return 1 funksjon

        movb    (%dl), %dh      # Legger en kopi av b i dh
        andb    $0x80, %dl      # AND'er b med 0x80, og gjoer %dl til svaret
        cmpb    $0, %dl         # Sammenligner b AND 0x80 med 0
        je      nbytes1         # Hvis ja, hopp

        movb    (%dh), %dl      # Flytter dh(b) til dl igjen (b som ikke er ANDa)
        andb    $0xE0, %dl      # AND'er dl med 0xE0, lagres i dl
        cmpb    $0xC0, %dl      # Sammenligner..
        je      nbytes2

        movb    (%dh), %dl      # Flytter dh(b) til dl igjen..
        andb    $0xF0, %dl      # ANDer dl, svar lagres i dl
        cmpb    $0xE0, %dl      # Sammenligner..
        je      nbytes3

        movb    (%dh), %dl      # Setter dl til b igjen(dh)
        andb    $0xF8, %dl      # ANDer dl, ...
        cmpb    $0xF0, %dl      # Sammenligner..
        je      nbytes4

        jmp     return0

nbytes1:
        movl    $1, %esi        # Lagrer nbytes inn i esi
        jmp     forloop         # Hopper til forloopen

nbytes2:
        movl    $2, %esi        # Lagrer nbytes inn i esi
        jmp     forloop         # Hopper til forloopen

nbytes3:
        movl    $3, %esi        # Lagrer nbytes inn i esi
        jmp     forloop         # Hopper til forloopen

nbytes4:
        movl    $4, %esi        # Lagrer nbytes inn i esi
        jmp     forloop         # Hopper til forloopen

forloop:
        movl    (%ebx), %edi    # a inn i edi
        andl    $0xC0, %edi     # ANDer a med 0xC0
        cmpl    $0x80, %edi     # Sammenligner ..
        jne     return0         # Hvis NEI, hopp

        cmb     %esi, %cl       # Sammenlinger ix med nbytes
        je      while           # Hvis ja, fortsett while

        incb    %cl             # Oeker ix
        incl    %ebx            # Oeker i

        jmp     forloop         # Fortsette forloopen

return0:
        movl    $0, %eax        # Setter retur (eax) til 0
        jmp     ferdig          # Hopper til ferdig

return1:
        movl    $1, %eax        # Setter retur (eax) til 1
        jmp     ferdig          # Hopper til ferdig

ferdig:
        popl    %ebp            # Alt som pushes maa poppes
        popl    %esi            # Alt som pushes maa poppes
        popl    %edi            # Alt som pushes maa poppes
        ret
