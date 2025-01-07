package model;

import controller.Manager; // Import the Manager class
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
public class Log {
    private final StringBuilder logBuffer = new StringBuilder();
    private static Log instance;
    private double totalFees = 0.0;
    private final List<String> collectedParcels = new ArrayList<>();


    private Log() {
        // Private constructor for Singleton pattern
    }

    public static synchronized Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    public void addEvent(String event) {
        logBuffer.append(event).append("\n");
    }

    public void addToTotalFees(double fee) {
        totalFees += fee;
    }

    public void addCollectedParcel(String parcelInfo) {
        collectedParcels.add(parcelInfo);
    }

    public void writeReport(String filename, Manager manager) {
        StringBuilder report = new StringBuilder();
        report.append("Parcel Depot Management Report\n");
        report.append("=============================\n\n");
        report.append("Total Customers Processed: ").append(manager.getQueue().getProcessedCount()).append("\n");
        report.append("Total Parcels Collected: ").append(collectedParcels.size()).append("\n");
        report.append("Total Fees Collected: £").append(String.format("%.2f", totalFees)).append("\n\n");

        // Add processed parcels
        report.append("Processed Parcels:\n");
        report.append("------------------\n");
        for (String event : collectedParcels) {
            report.append(event).append("\n");
        }

        // Add pending parcels
        report.append("\nPending Parcels in Depot:\n");
        report.append("--------------------------\n");
        for (Parcel parcel : manager.getParcelMap().getWaitingParcels()) {
            report.append(parcel).append("\n");
        }

        // Add summary statistics
        report.append("\nSummary Statistics:\n");
        report.append("-------------------\n");
        report.append("Total Parcels in Depot: ").append(manager.getParcelMap().getParcels().size()).append("\n");
        report.append("Parcels Waiting: ").append(manager.getParcelMap().getWaitingCount()).append("\n");
        report.append("Parcels Collected: ").append(collectedParcels.size()).append("\n\n");

        report.append("Thank you for using Parcel Depot Management System!\n");
        report.append("Generated on: ").append(java.time.LocalDateTime.now()).append("\n");

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(report.toString());
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }

}
