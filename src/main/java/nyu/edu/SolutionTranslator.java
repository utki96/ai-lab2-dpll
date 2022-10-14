package nyu.edu;

import java.util.HashMap;
import java.util.Map;

public class SolutionTranslator {

    private Map<String, String> convertBack(Map<String, Integer> assignments) {
        if (assignments == null || assignments.keySet().size() == 0) {
            return null;
        }
        Map<String, String> solution = new HashMap<>();
        for (String key : assignments.keySet()) {
            solution.put(key, Util.colorGenerator(assignments.get(key)));
        }
        return solution;
    }

    public void printSolution(Map<String, Integer> assignments) {
        Map<String, String> solution = convertBack(assignments);
        System.out.println("-".repeat(75));
        if (solution == null) {
            System.out.println("NO Solution");
        } else {
            System.out.println("Final Assignments:");
            for (String key : solution.keySet()) {
                System.out.println(key + " = " + solution.get(key));
            }
        }
    }
}
