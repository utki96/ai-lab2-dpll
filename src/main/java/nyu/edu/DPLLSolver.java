package nyu.edu;

import nyu.edu.dto.Atom;
import nyu.edu.dto.Clause;
import nyu.edu.dto.Constants;
import nyu.edu.dto.Pair;

import java.util.*;

public class DPLLSolver {

    private boolean isVerbose;

    public DPLLSolver(boolean isVerbose) {
        this.isVerbose = isVerbose;
    }

    public Map<String, Boolean> dpll(Set<String> atoms, List<Clause> clauses) {
        if (isVerbose) {
            System.out.println(new String(new char[50]).replace("\0", "-"));
            System.out.println("SOLUTION STEPS: ");
        }
        Map<String, Boolean> assignments = new HashMap<>(), finalResMap = DPLLSolverUtil(atoms, assignments, clauses);
        return finalResMap;
    }

    private Map<String, Boolean> DPLLSolverUtil(Set<String> atoms, Map<String, Boolean> assignments, List<Clause> clauses) {
        boolean tryEasyCases = true;
        while(tryEasyCases) {
            if (clauses.isEmpty()) {        // Success: All clauses are satisfied
                assignDefaultValues(atoms, assignments);
                return assignments;
            }
            if (checkEmptyClause(clauses)) {
                if (isVerbose) {
                    System.out.println("contradiction: backtracking to last guess");
                }
                return null;
            }

            // EASY CASES: SINGLE LITERAL and PURE LITERAL ELIMINATION
            Atom easyCaseAtom = getSingleLiteral(clauses);
            boolean unitLiteral = true;
            if (easyCaseAtom == null) {
                unitLiteral = false;
                easyCaseAtom = getPureLiteral(clauses);
            }
            if (easyCaseAtom != null) {
                if (isVerbose && unitLiteral) {
                    System.out.println("easy case: unit literal " + easyCaseAtom.getLabel() + " -> " + ! easyCaseAtom.isNot());
                } else if (isVerbose){
                    System.out.println("easy case: pure literal " + easyCaseAtom.getLabel() + " -> " + ! easyCaseAtom.isNot());
                }
                atoms.remove(easyCaseAtom.getLabel());
                assignments.put(easyCaseAtom.getLabel(), ! easyCaseAtom.isNot());
                propagate(easyCaseAtom, clauses);
            } else {
                tryEasyCases = false;
            }
        }

        // GUESS value for the first atom in the set
        List<String> guessList = new ArrayList<>(atoms);
        Collections.sort(guessList);
        String atomGuess = guessList.get(0);
        List<Clause> clausesCopy = Util.copyClauses(clauses);
        Set<String> atomsCopy = Util.copySet(atoms);
        Map<String, Boolean> assignmentCopy = Util.copyAssignmentMap(assignments);

        if (isVerbose) {
            System.out.println("hard case: guessing " + atomGuess + " -> true");
        }
        Atom guessAtom = new Atom(atomGuess, false);    // Guessing true first
        atoms.remove(atomGuess);
        assignments.put(atomGuess, true);
        propagate(guessAtom, clauses);

        Map<String, Boolean> finalResMap = DPLLSolverUtil(atoms, assignments, clauses);
        if (finalResMap != null) {
            return finalResMap;
        }
        if (isVerbose) {
            System.out.println("hard case: guessing " + atomGuess + " -> false");
        }
        guessAtom = new Atom(atomGuess, true);         // Guessing false now
        atomsCopy.remove(atomGuess);
        assignmentCopy.put(atomGuess, false);
        propagate(guessAtom, clausesCopy);
        return DPLLSolverUtil(atomsCopy, assignmentCopy, clausesCopy);
    }

    private void propagate(Atom propagateAtom, List<Clause> clauses) {
        List<Clause> clausesToRemove = new ArrayList<>();
        for (Clause clause : clauses) {
            List<Atom> atomToRemove = new ArrayList<>();
            boolean removeClause = false;
            for (Atom atom : clause.getAtoms()) {
                if (atom.getLabel().equalsIgnoreCase(propagateAtom.getLabel())) {
                    if (atom.isNot() == propagateAtom.isNot()) {
                        removeClause = true;
                        clausesToRemove.add(clause);
                        break;
                    } else {
                        atomToRemove.add(atom);
                    }
                }
            }
            if (! removeClause) {
                clause.getAtoms().removeAll(atomToRemove);
            }
        }
        clauses.removeAll(clausesToRemove);
    }

    private Atom getPureLiteral(List<Clause> clauses) {
        // Map -> { atomLabel : Pair(positive count, negative count) }
        Map<String, Pair<Integer, Integer>> atomCountMap = new HashMap<>();
        for (Clause clause : clauses) {
            for (Atom atom : clause.getAtoms()) {
                Pair<Integer, Integer> countPair = atomCountMap.getOrDefault(atom.getLabel(), new Pair<>(0, 0));
                if (atom.isNot()) {
                    countPair.setRight(countPair.getRight() + 1);
                } else {
                    countPair.setLeft(countPair.getLeft() + 1);
                }
                atomCountMap.put(atom.getLabel(), countPair);
            }
        }
        for (String key : atomCountMap.keySet()) {
            Pair<Integer, Integer> countPair = atomCountMap.get(key);
            if (countPair.getLeft() == 1 && countPair.getRight() == 0) {
                return new Atom(key, false);
            }
            if (countPair.getRight() == 1 && countPair.getLeft() == 0) {
                return new Atom(key, true);
            }
        }
        return null;
    }

    private Atom getSingleLiteral(List<Clause> clauses) {
        for (Clause clause : clauses) {
            if (clause.getAtoms().size() == 1) {
                return clause.getAtoms().get(0);
            }
        }
        return null;
    }

    private boolean checkEmptyClause(List<Clause> clauses) {
        for (Clause clause : clauses) {
            if (clause.getAtoms().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void assignDefaultValues(Set<String> atoms, Map<String, Boolean> assignments) {
        for (String atomLabel : atoms) {
            assignments.put(atomLabel, Constants.DEFAULT_ASSIGNMENT);
        }
        atoms.clear();
    }
}
