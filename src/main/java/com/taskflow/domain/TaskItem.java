package com.taskflow.domain;

public abstract class TaskItem {
    private final String title;
    private final int priority;
    private boolean done;

    protected TaskItem(String title, int priority) throws InvalidTaskException {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidTaskException("Task title cannot be empty.");
        }
        if (title.contains(";")) {
            throw new InvalidTaskException("Task title cannot contain a semicolon (;).");
        }
        if (priority < 1 || priority > 5) {
            throw new InvalidTaskException("Priority must be between 1 and 5.");
        }
        this.title = title.trim();
        this.priority = priority;
        this.done = false;
    }

    public String getTitle() {
        return title;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isDone() {
        return done;
    }

    public void markDone() {
        this.done = true;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public abstract String getTaskType();

    public abstract int calculateUrgencyScore();

    public abstract String toStorageString();

    public String formatDetails() {
        String status = done ? "[X]" : "[ ]";
        return String.format("%s [%s] %s (Priority: %d, Urgency: %d)",
                status, getTaskType(), title, priority, calculateUrgencyScore());
    }
}
