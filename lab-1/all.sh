#!/bin/bash

for i in {2..4};
do
  cd qn$i
  python3 segregate.py
  gnuplot plot_rmt.sh
  gnuplot plot_rtm.sh
  cd ..
done