#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <omp.h>
#include <limits.h>

int min_number_multiplications(int order[], int n)
{
    long long int dp[n][n];

    int j, size, i, k, x;

    for (int i = 1; i < n; i++)
    {
        dp[i][i] = 0;
    }

    for (size = 2; size < n; size++)
    {
#pragma omp parallel for shared(dp, order, n, size) private(j, i, k, x)
        for (i = 1; i < n - size + 1; i++)
        {
            j = i + size - 1;
            dp[i][j] = INT_MAX;
            for (k = i; k <= j - 1; k++)
            {
                x = dp[i][k] + dp[k + 1][j] + order[i - 1] * order[k] * order[j];
                if (x < dp[i][j])
                {
                    dp[i][j] = x;
                }
            }
        }
    }

    return dp[1][n - 1];
}

int main()
{
    srand(time(NULL));
    struct timeval start, end;
    double elapsed_time;
    int num_threads;

    int n = 1000;
    int *order = (int *)malloc(n * sizeof(int));

    for (int i = 0; i < n; i++)
    {
        order[i] = rand() % 10 + 1;
    }

    for (num_threads = 1; num_threads <= 16; num_threads += 2)
    {
        omp_set_num_threads(num_threads);
        gettimeofday(&start, NULL);
        long long int ans = min_number_multiplications(order, n);
        gettimeofday(&end, NULL);
        elapsed_time = (end.tv_sec - start.tv_sec) * 1000.0;
        elapsed_time += (end.tv_usec - start.tv_usec) / 1000.0;

        printf("%d\t%lld\t%f\n", num_threads, ans, elapsed_time);
        if (num_threads == 1)
        {
            num_threads = 0;
        }
    }
}