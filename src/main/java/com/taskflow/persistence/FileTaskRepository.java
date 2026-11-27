package com.taskflow.persistence;

import com.taskflow.domain.DeadlineTask;
import com.taskflow.domain.InvalidTaskException;
import com.taskflow.domain.SimpleTask;
import com.taskflow.domain.TaskItem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FileTaskRepository implements TaskRepository {
    private final Path filePath;

    public FileTaskRepository(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public void save(List<TaskItem> tasks) throws IOException {
        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (TaskItem task : tasks) {
                writer.write(task.toStorageString());
                writer.newLine();
            }
        }
    }

    @Override
    public List<TaskItem> load() throws IOException, InvalidTaskException {
        List<TaskItem> loaded = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return loaded;
        }
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                loaded.add(parseLine(line));
            }
        }
        return loaded;
    }

    private TaskItem parseLine(String line) throws InvalidTaskException {
        String[] parts = line.split(";");
        if (parts.length < 4) {
            throw new InvalidTaskException("Corrupted data line: " + line);
        }

        String type = parts[0];
        String title = parts[1];
        int priority;
        try {
            priority = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            throw new InvalidTaskException("Invalid priority value in file: " + parts[2]);
        }
        boolean done = Boolean.parseBoolean(parts[3]);

        TaskItem item;
        if ("SIMPLE".equals(type)) {
            item = new SimpleTask(title, priority);
        } else if ("DEADLINE".equals(type) && parts.length >= 5) {
            try {
                LocalDate dueDate = LocalDate.parse(parts[4]);
                item = new DeadlineTask(title, priority, dueDate);
            } catch (DateTimeParseException e) {
                throw new InvalidTaskException("Invalid due date in file: " + parts[4]);
            }
        } else {
            throw new InvalidTaskException("Unknown task type in file: " + type);
        }

        item.setDone(done);
        return item;
    }
}
