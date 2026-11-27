package com.taskflow;

import com.taskflow.domain.InvalidTaskException;
import com.taskflow.domain.TaskBoard;
import com.taskflow.persistence.FileTaskRepository;
import com.taskflow.service.TaskService;
import com.taskflow.service.UrgencySortStrategy;
import com.taskflow.ui.ConsoleUI;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        TaskBoard board = new TaskBoard("My Personal Tasks");
        FileTaskRepository repository = new FileTaskRepository(Path.of("data", "tasks.txt"));
        TaskService service = new TaskService(board, repository, new UrgencySortStrategy());

        try {
            service.load();
        } catch (IOException | InvalidTaskException e) {
            System.out.println("Warning: Could not load existing tasks: " + e.getMessage());
        }

        ConsoleUI ui = new ConsoleUI(service);
        ui.start();
    }
}
