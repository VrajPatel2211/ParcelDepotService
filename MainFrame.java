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
    private JTable CustomerTable;
    private JTable parcelTable;
    private final Manager manager;

    public MainFrame(List<Customer> Customers, List<Parcel> parcels, Manager manager) {
        this.manager = manager;
        setTitle("Parcel Depot Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Added some space between components

        // Set the background color of the frame
        getContentPane().setBackground(Color.DARK_GRAY); // Dark gray background

        // Initialize tables
        initializeCustomerTable(Customers);
        initializeParcelTable(parcels);

        // Create panels for tables
        JPanel tablePanel = new JPanel(new GridLayout(1, 2, 10, 10)); // New panel for tables
        tablePanel.setBackground(Color.DARK_GRAY); // Ensure the panel has the same background
        tablePanel.add(new JScrollPane(CustomerTable));
        tablePanel.add(new JScrollPane(parcelTable));
        add(tablePanel, BorderLayout.CENTER);

        // Create button panel on the right side with GridLayout
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10)); // Vertical button layout
        buttonPanel.setBackground(Color.DARK_GRAY); // Same background as main frame
        buttonPanel.setPreferredSize(new Dimension(200, 0)); // Fix the width of the button panel
        addGenerateReportButton(buttonPanel);
        addProcessCustomerButton(buttonPanel);
        addAddCustomerButton(buttonPanel);
        addAddParcelButton(buttonPanel);
        addFindParcelButton(buttonPanel);

        add(buttonPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeCustomerTable(List<Customer> Customers) {
        String[] columns = {"Seq No", "Name", "Parcel ID"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Customer Customer : Customers) {
            model.addRow(new Object[]{
                    Customer.getsqNum(),
                    Customer.getName(),
                    Customer.getParcelId()
            });
        }

        if (CustomerTable == null) {
            CustomerTable = new JTable(model);
        } else {
            CustomerTable.setModel(model);
        }

        // Customizing table appearance
        CustomerTable.setSelectionBackground(Color.BLACK); // Black selection background
        CustomerTable.setSelectionForeground(Color.WHITE); // White text color for selected rows
        CustomerTable.setRowHeight(30); // Increase row height for better readability
        CustomerTable.setFillsViewportHeight(true); // Make table fill the available space
        CustomerTable.setGridColor(Color.GRAY); // Gray grid color

        // Set the text color to white for all cells
        CustomerTable.setForeground(Color.WHITE);

        // Add alternating row colors with black and gray
        CustomerTable.setDefaultRenderer(Object.class, new AlternateRowRenderer());
    }

    private void initializeParcelTable(List<Parcel> parcels) {
        String[] columns = {"Parcel ID", "Dimensions", "Weight", "Days in Depot", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Parcel parcel : parcels) {
            model.addRow(new Object[] {
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

        // Apply color-coded renderer for the status column
        parcelTable.getColumnModel().getColumn(4).setCellRenderer(new StatusColorRenderer());

        // Customizing table appearance
        parcelTable.setSelectionBackground(Color.BLACK); // Black selection background
        parcelTable.setSelectionForeground(Color.WHITE); // White text color for selected rows
        parcelTable.setRowHeight(30); // Increase row height for better readability
        parcelTable.setFillsViewportHeight(true); // Make table fill the available space
        parcelTable.setGridColor(Color.GRAY); // Gray grid color

        // Set the text color to white for all cells
        parcelTable.setForeground(Color.WHITE);

        // Add alternating row colors with black and gray
        parcelTable.setDefaultRenderer(Object.class, new AlternateRowRenderer());
    }

    private void addProcessCustomerButton(JPanel buttonPanel) {
        JButton processButton = new JButton("Process Customer");
        customizeButton(processButton);
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
        customizeButton(reportButton);
        reportButton.addActionListener(e -> {
            String reportFilename = "resources/report.txt";
            manager.saveReport(reportFilename);
            JOptionPane.showMessageDialog(this, "Report saved to " + reportFilename);
        });
        buttonPanel.add(reportButton);
    }

    private void addAddCustomerButton(JPanel buttonPanel) {
        JButton addCustomerButton = new JButton("Add Customer");
        customizeButton(addCustomerButton);
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
        customizeButton(addParcelButton);
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
        customizeButton(findParcelButton);
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
        List<Customer> Customers = manager.getAllCustomers();
        initializeCustomerTable(Customers);
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

    // Custom row renderer to add alternating row colors
    private static class AlternateRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (row % 2 == 0) {
                c.setBackground(Color.GRAY); // Gray for even rows
            } else {
                c.setBackground(Color.BLACK); // Black for odd rows
            }
            return c;
        }
    }

    // Custom button styling for a more polished look
    private void customizeButton(JButton button) {
        button.setBackground(Color.GRAY); // Gray background
        button.setForeground(Color.WHITE); // White text
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Bold font
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Black border
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor on hover

        // Hover effect for buttons
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.DARK_GRAY); // Darker gray on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.GRAY); // Default gray color

            }
        });
    }
}
