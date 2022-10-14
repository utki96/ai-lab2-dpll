package nyu.edu;

import nyu.edu.dto.Atom;
import nyu.edu.dto.Clause;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static String colorGenerator(int colorNum) {
        switch(colorNum) {
            case 1: return "Red";
            case 2: return "Green";
            case 3: return "Blue";
            case 4: return "Yellow";
        }
        if (colorNum >= 5) {
            return String.valueOf((char)('A' + (colorNum - 5) % 26)).repeat((colorNum - 5) / 26 + 1);
        }
        return "Default";
    }

    public static List<Clause> copyClauses(List<Clause> original) {
        List<Clause> copy = new ArrayList<>();
        for (Clause clause : original) {
            List<Atom> atoms = new ArrayList<>();
            for (Atom atom : clause.getAtoms()) {
                atoms.add(new Atom(atom.getLabel(), atom.getColor(), atom.isNot()));
            }
            copy.add(new Clause(atoms));
        }
        return copy;
    }
}
