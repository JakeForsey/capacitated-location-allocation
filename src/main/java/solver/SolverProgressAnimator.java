package solver;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import domain.DemandLocation;
import domain.LocationAllocationSolution;
import domain.StartLocation;
import io.CsvAnimationDemandLocation;
import io.CsvAnimationStartLocation;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SolverProgressAnimator implements SolverEventListener<LocationAllocationSolution> {

    private long previousMillis = 0;

    private List<CsvAnimationDemandLocation> animationDemandLocations = new ArrayList<CsvAnimationDemandLocation>();
    private List<CsvAnimationStartLocation> animationStartLocations = new ArrayList<CsvAnimationStartLocation>();

    public void bestSolutionChanged(BestSolutionChangedEvent<LocationAllocationSolution> bestSolutionChangedEvent) {
        LocationAllocationSolution solution = bestSolutionChangedEvent.getNewBestSolution();
        long millis = bestSolutionChangedEvent.getTimeMillisSpent();
        long softScore = solution.getScore().getSoftScore();

        for (DemandLocation demandLocation : solution.getDemandLocationList()) {
            animationDemandLocations.add(
                    new CsvAnimationDemandLocation(
                            demandLocation.getX(),
                            demandLocation.getY(),
                            demandLocation.getStartLocation().getId(),
                            softScore,
                            millis,
                            previousMillis
                    )
            );
        }

        List<StartLocation> selectedStartLocations = solution.getDemandLocationList()
                .stream()
                .map(DemandLocation::getStartLocation)
                .distinct()
                .collect(Collectors.toList());

        for (StartLocation startLocation : selectedStartLocations) {
            animationStartLocations.add(
                    new CsvAnimationStartLocation(
                            startLocation.getX(),
                            startLocation.getY(),
                            startLocation.getId(),
                            softScore,
                            millis,
                            previousMillis
                    )
            );
        }

        previousMillis = millis;
    }

    public void writeDemandLocationAnimation(File file) throws IOException {
        CsvMapper mapper = new CsvMapper();
        ObjectWriter writer = mapper.writerWithSchemaFor(CsvAnimationDemandLocation.class);

        writer.writeValues(file).writeAll(animationDemandLocations);
    }

    public void writeStartLocationAnimation(File file) throws IOException {
        CsvMapper mapper = new CsvMapper();
        ObjectWriter writer = mapper.writerWithSchemaFor(CsvAnimationStartLocation.class);

        writer.writeValues(file).writeAll(animationStartLocations);
    }

}
