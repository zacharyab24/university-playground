package com.zacharyab24.playground.os.scheduler;

import com.zacharyab24.comp2240.scheduling.model.Result;
import com.zacharyab24.playground.os.scheduler.model.SchedulerResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SchedulerResponseTest {

    @Test
    void fromResult_mapsAllFields() {
        Result result = new Result("FCFS", "17.20", "13.00");

        SchedulerResponse response = SchedulerResponse.fromResult(result);

        assertThat(response.processorID()).isEqualTo("FCFS");
        assertThat(response.turnAround()).isEqualTo("17.20");
        assertThat(response.waitingTime()).isEqualTo("13.00");
    }
}