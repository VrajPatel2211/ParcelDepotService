package model;

public class Customer {
    private final int sequenceNumber;
    private final String name;
    private final String parcelId;

    public Customer(int sequenceNumber, String name, String parcelId) {
        this.sequenceNumber = sequenceNumber;
        this.name = name;
        this.parcelId = parcelId;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public String getName() {
        return name;
    }

    public String getParcelId() {
        return parcelId;
    }
}
