package nyu.edu;

import nyu.edu.dto.Constants;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class InputParser {

    public Map<String, Set<String>> parseInput(String filePath) {
        List<String> instructions = readInputFile(filePath);
        Map<String, Set<String>> graph = new HashMap<>();
        for (String instruction : instructions) {
            processInputLine(instruction, graph);
        }
        printGraph(graph);
        return graph;
    }

    private void printGraph(Map<String, Set<String>> graph) {
        List<String> labels = new ArrayList<>(graph.keySet());
        Collections.sort(labels);
        for (String label : labels) {
            System.out.print(label + " -> ");
            for (String neighbor : graph.get(label)) {
                System.out.print(neighbor + ", ");
            }
            System.out.println();
        }
    }

    private void processInputLine(String instruction, Map<String, Set<String>> graph) throws RuntimeException {
        int parentDelimIndex = instruction.indexOf(Constants.GRAPH_DELIMITER);
        if (parentDelimIndex < 0) {
            System.out.println("LINE: " + instruction);
            throw new RuntimeException("Invalid input line: " + instruction);
        }

        String parentLabel = instruction.substring(0, parentDelimIndex).trim();

        int neighborStartIndex = instruction.indexOf(Constants.NODES_START_DELIM), neighborEndIndex = instruction.indexOf(Constants.NODES_END_DELIM);
        if (neighborStartIndex < 0 || neighborEndIndex < 0 || neighborEndIndex < neighborStartIndex) {
            throw new RuntimeException("Invalid input line: " + instruction);
        }
        String[] neighbors = instruction.substring(neighborStartIndex + 1, neighborEndIndex).trim().split(Constants.NODES_DELIM);
        if (neighbors.length == 0) {
            throw new RuntimeException("Invalid input line: " + instruction);
        }

        Set<String> parentChildren = graph.getOrDefault(parentLabel, new HashSet<>());
        for (String neighborLabel : neighbors) {
            neighborLabel = neighborLabel.trim();
            if (! neighborLabel.isBlank()) {
                Set<String> neighborChildren = graph.getOrDefault(neighborLabel, new HashSet<>());
                parentChildren.add(neighborLabel);
                neighborChildren.add(parentLabel);
                graph.put(neighborLabel, neighborChildren);
            }
            graph.put(parentLabel, parentChildren);
        }
    }

    private List<String> readInputFile(String filePath) throws RuntimeException {
        List<String> instructions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while (line != null) {
                if (! (line.startsWith(Constants.COMMENT_LINE) || line.isBlank())) {
                    instructions.add(line.trim());
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException fnfEx) {
            throw new RuntimeException("Invalid path for file: " + filePath + ". Error msg: " + fnfEx.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Failed to read Input File. Error msg: " + ex.getMessage());
        }
        return instructions;
    }
}
