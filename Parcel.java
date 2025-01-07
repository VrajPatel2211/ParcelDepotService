package model;

public class Parcel {
    private final String parcelId;
    private final int daysInDepot;
    private final double weight;
    private final double length;
    private final double width;
    private final double height;
    private String status;

    public Parcel(String parcelId, int daysInDepot, double weight, double length, double width, double height) {
        this.parcelId = parcelId;
        this.daysInDepot = daysInDepot;
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
        this.status = "waiting";
    }

    // Getters
    public String getParcelId() {
        return parcelId;
    }

    public int getDaysInDepot() {
        return daysInDepot;
    }

    public double getWeight() {
        return weight;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Parcel{" +
                "parcelId='" + parcelId + '\'' +
                ", daysInDepot=" + daysInDepot +
                ", weight=" + weight +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", status='" + status + '\'' +
                '}';
    }
}
