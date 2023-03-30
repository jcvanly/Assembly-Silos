import java.util.*;

class Instruction {
    String name;
    List<String> args = new ArrayList<>();

    Instruction(String name, String... args) {
        this.name = name;
        Collections.addAll(this.args, args);
    }
}

class Program {
    int numRows;
    int numCols;
    List<List<Instruction>> silos = new ArrayList<>();
    List<Stream> streams = new ArrayList<>();
}

class Stream {
    //String streamType;
    int[] output;
    int[] input;
}

class Parser {
    public static Program parse(String[] lines) {
        Program program = new Program();
        int numRows, numCols;
        String[] gridSize = lines[0].split(" ");
        numRows = Integer.parseInt(gridSize[0]);
        numCols = Integer.parseInt(gridSize[1]);
        program.numRows = numRows;
        program.numCols = numCols;

        int lineIdx = 1;

        for (int siloIdx = 0; siloIdx < numRows * numCols; siloIdx++) {
            List<Instruction> siloInstructions = new ArrayList<>();
            while (!lines[lineIdx].equals("END")) {
                String[] instructionParts = lines[lineIdx].split(" ");
                Instruction instruction = new Instruction(instructionParts[0], Arrays.copyOfRange(instructionParts, 1, instructionParts.length));
                siloInstructions.add(instruction);
                lineIdx++;
            }
            program.silos.add(siloInstructions);
            lineIdx++; // Move past the "END" line
        }

        // Parse streams
        while (lineIdx < lines.length) {
            String[] streamParts = lines[lineIdx].split(" ");
            Stream stream = new Stream();
                List<Integer> inputValues = new ArrayList<>();
                List<Integer> outputValues = new ArrayList<>();
                String[] valueParts = lines[lineIdx].split(" ");

                for (int i = 0; i < valueParts.length; i++) {
                    String valuePart = valueParts[i];

                    if (!valuePart.equals("END")) {
                        if (valuePart.equals("INPUT")) {
                            continue;
                        }
                        inputValues.add(Integer.parseInt(valuePart));
                    }

                    else
                    {
                        for(int j = i+2; j < valueParts.length-1; j++)
                        {
                            outputValues.add(Integer.parseInt(valueParts[j]));
                        }
                        break;
                    }
                }

                stream.input = inputValues.stream().mapToInt(Integer::intValue).toArray();
                stream.output = outputValues.stream().mapToInt(Integer::intValue).toArray();

            program.streams.add(stream);
            lineIdx++; // Move past the "END" line
        }

        return program;
    }

    static Program parseCode(String code) {
        String[] lines = code.split("\\r?\\n");
        return parse(lines);
    }
}
