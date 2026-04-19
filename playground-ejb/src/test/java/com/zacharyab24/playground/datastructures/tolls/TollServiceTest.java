package com.zacharyab24.playground.datastructures.tolls;

import com.zacharyab24.playground.datastructures.tolls.model.TollRecord;
import com.zacharyab24.playground.datastructures.tolls.model.TollReportRequest;
import com.zacharyab24.playground.datastructures.tolls.model.TollReportResponse;
import com.zacharyab24.seng1120.linkedlist.EToll;
import com.zacharyab24.seng1120.shared.LinkedList;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TollServiceTest {

    private static final List<TollRecord> BOOTH_1 = List.of(
            new TollRecord("ABC123", "Car", 3.50),
            new TollRecord("XYZ789", "Truck", 8.00),
            new TollRecord("MOT001", "Motorcycle", 1.50)
    );

    private static final List<TollRecord> BOOTH_2 = List.of(
            new TollRecord("DEF456", "Light Truck", 5.00),
            new TollRecord("ABC123", "Car", 3.50)
    );

    @Test
    void generateReport_deduplicatesVehicles() {
        TollReportRequest request = new TollReportRequest(BOOTH_1, BOOTH_2);

        TollReportResponse response = TollService.generateReport(request);

        // ABC123 appears in both booths, should only appear once in merged
        long abc123Count = response.mergedRecords().stream()
                .filter(r -> r.license().equals("ABC123"))
                .count();
        assertThat(abc123Count).isEqualTo(1);
    }

    @Test
    void generateReport_totalVehiclesExcludesDuplicates() {
        TollReportRequest request = new TollReportRequest(BOOTH_1, BOOTH_2);

        TollReportResponse response = TollService.generateReport(request);

        // 3 from booth1 + 2 from booth2 - 1 duplicate = 4
        assertThat(response.totalVehicles()).isEqualTo(4);
    }

    @Test
    void generateReport_totalIncomeReflectsDeduplicated() {
        TollReportRequest request = new TollReportRequest(BOOTH_1, BOOTH_2);

        TollReportResponse response = TollService.generateReport(request);

        // XYZ789(8) + MOT001(1.5) + DEF456(5) + ABC123(3.5) = 18.0
        assertThat(response.totalIncome()).isEqualTo(18.0);
    }

    @Test
    void generateReport_countsByType() {
        TollReportRequest request = new TollReportRequest(BOOTH_1, BOOTH_2);

        TollReportResponse response = TollService.generateReport(request);

        assertThat(response.countByType().get("Car")).isEqualTo(1);
        assertThat(response.countByType().get("Truck")).isEqualTo(1);
        assertThat(response.countByType().get("Motorcycle")).isEqualTo(1);
        assertThat(response.countByType().get("Light Truck")).isEqualTo(1);
    }

    @Test
    void generateReport_noDuplicates_mergesAll() {
        List<TollRecord> booth1 = List.of(new TollRecord("AAA111", "Car", 3.50));
        List<TollRecord> booth2 = List.of(new TollRecord("BBB222", "Truck", 8.00));
        TollReportRequest request = new TollReportRequest(booth1, booth2);

        TollReportResponse response = TollService.generateReport(request);

        assertThat(response.totalVehicles()).isEqualTo(2);
        assertThat(response.mergedRecords()).hasSize(2);
    }

    @Test
    void generateReport_emptyBooths() {
        TollReportRequest request = new TollReportRequest(List.of(), List.of());

        TollReportResponse response = TollService.generateReport(request);

        assertThat(response.totalVehicles()).isZero();
        assertThat(response.totalIncome()).isZero();
        assertThat(response.mergedRecords()).isEmpty();
    }

    @Test
    void toEtollList_convertsAllRecords() {
        List<TollRecord> records = List.of(
                new TollRecord("AAA", "Car", 1.0),
                new TollRecord("BBB", "Truck", 2.0)
        );

        LinkedList<EToll> list = TollService.toEtollList(records);

        assertThat(list.getSize()).isEqualTo(2);
        assertThat(list.getHead().getData().license()).isEqualTo("AAA");
        assertThat(list.getTail().getData().license()).isEqualTo("BBB");
    }

    @Test
    void toTollRecordList_convertsAllNodes() {
        LinkedList<EToll> list = new LinkedList<>();
        list.addToTail(new EToll("AAA", "Car", 1.0));
        list.addToTail(new EToll("BBB", "Truck", 2.0));

        List<TollRecord> records = TollService.toTollRecordList(list);

        assertThat(records).hasSize(2);
        assertThat(records.get(0).license()).isEqualTo("AAA");
        assertThat(records.get(1).license()).isEqualTo("BBB");
    }
}
