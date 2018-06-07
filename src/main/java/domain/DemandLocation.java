package domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
@XStreamAlias("DemandLocation")
public class DemandLocation {

    private double x;
    private double y;

    private StartLocation startLocation;

    public DemandLocation() {}

    public DemandLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Optaplanner planning variables getter / setter
    @PlanningVariable(valueRangeProviderRefs = {"startLocationRange"})
    public StartLocation getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(StartLocation startLocation) {
        this.startLocation = startLocation;
    }

    // Optaplanner cost variables getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
