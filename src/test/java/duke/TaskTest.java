package duke;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/** Tests task status changes and user-facing task formatting. */
class TaskTest {
    @Test
    void status_newTaskIsNotDone_markAndUnmarkUpdateStatus() {
        Task task = new Task("read book");

        assertEquals(" ", task.getStatusIcon());
        task.markAsDone();
        assertEquals("X", task.getStatusIcon());
        task.markAsNotDone();
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    void toString_todoIncludesDescriptionAndStatus() {
        Task task = new Task("read book");

        assertEquals("[T][ ] read book", task.toString());
        task.markAsDone();
        assertEquals("[T][X] read book", task.toString());
    }

    @Test
    void toString_deadlineIncludesFormattedDateTime() {
        Task task = new Task("return book", LocalDateTime.of(2019, 12, 2, 18, 0));

        assertEquals("[D][ ] return book (by: Dec 2 2019 6:00 PM)", task.toString());
    }

    @Test
    void toString_eventIncludesStartAndEndDetails() {
        Task task = new Task(TaskType.EVENT, "project meeting", null,
                "Mon 2pm", "4pm");

        assertEquals("[E][ ] project meeting (from: Mon 2pm to: 4pm)", task.toString());
    }
}
