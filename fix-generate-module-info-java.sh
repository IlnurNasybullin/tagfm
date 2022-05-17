module_name=$1
jar_name=$2

echo ">> Generate module-info.java for $module_name"
libs=./cli/build/install/cli/lib
jdeps --module-path "$JAVA_HOME/jmods;$libs" \
  --generate-module-info "./bundles/" \
  --multi-release 17 \
  $libs/$jar_name