package main;

import commands.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private final Map<String, InstructionFactory> instructionFactories;

    public Parser() {
        instructionFactories = new HashMap<>();
        instructionFactories.put("NOOP", Noop::new);
        instructionFactories.put("MOVE", tokens -> new Move(tokens[1], tokens[2]));
        instructionFactories.put("SAVE", Save::new);
        instructionFactories.put("SWAP", Swap::new);
        instructionFactories.put("ADD", tokens -> new Add(tokens[1]));
        instructionFactories.put("SUB", tokens -> new Sub(tokens[1]));
        instructionFactories.put("NEGATE", Negate::new);
        // commands.Add more instruction factories here
    }

    public List<Instruction> parse(String program) {
        List<Instruction> instructions = new ArrayList<>();
        Map<String, Label> labels = new HashMap<>();
        String[] lines = program.split("\n");

        // Scan the program for labels
        int instructionIndex = 0;
        for (String line : lines) {
            String[] tokens = line.split(" ");
            String command = tokens[0];

            if (command.startsWith(":") && command.endsWith(":")) {
                // Found a label
                String labelName = command.substring(1, command.length() - 1);
                labels.put(labelName, new Label(labelName, instructionIndex));
            } else {
                // Found an instruction
                instructionIndex++;
            }
        }

        // Create instructions, substituting label names with their corresponding instruction indices
        instructionIndex = 0;
        for (String line : lines) {
            String[] tokens = line.split(" ");
            String command = tokens[0];

            if (command.startsWith(":") && command.endsWith(":")) {
                // Found a label, do nothing
            } else {
                // Found an instruction
                if (instructionFactories.containsKey(command)) {
                    InstructionFactory factory = instructionFactories.get(command);
                    Instruction instruction = factory.create(substituteLabels(tokens, labels));
                    instructions.add(instruction);
                } else {
                    throw new IllegalArgumentException("Unknown command: " + command);
                }
                instructionIndex++;
            }
        }

        return instructions;
    }

    private String[] substituteLabels(String[] tokens, Map<String, Label> labels) {
        String[] substitutedTokens = new String[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (labels.containsKey(token)) {
                substitutedTokens[i] = Integer.toString(labels.get(token).getInstructionIndex());
            } else {
                substitutedTokens[i] = token;
            }
        }
        return substitutedTokens;
    }

    private interface InstructionFactory {
        Instruction create(String[] tokens);
    }
}
