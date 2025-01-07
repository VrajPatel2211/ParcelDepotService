package view;

import controller.Manager;
import model.Customer;
import model.Parcel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private JTable customerTable;
    private JTable parcelTable;
    private final Manager manager;

    public MainFrame(List<Customer> customers, List<Parcel> parcels, Manager manager) {
        this.manager = manager;
        setTitle("Parcel Depot Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize tables
        initializeCustomerTable(customers);
        initializeParcelTable(parcels);

        // Create panels for tables
        add(new JScrollPane(customerTable), BorderLayout.WEST);
        add(new JScrollPane(parcelTable), BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        addProcessCustomerButton(buttonPanel);
        addAddCustomerButton(buttonPanel);
        addAddParcelButton(buttonPanel);
        addFindParcelButton(buttonPanel);
        addGenerateReportButton(buttonPanel);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeCustomerTable(List<Customer> customers) {
        String[] columns = {"Seq No", "Name", "Parcel ID"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Customer customer : customers) {
            model.addRow(new Object[]{
                    customer.getSequenceNumber(),
                    customer.getName(),
                    customer.getParcelId()
            });
        }

        if (customerTable == null) {
            customerTable = new JTable(model);
        } else {
            customerTable.setModel(model);
        }
    }

    private void initializeParcelTable(List<Parcel> parcels) {
        String[] columns = {"Parcel ID", "Dimensions", "Weight", "Days in Depot", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Parcel parcel : parcels) {
            model.addRow(new Object[]{
                    parcel.getParcelId(),
                    parcel.getLength() + " x " + parcel.getWidth() + " x " + parcel.getHeight(),
                    parcel.getWeight(),
                    parcel.getDaysInDepot(),
                    parcel.getStatus()
            });
        }

        if (parcelTable == null) {
            parcelTable = new JTable(model);
        } else {
            parcelTable.setModel(model);
        }

        // Apply color-coded renderer
        parcelTable.getColumnModel().getColumn(4).setCellRenderer(new StatusColorRenderer());
    }

    private void addProcessCustomerButton(JPanel buttonPanel) {
        JButton processButton = new JButton("Process Customer");
        processButton.addActionListener(e -> {
            String message = manager.processNextCustomer();
            JOptionPane.showMessageDialog(this, message);

            // Update tables after processing
            updateCustomerTable();
            updateParcelTable();
        });
        buttonPanel.add(processButton);
    }


    private void addGenerateReportButton(JPanel buttonPanel) {
        JButton reportButton = new JButton("Generate Report");
        reportButton.addActionListener(e -> {
            String reportFilename = "resources/report.txt";
            manager.saveReport(reportFilename);
            JOptionPane.showMessageDialog(this, "Report saved to " + reportFilename);
        });
        buttonPanel.add(reportButton);
    }

    private void addAddCustomerButton(JPanel buttonPanel) {
        JButton addCustomerButton = new JButton("Add Customer");
        addCustomerButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter Customer Name:");
            String parcelId = JOptionPane.showInputDialog(this, "Enter Parcel ID:");
            if (name != null && parcelId != null) {
                Customer newCustomer = new Customer(manager.getQueue().getCustomers().size() + 1, name, parcelId);
                manager.addCustomer(newCustomer);
                JOptionPane.showMessageDialog(this, "Customer added successfully!");
                updateCustomerTable();
            }
        });
        buttonPanel.add(addCustomerButton);
    }

    private void addAddParcelButton(JPanel buttonPanel) {
        JButton addParcelButton = new JButton("Add Parcel");
        addParcelButton.addActionListener(e -> {
            try {
                String parcelId = JOptionPane.showInputDialog(this, "Enter Parcel ID:");
                String dimensions = JOptionPane.showInputDialog(this, "Enter Dimensions (LxWxH):");
                String weightStr = JOptionPane.showInputDialog(this, "Enter Weight:");
                String daysInDepotStr = JOptionPane.showInputDialog(this, "Enter Days in Depot:");

                if (parcelId != null && dimensions != null && weightStr != null && daysInDepotStr != null) {
                    String[] dims = dimensions.split("x");
                    double length = Double.parseDouble(dims[0].trim());
                    double width = Double.parseDouble(dims[1].trim());
                    double height = Double.parseDouble(dims[2].trim());
                    double weight = Double.parseDouble(weightStr.trim());
                    int daysInDepot = Integer.parseInt(daysInDepotStr.trim());

                    Parcel parcel = new Parcel(parcelId, daysInDepot, weight, length, width, height);
                    manager.addParcel(parcel);
                    JOptionPane.showMessageDialog(this, "Parcel added successfully!");
                    updateParcelTable();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input format. Please try again.");
            }
        });
        buttonPanel.add(addParcelButton);
    }


    private void addFindParcelButton(JPanel buttonPanel) {
        JButton findParcelButton = new JButton("Find Parcel");
        findParcelButton.addActionListener(e -> {
            String parcelId = JOptionPane.showInputDialog(this, "Enter Parcel ID:");
            if (parcelId != null) {
                Parcel parcel = manager.findParcelById(parcelId);
                if (parcel != null) {
                    JOptionPane.showMessageDialog(this, "Parcel Details:\n" + parcel);
                } else {
                    JOptionPane.showMessageDialog(this, "Parcel not found!");
                }
            }
        });
        buttonPanel.add(findParcelButton);
    }

    private void updateCustomerTable() {
        List<Customer> customers = manager.getAllCustomers();
        initializeCustomerTable(customers);
        revalidate();
        repaint();
    }

    private void updateParcelTable() {
        List<Parcel> parcels = manager.getAllParcels();
        initializeParcelTable(parcels);
        revalidate();
        repaint();
    }

    // Custom Renderer for Status Column
    private static class StatusColorRenderer extends DefaultTableCellRenderer {
        @Override
        protected void setValue(Object value) {
            super.setValue(value);
            if (value != null) {
                String status = value.toString();
                if ("collected".equalsIgnoreCase(status)) {
                    setBackground(Color.GREEN);
                    setForeground(Color.BLACK);
                } else if ("waiting".equalsIgnoreCase(status)) {
                    setBackground(Color.RED);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }
            }
        }
    }
}

