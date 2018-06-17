package app;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import domain.DemandLocation;
import domain.LocationAllocationSolution;
import domain.StartLocation;
import io.CsvDemandLocation;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import solver.SolverProgressAnimator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App {

    private static final int MAP_HEIGHT = 100;
    private static final int MAP_WIDTH = 100;

    private static final int N_POTENTIAL_START_LOCATIONS = 100;
    private static final int N_TARGET_START_LOCATIONS = 3;

    private static final int N_DEMAND_LOCATIONS = 500;

    private static final String SOLUTION_FILE_PATH = "/home/jake/Data/temp/solution.csv";
    private static final String DEMAND_LOCATIONS_ANIMATION_FILE_PATH = "/home/jake/Data/temp/demand_location_animation.csv";
    private static final String START_LOCATIONS_ANIMATION_FILE_PATH = "/home/jake/Data/temp/start_location_animation.csv";

    public static void main(String[] args) throws IOException {

        List<StartLocation> startLocations = createStartLocations();
        List<DemandLocation> demandLocations = createDemandLocations();

        SolverFactory<LocationAllocationSolution> solverFactory = SolverFactory.createFromXmlResource(
                "SolverConfig.xml");
        Solver<LocationAllocationSolution> solver = solverFactory.buildSolver();


        // create a custom event listener that saves algorithm moves so that they can be animated
        SolverProgressAnimator solverProgressAnimator = new SolverProgressAnimator();
        solver.addEventListener(solverProgressAnimator);

        LocationAllocationSolution unsolvedLocationAllocationSolution = new LocationAllocationSolution(
                demandLocations,
                startLocations,
                N_TARGET_START_LOCATIONS
        );

        // solve the problem
        LocationAllocationSolution solvedLocationAllocationSolution = solver.solve(unsolvedLocationAllocationSolution);

        // save the animation
        solverProgressAnimator.writeDemandLocationAnimation(new File(DEMAND_LOCATIONS_ANIMATION_FILE_PATH));
        solverProgressAnimator.writeStartLocationAnimation(new File(START_LOCATIONS_ANIMATION_FILE_PATH));

        // save the best solution
        saveSolution(solvedLocationAllocationSolution);
        }

    private static List<StartLocation> createStartLocations() {

        Random random = new Random();

        List<StartLocation> startLocations = new ArrayList<StartLocation>();
        for (int i = 0; i < N_POTENTIAL_START_LOCATIONS; i++) {

            startLocations.add(new StartLocation(random.nextInt(MAP_HEIGHT), random.nextInt(MAP_WIDTH), "start_location_" + i));
        }
        return startLocations;
    }

    private static List<DemandLocation> createDemandLocations() {

        Random random = new Random();

        List<DemandLocation> demandLocations = new ArrayList<DemandLocation>();
        for (int i = 0; i < N_DEMAND_LOCATIONS; i++) {

            demandLocations.add(new DemandLocation(random.nextInt(MAP_HEIGHT), random.nextInt(MAP_WIDTH)));
        }
        return demandLocations;
    }

    private static void saveSolution(LocationAllocationSolution solution) throws IOException {
        //ObjectMapper mapper = new ObjectMapper();

        //mapper.writeValue(new File("/home/jake/Data/solution.json"), solution.getDemandLocationList());

        List<CsvDemandLocation> csvDemandLocations = new ArrayList<CsvDemandLocation>();
        for (DemandLocation demandLocation : solution.getDemandLocationList()) {
            CsvDemandLocation csvDemandLocation = new CsvDemandLocation(
                    demandLocation.getX(),
                    demandLocation.getY(),
                    demandLocation.getStartLocation().getId()
            );
            csvDemandLocations.add(csvDemandLocation);
        }

        CsvMapper mapper = new CsvMapper();
        ObjectWriter writer = mapper.writerWithSchemaFor(CsvDemandLocation.class);


        writer.writeValues(new File(SOLUTION_FILE_PATH)).writeAll(csvDemandLocations);
    }

}
