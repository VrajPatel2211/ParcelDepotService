package model;

public class Customer 
{
    private final int sqNum;
    private final String name;
    private final String parcelId;

    public Customer(int sqNum, String name, String parcelId) 
    {
        this.sqNum = sqNum;
        this.name = name;
        this.parcelId = parcelId;
    }

    public int getsqNum() {
        return sqNum;
    } // get parcel sequence number

    public String getName() {
        return name;
    }

    public String getParcelId() {
        return parcelId;
    }
}
