#include <stdio.h>
#include <unistd.h>
#include <sys/syscall.h>

#ifdef __cplusplus
extern "C"{
#endif

int tp_syscall(int number,...);

#ifdef __cplusplus
}
#endif

void test_print_hello(const char* msg)
{
    printf("msg:%s\n", msg);
    
}

int my_clock_gettime(int id, struct timespec* ts)
{
#if defined(__arm__)
    return tp_syscall(__NR_clock_gettime,id,ts);
#elif defined(__i386__)
    return tp_syscall(__NR_clock_gettime,id,ts);
#elif defined(__arm64__)||defined(__aarch64__)
    return tp_syscall(__NR_clock_gettime,id,ts);
#else
    return syscall(__NR_clock_gettime,id,ts);
#endif
    
}