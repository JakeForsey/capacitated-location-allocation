package domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
@XStreamAlias("LocationAllocationSolution")
public class LocationAllocationSolution {

    private List<StartLocation> startLocationList;
    private List<DemandLocation> demandLocationList;

    private HardSoftScore score;

    private int nStartLocations;

    public LocationAllocationSolution() {}

    public LocationAllocationSolution(List<DemandLocation> demandLocationList,
                                      List<StartLocation> startLocationList,
                                      int nStartLocations){
        this.demandLocationList = demandLocationList;
        this.startLocationList = startLocationList;
        this.nStartLocations = nStartLocations;
    }

    @Override
    public String toString() {
        return "LocationAllocationSolution{" +
                "startLocationList=" + startLocationList +
                ", demandLocationList=" + demandLocationList +
                ", score=" + score +
                '}';
    }

    @ValueRangeProvider(id = "startLocationRange")
    @ProblemFactCollectionProperty
    public List<StartLocation> getStartLocationList(){
        return startLocationList;
    }

    @PlanningEntityCollectionProperty
    public List<DemandLocation> getDemandLocationList() {
        return demandLocationList;
    }

    @PlanningScore
    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score){
        this.score = score;
    }

    public int getnStartLocations() {
        return nStartLocations;
    }
}
