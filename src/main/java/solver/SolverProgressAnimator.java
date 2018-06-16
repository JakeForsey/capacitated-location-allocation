package solver;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import domain.DemandLocation;
import domain.LocationAllocationSolution;
import io.CsvAnimationDemandLocation;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SolverProgressAnimator implements SolverEventListener<LocationAllocationSolution> {

    private long previousMillis = 0;

    private List<CsvAnimationDemandLocation> animationDemandLocation = new ArrayList<CsvAnimationDemandLocation>();

    public void bestSolutionChanged(BestSolutionChangedEvent<LocationAllocationSolution> bestSolutionChangedEvent) {
        LocationAllocationSolution solution = bestSolutionChangedEvent.getNewBestSolution();
        long millis = bestSolutionChangedEvent.getTimeMillisSpent();
        long softScore = solution.getScore().getSoftScore();

        for (DemandLocation demandLocation : solution.getDemandLocationList()) {
            animationDemandLocation.add(
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
        previousMillis = millis;
    }

    public void writeAnimation(File file) throws IOException {
        CsvMapper mapper = new CsvMapper();
        ObjectWriter writer = mapper.writerWithSchemaFor(CsvAnimationDemandLocation.class);

        writer.writeValues(file).writeAll(animationDemandLocation);

    }
}
