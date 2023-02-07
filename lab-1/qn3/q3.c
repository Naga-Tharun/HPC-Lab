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

void matrix_multiplication_transpose(double *A, double *B, double *C, int n)
{
    int var_i, var_j, var_k;
    double sum;

#pragma omp parallel for private(var_i, var_j, var_k, sum)
    for (var_i = 0; var_i < n; var_i++)
    {
        for (var_j = 0; var_j < n; var_j++)
        {
            sum = 0.0;
            for (var_k = 0; var_k < n; var_k++)
            {
                sum += A[var_i * n + var_k] * B[var_j * n + var_k];
            }
            C[var_i * n + var_j] = sum;
        }
    }
}

void matrix_pow(double *A, double *B, int n, int power)
{
    int var_i;
    double *C = (double *)malloc(n * n * sizeof(double));

    for (var_i = 0; var_i < power - 1; var_i++)
    {
        matrix_multiplication_transpose(A, B, C, n);
        memcpy(A, C, n * n * sizeof(double));
    }

    free(C);
}

int main(int argc, char *argv[])
{
    int var_i, var_j, var_k;
    int n, power;
    double *A, *B;
    double start_time, end_time, elapsed;

    n = atoi(argv[1]);
    A = (double *)malloc(n * n * sizeof(double));
    B = (double *)malloc(n * n * sizeof(double));
    for (var_i = 0; var_i < n * n; var_i++)
    {
        A[var_i] = (double)rand() / RAND_MAX;
    }
    for (var_i = 0; var_i < n; ++var_i) // finds the transpose of the matrix B
    {
        for (int var_j = 0; var_j < n; ++var_j)
        {
            B[var_j * n + var_i] = A[var_i * n + var_j];
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

    return 0;
}