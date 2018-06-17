package io;

public class CsvAnimationStartLocation {
    private final double x;
    private final double y;

    private final String startLocationId;

    private final long score;

    private long previousMillis;
    private final long millis;

    public CsvAnimationStartLocation(double x, double y, String startLocationId, long score, long millis, long previousMillis) {
        this.x = x;
        this.y = y;
        this.startLocationId = startLocationId;
        this.score = score;
        this.millis = millis;
        this.previousMillis = previousMillis;
    }

    public String getStartLocationId() {
        return startLocationId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public long getScore() {
        return score;
    }

    public long getPreviousMillis() {
        return previousMillis;
    }

    public long getMillis() {
        return millis;
    }
}
