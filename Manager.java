package controller;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class Manager
{
    private final CustomerQueue queue;
    private final ParcelMap parcelMap;
    private final Worker worker;

    public Manager()
    {
        queue = new CustomerQueue();
        parcelMap = ParcelMap.getInstance(); // Use Singleton method
        worker = new Worker();
    }

    // Getters for queue and parcelMap
    public CustomerQueue getQueue() {
        return queue;
    }

    public ParcelMap getParcelMap() {
        return parcelMap;
    }

    // Load customers from file
    public void loadCustomers(String filename) 
    {
        System.out.println("Loading customers from file: " + filename);
        try (var is = getClass().getClassLoader().getResourceAsStream(filename)) 
        {
            if (is == null) throw new NullPointerException("File not found: " + filename);
            var reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) 
            {
                String[] parts = line.split(",");
                int seqNo = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                String parcelId = parts[2].trim();
                queue.addCustomer(new Customer(seqNo, name, parcelId));
            }
        } 
        
        catch (Exception e) 
        {
            System.err.println("Error loading customer data: " + e.getMessage());
        }
        System.out.println("Loaded customers: " + queue.getCustomers().size());
    }

    // Load parcels from file
    public void loadParcels(String filename) 
    {
        System.out.println("Loading parcels from file: " + filename);
        try (var is = getClass().getClassLoader().getResourceAsStream(filename)) 
        {
            if (is == null) throw new NullPointerException("File not found: " + filename);
            var reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) 
            {
                String[] parts = line.split(",");
                String parcelId = parts[0].trim();
                int daysInDepot = Integer.parseInt(parts[1].trim());
                double weight = Double.parseDouble(parts[2].trim());
                double length = Double.parseDouble(parts[3].trim());
                double width = Double.parseDouble(parts[4].trim());
                double height = Double.parseDouble(parts[5].trim());
                parcelMap.addParcel(new Parcel(parcelId, daysInDepot, weight, length, width, height));
            }
        } 
        
        catch (Exception e) 
        {          
            System.err.println("Error loading parcel data: " + e.getMessage());
        }
        System.out.println("Loaded parcels: " + parcelMap.getParcels().size());
    }

    public String processNextCustomer() 
    {
        if (queue.isEmpty()) 
        {
            return "No customers in the queue.";
        }

        Customer customer = queue.peekCustomer();
        if (customer == null) 
        {
            return "No customers in the queue.";
        }

        Parcel parcel = parcelMap.getParcelById(customer.getParcelId());
        if (parcel == null) 
        {
            return "Parcel not found for customer: " + customer.getName();
        }

        double fee = worker.processCustomer(customer, parcel);
        queue.removeCustomer();
        parcelMap.updateParcelStatus(parcel.getParcelId(), "collected");

        String event = "Parcel " + parcel.getParcelId() + " collected by customer " + customer.getName() + " | Fee: £" + String.format("%.2f", fee);
        Log.getInstance().addCollectedParcel(event);
        Log.getInstance().addToTotalFees(fee);

        return "Processed customer: " + customer.getName() + " | Fee: £" + String.format("%.2f", fee);
    }

    public void saveReport(String filename) 
    {
        Log.getInstance().writeReport(filename, this);
    }

    // Add a new customer to the queue
    public void addCustomer(Customer customer) 
    {
        queue.addCustomer(customer);
        System.out.println("Added customer: " + customer.getName());
    }

    // Add a new parcel to the map
    public void addParcel(Parcel parcel)
    {
        parcelMap.addParcel(parcel);
        System.out.println("Added parcel: " + parcel.getParcelId());
    }

    // Find a parcel by its ID
    public Parcel findParcelById(String parcelId) 
    {
        return parcelMap.getParcelById(parcelId);
    }

    // Get all customers as a list
    public List<Customer> getAllCustomers() 
    {
        return new ArrayList<>(queue.getCustomers()); // Convert Queue to List
    }

    // Get all parcels as a list
    public List<Parcel> getAllParcels()
    {
        return List.copyOf(parcelMap.getParcels().values());
    }
}

