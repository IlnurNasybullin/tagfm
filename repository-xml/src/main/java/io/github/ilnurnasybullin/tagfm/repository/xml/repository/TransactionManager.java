package io.github.ilnurnasybullin.tagfm.repository.xml.repository;

public class TransactionManager implements AutoCloseable {

    private final Runnable rollback;
    private boolean isSuccess;

    public TransactionManager(Runnable commit, Runnable rollback) {
        this.rollback = rollback;
        commit.run();
    }

    public void acknowledge() {
        isSuccess = true;
    }

    @Override
    public void close() {
        if (!isSuccess) {
            rollback.run();
        }
    }

}
