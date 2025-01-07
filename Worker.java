package controller;

import model.*;

public class Worker {

    public Worker() {
    }

    public double processCustomer(Customer customer, Parcel parcel) {
        double fee = calculateFee(parcel);

        parcel.setStatus("collected");
        Log.getInstance().addEvent("Parcel " + parcel.getParcelId() + " collected by customer " +
                customer.getName() + " | Fee: £" + fee);
        return fee;
    }


    private double calculateFee(Parcel parcel) {
        double baseFee = 5.0;
        double weightFee = parcel.getWeight() * 0.5;
        double dimensionFee = parcel.getLength() * parcel.getWidth() * parcel.getHeight() * 0.01;
        double daysInDepotFee = parcel.getDaysInDepot() * 0.2;

        double totalFee = baseFee + weightFee + dimensionFee + daysInDepotFee;

        if (parcel.getParcelId().startsWith("X")) {
            totalFee *= 0.9; // 10% discount
        }
        if (parcel.getLength() * parcel.getWidth() * parcel.getHeight() < 50) {
            totalFee -= 2.0; // £2 off
        }
        if (parcel.getWeight() < 2.0) {
            totalFee *= 0.95; // 5% discount
        }

        return Math.max(totalFee, 0.0);
    }
}
