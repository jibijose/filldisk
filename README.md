[![Build Status](https://ci.appveyor.com/api/projects/status/github/jibijose/filldisk?branch=master&svg=true)](https://ci.appveyor.com/project/jibijose/filldisk)

# Introduction
Utility to fill random data in drive (or drive till quota is over)

# Build setup

## Requirements
| Software      | Version |        Verify |                                               Comments |
|---------------|:-------:|--------------:|-------------------------------------------------------:|
| java          |   8+    | java -version | Any version 8 or above<br/>Check appveyor build status |

## Build locally
```
./mvnw clean package
```
Runnable jar target\dirhashfiles-1.0.0-shaded.jar is created.

# Usage
java -jar target/filldisk-1.0.0-SNAPSHOT.jar

-d <arg>    Drive/dir to fill data
-f <arg>    Left side, mandatory for comparehash mode  
-r <arg>    Random mode [TUNEDRANDOM]  
-t <arg>    Threads   

# Commands   
mkdir DUMPDIR3
sed -i 's/a/3/g' *
rm -rf *.bak