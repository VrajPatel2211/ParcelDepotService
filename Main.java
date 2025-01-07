package view;

import controller.Manager;

import javax.swing.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Initialize the manager
        Manager manager = new Manager();

        // Load customers and parcels from files
        manager.loadCustomers("resources/Custs.csv");
        manager.loadParcels("resources/Parcels.csv");

        // Launch the GUI
        SwingUtilities.invokeLater(() -> new MainFrame(
                new ArrayList<>(manager.getQueue().getCustomers()),
                new ArrayList<>(manager.getParcelMap().getParcels().values()),
                manager
        ));

        // Save the report on application exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            manager.saveReport("resources/report.txt");
            System.out.println("Report saved on application exit.");
        }));
    }
}
