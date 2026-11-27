package com.taskflow.service;

import com.taskflow.domain.TaskItem;

import java.util.List;

public interface TaskSortStrategy {
    String getName();
    List<TaskItem> sort(List<TaskItem> tasks);
}
