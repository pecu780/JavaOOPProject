package com.taskflow.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DeadlineTask extends TaskItem {
    private final LocalDate dueDate;

    public DeadlineTask(String title, int priority, LocalDate dueDate) throws InvalidTaskException {
        super(title, priority);
        if (dueDate == null) {
            throw new InvalidTaskException("Due date cannot be null.");
        }
        this.dueDate = dueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    @Override
    public String getTaskType() {
        return "DEADLINE";
    }

    @Override
    public int calculateUrgencyScore() {
        if (isDone()) {
            return 0;
        }
        int baseScore = getPriority() * 10;
        long daysUntilDue = ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
        if (daysUntilDue < 0) {
            return baseScore + 50;
        } else if (daysUntilDue <= 3) {
            return baseScore + 30;
        } else if (daysUntilDue <= 7) {
            return baseScore + 15;
        }
        return baseScore + 5;
    }

    @Override
    public String formatDetails() {
        return super.formatDetails() + " | Due: " + dueDate;
    }

    @Override
    public String toStorageString() {
        return getTaskType() + ";" + getTitle() + ";" + getPriority() + ";" + isDone() + ";" + dueDate;
    }
}
