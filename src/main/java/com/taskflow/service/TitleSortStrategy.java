package com.taskflow.service;

import com.taskflow.domain.TaskItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TitleSortStrategy implements TaskSortStrategy {

    @Override
    public String getName() {
        return "Title (A-Z)";
    }

    @Override
    public List<TaskItem> sort(List<TaskItem> tasks) {
        List<TaskItem> sorted = new ArrayList<>(tasks);
        sorted.sort(Comparator.comparing(TaskItem::getTitle, String.CASE_INSENSITIVE_ORDER));
        return sorted;
    }
}
