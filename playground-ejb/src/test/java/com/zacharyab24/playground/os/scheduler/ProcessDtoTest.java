package com.zacharyab24.playground.os.scheduler;

import com.zacharyab24.comp2240.scheduling.model.Process;
import com.zacharyab24.playground.os.scheduler.model.ProcessDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessDtoTest {

    @Test
    void toProcess_mapsAllFields() {
        ProcessDto dto = new ProcessDto("p1", 3, 10, 5);

        Process process = dto.toProcess();

        assertThat(process.getProcessorID()).isEqualTo("p1");
        assertThat(process.getArrivalTime()).isEqualTo(3);
        assertThat(process.getServiceTime()).isEqualTo(10);
        assertThat(process.getPriority()).isEqualTo(5);
    }
}