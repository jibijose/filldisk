# Introduction
Utility to fill random data in drive (or drive till quota is over)

# Build status   
[![Build Status](https://ci.appveyor.com/api/projects/status/github/jibijose/filldisk?branch=master&svg=true)](https://ci.appveyor.com/project/jibijose/filldisk)   
[![Github actions build](https://github.com/jibijose/filldisk/actions/workflows/maven.yml/badge.svg)](https://github.com/jibijose/filldisk/actions/workflows/maven.yml)   
[![Build Status](https://dev.azure.com/jibijose/github-code/_apis/build/status%2Ffilldisk%2Fmaven-builds?branchName=master)](https://dev.azure.com/jibijose/github-code/_build/latest?definitionId=7&branchName=master)
https://dev.azure.com/jibijose/github-code/_apis/build/status%2Ffilldisk%2Fmaven-builds?branchName=master   


# Build setup

## Requirements
| Software      | Version |        Verify |                                                Comments |
|---------------|:-------:|--------------:|--------------------------------------------------------:|
| java          |   11+   | java -version | Any version 11 or above<br/>Check appveyor build status |

## Build locally
```
./mvnw clean package
```
Runnable jar target\dirhashfiles-1.0.0-shaded.jar is created.

# Usage
java -jar target/filldisk-1.0.0-SNAPSHOT.jar


-t <arg>    Threads  
-f <arg>    Fill type [4|10]
-r <arg>    Random mode [STATIC|TUNEDRANDOM|RANDOM]  
-d <arg>    Drive/dir to fill data  

# Examples
```
java -Xms1G -Xmx4G -jar target/filldisk-1.0.0-SNAPSHOT.jar -t 4 -f  4 -r STATIC -d C 
java -Xms1G -Xmx4G -jar target/filldisk-1.0.0-SNAPSHOT.jar -t 2 -f 10 -r TUNEDRANDOM -d C:\
java -Xms1G -Xmx4G -jar target/filldisk-1.0.0-SNAPSHOT.jar -t 2 -f  4 -r RANDOM -d "C:\\JJ"
```

# Commands   
```
mkdir DUMPDIR3   
sed -i 's/a/3/g' *   
rm -rf *.bak   
```