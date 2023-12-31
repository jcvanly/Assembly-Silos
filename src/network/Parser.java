package network;

import commands.*;
import java.io.*;
import java.util.*;

/**
 * Luke McDougall, Jack Vanlyssel, Spoorthi Menta
 *
 * Parses an input file containing a string into a list of instruction objects,
 * grid dimensions, silo instructions, input streams, and output streams.
 */
public class Parser {
    private final Map<String, InstructionFactory> instructionFactories;

    /**
     * Creates a new parser and initializes the instructionFactories map,
     * which maps instruction names to their corresponding factories.
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
        instructionFactories.put("JUMP", tokens -> new Jump(tokens[1]));
        instructionFactories.put("JEZ", tokens -> new Jez(tokens[1]));
        instructionFactories.put("JNZ", tokens -> new Jnz(tokens[1]));
        instructionFactories.put("JGZ", tokens -> new Jgz(tokens[1]));
        instructionFactories.put("JLZ", tokens -> new Jlz(tokens[1]));
        instructionFactories.put("JRO", tokens -> new Jro(tokens[1]));
    }

    /**
     * The parse method accepts a program as a string and returns a list of Instruction objects.
     * It first scans the program for labels, then creates the instructions, and finally substitutes
     * label names with their corresponding instruction indices.
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
                labels.put(labelName, new Label(instructionIndex));
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
                    //if the command is empty, do nothing
                    if (command.equals("")) {
                        continue;
                    }
                    throw new IllegalArgumentException("Unknown command: " + command);
                }
                instructionIndex++;
            }
        }
        return instructions;
    }

    /**
     * substituteLabels is a helper method that replaces label names
     * in tokens with their corresponding instruction indices. It is
     * used by InstructionFactory to track the instruction number.
     */
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

    /**
     * Creates Instruction objects from an array of tokens.
     */
    private interface InstructionFactory {
        Instruction create(String[] tokens);
    }

    /**
     *  InputFileData is a nested class that holds the parsed data from the input file,
     *  including the grid dimensions, silo instructions, input streams, and output streams.
     *  It contains functions to get the position of a silo, the instructions given to a silo,
     *  and the input and output streams of a silo.
     */
    public class InputFileData {
        private final int numRows;
        private final int numCols;
        private final List<String> siloInstructions;
        private final List<Stream> inputStreams;
        private final List<Stream> outputStreams;
        public InputFileData(int numRows, int numCols, List<String> siloInstructions,
                             List<Stream> inputStreams, List<Stream> outputStreams) {
            this.numRows = numRows;
            this.numCols = numCols;
            this.siloInstructions = siloInstructions;
            this.inputStreams = inputStreams;
            this.outputStreams = outputStreams;
        }

        public int getNumRows() {
            return numRows;
        }

        public int getNumCols() {
            return numCols;
        }

        public List<String> getSiloInstructions() {
            return siloInstructions;
        }

        public List<Stream> getInputStreams() {
            return inputStreams;
        }

        public List<Stream> getOutputStreams() {
            return outputStreams;
        }

    }

    /**
     * parseInputFile is responsible for parsing the input file given a file path.
     * It reads the file line by line, processing the grid dimensions, input streams,
     * output streams, and silo instructions. The method returns an InputFileData
     * object containing the parsed data.
     */

    public InputFileData parseInputFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int numRows = 0;
        int numCols = 0;

        List<String> siloInstructions = new ArrayList<>();
        List<Stream> inputStreams = new ArrayList<>();
        List<Stream> outputStreams = new ArrayList<>();
        StringBuilder currentSilo = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(" ");
            if (numRows == 0 && numCols == 0) {
                numRows = Integer.parseInt(tokens[0]);
                numCols = Integer.parseInt(tokens[1]);
            } else if ("INPUT".equals(tokens[0])) {
                String[] coordinates = reader.readLine().split(" ");
                int row = Integer.parseInt(coordinates[0]);
                int col = Integer.parseInt(coordinates[1]);
                Stream input = new Stream(row, col, true);
                while (!(line = reader.readLine()).equals("END")) {
                    input.addValue(Integer.parseInt(line));
                }
                inputStreams.add(input);
            } else if ("OUTPUT".equals(tokens[0])) {
                String[] coordinates = reader.readLine().split(" ");
                int row = Integer.parseInt(coordinates[0]);
                int col = Integer.parseInt(coordinates[1]);
                Stream output = new Stream(row, col, false);
                outputStreams.add(output);
                reader.readLine(); // Read END line
            } else if ("END".equals(tokens[0])) {
                siloInstructions.add(currentSilo.toString().trim());
                currentSilo = new StringBuilder();
            } else {
                currentSilo.append(line).append("\n");
            }
        }

        // Check if there are enough silos, and if not, fill the remaining ones with "NOOP"
        int totalSilos = numRows * numCols;
        while (siloInstructions.size() < totalSilos) {
            siloInstructions.add("NOOP");
        }

        reader.close();
        return new InputFileData(numRows, numCols, siloInstructions, inputStreams, outputStreams);
    }


}
