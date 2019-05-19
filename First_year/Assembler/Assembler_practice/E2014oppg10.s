#       int strini(uc a[], uc b[])
#       #ecx = i,   #ebx = a[],     #edx = b[]


        .globl strini

strini:
        pushl   %ebp
        movl    %esp, %ebp
        pushl   #ebx

        movl    $0, %ecx        # i = 0
        movl    8($ebp), %ebx
        movl    12(%ebp), %edx

mainloop:
        cmpl    $0, (%edx)
        je      ferdig1

        cmpl    (%ebx, %ecx, 1), (%edx, %ecx, 1)
        jne     ferdig0

        incl    %ecx
        jmp     mainloop

        # ELLER

        cmpl    (%ebx), %ecx
        jne     ferdig0

        incl    %ebx
        incl    %edx

        jmp     mainloop

ferdig1:
        movl    $1, %eax
        popl    %ebp
        popl    %ebx

        ret

ferdig0:
        movl    $0, %eax
        popl    %ebp
        popl    %ebx

        ret


#       uc *strstr(uc* data, uc *s)
#       %ebx = data,    %edx = s

        .globl  strstr

strstr:
        pushl   %ebp
        movl    %esp, %ebp
        pushl   %ebx

        movl    8(%ebp), %ebx
        movl    12(%ebp), %edx

mainloop:
        cmpl    $0, (%ebx)
        je      ferdig0

        pushl   %edx
        pushl   %ebx
        call    strini

        cmpl    $1, %eax
        je      returdata

        incl    %ebx
        jmp     mainloop

returdata:
        pushl   (%ebx), %eax
        popl    %ebp
        popl    %ebx

        ret

ferdig0:
        movl    $0, %eax
        popl    %ebp
        popl    %ebx

        ret
