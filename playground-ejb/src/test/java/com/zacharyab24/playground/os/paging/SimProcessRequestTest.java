package com.zacharyab24.playground.os.paging;

import com.zacharyab24.comp2240.paging.model.SimProcess;
import com.zacharyab24.playground.os.paging.model.SimProcessRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SimProcessRequestTest {

    @Test
    void toSimProcess_mapsAllFields() {
        SimProcessRequest dto = new SimProcessRequest(1, "Process1", List.of(1, 2, 3));

        SimProcess process = dto.toSimProcess();

        assertThat(process.getPid()).isEqualTo(1);
        assertThat(process.getName()).isEqualTo("Process1");
        assertThat(process.getPages()).containsExactly(1, 2, 3);
    }

    @Test
    void toSimProcess_pagesAreMutableCopy() {
        List<Integer> pages = List.of(1, 2, 3);
        SimProcessRequest dto = new SimProcessRequest(1, "test", pages);

        SimProcess process = dto.toSimProcess();
        process.getPages().add(4);

        // Original list is unaffected
        assertThat(pages).hasSize(3);
    }
}