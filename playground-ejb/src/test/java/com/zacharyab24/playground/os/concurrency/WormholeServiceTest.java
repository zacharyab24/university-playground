package com.zacharyab24.playground.os.concurrency;

import com.zacharyab24.comp2240.concurrency.wormhole.CrossingResult;
import com.zacharyab24.playground.os.concurrency.model.WormholeRequest;
import com.zacharyab24.playground.os.concurrency.model.WormholeResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WormholeServiceTest {

    @Test
    void runSimulation_returnsTotalCrossings() throws InterruptedException {
        WormholeRequest request = new WormholeRequest(1, 1, 2);

        WormholeResponse response = WormholeService.runSimulation(request);

        // 1 human * 2 trips + 1 alien * 2 trips = 4 crossings
        assertThat(response.totalCrossings()).isEqualTo(4);
    }

    @Test
    void runSimulation_crossingsMatchTotal() throws InterruptedException {
        WormholeRequest request = new WormholeRequest(2, 1, 1);

        WormholeResponse response = WormholeService.runSimulation(request);

        assertThat(response.crossings()).hasSize(response.totalCrossings());
    }

    @Test
    void runSimulation_travellerIdsAreCorrect() throws InterruptedException {
        WormholeRequest request = new WormholeRequest(2, 1, 1);

        WormholeResponse response = WormholeService.runSimulation(request);

        assertThat(response.crossings())
                .extracting(CrossingResult::travellerId)
                .containsExactlyInAnyOrder("E_H1", "E_H2", "P_A1");
    }

    @Test
    void runSimulation_noTravellers_returnsEmptyResults() throws InterruptedException {
        WormholeRequest request = new WormholeRequest(0, 0, 1);

        WormholeResponse response = WormholeService.runSimulation(request);

        assertThat(response.totalCrossings()).isZero();
        assertThat(response.crossings()).isEmpty();
    }

    @Test
    void runSimulation_singleTravellerMultipleTrips() throws InterruptedException {
        WormholeRequest request = new WormholeRequest(1, 0, 3);

        WormholeResponse response = WormholeService.runSimulation(request);

        assertThat(response.totalCrossings()).isEqualTo(3);
        assertThat(response.crossings())
                .extracting(CrossingResult::travellerId)
                .containsOnly("E_H1");
    }
}
