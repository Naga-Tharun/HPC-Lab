#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

#define MIN(a, b) (((a) < (b)) ? (a) : (b))
#define MAX(a, b) (((a) > (b)) ? (a) : (b))

void matrix_mult(int n, double *A, double *B, double *C, int block_size)
{
    int i, j, k, ii, jj, kk;
#pragma omp parallel for private(i, j, k, ii, jj, kk)
    for (ii = 0; ii < n; ii += block_size)
        for (jj = 0; jj < n; jj += block_size)
            for (kk = 0; kk < n; kk += block_size)
            {
                // printf("--------------\n");

                for (i = ii; i < MIN(ii + block_size, n); i++)
                    for (j = jj; j < MIN(jj + block_size, n); j++)
                        for (k = kk; k < MIN(kk + block_size, n); k++)
                            C[i * n + j] += A[i * n + k] * B[k * n + j];
            }
}

void matrix_power(int n, double *A, int power, double *result, int block_size)
{
    int i, j, p;
    double *temp = (double *)malloc(n * n * sizeof(double));

    for (i = 0; i < n * n; i++)
        result[i] = 0;

    for (p = 0; p < power; p++)
    {
        // #pragma omp parallel for private(i,j)
        for (i = 0; i < n * n; i++)
            temp[i] = A[i];

        matrix_mult(n, temp, A, result, block_size);
    }

    free(temp);
}

int main(int argc, char *argv[])
{
    int i, j, n, power, block_size, num_threads;
    double *A, *result;
    double start_time, end_time;
    
    // for (n = 512; n <= 2048; n *= 2)
    // {
        n = 512;
        for (power = 2; power <= 16; power++)
        {
            for (block_size = 4; block_size <= 64; block_size *= 2)
            {
                for (num_threads = 2; num_threads <= 16; num_threads += 2)
                {
                    A = (double *)malloc(n * n * sizeof(double));
                    result = (double *)malloc(n * n * sizeof(double));

                    for (i = 0; i < n * n; i++)
                        A[i] = (double)rand() / RAND_MAX;

                    omp_set_num_threads(num_threads);

                    start_time = omp_get_wtime();
                    
                    matrix_power(n, A, power, result, block_size);
                    end_time = omp_get_wtime();
                    // #pragma omp barrier
                    FILE* fp = fopen("base.csv","a");
                    fprintf(fp,"%d,%d,%d,%lf\n",n,power,num_threads,end_time - start_time);
                    fclose(fp);
                    // size,power,block,thread,
                    free(A);
                    free(result);
                }
            }
        }
                    return 0;
    // } 

//  n = 512;
//     power = 2;
//     block_size = 2;
//     num_threads = 5;

//     A = (double *)malloc(n * n * sizeof(double));
//     result = (double *)malloc(n * n * sizeof(double));

//     for (i = 0; i < n * n; i++)
//         A[i] = (double)rand() / RAND_MAX;

//     // omp_set_num_threads(num_threads);

//     start_time = omp_get_wtime();
//     //int n, double *A, int power, double *result, int block_size
//     matrix_power(n,A,power,result,block_size);
//     end_time = omp_get_wtime();
//     printf("%lf",end_time-start_time);

   
}