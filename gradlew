#!/usr/bin/env sh
echo "This project expects Gradle to be available on the runner.
If you want to use the Gradle wrapper, add the wrapper files.
Attempting to run 'gradle'..."
gradle "$@"
