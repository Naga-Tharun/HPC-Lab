#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

#define MIN(a, b) (((a) < (b)) ? (a) : (b))
#define MAX(a, b) (((a) > (b)) ? (a) : (b))

void block_matrix_multiplication_transpose(int n, double *A, double *B, double *C, int block_size)
{
    int var_i, var_j, var_k, var_ii, var_jj, var_kk;
#pragma omp parallel for private(var_i, var_j, var_k, var_ii, var_jj, var_kk)
    for (var_ii = 0; var_ii < n; var_ii += block_size)
        for (var_jj = 0; var_jj < n; var_jj += block_size)
            for (var_kk = 0; var_kk < n; var_kk += block_size)
            {
                for (var_i = var_ii; var_i < MIN(var_ii + block_size, n); var_i++)
                    for (var_j = var_jj; var_j < MIN(var_jj + block_size, n); var_j++)
                        for (var_k = var_kk; var_k < MIN(var_kk + block_size, n); var_k++)
                            C[var_i * n + var_j] += A[var_i * n + var_k] * B[var_j * n + var_k]; // Row by Row
            }
}

void matrix_power(int n, double *A, int power, double *result, int block_size)
{
    int var_i, var_j, present_power;
    double *temp = (double *)malloc(n * n * sizeof(double));

    for (var_i = 0; var_i < n * n; var_i++)
        result[var_i] = 0;

    for (present_power = 0; present_power < power; present_power++)
    {
        for (var_i = 0; var_i < n; ++var_i)
        {
            for (int var_j = 0; var_j < n; ++var_j)
            {
                temp[var_j * n + var_i] = A[var_i * n + var_j]; // transpose matix
            }
        }

        block_matrix_multiplication_transpose(n, temp, A, result, block_size);
    }

    free(temp);
}

int main(int argc, char *argv[])
{
    int var_i, var_j, n, power, block_size, num_threads;
    double *A, *result;
    double start_time, end_time;

    n = atoi(argv[1]);
    for (power = 2; power <= 16; power++)
    {
        for (block_size = 4; block_size <= 64; block_size *= 2)
        {
            for (num_threads = 1; num_threads <= 16; num_threads += 2)
            {
                A = (double *)malloc(n * n * sizeof(double));
                result = (double *)malloc(n * n * sizeof(double));

                for (var_i = 0; var_i < n * n; var_i++)
                    A[var_i] = (double)rand() / RAND_MAX;

                omp_set_num_threads(num_threads);

                start_time = omp_get_wtime();

                matrix_power(n, A, power, result, block_size);
                end_time = omp_get_wtime();
                printf("%d\t%d\t%d\t%d\t%lf\n", n, power, num_threads, block_size, end_time - start_time);

                free(A);
                free(result);

                if (num_threads == 1)
                {
                    num_threads = 0;
                }
            }
        }
    }

    return 0;
}