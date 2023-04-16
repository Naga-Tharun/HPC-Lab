#!/bin/bash

clear

javac peterson_lock.java
javac peterson_lock_verify.java

java peterson_lock_verify

rm -rf peterson_lock.class peterson_lock_verify.class