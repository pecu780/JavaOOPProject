package com.taskflow.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskBoard {
    private final String name;
    private final List<TaskItem> tasks;

    public TaskBoard(String name) {
        this.name = (name == null || name.isBlank()) ? "Personal Board" : name.trim();
        this.tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addTask(TaskItem task) throws InvalidTaskException {
        if (task == null) {
            throw new InvalidTaskException("Task cannot be null.");
        }
        tasks.add(task);
    }

    public void removeTask(TaskItem task) throws InvalidTaskException {
        if (!tasks.remove(task)) {
            throw new InvalidTaskException("Task not found on board.");
        }
    }

    public List<TaskItem> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public void replaceAll(List<TaskItem> loadedTasks) {
        tasks.clear();
        tasks.addAll(loadedTasks);
    }
}
