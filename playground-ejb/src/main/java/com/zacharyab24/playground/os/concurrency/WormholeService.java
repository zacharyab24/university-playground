package com.zacharyab24.playground.os.concurrency;

import com.zacharyab24.comp2240.concurrency.wormhole.Location;
import com.zacharyab24.comp2240.concurrency.wormhole.Traveller;
import com.zacharyab24.comp2240.concurrency.wormhole.WormHole;
import com.zacharyab24.playground.os.concurrency.model.WormholeRequest;
import com.zacharyab24.playground.os.concurrency.model.WormholeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class WormholeService {
    private static final String EARTH_HUMAN = "E_H";
    private static final String PROXIMA_B_ALIEN = "P_A";

    public static WormholeResponse runSimulation(WormholeRequest request) throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);
        WormHole wh = new WormHole();

        List<Traveller> travellers = new ArrayList<>();
        for (int i = 0; i < request.earthHumans(); i++) {
            travellers.add(new Traveller(EARTH_HUMAN + (i + 1), Location.EARTH, request.trips(), semaphore, wh));
        }
        for (int i = 0; i < request.proximaAliens(); i++) {
            travellers.add(new Traveller(PROXIMA_B_ALIEN + (i + 1), Location.PROXIMA_B, request.trips(), semaphore, wh));
        }
        travellers.forEach(Thread::start);
        for (Traveller t : travellers) {
            t.join();
        }
        return WormholeResponse.fromWormhole(wh);
    }
}
