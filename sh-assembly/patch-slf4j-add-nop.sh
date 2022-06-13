echo ">> Fix slf4j-api - add package org.slf4j.impl from slf4j-nop lib"
jar xf "./bundles/libs/slf4j-nop-1.7.29.jar"

libs_root="./cli/build/install/cli/lib"
slf4j_impl="./org/slf4j/impl"

jar uf "$libs_root/slf4j-api-1.7.29.jar" \
    "$slf4j_impl/StaticLoggerBinder.class" \
    "$slf4j_impl/StaticMarkerBinder.class" \
    "$slf4j_impl/StaticMDCBinder.class"

rm -rf "./org"
rm -rf "./META-INF"