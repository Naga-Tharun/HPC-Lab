# README

This folder contains four sub-folders - `qn1`, `qn2`, `qn3`, and `qn4`. Each sub-folder contains Java code files and their respective verification files. The purpose of these code files is to implement different types of locking mechanisms in concurrent programming.

## Folder Structure

- `qn1`: Contains `HierarchicalBackoffLock.java` and `HierarchicalBackoffverify.java` files
- `qn2`: Contains `HierarchicalMCSLock.java` and `HierarchicalMCSLockverify.java` files
- `qn3`: Contains `QueueLockWithTimeout.java` and `QueueLockWithTimeoutverify.java` files
- `qn4`: Contains `CohortLock.java` and `CohortLockverify.java` files
- `run.sh`: This script file is present in every sub-folder and is used to run the code files.

## How to Run

To run the code files, navigate to the sub-folder of your choice and execute the following command in the terminal:

```
bash run.sh
```

This will compile and run the Java code files, generating the output in a file named `output.dat` in the same sub-folder. The verification files can also be executed in the same way to verify the correctness of the locking mechanisms implemented.

## Files Description

- `HierarchicalBackoffLock.java` - Implements the Hierarchical Backoff Locking mechanism.
- `HierarchicalBackoffverify.java` - Verifies the correctness of the Hierarchical Backoff Locking mechanism.
- `HierarchicalMCSLock.java` - Implements the Hierarchical MCS Locking mechanism.
- `HierarchicalMCSLockverify.java` - Verifies the correctness of the Hierarchical MCS Locking mechanism.
- `QueueLockWithTimeout.java` - Implements the Queue Locking mechanism with a timeout feature.
- `QueueLockWithTimeoutverify.java` - Verifies the correctness of the Queue Locking mechanism with a timeout feature.
- `CohortLock.java` - Implements the Cohort Locking mechanism.
- `CohortLockverify.java` - Verifies the correctness of the Cohort Locking mechanism.

## Output

After executing the code files using `run.sh`, the output will be generated in a file named `output.dat` in the same sub-folder. The output file contains the results of the code execution and can be used to verify the correctness of the implemented locking mechanisms.
