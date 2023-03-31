# **Concurrent Double Linked List with the following synchronization techniques:**

1. **Coarse-Grain Synchronization**
2. **Fine-Grain Synchronization**
3. **Optimistic Synchronization**
4. **Lazy Synchronization**

**With the following workloads:**

**100C-0I-0D
70C-20I-10D
50C-25I-25D
30C-35I-35D
0C-50I-50D**

There are 4 folders, corresponding to each synchronization technique. To execute the required java file, go the corresponding synchronization technique directory and run the script file using the following command.

`bash run.sh`

Example:

```bash
cd CoarseGrain
bash run.sh
```

The above will generate a folder runtime_data, which will contain multiple subdirectories containing three different keys range: 2 × 10^5, 2 × 10^6, and 2 × 10^7.

Each of the subfolders contain .dat files with the output and execution time.
