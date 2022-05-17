echo ">> Fix all modules"
modules=(
"io.micronaut.core" "io.micronaut.inject"
"java.activation" "java.annotation"
"org.slf4j"
"org.yaml.snakeyaml"
)
jars=(
"micronaut-core-3.4.3.jar" "micronaut-inject-3.4.3.jar"
"javax.activation-api-1.2.0.jar" "javax.annotation-api-1.3.2.jar"
"slf4j-api-1.7.29.jar"
"snakeyaml-1.30.jar"
)

for i in ${!modules[@]}; do
  sh ./fix-jar-modules-cli.sh ${modules[$i]} ${jars[$i]}
done