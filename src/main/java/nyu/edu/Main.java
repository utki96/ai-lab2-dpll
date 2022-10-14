package nyu.edu;

import nyu.edu.dto.Clause;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        String filePath = "/home/utkarshtyg/Documents/aus/oz.txt";
        int nColors = 4;

        InputParser parser = new InputParser();
        Map<String, Set<String>> graph = parser.parseInput(filePath);

        ClauseGenerator generator = new ClauseGenerator(nColors);
        List<Clause> clauses = generator.graphConstraints(graph);

        DPLLSolver solver = new DPLLSolver(nColors);
        Map<String, Integer> assignments = solver.dpll(clauses, new HashSet<>(graph.keySet()));

        SolutionTranslator translator = new SolutionTranslator();
        translator.printSolution(assignments);
    }
}