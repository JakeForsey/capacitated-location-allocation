package score;

import domain.DemandLocation;
import domain.LocationAllocationSolution;
import domain.StartLocation;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationAllocationEasyScoreCalculator implements EasyScoreCalculator<LocationAllocationSolution> {

    public HardSoftScore calculateScore(LocationAllocationSolution locationAllocationSolution) {
        int hardScore = 0;
        int softScore = 0;

        Map<StartLocation, Integer> startLocationDemandCounts = new HashMap<>();

        List<DemandLocation> demandLocationsList = locationAllocationSolution.getDemandLocationList();

        for (DemandLocation demandLocation : demandLocationsList) {
            StartLocation startLocation = demandLocation.getStartLocation();
            if (startLocation != null) {
                softScore += getDistance(startLocation, demandLocation);

                if (startLocationDemandCounts.get(startLocation) != null) {
                    startLocationDemandCounts.put(startLocation, startLocationDemandCounts.get(startLocation) + 1);
                } else {
                    startLocationDemandCounts.put(startLocation, 1);
                }

            }
        }

        for (int demandCount : startLocationDemandCounts.values()) {
            double targetAssignedDemand = (demandLocationsList.size() / locationAllocationSolution.getnStartLocations());
            // if the demand count is outside a buffer of the desired demand count then give a binary hard score
            if (demandCount > targetAssignedDemand + 2) {
                // add the difference between target and actual score to the hard score count
                // hardScore += (int) Math.abs(targetAssignedDemand - demandCount);
                // add a binary 1 to the hard score
                hardScore += 1;
            }

            if (demandCount < targetAssignedDemand - 2) {
                // add the difference between target and actual score to the hard score count
                // hardScore += (int) Math.abs(targetAssignedDemand - demandCount);
                // add a binary 1 to the hard score
                hardScore += 1;

            }
        }

        return HardSoftScore.valueOf(-hardScore, -softScore);
    }

    // Slow version that I know works
    public HardSoftScore calculateScore2(LocationAllocationSolution locationAllocationSolution) {

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
                // if the demand count is outside a buffer of the desired demand count then give a binary hard score
                if (demandCount > targetAssignedDemand + 2) {
                    // add the difference between target and actual score to the hard score count
                    // hardScore += (int) Math.abs(targetAssignedDemand - demandCount);
                    // add a binary 1 to the hard score
                    hardScore += 1;
                }

                if (demandCount < targetAssignedDemand - 2) {
                    // add the difference between target and actual score to the hard score count
                    // hardScore += (int) Math.abs(targetAssignedDemand - demandCount);
                    // add a binary 1 to the hard score
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
