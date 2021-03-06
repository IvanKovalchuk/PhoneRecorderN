package com.kivsw.phonerecorder.model.task_executor.tasks;

/**
 * class represents a phone task
 * it MUST be executed vie TaskExecutor class
 */

public interface ITask {
    boolean startTask(); // return true if a new task has been started
    void stopTask();     // notify the task it must be stopped
}
