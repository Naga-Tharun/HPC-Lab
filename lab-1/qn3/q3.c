#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <omp.h>
#include <string.h>
#include <sys/time.h>

#define MIN_MATRIX_SIZE 512
#define MAX_MATRIX_SIZE 2048
#define MIN_POWER 2
#define MAX_POWER 16

void matrix_mult(double *A, double *B, double *C, int n)
{
    int i, j, k;
    double sum;

#pragma omp parallel for private(i, j, k, sum)
    for (i = 0; i < n; i++)
    {
        for (j = 0; j < n; j++)
        {
            sum = 0.0;
            for (k = 0; k < n; k++)
            {
                sum += A[i * n + k] * B[j * n + k];
            }
            C[i * n + j] = sum;
        }
    }
}

void matrix_pow(double *A, double *B, int n, int power)
{
    int i;
    double *C = (double *)malloc(n * n * sizeof(double));

    for (i = 0; i < power - 1; i++)
    {
        matrix_mult(A, B, C, n);
        memcpy(A, C, n * n * sizeof(double));
    }

    free(C);
}

int main(int argc, char *argv[])
{
    int i, j, k;
    int n, power;
    double *A, *B;
    double start_time, end_time, elapsed;

    n = atoi(argv[1]);

    // for (n = MIN_MATRIX_SIZE; n <= MAX_MATRIX_SIZE; n *= 2)
    // {
    A = (double *)malloc(n * n * sizeof(double));
    B = (double *)malloc(n * n * sizeof(double));
    for (i = 0; i < n * n; i++)
    {
        A[i] = (double)rand() / RAND_MAX;
    }
    for (i = 0; i < n; ++i) // finds the transpose of the matrix B
    {
        for (int j = 0; j < n; ++j)
        {
            B[j * n + i] = A[i * n + j];
        }
    }
    printf("\n");
    for (power = MIN_POWER; power <= MAX_POWER; power++)
    {
        for (int th = 1; th <= 16; th += 2)
        {
            struct timeval tv1, tv2;
            struct timezone tz;
            gettimeofday(&tv1, &tz);
            omp_set_num_threads(th);
            matrix_pow(A, B, n, power);
            gettimeofday(&tv2, &tz);
            elapsed = (double)(tv2.tv_sec - tv1.tv_sec) + (double)(tv2.tv_usec - tv1.tv_usec) * 1.e-6;

            printf("%d\t%d\t%d\t%lf\n", n, power, th, elapsed);
            if (th == 1)
            {
                th = 0;
            }
        }
    }

    free(A);
    free(B);
    // }

    return 0;
}