package com.taskflow;

import com.taskflow.domain.DeadlineTask;
import com.taskflow.domain.InvalidTaskException;
import com.taskflow.domain.SimpleTask;
import com.taskflow.domain.TaskBoard;
import com.taskflow.domain.TaskItem;
import com.taskflow.persistence.FileTaskRepository;
import com.taskflow.service.TaskService;
import com.taskflow.service.TitleSortStrategy;
import com.taskflow.service.UrgencySortStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    @Test
    void testPolymorphicUrgencyCalculation() throws InvalidTaskException {
        TaskItem simple = new SimpleTask("Read notes", 3);
        TaskItem deadline = new DeadlineTask("Submit assignment", 3, LocalDate.now().plusDays(1));

        assertEquals(30, simple.calculateUrgencyScore());
        assertEquals(60, deadline.calculateUrgencyScore());
        assertTrue(deadline.calculateUrgencyScore() > simple.calculateUrgencyScore());
    }

    @Test
    void testDomainValidationThrowsCustomException() {
        assertThrows(InvalidTaskException.class, () -> new SimpleTask("", 3));
        assertThrows(InvalidTaskException.class, () -> new SimpleTask("Bad;Title", 3));
        assertThrows(InvalidTaskException.class, () -> new SimpleTask("Valid", 0));
        assertThrows(InvalidTaskException.class, () -> new SimpleTask("Valid", 6));
        assertThrows(InvalidTaskException.class, () -> new DeadlineTask("Valid", 3, null));
    }

    @Test
    void testStrategyPatternSwitchesSortingBehavior(@TempDir Path tempDir) throws Exception {
        TaskService service = new TaskService(
                new TaskBoard("Test Board"),
                new FileTaskRepository(tempDir.resolve("tasks.txt")),
                new UrgencySortStrategy()
        );

        service.addSimpleTask("Zebra task", 5);
        service.addSimpleTask("Alpha task", 1);

        List<TaskItem> byUrgency = service.getSortedTasks();
        assertEquals("Zebra task", byUrgency.get(0).getTitle());

        service.setSortStrategy(new TitleSortStrategy());
        List<TaskItem> byTitle = service.getSortedTasks();
        assertEquals("Alpha task", byTitle.get(0).getTitle());
    }

    @Test
    void testPersistenceSaveAndLoad(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("tasks.txt");
        FileTaskRepository repo = new FileTaskRepository(file);

        TaskService writerService = new TaskService(new TaskBoard("Board 1"), repo, new UrgencySortStrategy());
        writerService.addSimpleTask("Buy groceries", 2);
        writerService.addDeadlineTask("Finish Prog 2", 5, LocalDate.of(2026, 11, 29));
        writerService.markTaskDone(1); // Marks "Finish Prog 2" (highest urgency)
        writerService.save();

        TaskService readerService = new TaskService(new TaskBoard("Board 2"), repo, new TitleSortStrategy());
        readerService.load();

        List<TaskItem> loaded = readerService.getSortedTasks();
        assertEquals(2, loaded.size());
        assertEquals("Buy groceries", loaded.get(0).getTitle());
        assertFalse(loaded.get(0).isDone());
        assertEquals("Finish Prog 2", loaded.get(1).getTitle());
        assertTrue(loaded.get(1).isDone());
        assertInstanceOf(DeadlineTask.class, loaded.get(1));
    }

    @Test
    void testInvalidIndexThrowsException(@TempDir Path tempDir) {
        TaskService service = new TaskService(
                new TaskBoard("Empty"),
                new FileTaskRepository(tempDir.resolve("empty.txt")),
                new UrgencySortStrategy()
        );

        assertThrows(InvalidTaskException.class, () -> service.markTaskDone(1));
        assertThrows(InvalidTaskException.class, () -> service.deleteTask(1));
    }
}
