package io.github.ilnurnasybullin.tagfm.cli.command;

import jakarta.inject.Singleton;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;

@Singleton
@CommandLine.Command(name = "parent")
public class BindParentCommand implements Runnable {

    @CommandLine.Parameters(index = "0", arity = "1")
    private String parentTag;

    @CommandLine.Parameters(index = "1", split = ",")
    private final List<String> childTag = new ArrayList<>();

    @Override
    public void run() {

    }
}
