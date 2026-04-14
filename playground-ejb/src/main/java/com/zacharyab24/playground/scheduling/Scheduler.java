package com.zacharyab24.playground.scheduling;

import com.zacharyab24.comp2240.scheduling.algorithms.FCFS;
import com.zacharyab24.comp2240.scheduling.algorithms.PP;
import com.zacharyab24.comp2240.scheduling.algorithms.PRR;
import com.zacharyab24.comp2240.scheduling.algorithms.SPN;
import com.zacharyab24.comp2240.scheduling.model.Process;
import com.zacharyab24.comp2240.scheduling.model.Result;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This is a 1:1 port of A1.java from COMP2240
 * The function of this program is to simulate scheduling algorithms for a list of processes provided in a text file.
 * The scheduling algorithms implemented are:
 * <ul>
 * <li>First Come, First Served</li>
 * <li>Shortest Process Next</li>
 * <li>Preemptive Priority</li>
 * <li>Priority Round Robin</li>
 * </ul>
 * It is assumed that the input file is in the correct, if an incorrect file is given, the code will error out
 */
public class Scheduler {

    /* TODO:
        - make runScheduler take a string, not a file,
        - make runScheduler return some form of structured data, not a string
        - update readData to not take a file
        - add error handling to readData
    */

    private static int dispatcher;

    /**
     * Runs the 4 scheduling algorithms
     *
     * @param input file containing the processes
     * @return formatted output string for
     */
    public static String runScheduler(InputStream input) {
        ArrayList<Process> processes = readData(input);

        // Run algorithms
        Result fcfs = new FCFS(dispatcher, processes).run();
        Result spn = new SPN(dispatcher, processes).run();
        Result pp = new PP(dispatcher, processes).run();
        Result prr = new PRR(dispatcher, processes).run();

        // Generate summary
        StringBuilder sb = new StringBuilder();

        sb.append("Summary\n");
        sb.append("%-9s %-25s %-20s%n".formatted("Algorithm", "Average Turnaround Time", "Average Waiting Time"));
        sb.append("%-9s %-25s %-20s%n".formatted(fcfs.processorID(), fcfs.turnAround(), fcfs.waitingTime()));
        sb.append("%-9s %-25s %-20s%n".formatted(spn.processorID(), spn.turnAround(), spn.waitingTime()));
        sb.append("%-9s %-25s %-20s%n".formatted(pp.processorID(), pp.turnAround(), pp.waitingTime()));
        sb.append("%-9s %-25s %-20s%n".formatted(prr.processorID(), prr.turnAround(), prr.waitingTime()));

        return sb.toString();
    }

    /**
     * Function to read the contents of a file. The specified file should be in the
     * correct format, no proper error handling has been done as it was stated we
     * can assume the file will be correct
     *
     * @param input the file to read
     * @return an ArrayList of type Process that contains the data from the file
     */
    private static ArrayList<Process> readData(InputStream input) {
        ArrayList<Process> processes = new ArrayList<>();
        // Variables are uninitialised placeholders, guaranteed to be set before use
        // as the file is assumed to be in the correct format
        String pid = "";
        int arrTime = 0, srvTime = 0, priority = 0;
        try (Scanner sc = new Scanner(input)) {
            while (sc.hasNext()) {
                switch (sc.next()) {
                    case "DISP:" -> dispatcher = sc.nextInt();
                    case "PID:" -> pid = sc.nextLine().trim();
                    case "ArrTime:" -> arrTime = sc.nextInt();
                    case "SrvTime:" -> srvTime = sc.nextInt();
                    case "Priority:" -> {
                        priority = sc.nextInt();
                        processes.add(new Process(pid, arrTime, srvTime, priority));
                    }
                }
            }
        }
        return processes;
    }
}
