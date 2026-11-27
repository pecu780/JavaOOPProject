package com.taskflow.persistence;

import com.taskflow.domain.InvalidTaskException;
import com.taskflow.domain.TaskItem;

import java.io.IOException;
import java.util.List;

public interface TaskRepository {
    void save(List<TaskItem> tasks) throws IOException;
    List<TaskItem> load() throws IOException, InvalidTaskException;
}
