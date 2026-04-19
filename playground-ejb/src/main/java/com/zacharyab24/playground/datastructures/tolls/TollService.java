package com.zacharyab24.playground.datastructures.tolls;

import com.zacharyab24.playground.datastructures.tolls.model.TollRecord;
import com.zacharyab24.playground.datastructures.tolls.model.TollReportRequest;
import com.zacharyab24.playground.datastructures.tolls.model.TollReportResponse;
import com.zacharyab24.seng1120.linkedlist.EToll;
import com.zacharyab24.seng1120.linkedlist.ETollUtils;
import com.zacharyab24.seng1120.shared.LinkedList;

import com.zacharyab24.seng1120.shared.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TollService {

    /**
     * Run function for data structures A1. Takes two toll booth lists, combines them,
     * removes all duplicates, then gets a count of total vehicles, total income, count for each type
     * of vehicle and the combined list
     *
     * @param request the request object
     * @return TollReportResponse object containing the daily report
     */
    public static TollReportResponse generateReport(TollReportRequest request) {
        LinkedList<EToll> tollBooth1 = toEtollList(request.booth1());
        LinkedList<EToll> tollBooth2 = toEtollList(request.booth2());
        LinkedList<EToll> dailyReport = new LinkedList<>();

        tollBooth1.removeAll(tollBooth2);
        dailyReport.concatenate(tollBooth1);
        dailyReport.concatenate(tollBooth2);

        Map<String, Integer> countByType = Map.of(
            "Car", ETollUtils.count(dailyReport, "Car"),
            "Motorcycle", ETollUtils.count(dailyReport, "Motorcycle"),
            "Light Truck", ETollUtils.count(dailyReport, "Light Truck"),
            "Truck", ETollUtils.count(dailyReport, "Truck")
        );

        int vehicleCount = dailyReport.getSize();

        return new TollReportResponse(
                vehicleCount,
                ETollUtils.totalIncome(dailyReport),
                countByType,
                toTollRecordList(dailyReport)
        );
    }

    /**
     * Converts the dto booth list to a LinkedList (custom library version, not Java.Utils) of Etoll type
     *
     * @param records dto records to convert
     * @return the eToll list
     */
    protected static LinkedList<EToll> toEtollList(List<TollRecord> records) {
        LinkedList<EToll> list = new LinkedList<>();
        records.stream()
                .map(r -> new EToll(r.license(), r.type(), r.charge()))
                .forEach(list::addToTail);
        return list;
    }

    /**
     * Converts a custom LinkedList of EToll back to a List of TollRecord DTOs.
     *
     * @param list the EToll linked list to convert
     * @return a list of TollRecord DTOs
     */
    protected static List<TollRecord> toTollRecordList(LinkedList<EToll> list) {
        List<TollRecord> records = new ArrayList<>();
        Node<EToll> current = list.getHead();
        while (current != null) {
            EToll e = current.getData();
            records.add(new TollRecord(e.license(), e.type(), e.charge()));
            current = current.getNext();
        }
        return records;
    }
}