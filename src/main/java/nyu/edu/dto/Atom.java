package nyu.edu.dto;

public class Atom {
    private String label;
    private boolean isNot;
    public Atom(String label, boolean isNot) {
        this.label = label;
        this.isNot = isNot;
    }

    @Override
    public String toString() {
        if (this.isNot) {
            return "! " + this.label;
        }
        return this.label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isNot() {
        return isNot;
    }

    public void setNot(boolean not) {
        isNot = not;
    }
}
