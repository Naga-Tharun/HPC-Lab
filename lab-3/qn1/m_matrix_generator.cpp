#include <iostream>
#include <stdlib.h>
#include <string>
#include <fstream>
using namespace std;

int main(int argc, char *argv[]){
    int size1 = atoi(argv[1]);
    int size2 = atoi(argv[2]);
    string filename = argv[3];

    ofstream fout(filename);
    for (int i = 0; i < size1; i++)
    {
        for (int j = 0; j < size2; j++)
        {
            int random = (rand() % 300);
            fout << random << " ";
        }
        fout << endl;
    }
    fout.close();
}