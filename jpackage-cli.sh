echo ">> Start jpackage"
jpackage --type exe \
  --name "tagfm" \
  --description "Tagged File Manager" \
  --vendor "Open Source" \
  --app-version "0.0.1" \
  --module-path "./cli/build/install/cli/lib;$JAVA_HOME/jmods" \
  --module "tagfm.cli/io.github.ilnurnasybullin.tagfm.cli.FileManagerCli" \
  --win-console \
  --win-dir-chooser