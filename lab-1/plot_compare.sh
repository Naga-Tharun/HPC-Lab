#!/bin/bash
set autoscale
set term pdf size 8,5
set output "plot_compare.pdf"

set xlabel "Threads" font "Bold,9" offset 0,1
set ylabel "Runtime (seconds)" font "Bold,9" offset 3,0
set xtics font "Verdana,7"
set ytics font "Bold,7"
set key font "Bold,5" sample 0.3
set boxwidth 1
set style data histogram 
set style histogram cluster gap 1
set style fill solid border -1


do for [i=2:16] {
    set multiplot layout 2,2 title "Comparing the runtime for all the 4 methods implemented"

    set title "Runtime vs threads for matrix size: 512 and power: ".i font "Bold,9" offset 1.0,-0.5
    plot "qn1/plot_data/RTM/matrix_512/RTM_".i.".dat" using 4:xtic(3) title "OMM",\
    "qn3/plot_data/RTM/matrix_512/RTM_".i.".dat" using 4:xtic(3) title "OMM transpose",\
    "qn2/plot_data/RTM/block_32/matrix_512/RTM_".i.".dat" using 5:xtic(3) title "BMM",\
    "qn4/plot_data/RTM/block_32/matrix_512/RTM_".i.".dat" using 5:xtic(3) title "BMM transpose"

    set title "Runtime vs threads for matrix size: 1024 and power: ".i font "Bold,9" offset 1.0,-0.5
    plot "qn1/plot_data/RTM/matrix_1024/RTM_".i.".dat" using 4:xtic(3) title "OMM",\
    "qn3/plot_data/RTM/matrix_1024/RTM_".i.".dat" using 4:xtic(3) title "OMM transpose",\
    "qn2/plot_data/RTM/block_32/matrix_1024/RTM_".i.".dat" using 5:xtic(3) title "BMM",\
    "qn4/plot_data/RTM/block_32/matrix_1024/RTM_".i.".dat" using 5:xtic(3) title "BMM transpose"

    set title "Runtime vs threads for matrix size: 2048 and power: ".i font "Bold,9" offset 1.0,-0.5
    plot "qn1/plot_data/RTM/matrix_2048/RTM_".i.".dat" using 4:xtic(3) title "OMM",\
    "qn3/plot_data/RTM/matrix_2048/RTM_".i.".dat" using 4:xtic(3) title "OMM transpose",\
    "qn2/plot_data/RTM/block_32/matrix_2048/RTM_".i.".dat" using 5:xtic(3) title "BMM",\
    "qn4/plot_data/RTM/block_32/matrix_2048/RTM_".i.".dat" using 5:xtic(3) title "BMM transpose"
}
unset multiplot