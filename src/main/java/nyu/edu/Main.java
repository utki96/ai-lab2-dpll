package nyu.edu;

import nyu.edu.dto.Clause;
import nyu.edu.dto.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Main {
    public static void main(String[] args) {
        try {
            String filePath = args[args.length - 1];
            int nColors = Integer.parseInt(args[args.length - 2]);
            boolean isVerbose = false;
            for (int i = args.length - 3; i >= 0; i--) {
                if (isVerboseRequested(args[i])) {
                    isVerbose = true;
                }
            }

            InputParser parser = new InputParser();
            Map<String, Set<String>> graph = parser.parseInput(filePath);

            ClauseGenerator generator = new ClauseGenerator(nColors, isVerbose);
            Pair<Set<String>, List<Clause>> clausePair = generator.graphConstraints(graph);

            DPLLSolver solver = new DPLLSolver(isVerbose);
            Map<String, Boolean> assignments = solver.dpll(clausePair.getLeft(), clausePair.getRight());

            SolutionTranslator translator = new SolutionTranslator(nColors);
            List<String> labels = new ArrayList<>(graph.keySet());
            Collections.sort(labels);
            translator.convertBack(labels, assignments);
        } catch (Exception ex) {
            System.out.println(ex);
            System.exit(1);
        }
        System.exit(0);
    }

    private static boolean isVerboseRequested(String config) {
        return config.equalsIgnoreCase("-v") || config.equalsIgnoreCase("v");
    }
}