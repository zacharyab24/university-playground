package com.zacharyab24.playground.os.concurrency;

import com.zacharyab24.comp2240.concurrency.common.TaskResult;
import com.zacharyab24.playground.os.concurrency.model.TaskResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskResponseTest {

    @Test
    void fromTaskResult_mapsAllFields() {
        TaskResult taskResult = new TaskResult(1, 5, 25, 50L);

        TaskResponse response = TaskResponse.fromTaskResult("tasks1.txt", taskResult);

        assertThat(response.taskFile()).isEqualTo("tasks1.txt");
        assertThat(response.threadNo()).isEqualTo(1);
        assertThat(response.input()).isEqualTo(5);
        assertThat(response.result()).isEqualTo(25);
        assertThat(response.completedAtMillis()).isEqualTo(50L);
    }
}