package model;

import java.util.LinkedList;
import java.util.Queue;

public class QueueofCustomers {
    private Queue<Customer> customers;
    private int processedCount = 0; // Counter for processed customers

    public QueueofCustomers() {
        customers = new LinkedList<>();
        processedCount = 0;
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public Customer peekCustomer() {
        return customers.peek();
    }

    public void removeCustomer() {
        if (!customers.isEmpty()) {
            customers.poll();
            processedCount++;
        }
    }

    public boolean isEmpty() {
        return customers.isEmpty();
    }

    public Queue<Customer> getCustomers() {
        return customers;
    }

    public int getProcessedCount() {
        return processedCount;
    }

    public int size() {
        return customers.size();
    }
}
