package nyu.edu.dto;

import java.util.ArrayList;
import java.util.List;

public class Clause {
    private List<Atom> atoms;

    public Clause() {
        this.atoms = new ArrayList<>();
    }

    public Clause(List<Atom> atoms) {
        this.atoms = atoms;
    }

    @Override
    public String toString() {
        StringBuilder strVal = new StringBuilder();
        int c = 0;
        for (Atom atom : this.atoms) {
            if (c > 0) {
                strVal.append(" | ");
            }
            strVal.append(atom.toString());
            c++;
        }
        return strVal.toString();
    }

    public List<Atom> getAtoms() {
        return atoms;
    }
}
