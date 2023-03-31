# HPC LAB 3

**How to run:**

Open the directory corresponding to the respective question labeled as qn1, qn2,.

For example, qn1.

In qn1 directory, run q1.sh using bash script by

```
bash run.sh
```

This creates data dump files by running the threshold and matrix generator programs.

The runtime.dat and time_X.dat are generated with output values from the given programs

We plot the graphs using plot.sh.

```
gnuplot plot.sh
```

The plots are available in the file plots.pdf

**Question 1 (Threshold):**

Given a r x c matrix of integers called M which can be treated as an image. Given M, you should construct a binary image B such that Bij = 1 if no more than p percentage of pixels in M are greater than Mij.

Programs:

- m_matrix_generator.cpp
- threshold.cpp

Outputs:

- runtime_data.dat
- time_8192_8192.dat

Plot in plots.pdf


**Question 2 (Matrix Chain multiplication):**

Given a chain of n matrices (i.e., A1, A2, A3, ..., An) and dimensions (rows and columns) of the matrices are p0 × p1, p1 × p2, p2 × p3 ... pn−1 × pn, respectively. Compute the product A1, A2, A3, ..., An, and measure the speedup.

Programs:

- matrix_chain_mult[.](https://github.com/Naga-Tharun/HPC-Lab/blob/master/lab-3/qn2/matrix_chain_mult.c "matrix_chain_mult.c")c

Outputs:

- output.dat

Plot in plots.pdf


**Question 3 (Conway's game of life):**

Conway's game of life is a cellular automaton where the game depends on the initial configuration and nothing else. The initial
configuration is two dimensional grid of cells each of which is either dead or alive. The game proceeds in steps where every cell interacts with the vertical, horizontal or diagonal neighbors and decides on its status in the next step.

Programs:

- seed_generator.cpp
- game_of_life.cpp

Outputs:

- output.dat

Plot in plots.pdf
