package nyu.edu;

import nyu.edu.dto.Atom;
import nyu.edu.dto.Clause;
import nyu.edu.dto.Constants;

import java.util.*;

public class DPLLSolver {

    private int nColors;

    public DPLLSolver(int nColors) {
        this.nColors = nColors;
    }

    public Map<String, Integer> dpll(List<Clause> clauses, Set<String> atoms) {
        Map<String, Integer> assignments = new HashMap<>();
        if (DPLLSolverUtil(atoms, assignments, clauses)) {
            return assignments;
        }
        return null;
    }

    private boolean DPLLSolverUtil(Set<String> atoms, Map<String, Integer> assignments, List<Clause> clauses) {
        ClauseGenerator.printClauses(clauses);

        if (clauses.isEmpty()) {        // Success: All clauses are satisfied
            assignDefaultValues(atoms, assignments);
            return true;
        }
        if (checkEmptyClause(clauses)) {
            return false;
        }

        // EASY CASES: SINGLE LITERAL and PURE LITERAL ELIMINATION
        Atom singleAtom = getSingleLiteral(clauses);
        if (singleAtom != null) {
            System.out.println("SINGLE ATOM: " + singleAtom.getLabel() + " -> " + singleAtom.getColor());
            atoms.remove(singleAtom.getLabel());
            assignments.put(singleAtom.getLabel(), singleAtom.getColor());
            propagate(singleAtom, clauses);
            if (DPLLSolverUtil(atoms, assignments, clauses)) {
                return true;
            } else {
                atoms.add(singleAtom.getLabel());
                assignments.remove(singleAtom.getLabel());
                return false;
            }
        }
        Atom pureAtom = getPureLiteral(atoms, clauses);
        if (pureAtom != null) {
            System.out.println("PURE ATOM: " + pureAtom.getLabel() + " -> " + pureAtom.getColor());
            atoms.remove(pureAtom.getLabel());
            assignments.put(pureAtom.getLabel(), pureAtom.getColor());
            propagate(pureAtom, clauses);
            if (DPLLSolverUtil(atoms, assignments, clauses)) {
                return true;
            } else {
                atoms.add(pureAtom.getLabel());
                assignments.remove(pureAtom.getLabel());
                return false;
            }
        }

        // GUESS all colours sequentially for a particular atom
        Set<String> guessSet = new HashSet<>(atoms);
        String atomGuess = guessSet.iterator().next();
        for (int color = 1; color <= nColors; color++) {
            if (isColorAssignmentForAtom(atomGuess, color, clauses)) {
                System.out.println("GUESSING: " + atomGuess + " -> " + color);
                List<Clause> clausesCopy = Util.copyClauses(clauses);
                Atom guessAtom = new Atom(atomGuess, color, false);
                atoms.remove(atomGuess);
                assignments.put(atomGuess, color);
                propagate(guessAtom, clausesCopy);
                if (DPLLSolverUtil(atoms, assignments, clausesCopy)) {
                    return true;
                } else {
                    atoms.add(atomGuess);
                    assignments.remove(atomGuess);
                }
            }
        }
        return false;
    }

    private void propagate(Atom propagateAtom, List<Clause> clauses) {
        List<Clause> clausesToRemove = new ArrayList<>();
        for (Clause clause : clauses) {
            List<Atom> atomToRemove = new ArrayList<>();
            boolean removeClause = false;
            for (Atom atom : clause.getAtoms()) {
                if (atom.getLabel().equalsIgnoreCase(propagateAtom.getLabel()) && atom.getColor() == propagateAtom.getColor()) {
                    if (atom.isNot()) {
                        atomToRemove.add(atom);
                    } else {
                        removeClause = true;
                        clausesToRemove.add(clause);
                        break;
                    }
                } else if (atom.getLabel().equalsIgnoreCase(propagateAtom.getLabel()) && atom.getColor() != propagateAtom.getColor()) {
                    if (atom.isNot()) {
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

    private boolean isColorAssignmentForAtom(String atomGuess, int color, List<Clause> clauses) {
        for (Clause clause : clauses) {
            for (Atom atom : clause.getAtoms()) {
                if (atom.getLabel().equalsIgnoreCase(atomGuess) && atom.getColor() == color && ! atom.isNot()) {
                    return true;
                }
            }
        }
        return false;
    }

    private Atom getPureLiteral(Set<String> atomLabels, List<Clause> clauses) {
        for (String atomLabel : atomLabels) {
            boolean shortCircuit = false;
            int[] positiveCounts = new int[nColors], negativeCounts = new int[nColors];
            for (int i = 0; i < nColors; i++) {
                positiveCounts[i] = 0;
                negativeCounts[i] = 0;
            }
            for (Clause clause : clauses) {
                if (shortCircuit) {
                    break;
                }
                for (Atom atom : clause.getAtoms()) {
                    if (atom.getLabel().equalsIgnoreCase(atomLabel)) {
                        if (atom.isNot()) {
                            negativeCounts[atom.getColor() - 1]++;
                            if (positiveCounts[atom.getColor() - 1] > 0) {
                                shortCircuit = true;
                                break;
                            }
                        } else {
                            positiveCounts[atom.getColor() - 1]++;
                            if (negativeCounts[atom.getColor() - 1] > 0) {
                                shortCircuit = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (! shortCircuit) {
                int c = 0, color = 0;
                for (int i = 0; i < nColors; i++) {
                    if (positiveCounts[i] > 0 || negativeCounts[i] > 0) {
                        color = i+1;
                        c++;
                    }
                }
                if (c == 1) {
                    return new Atom(atomLabel, color, false);
                }
            }
        }
        return null;
    }

    private Atom getSingleLiteral(List<Clause> clauses) {
        for (Clause clause : clauses) {
            if (clause.getAtoms().size() == 1 && ! clause.getAtoms().get(0).isNot()) {
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

    private void assignDefaultValues(Set<String> atoms, Map<String, Integer> assignments) {
        for (String atomLabel : atoms) {
            assignments.put(atomLabel, Constants.DEFAULT_ASSIGNMENT);
        }
        atoms.clear();
    }
}
