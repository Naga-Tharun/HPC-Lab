#include <iostream>
#include <vector>
#include <omp.h>

using namespace std;

vector<vector<int>> sol;

bool isSafe(vector<int>& board, int row, int col) {
    // Check the column
    for (int i = 0; i < row; i++) {
        if (board[i] == col) {
            return false;
        }
    }

    // Check the diagonal
    for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
        if (board[i] == j) {
            return false;
        }
    }

    // Check the other diagonal
    for (int i = row - 1, j = col + 1; i >= 0 && j < board.size(); i--, j++) {
        if (board[i] == j) {
            return false;
        }
    }

    return true;
}

void solveNQueens(int n, int row, vector<int> board, int& solutions) {
    if (row == n) {

        #pragma omp critical
        {
            solutions++;
            sol.push_back(board);
        }
        return;
    }

    // Try placing a queen in each column of the current row
    #pragma omp parallel for reduction(+:solutions)
    for (int col = 0; col < n; col++) {
        vector<int> new_board = board;
        if (isSafe(new_board, row, col)) {
            new_board[row] = col;
            solveNQueens(n, row + 1, new_board, solutions);
        }
    }
}

int main() {

    freopen("output.txt", "w", stdout);
    
    vector<int> board(16, -1); // Initialize board with -1 to represent empty cells
    int solutions = 0;

    for (int n = 10; n <= 16; n += 2) {
        for (int num_threads = 1; num_threads <= 16; num_threads+=2)
        {
            sol.clear();
            omp_set_num_threads(num_threads);
            solutions = 0;
            double start_time = omp_get_wtime();
            solveNQueens(n, 0, board, solutions);
            double end_time = omp_get_wtime();
            cout << n << "\t" << num_threads << "\t" << solutions <<"\t"<< end_time - start_time << "\n";
            num_threads=(num_threads==1)?0:num_threads;
        }
        cout<<"\nQueen positions for different solutions: \n";
        // these positions are represented in a 1D array of size n
        // each element in the array represent the position of queen in each column of the row(respective index of the element)
        for(int i=0;i<sol.size();i++)
        {
            for(int j=0;j<n;j++)
            {
                cout<<sol[i][j]<<"\t";
            }
            cout<<"\n";
        }
        cout<<"\n";

    }

    return 0;
}