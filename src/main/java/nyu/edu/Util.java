package nyu.edu;

import nyu.edu.dto.Atom;
import nyu.edu.dto.Clause;

import java.util.*;

public class Util {

    public static String colorGenerator(int colorNum) {
        switch(colorNum) {
            case 1: return "Red";
            case 2: return "Green";
            case 3: return "Blue";
            case 4: return "Yellow";
        }
        if (colorNum >= 5) {
            return new String(new char[(colorNum - 5) / 26 + 1]).replace('\0', (char) ('A' + (colorNum - 5) % 26));
        }
        return "Default";
    }

    public static List<Clause> copyClauses(List<Clause> original) {
        List<Clause> copy = new ArrayList<>();
        for (Clause clause : original) {
            List<Atom> atoms = new ArrayList<>();
            for (Atom atom : clause.getAtoms()) {
                atoms.add(new Atom(atom.getLabel(), atom.isNot()));
            }
            copy.add(new Clause(atoms));
        }
        return copy;
    }

    public static Set<String> copySet(Set<String> originalSet) {
        return new HashSet<>(originalSet);
    }

    public static Map<String, Boolean> copyAssignmentMap(Map<String, Boolean> originalMap) {
        Map<String, Boolean> copyMap = new HashMap<>();
        for (String key : originalMap.keySet()) {
            copyMap.put(key, originalMap.get(key));
        }
        return copyMap;
    }
}
