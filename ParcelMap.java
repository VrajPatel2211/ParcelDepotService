package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParcelMap {
    private static ParcelMap instance;
    private final Map<String, Parcel> parcels;

    public List<Parcel> getWaitingParcels() {
        return parcels.values().stream()
                .filter(parcel -> "waiting".equalsIgnoreCase(parcel.getStatus()))
                .collect(Collectors.toList());
    }

    public int getWaitingCount() {
        return (int) parcels.values().stream()
                .filter(parcel -> "waiting".equalsIgnoreCase(parcel.getStatus()))
                .count();
    }

    // Private constructor for Singleton pattern
    private ParcelMap() {
        parcels = new HashMap<>();
    }

    // Static method to get the single instance of ParcelMap
    public static synchronized ParcelMap getInstance() {
        if (instance == null) {
            instance = new ParcelMap();
        }
        return instance;
    }

    // Add a parcel to the map
    public void addParcel(Parcel parcel) {
        parcels.put(parcel.getParcelId(), parcel);
    }

    // Get a parcel by its ID
    public Parcel getParcelById(String parcelId) {
        return parcels.get(parcelId);
    }
    // Update parcel Status
    public void updateParcelStatus(String parcelId, String newStatus) {
        Parcel parcel = parcels.get(parcelId);
        if (parcel != null) {
            parcel.setStatus(newStatus); // Update the parcel's status
        }
    }

    // Get all parcels
    public Map<String, Parcel> getParcels() {
        return parcels;
    }
}
