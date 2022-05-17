module_name=$1
jar_name=$2

echo ">> Compile module.info for $module_name"
javac -p "./cli/build/install/cli/lib" \
  -d "./cli/build/modules/$module_name" \
  --patch-module $module_name=./cli/build/install/cli/lib/$jar_name \
  bundles/$module_name/versions/17/module-info.java

echo ">> Update jar (add module-info.class) for $module_name"
jar uf "./cli/build/install/cli/lib/$jar_name" \
  -C "./cli/build/modules/$module_name" module-info.class