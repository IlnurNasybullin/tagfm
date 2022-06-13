slf4j_nop_dir="./bundles/libs"
jar_file="slf4j-nop-1.7.29.jar"

maven_url="https://repo.maven.apache.org/maven2"
groupId="org/slf4j"
artifactId="slf4j-nop"
version="1.7.29"

if [ ! -d "$slf4j_nop_dir" ]
then
  mkdir "$slf4j_nop_dir"
fi

if [ ! -f "$slf4j_nop_dir/$jar_file" ]
then
  curl --output "$slf4j_nop_dir/$jar_file" "$maven_url/$groupId/$artifactId/$version/$jar_file"\
  echo ">> $jar_file was installed"
else
  echo ">> $jar_file has already installed"
fi