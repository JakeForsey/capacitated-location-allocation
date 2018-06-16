package domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import solver.KMeansDifficultyWeightFactory;


@PlanningEntity(difficultyWeightFactoryClass = KMeansDifficultyWeightFactory.class)
@XStreamAlias("DemandLocation")
// implement Clusterable so that apache clustering can be used for initialisation
public class DemandLocation implements Clusterable {

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

    // Clusterable method for KMeans initialisation
    public double[] getPoint() {
        return new double[] { getX(), getY() };
    }
}
