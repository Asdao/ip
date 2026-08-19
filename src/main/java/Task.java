/**
 * Represents one task in Furina's in-memory task list.
 */
public class Task {
    protected TaskType type;
    protected String description;
    protected boolean isDone;
    protected String by;
    protected String from;
    protected String to;

    /**
     * Creates a task that is initially not done.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this(TaskType.TODO, description, null, null, null);
    }

    /**
     * Creates a task with a type and optional deadline or event details.
     *
     * @param type the task type
     * @param description the text describing the task
     * @param by the deadline text, if this is a deadline task
     * @param from the event start text, if this is an event task
     * @param to the event end text, if this is an event task
     */
    public Task(TaskType type, String description, String by, String from, String to) {
        this.type = type;
        this.description = description;
        this.isDone = false;
        this.by = by;
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the status marker used when displaying this task.
     *
     * @return X when done, or a blank space when not done
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Marks this task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the task in the format used by the chatbot.
     *
     * @return the status marker and task description
     */
    @Override
    public String toString() {
        String details = "";
        if (type == TaskType.DEADLINE) {
            details = " (by: " + by + ")";
        } else if (type == TaskType.EVENT) {
            details = " (from: " + from + " to: " + to + ")";
        }
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] "
                + description + details;
    }
}
