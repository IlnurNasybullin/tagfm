echo ">> Start gradle (install dist)"
gradle :cli:installDist

assembly_root="./sh-assembly"

sh "$assembly_root/install-slf4j-nop.sh"
sh "$assembly_root/patch-slf4j-add-nop.sh"
sh "$assembly_root/patch-all-modules.sh"
sh "$assembly_root/jpackage-windows-exe.sh"
