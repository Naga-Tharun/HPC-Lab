#include <iostream>
#include <fstream>
#include <vector>
#include <omp.h>
#include <math.h>
#include <stdlib.h>
#include <cstring>
#include <limits.h>
#include <algorithm>

using namespace std;

void merge(int *X, int n, int *tmp)
{
    int i = 0;
    int j = n / 2;
    int ti = 0;

    while (i < n / 2 && j < n)
    {
        if (X[i] < X[j])
        {
            tmp[ti] = X[i];
            ti++;
            i++;
        }
        else
        {
            tmp[ti] = X[j];
            ti++;
            j++;
        }
    }
    while (i < n / 2)
    {
        tmp[ti] = X[i];
        ti++;
        i++;
    }
    while (j < n)
    {
        tmp[ti] = X[j];
        ti++;
        j++;
    }

    memcpy(X, tmp, n * sizeof(int));
}

void merge_sort(int *X, int n, int *tmp)
{
    if (n < 2)
    {
        return;
    }

#pragma omp task firstprivate(X, n, tmp)
    merge_sort(X, n / 2, tmp);

#pragma omp task firstprivate(X, n, tmp)
    merge_sort(X + (n / 2), n - (n / 2), tmp);

// wait untill the above two statements complete execution
#pragma omp taskwait

    // merging sorted halves to one
    merge(X, n, tmp);
}

void construct_binary_image(vector<vector<int>> M, vector<vector<int>> &B, double p)
{
    int rows = M.size();
    int cols = M[0].size();
    int *a = (int *)malloc(rows * cols * sizeof(int));
    int *tmp = (int *)malloc(rows * cols * sizeof(int));
    // vector<int> a(rows*cols);

    int i, j;
#pragma omp parallel for collapse(2)
    for (i = 0; i < rows; i++)
    {
        for (j = 0; j < cols; j++)
        {
            a[i * cols + j] = M[i][j];
        }
    }

    merge_sort(a, rows * cols, tmp);

#pragma omp parallel for collapse(2)
    for (i = 0; i < rows; i++)
    {
        for (j = 0; j < cols; j++)
        {
            int index = rows * cols - 1 - floor(p / 100.0 * rows * cols);
            if (index == -1)
            {
                B[i][j] = 1;
            }
            else
            {
                if (a[index] > M[i][j])
                {
                    B[i][j] = 0;
                }
                else
                {
                    B[i][j] = 1;
                }
            }
        }
    }
}

int main(int argc, char *argv[])
{
    int r = atoi(argv[1]);
    int c = atoi(argv[2]);
    int p = atoi(argv[4]);
    string input_file = argv[3];
    string output_file = argv[5];

    // Read input matrix from file
    vector<vector<int>> M(r, vector<int>(c));
    ifstream fin(input_file);
    for (int i = 0; i < r; i++)
    {
        for (int j = 0; j < c; j++)
        {
            fin >> M[i][j];
        }
    }
    fin.close();

    vector<vector<int>> B(r, vector<int>(c));
    for (int num_threads = 1; num_threads <= 16; num_threads += 2)
    {
        // B.clear();
        omp_set_num_threads(num_threads);
        double start_time = omp_get_wtime();
        // Compute binary image
        construct_binary_image(M, B, p);
        double end_time = omp_get_wtime();
        cout << r << "\t" << c << "\t" << p << "\t" << num_threads << "\t" << end_time - start_time << "\n";
        num_threads = (num_threads == 1) ? 0 : num_threads;
    }
    // Write output matrix to file
    ofstream fout(output_file);
    for (int i = 0; i < r; i++)
    {
        for (int j = 0; j < c; j++)
        {
            fout << B[i][j] << " ";
        }
        fout << endl;
    }
    fout.close();

    return 0;
}