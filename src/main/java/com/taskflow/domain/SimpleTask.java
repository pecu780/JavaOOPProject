package com.taskflow.domain;

public class SimpleTask extends TaskItem {

    public SimpleTask(String title, int priority) throws InvalidTaskException {
        super(title, priority);
    }

    @Override
    public String getTaskType() {
        return "SIMPLE";
    }

    @Override
    public int calculateUrgencyScore() {
        return isDone() ? 0 : getPriority() * 10;
    }

    @Override
    public String toStorageString() {
        return getTaskType() + ";" + getTitle() + ";" + getPriority() + ";" + isDone();
    }
}
