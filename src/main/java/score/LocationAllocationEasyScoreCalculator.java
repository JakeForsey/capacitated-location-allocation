package score;

import domain.DemandLocation;
import domain.LocationAllocationSolution;
import domain.StartLocation;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

import java.util.List;

public class LocationAllocationEasyScoreCalculator implements EasyScoreCalculator<LocationAllocationSolution> {


    public HardSoftScore calculateScore(LocationAllocationSolution locationAllocationSolution) {

        int hardScore = 0;
        int softScore = 0;

        List<DemandLocation> demandLocationList = locationAllocationSolution.getDemandLocationList();
        List<StartLocation> startLocationList = locationAllocationSolution.getStartLocationList();

        for (DemandLocation demandLocation : demandLocationList) {
            if (demandLocation.getStartLocation() != null) {
                softScore += getDistance(demandLocation.getStartLocation(), demandLocation);
            }
        }

        // Check for balance
        for (StartLocation startLocation : startLocationList) {

            int demandCount = 0;
            for (DemandLocation demandLocation : demandLocationList) {

                if (demandLocation.getStartLocation() != null) {
                    if (demandLocation.getStartLocation().equals(startLocation)) {
                        demandCount += 1;
                    }

                }
            }

            if (demandCount != 0) {
                double targetAssignedDemand = (demandLocationList.size() / locationAllocationSolution.getnStartLocations());
                if (demandCount > targetAssignedDemand + 2) {
                    hardScore += 1;
                }
                if (demandCount < targetAssignedDemand - 2) {
                    hardScore += 1;
                }
            }
        }

        return HardSoftScore.valueOf(-hardScore, -softScore);
    }

    private static double getDistance(StartLocation startLocation, DemandLocation demandLocation) {

        double deltaX = startLocation.getX() - demandLocation.getX();
        double deltaY = startLocation.getY() - demandLocation.getY();

        return Math.sqrt((deltaY * deltaY) +(deltaX * deltaX));
    }
}
