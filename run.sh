#!/bin/bash

# In case of need to recompile
mvn clean package

java -cp target/jtask-tracker-1.0-SNAPSHOT.jar com.tracker.jtask.Main $*
