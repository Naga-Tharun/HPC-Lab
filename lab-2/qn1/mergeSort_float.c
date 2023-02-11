#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <omp.h>

#define N 40000000

void merge(float *a, float *temp, int left, int mid, int right)
{
    int i, j, k;
    i = left;
    j = mid + 1;
    k = 0;

    while (i <= mid && j <= right)
    {
        if (a[i] < a[j])
        {
            temp[k] = a[i];
            i++;
        }
        else
        {
            temp[k] = a[j];
            j++;
        }
        k++;
    }

    while (i <= mid)
    {
        temp[k] = a[i];
        i++;
        k++;
    }

    while (j <= right)
    {
        temp[k] = a[j];
        j++;
        k++;
    }

    for (i = left, k = 0; i <= right; i++, k++)
        a[i] = temp[k];
}

void bottom_up_merge_sort(float *a, int n)
{
    float *temp = (float *)malloc(n * sizeof(float));
    for (int width = 1; width < n; width = 2 * width)
    {
#pragma omp parallel for schedule(static)
        for (int i = 0; i < n; i = i + 2 * width)
        {
            int left = i;
            int mid = i + width - 1;
            int right = i + 2 * width - 1;
            if (mid >= n)
                mid = n - 1;
            if (right >= n)
                right = n - 1;
            merge(a, temp, left, mid, right);
        }
    }
    free(temp);
}

int main()
{

    float *a = (float *)malloc(N * sizeof(float));
    struct timeval start, end;
    double elapsed_time;
    int num_threads;

    // Seed the random number generator
    srand(time(NULL));

    // Generate random numbers and store them in the array
    for (int i = 0; i < N; i++)
        a[i] = (float)rand()/(float)RAND_MAX;

    // Sort the array using bottom-up merge sort
    for (num_threads = 1; num_threads <= 16; num_threads++)
    {
        omp_set_num_threads(num_threads);
        gettimeofday(&start, NULL);
        bottom_up_merge_sort(a, N);
        gettimeofday(&end, NULL);
        elapsed_time = (end.tv_sec - start.tv_sec) * 1000.0;
        elapsed_time += (end.tv_usec - start.tv_usec) / 1000.0;
        printf("%d\t%f\n", num_threads, elapsed_time);
    }

    return 0;
}
