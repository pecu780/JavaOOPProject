package com.taskflow.service;

import com.taskflow.domain.DeadlineTask;
import com.taskflow.domain.InvalidTaskException;
import com.taskflow.domain.SimpleTask;
import com.taskflow.domain.TaskBoard;
import com.taskflow.domain.TaskItem;
import com.taskflow.persistence.TaskRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class TaskService {
    private final TaskBoard board;
    private final TaskRepository repository;
    private TaskSortStrategy sortStrategy;

    public TaskService(TaskBoard board, TaskRepository repository, TaskSortStrategy sortStrategy) {
        this.board = board;
        this.repository = repository;
        this.sortStrategy = sortStrategy;
    }

    public String getBoardName() {
        return board.getName();
    }

    public String getCurrentStrategyName() {
        return sortStrategy.getName();
    }

    public void setSortStrategy(TaskSortStrategy sortStrategy) {
        if (sortStrategy != null) {
            this.sortStrategy = sortStrategy;
        }
    }

    public void addSimpleTask(String title, int priority) throws InvalidTaskException {
        board.addTask(new SimpleTask(title, priority));
    }

    public void addDeadlineTask(String title, int priority, LocalDate dueDate) throws InvalidTaskException {
        board.addTask(new DeadlineTask(title, priority, dueDate));
    }

    public List<TaskItem> getSortedTasks() {
        return sortStrategy.sort(board.getTasks());
    }

    public void markTaskDone(int oneBasedIndex) throws InvalidTaskException {
        TaskItem target = getByDisplayedIndex(oneBasedIndex);
        target.markDone();
    }

    public void deleteTask(int oneBasedIndex) throws InvalidTaskException {
        TaskItem target = getByDisplayedIndex(oneBasedIndex);
        board.removeTask(target);
    }

    public void save() throws IOException {
        repository.save(board.getTasks());
    }

    public void load() throws IOException, InvalidTaskException {
        List<TaskItem> loaded = repository.load();
        board.replaceAll(loaded);
    }

    private TaskItem getByDisplayedIndex(int oneBasedIndex) throws InvalidTaskException {
        List<TaskItem> sorted = getSortedTasks();
        int index = oneBasedIndex - 1;
        if (index < 0 || index >= sorted.size()) {
            throw new InvalidTaskException("Invalid task number: " + oneBasedIndex);
        }
        return sorted.get(index);
    }
}
