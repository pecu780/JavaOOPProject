package com.taskflow.service;

import com.taskflow.domain.TaskItem;

import java.util.ArrayList;
import java.util.List;

public class UrgencySortStrategy implements TaskSortStrategy {

    @Override
    public String getName() {
        return "Urgency (Highest First)";
    }

    @Override
    public List<TaskItem> sort(List<TaskItem> tasks) {
        List<TaskItem> sorted = new ArrayList<>(tasks);
        sorted.sort((a, b) -> Integer.compare(b.calculateUrgencyScore(), a.calculateUrgencyScore()));
        return sorted;
    }
}
