#include <iostream>
#include <stdlib.h>
#include <omp.h>
#include <time.h>
#include <fstream>

using namespace std;

int count_neighbors(int **grid, int rows, int cols, int i, int j)
{
    int neighbor_count = 0;

    // Count the number of neighbors that are alive
    for (int x = -1; x <= 1; x++)
    {
        for (int y = -1; y <= 1; y++)
        {
            int row = (i + x + rows) % rows;
            int col = (j + y + cols) % cols;
            neighbor_count += grid[row][col];
        }
    }
    neighbor_count -= grid[i][j];
    return neighbor_count;
}

void update_grid(int **grid, int **next, int rows, int cols)
{
// Update the state of the grid according to the rules of the Game of Life
#pragma omp parallel for
    for (int i = 0; i < rows; i++)
    {
        for (int j = 0; j < cols; j++)
        {
            int neighbor_count = count_neighbors(grid, rows, cols, i, j);

            if (grid[i][j] == 1 && (neighbor_count < 2 || neighbor_count > 3))
            {
                next[i][j] = 0;
            }
            else if (grid[i][j] == 0 && neighbor_count == 3)
            {
                next[i][j] = 1;
            }
            else
            {
                next[i][j] = grid[i][j];
            }
        }
    }
}

int main(int argc, char *argv[])
{
    int r = atoi(argv[2]);
    int c = atoi(argv[3]);
    int steps = atoi(argv[4]);
    string input_file = argv[1];
    string output_file = argv[5];
    double start_time, end_time;

    // Allocating memory for the grid and next arrays
    int **grid = (int **)malloc(r * sizeof(int *));
    int **next = (int **)malloc(r * sizeof(int *));

    for (int i = 0; i < r; i++)
    {
        grid[i] = (int *)malloc(c * sizeof(int));
        next[i] = (int *)malloc(c * sizeof(int));
    }

    for (int num_threads = 1; num_threads <= 16; num_threads += 2)
    {
        // Initialize the grid
        // Read input seed from file
        ifstream fin(input_file);
        for (int i = 0; i < r; i++)
        {
            for (int j = 0; j < c; j++)
            {
                fin >> grid[i][j];
            }
        }
        fin.close();

        start_time = omp_get_wtime();
        omp_set_num_threads(num_threads);

        // Update the grid for the specified number of steps
        for (int k = 0; k < steps; k++)
        {
            update_grid(grid, next, r, c);

            // // Copy the next state of the grid to the current state
            // int **temp = grid;
            // grid = next;
            // next = temp;

            for(int p=0; p<r; p++){
                for(int q=0; q<c; q++){
                    grid[p][q] = next[p][q];
                }
            }
        }

        end_time = omp_get_wtime();

        cout << num_threads << "\t" << end_time - start_time << "\n";
        if (num_threads == 1)
        {
            num_threads = 0;
        }
    }

    // Write final seed to file
    ofstream fout(output_file);
    for (int i = 0; i < r; i++)
    {
        for (int j = 0; j < c; j++)
        {
            fout << next[i][j] << " ";
        }
        fout << endl;
    }
    fout.close();

    // Free memory allocated for the grid and next arrays
    for (int i = 0; i < r; i++)
    {
        free(grid[i]);
        free(next[i]);
    }
    free(grid);
    free(next);

    return 0;
}