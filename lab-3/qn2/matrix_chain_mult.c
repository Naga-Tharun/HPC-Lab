#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

#define MAX_SIZE 100

int main()
{
    int n = 10;
    int p[MAX_SIZE] = {1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};
    // int p[MAX_SIZE] = {10, 10, 10, 10, 10, 10, 10, 10, 10, 10};

    int m[MAX_SIZE][MAX_SIZE];
    int s[MAX_SIZE][MAX_SIZE];

    int i, j, k, len;

    // Initialize matrices
    int ***a = (int ***)malloc(n * sizeof(int **));
    int ***b = (int ***)malloc(n * sizeof(int **));
    int ***c = (int ***)malloc(n * sizeof(int **));
    for (i = 0; i < n; i++)
    {
        a[i] = (int **)malloc(p[i] * sizeof(int *));
        b[i] = (int **)malloc(p[i] * sizeof(int *));
        c[i] = (int **)malloc(p[i] * sizeof(int *));
        for (j = 0; j < p[i]; j++)
        {
            a[i][j] = (int *)malloc(p[i + 1] * sizeof(int));
            b[i][j] = (int *)malloc(p[i + 1] * sizeof(int));
            c[i][j] = (int *)malloc(p[i + 1] * sizeof(int));
            for (k = 0; k < p[i + 1]; k++)
            {
                a[i][j][k] = rand() % 10;
                b[i][j][k] = rand() % 10;
                c[i][j][k] = 0;
            }
        }
    }

    double start_time, end_time;

    // Compute minimum number of multiplications using dynamic programming
    for (len = 2; len <= n; len++)
    {
        for (i = 1; i <= n - len + 1; i++)
        {
            j = i + len - 1;
            m[i][j] = MAX_SIZE * MAX_SIZE;
            for (k = i; k <= j - 1; k++)
            {
                int cost = m[i][k] + m[k + 1][j] + p[i - 1] * p[k] * p[j];
                if (cost < m[i][j])
                {
                    m[i][j] = cost;
                    s[i][j] = k;
                }
            }
        }
    }

    for (int num_threads = 1; num_threads <= 16; num_threads += 2)
    {
        omp_set_num_threads(num_threads);
        // Multiply matrices using OpenMP
        start_time = omp_get_wtime();
#pragma omp parallel for private(i, j, k) shared(a, b, c, p, n, s)
        for (len = 2; len <= n; len++)
        {
            for (i = 1; i <= n - len + 1; i++)
            {
                j = i + len - 1;
                for (k = i; k <= j - 1; k++)
                {
                    int ii, jj, kk;
                    for (ii = 0; ii < p[i - 1]; ii++)
                    {
                        for (jj = 0; jj < p[j]; jj++)
                        {
                            for (kk = 0; kk < p[k]; kk++)
                            {
                                c[i - 1][ii][jj] += a[i - 1][ii][kk] * b[k - 1][kk][jj];
                            }
                        }
                    }
                }
            }
        }
        end_time = omp_get_wtime();
        printf("%d\t%f\n", num_threads, end_time - start_time);
        if (num_threads == 1)
        {
            num_threads = 0;
        }
    }
    // Free memory
    for (i = 0; i < n; i++)
    {
        for (j = 0; j < p[i]; j++)
        {
            free(a[i][j]);
            free(b[i][j]);
            free(c[i][j]);
        }
        free(a[i]);
        free(b[i]);
        free(c[i]);
    }
    free(a);
    free(b);
    free(c);

    return 0;
}
