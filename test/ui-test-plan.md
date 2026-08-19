# Furina UI test plan

The runner compares program output exactly. User-entered command lines are recorded as console input and are not expected in the program-output blocks because a terminal echoes them.

## Test case 1: Greeting and exit

Aim: Verify that Furina greets the user and exits after `bye`.

Input:

```text
bye
```

Expected output:

```text
____________________________________________________________
    F U R I N A
Hello! I'm Furina.
What can I do for you?
____________________________________________________________
____________________________________________________________
    Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case 2: Task types and listing

Aim: Verify todo, deadline, and event tasks retain their details and display correctly in a list.

Input:

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

Expected output:

```text
____________________________________________________________
    F U R I N A
Hello! I'm Furina.
What can I do for you?
____________________________________________________________
____________________________________________________________
    Got it. I've added this task:
      [T][ ] borrow book
    Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
    Got it. I've added this task:
      [D][ ] return book (by: Sunday)
    Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
    Got it. I've added this task:
      [E][ ] project meeting (from: Mon 2pm to: 4pm)
    Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
    Here are the tasks in your list:
    1.[T][ ] borrow book
    2.[D][ ] return book (by: Sunday)
    3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
    Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case 3: Mark and unmark

Aim: Verify that task completion can be set and reversed without changing the task description.

Input:

```text
todo read book
todo return book
mark 1
unmark 1
list
bye
```

Expected output:

```text
____________________________________________________________
    F U R I N A
Hello! I'm Furina.
What can I do for you?
____________________________________________________________
____________________________________________________________
    Got it. I've added this task:
      [T][ ] read book
    Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
    Got it. I've added this task:
      [T][ ] return book
    Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
    Nice! I've marked this task as done:
      [T][X] read book
____________________________________________________________
____________________________________________________________
    OK, I've marked this task as not done yet:
      [T][ ] read book
____________________________________________________________
____________________________________________________________
    Here are the tasks in your list:
    1.[T][ ] read book
    2.[T][ ] return book
____________________________________________________________
____________________________________________________________
    Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case 4: Blank input and invalid task number

Aim: Verify blank lines are ignored and invalid status commands do not crash the program.

Input:

```text

   
todo read book
mark nope
unmark 99
list
bye
```

Expected output:

```text
____________________________________________________________
    F U R I N A
Hello! I'm Furina.
What can I do for you?
____________________________________________________________
____________________________________________________________
    Got it. I've added this task:
      [T][ ] read book
    Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
    Please provide a valid task number.
____________________________________________________________
____________________________________________________________
    That task does not exist.
____________________________________________________________
____________________________________________________________
    Here are the tasks in your list:
    1.[T][ ] read book
____________________________________________________________
____________________________________________________________
    Bye. Hope to see you again soon!
____________________________________________________________
```
