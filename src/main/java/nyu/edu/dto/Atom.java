package nyu.edu.dto;

public class Atom {
    private String label;
    private boolean isNot;
    private int color;

    public Atom(String label, int color, boolean isNot) {
        this.label = label;
        this.isNot = isNot;
        this.color = color;
    }

    @Override
    public String toString() {
        String strVal = "Color(" + this.label + ", " + this.color + ")";
        if (this.isNot) {
            return "! " + strVal;
        }
        return strVal;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
