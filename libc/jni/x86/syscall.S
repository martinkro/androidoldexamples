.text
.globl tp_syscall
.align 4
tp_syscall:
    .cfi_startproc
    # Push the callee save registers.
    push    %ebx
    push    %esi
    push    %edi
    push    %ebp

    # Load all the arguments from the calling frame.
    # (Not all will be valid, depending on the syscall.)
    mov     20(%esp),%eax
    mov     24(%esp),%ebx
    mov     28(%esp),%ecx
    mov     32(%esp),%edx
    mov     36(%esp),%esi
    mov     40(%esp),%edi
    mov     44(%esp),%ebp

    # Make the system call.
    int     $0x80

    # Error?
    cmpl    $-4095, %eax
    jb      1f
    # Yes, so set errno.
    negl    %eax
    pushl   %eax
    call    __safe_set_errno_internal
    addl    $4, %esp
1:
    # Restore the callee save registers.
    pop    %ebp
    pop    %edi
    pop    %esi
    pop    %ebx
    ret
    .cfi_endproc;
    .size tp_syscall,.-tp_syscall
