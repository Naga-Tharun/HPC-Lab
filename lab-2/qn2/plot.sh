#!/bin/bash

set autoscale
set term pdf size 13,3
set output "plots.pdf"
set xlabel "Number of threads" font "Bold,9" offset 0,1
set ylabel "Runtime (seconds)" font "Bold,9" offset 3,0
set xtics font "Verdana,7"
set ytics font "Bold,7"
set key font "Bold,5" sample 0.3
set boxwidth 1
set style data histogram
set style histogram cluster gap 1
set style fill solid border -1

set multiplot layout 1,4 title "Runtime vs threads for different board sizes"

set title "Runtime vs threads for 10x10 board" font "Bold,9" offset 1.0,-0.5
plot "plot_data/data_10.dat" using 4:xtic(2) title "10x10"

set title "Runtime vs threads for 12x12 board" font "Bold,9" offset 1.0,-0.5
plot "plot_data/data_12.dat" using 4:xtic(2) title "12x12"

set title "Runtime vs threads for 14x14 board" font "Bold,9" offset 1.0,-0.5
plot "plot_data/data_14.dat" using 4:xtic(2) title "14x14"

set title "Runtime vs threads for 16x16 board" font "Bold,9" offset 1.0,-0.5
plot "plot_data/data_16.dat" using 4:xtic(2) title "16x16"

unset multiplot