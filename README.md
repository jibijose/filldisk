./mvnw clean package

java -jar target/filldisk-1.0.0-SNAPSHOT.jar -d /System/Volumes/Data -f 10 -r TUNEDRANDOM -t 12

mkdir DUMPDIR3
sed -i 's/a/3/g' *
rm -rf *.bak