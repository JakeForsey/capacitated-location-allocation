package io;

public class CsvAnimationDemandLocation {

    private final double x;
    private final double y;

    private final String startLocationId;

    private final long score;

    private long previousMillis;
    private final long millis;

    public CsvAnimationDemandLocation(double x, double y, String startLocationId, long score, long millis, long previousMillis) {
        this.x = x;
        this.y = y;
        this.startLocationId = startLocationId;
        this.score = score;
        this.millis = millis;
        this.previousMillis = previousMillis;
    }

    public long getPreviousMillis() {
        return previousMillis;
    }

    public long getScore() {
        return score;
    }

    public long getMillis() {
        return millis;
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
