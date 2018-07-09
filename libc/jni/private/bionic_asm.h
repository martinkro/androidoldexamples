#ifndef PRIVATE_BIONIC_ASM_H
#define PRIVATE_BIONIC_ASM_H

#define MAX_ERRNO 4095

#define __bionic_asm_custom_entry(f)
#define __bionic_asm_custom_end(f)
#define __bionic_asm_function_type @function

#include <machine/asm.h>

#define SAFE_ENTRY(f) \
    .text; \
    .globl f; \
    _ALIGN_TEXT; \
    .type f, __bionic_asm_function_type; \
    f: \
    __bionic_asm_custom_entry(f); \
    .cfi_startproc \

#define SAFE_END(f) \
    .cfi_endproc; \
    .size f, .-f; \
    __bionic_asm_custom_end(f) \

/* Like ENTRY, but with hidden visibility. */
#define SAFE_ENTRY_PRIVATE(f) \
    ENTRY(f); \
    .hidden f \
    
#endif
