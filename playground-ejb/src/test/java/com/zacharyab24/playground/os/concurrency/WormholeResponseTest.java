package com.zacharyab24.playground.os.concurrency;

import com.zacharyab24.comp2240.concurrency.wormhole.Location;
import com.zacharyab24.comp2240.concurrency.wormhole.WormHole;
import com.zacharyab24.playground.os.concurrency.model.WormholeResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WormholeResponseTest {

    @Test
    void fromWormhole_mapsCountAndCrossings() {
        WormHole wormhole = new WormHole();
        wormhole.recordCrossing("E_H1", Location.EARTH, Location.PROXIMA_B, 1);
        wormhole.recordCrossing("P_A1", Location.PROXIMA_B, Location.EARTH, 1);

        WormholeResponse response = WormholeResponse.fromWormhole(wormhole);

        assertThat(response.totalCrossings()).isEqualTo(2);
        assertThat(response.crossings()).hasSize(2);
    }

    @Test
    void fromWormhole_emptyWormhole() {
        WormHole wormhole = new WormHole();

        WormholeResponse response = WormholeResponse.fromWormhole(wormhole);

        assertThat(response.totalCrossings()).isZero();
        assertThat(response.crossings()).isEmpty();
    }
}