package io;

public class CsvDemandLocation {
    private final double x;
    private final double y;

    private final String startLocationId;

    public CsvDemandLocation(double x, double y, String startLocationId) {
        this.x = x;
        this.y = y;
        this.startLocationId = startLocationId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getStartLocationId() {
        return startLocationId;
    }
}
