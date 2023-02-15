#!/bin/bash
set autoscale
set term pdf size 8,10
set output "RTM.pdf"
set xlabel "Threads" font "Bold,9" offset 0,1
set ylabel "Runtime (seconds)" font "Bold,9" offset 3,0
set xtics font "Verdana,7"
set ytics font "Bold,7"
set key font "Bold,5" sample 0.3
set boxwidth 1
set style data histogram 
set style histogram cluster gap 1
set style fill solid border -1

do for[k in "4 8 16 32 64"] {
    set multiplot layout 2,2 title "Runtime vs Threads by fixing the Matrix size for block size ".k
    set title "Runtime vs Threads with Matrix size 512" font "Bold,9" offset 1.0,-0.5
    plot for[j=2:16] "plot_data/RTM/block_".k."/matrix_512/RTM_".j.".dat" using 5:xtic(3) title "power ".j
    set title "Runtime vs Threads with Matrix size 1024" font "Bold,9" offset 1.0,-0.5
    plot for[j=2:16] "plot_data/RTM/block_".k."/matrix_1024/RTM_".j.".dat" using 5:xtic(3) title "power ".j
    set title "Runtime vs Threads with Matrix size 2048" font "Bold,9" offset 1.0,-0.5
    plot for[j=2:16] "plot_data/RTM/block_".k."/matrix_2048/RTM_".j.".dat" using 5:xtic(3) title "power ".j
}

unset multiplot