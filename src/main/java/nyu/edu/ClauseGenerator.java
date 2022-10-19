package nyu.edu;

import nyu.edu.dto.Atom;
import nyu.edu.dto.Clause;
import nyu.edu.dto.Constants;
import nyu.edu.dto.Pair;

import java.util.*;

public class ClauseGenerator {

    private int nColors;
    private boolean isVerbose;

    public ClauseGenerator(int nColors, boolean isVerbose) {
        this.nColors = nColors;
        this.isVerbose = isVerbose;
    }

    public Pair<Set<String>, List<Clause>> graphConstraints(Map<String, Set<String>> graph) {
        List<String> labels = new ArrayList<>(graph.keySet());
        Collections.sort(labels);
        Pair<Set<String>, List<Clause>> pair = clauseAtLeastOneColor(labels);
        List<Clause> clauses = pair.getRight();
        for (String label : labels) {
            clauses.addAll(clauseAdjacentDifferent(label, graph, pair.getLeft()));
        }
        if (isVerbose) {
            printClauses(clauses);
        }
        return new Pair<>(pair.getLeft(), clauses);
    }

    public static void printClauses(List<Clause> clauses) {
        System.out.println(new String(new char[50]).replace("\0", "-"));
        System.out.println("CNF CLAUSES: " + clauses.size());
        for (Clause clause : clauses) {
            System.out.println(clause.toString());
        }
    }

    private Pair<Set<String>, List<Clause>> clauseAtLeastOneColor(List<String> labels) {
        List<Clause> clauses = new ArrayList<>();
        Set<String> atomsSet = new HashSet<>();
        for (String label : labels) {
            List<Atom> atomsClause = new ArrayList<>();
            for (int color = 1; color <= nColors; color++) {
                String atomLabel = label + Constants.ATOM_DELIMITER + Util.colorGenerator(color);
                atomsClause.add(new Atom(atomLabel, false));
                atomsSet.add(atomLabel);
            }
            clauses.add(new Clause(atomsClause));
        }
        return new Pair<>(atomsSet, clauses);
    }

    private List<Clause> clauseAdjacentDifferent(String parent, Map<String, Set<String>> graph, Set<String> atomLabels) {
        List<Clause> clauses = new ArrayList<>();
        for (int i = 1; i <= nColors; i++) {
            String parentLabel = parent + Constants.ATOM_DELIMITER + Util.colorGenerator(i);
            if (! atomLabels.contains(parentLabel)) {
                throw new RuntimeException("Label not added to set, please check implementation: " + parentLabel);
            }
            for (String neighbor : graph.get(parent)) {
                String neighborLabel = neighbor + Constants.ATOM_DELIMITER + Util.colorGenerator(i);
                if (! atomLabels.contains(neighborLabel)) {
                    throw new RuntimeException("Label not added to set, please check implementation: " + neighborLabel);
                }
                List<Atom> atoms = new ArrayList<>();
                atoms.add(new Atom(parentLabel, true));
                atoms.add(new Atom(neighborLabel, true));
                clauses.add(new Clause(atoms));
            }
        }
        return clauses;
    }
}
