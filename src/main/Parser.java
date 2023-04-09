package main;

import commands.*;
import network.Stream;

import java.util.*;

/**
 * Parses a program into a list of instructions.
 */
public class Parser {
    private final Map<String, InstructionFactory> instructionFactories;

    /**
     * Creates a new parser.
     */
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

    /**
     * Parses a program into a list of instructions.
     *
     * @param program the program to parse
     * @return the list of instructions
     */
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

    public static class InputTextData {
        public int numRows;
        public int numCols;
        public List<List<Instruction>> siloInstructions;
        public List<Stream> inputStreams;
        public List<Stream> outputStreams;

        public InputTextData(int numRows, int numCols, List<List<Instruction>> siloInstructions,
                          List<Stream> inputStreams, List<Stream> outputStreams) {
            this.numRows = numRows;
            this.numCols = numCols;
            this.siloInstructions = siloInstructions;
            this.inputStreams = inputStreams;
            this.outputStreams = outputStreams;
        }
    }

    /**
     * Parses a program into a list of instructions.
     *
     * @param input the program to parse
     * @return the list of instructions
     */
    public InputTextData parseInputText(String input) {
        Scanner scanner = new Scanner(input);
        int numRows = scanner.nextInt();
        int numCols = scanner.nextInt();

        List<List<Instruction>> siloInstructions = new ArrayList<>();
        List<Stream> inputStreams = new ArrayList<>();
        List<Stream> outputStreams = new ArrayList<>();

        while (scanner.hasNext()) {
            String token = scanner.next();
            if (token.equals("END")) {
                List<Instruction> instructions = new ArrayList<>();
                while (scanner.hasNext() && !token.equals("END")) {
                    token = scanner.next();
                    if (!token.equals("END")) {
                        String[] tokens = new String[]{token};
                        if (instructionFactories.containsKey(token)) {
                            InstructionFactory factory = instructionFactories.get(token);
                            Instruction instruction = factory.create(substituteLabels(tokens, new HashMap<>()));
                            instructions.add(instruction);
                        } else {
                            throw new IllegalArgumentException("Unknown command: " + token);
                        }
                    }
                }
                siloInstructions.add(instructions);
            } else if (token.equals("INPUT")) {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                Stream inputSteam = new Stream(x, y, true);
                while (scanner.hasNextInt()) {
                    int value = scanner.nextInt();
                    inputSteam.addValue(value);
                }
                inputStreams.add(inputSteam);
            } else if (token.equals("OUTPUT")) {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                Stream outputStream = new Stream(x, y, false);
                outputStreams.add(outputStream);
                scanner.next(); // Consume the "END" token
            } else {
                throw new IllegalArgumentException("Unexpected token: " + token);
            }
        }

        scanner.close();
        return new InputTextData(numRows, numCols, siloInstructions, inputStreams, outputStreams);
    }

}
