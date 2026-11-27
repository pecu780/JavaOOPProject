package com.taskflow.ui;

import com.taskflow.domain.InvalidTaskException;
import com.taskflow.domain.TaskItem;
import com.taskflow.service.TaskService;
import com.taskflow.service.TitleSortStrategy;
import com.taskflow.service.UrgencySortStrategy;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final TaskService service;
    private final Scanner scanner;

    public ConsoleUI(TaskService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        System.out.println("=== TaskFlow: " + service.getBoardName() + " ===");

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> listTasks();
                case "2" -> handleAddSimpleTask();
                case "3" -> handleAddDeadlineTask();
                case "4" -> handleMarkDone();
                case "5" -> handleDeleteTask();
                case "6" -> handleSwitchStrategy();
                case "7" -> handleSave();
                case "0" -> {
                    handleSave();
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please choose 0-7.");
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("--- MENU (Sort: " + service.getCurrentStrategyName() + ") ---");
        System.out.println("1. List tasks");
        System.out.println("2. Add simple task");
        System.out.println("3. Add deadline task");
        System.out.println("4. Mark task as done");
        System.out.println("5. Delete task");
        System.out.println("6. Switch sorting strategy");
        System.out.println("7. Save tasks");
        System.out.println("0. Save and exit");
        System.out.print("Choose an option: ");
    }

    private void listTasks() {
        List<TaskItem> tasks = service.getSortedTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        System.out.println();
        System.out.println("Tasks sorted by: " + service.getCurrentStrategyName());
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).formatDetails());
        }
    }

    private void handleAddSimpleTask() {
        try {
            System.out.print("Task title: ");
            String title = scanner.nextLine();
            int priority = readInt("Priority (1-5): ");
            service.addSimpleTask(title, priority);
            System.out.println("Simple task added.");
        } catch (InvalidTaskException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }

    private void handleAddDeadlineTask() {
        try {
            System.out.print("Task title: ");
            String title = scanner.nextLine();
            int priority = readInt("Priority (1-5): ");
            System.out.print("Due date (YYYY-MM-DD): ");
            LocalDate dueDate = LocalDate.parse(scanner.nextLine().trim());
            service.addDeadlineTask(title, priority, dueDate);
            System.out.println("Deadline task added.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD (e.g., 2026-12-01).");
        } catch (InvalidTaskException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }

    private void handleMarkDone() {
        listTasks();
        if (service.getSortedTasks().isEmpty()) {
            return;
        }
        try {
            int number = readInt("Enter task number to mark as done: ");
            service.markTaskDone(number);
            System.out.println("Task marked as done.");
        } catch (InvalidTaskException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleDeleteTask() {
        listTasks();
        if (service.getSortedTasks().isEmpty()) {
            return;
        }
        try {
            int number = readInt("Enter task number to delete: ");
            service.deleteTask(number);
            System.out.println("Task deleted.");
        } catch (InvalidTaskException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void handleSwitchStrategy() {
        System.out.println("Select sorting strategy:");
        System.out.println("1. Urgency (Highest First)");
        System.out.println("2. Title (A-Z)");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();
        if ("1".equals(choice)) {
            service.setSortStrategy(new UrgencySortStrategy());
            System.out.println("Sorting strategy set to: " + service.getCurrentStrategyName());
        } else if ("2".equals(choice)) {
            service.setSortStrategy(new TitleSortStrategy());
            System.out.println("Sorting strategy set to: " + service.getCurrentStrategyName());
        } else {
            System.out.println("Invalid choice. Strategy unchanged.");
        }
    }

    private void handleSave() {
        try {
            service.save();
            System.out.println("Tasks saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save tasks: " + e.getMessage());
        }
    }

    private int readInt(String prompt) throws InvalidTaskException {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new InvalidTaskException("Expected a number, got: '" + input + "'");
        }
    }
}
