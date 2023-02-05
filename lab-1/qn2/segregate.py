import os

os.system("mkdir plot_data")

os.system("mkdir plot_data/RMT")
# Enable only for first time, then disable
for i in [4, 8, 16, 32, 64]:
  cmd = "mkdir plot_data/RMT/block_" + str(i)
  os.system(cmd)
  cmd = "mkdir plot_data/RMT/block_" + str(i) + "/thread_1"
  os.system(cmd)
  for j in range(2, 17, 2):
    cmd = "mkdir plot_data/RMT/block_" + str(i) + "/thread_" + str(j)
    os.system(cmd)

with open("dump.dat") as f:
  for line in f:
    tmp = line.split("\n")[0]
    tp = tmp.split("\t")

    cmd = "echo \"" + tmp + "\" >> plot_data/RMT/block_" + tp[3] + "/thread_" + tp[2] + "/RMT_" + tp[1] + ".dat"
    os.system(cmd)


os.system("mkdir plot_data/RTM")
# Enable only for first time, then disable
for i in [4, 8, 16, 32, 64]:
  cmd = "mkdir plot_data/RTM/block_" + str(i)
  os.system(cmd)
  for j in [512, 1024, 2048]:
    cmd = "mkdir plot_data/RTM/block_" + str(i) + "/matrix_" + str(j)
    os.system(cmd)

with open("dump.dat") as f:
  for line in f:
    tmp = line.split("\n")[0]
    tp = tmp.split("\t")

    cmd = "echo \"" + tmp + "\" >> plot_data/RTM/block_" + tp[3] + "/matrix_" + tp[0] + "/RTM_" + tp[1] + ".dat"
    os.system(cmd)