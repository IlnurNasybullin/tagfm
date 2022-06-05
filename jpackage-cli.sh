echo ">> Start jpackage"

mainModuleName="tagfm.cli"
mainClassName="io.github.ilnurnasybullin.tagfm.cli.TagFM"

jpackage --type exe \
  --name "tagfm" \
  --description "Tagged File Manager" \
  --vendor "Open Source" \
  --app-version "0.0.1" \
  --module-path "./cli/build/install/cli/lib;$JAVA_HOME/jmods" \
  --module "$mainModuleName/$mainClassName" \
  --win-console \
  --win-dir-chooser