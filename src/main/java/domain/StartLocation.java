package domain;

public class StartLocation {

    private final double x;
    private final double y;
    private final String id;

    public StartLocation(double x, double y, String id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getId() {
        return id;
    }
}
