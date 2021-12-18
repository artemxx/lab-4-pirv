set -x -e

input_dir="$1"
filter_file="$2"

mkdir -p my_classes
CLASSPATH=$(hadoop classpath) javac -d my_classes WordFilter.java

cd my_classes
jar cvf x.jar *
mv x.jar ..
cd ..

hdfs dfs -mkdir -p /user/root/
hdfs dfs -rm -r -f /user/root/input
hdfs dfs -rm -r -f /user/root/output
hdfs dfs -put -f "${input_dir}" /user/root/input

hadoop jar x.jar WordFilter input output "${filter_file}"

hdfs dfs -get -f /user/root/output/* .
# cat part-r-00000
