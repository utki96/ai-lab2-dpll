package nyu.edu;

import nyu.edu.dto.Constants;

import java.util.List;
import java.util.Map;

public class SolutionTranslator {

    private int nColors;

    public SolutionTranslator(int nColors) {
        this.nColors = nColors;
    }

    public void convertBack(List<String> labels, Map<String, Boolean> assignments) {
        System.out.println(new String(new char[75]).replace("\0", "-"));
        if (assignments == null || assignments.isEmpty()) {
            System.out.println("NO VALID ASSIGNMENT");
            return;
        }
        System.out.println("FINAL SOLUTION:");
        for (String label : labels) {
            for (int i = 1; i <= nColors; i++) {
                String atomLabel = label + Constants.ATOM_DELIMITER + Util.colorGenerator(i);
                if (assignments.containsKey(atomLabel) && assignments.get(atomLabel)) {
                    System.out.println(label + " = " + Util.colorGenerator(i));
                    break;
                }
            }
        }
    }
}
