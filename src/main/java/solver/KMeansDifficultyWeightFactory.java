package solver;

import domain.DemandLocation;
import domain.LocationAllocationSolution;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KMeansDifficultyWeightFactory implements SelectionSorterWeightFactory<LocationAllocationSolution, DemandLocation> {

    private final Map<DemandLocation, Integer> kMeansMapping = new HashMap<DemandLocation, Integer>();

    public Comparable createSorterWeight(LocationAllocationSolution solution, DemandLocation demandLocation) {
        if (kMeansMapping.size() == 0) {
            initKMeansMapping(solution);
        }

        return new KMeansDifficultyComparator(demandLocation, kMeansMapping);
    }

    // Initialise the kmeans mapping which will be passed to the comparator
    private void initKMeansMapping(LocationAllocationSolution solution) {
        KMeansPlusPlusClusterer<DemandLocation> clusterer = new KMeansPlusPlusClusterer<DemandLocation>(solution.getnStartLocations(), 10000);
        List<CentroidCluster<DemandLocation>> clusterResults = clusterer.cluster(solution.getDemandLocationList());

        for (int i=0; i < clusterResults.size(); i++) {
            for (DemandLocation demandLocation : clusterResults.get(i).getPoints()){
                kMeansMapping.put(demandLocation, i);
            }
        }
    }


    public static class KMeansDifficultyComparator implements Comparable<KMeansDifficultyComparator> {

        private final DemandLocation demandLocation;
        private final Map<DemandLocation, Integer> kMeansMapping;

        private KMeansDifficultyComparator(DemandLocation demandLocation, Map<DemandLocation, Integer> kMeansMapping) {
            this.demandLocation = demandLocation;
            this.kMeansMapping = kMeansMapping;
        }

        public int compareTo(KMeansDifficultyComparator other) {

            return new CompareToBuilder()
                    .append(kMeansMapping.get(this.demandLocation), kMeansMapping.get(other.demandLocation))
                    .append(this.demandLocation.getX(), other.demandLocation.getX())
                    .append(this.demandLocation.getY(), other.demandLocation.getY())
                    .toComparison();
        }
    }
}