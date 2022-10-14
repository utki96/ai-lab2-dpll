package nyu.edu;

import nyu.edu.dto.Atom;
import nyu.edu.dto.Clause;

import java.util.*;
import java.util.stream.Collectors;

public class ClauseGenerator {

    private int nColors;

    public ClauseGenerator(int nColors) {
        this.nColors = nColors;
    }

    public List<Clause> graphConstraints(Map<String, Set<String>> graph) {
        List<String> labels = new ArrayList<>(graph.keySet());
        Collections.sort(labels);
        List<Clause> clauses = new ArrayList<>(clauseAtLeastOneColor(labels));
        for (String label : labels) {
            clauses.addAll(clauseAdjacentDifferent(label, graph));
        }
        printClauses(clauses);
        return clauses;
    }

    public static void printClauses(List<Clause> clauses) {
        System.out.println("-".repeat(50));
        System.out.println("Clauses: " + clauses.size());
        for (Clause clause : clauses) {
            System.out.println(clause.toString());
        }
    }

    private List<Clause> clauseAtLeastOneColor(List<String> labels) {
        List<Clause> clauses = new ArrayList<>();
        for (String label : labels) {
            List<Atom> atoms = new ArrayList<>();
            for (int color = 1; color <= nColors; color++) {
                atoms.add(new Atom(label, color, false));
            }
            clauses.add(new Clause(atoms));
        }
        return clauses;
    }

    private List<Clause> clauseAdjacentDifferent(String parent, Map<String, Set<String>> graph) {
        List<Clause> clauses = new ArrayList<>();
        for (int i = 1; i <= nColors; i++) {
            for (String neighbor : graph.get(parent)) {
                List<Atom> atoms = new ArrayList<>();
                atoms.add(new Atom(parent, i, true));
                atoms.add(new Atom(neighbor, i, true));
                clauses.add(new Clause(atoms));
            }
        }
        return clauses;
    }
}
