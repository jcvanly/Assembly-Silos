import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.*;

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
    String streamType;
    int[] coords;
    int[] values;
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

        // System.out.println(lineIdx + " " + lines.length);
        //System.out.println(lines[lineIdx]);
        // Parse streams
        while (lineIdx < lines.length) {
            String[] streamParts = lines[lineIdx].split(" ");
            Stream stream = new Stream();
            stream.streamType = streamParts[0];
            int coord1 = Integer.parseInt(streamParts[1]);
            int coord2 = Integer.parseInt(streamParts[2]);
            stream.coords = new int[] {coord1, coord2};
            //lineIdx++;

            if (stream.streamType.equals("INPUT")) {
                List<Integer> values = new ArrayList<>();
                String[] valueParts = lines[lineIdx].split(" ");

                for (String valuePart : valueParts) {
                    if (!valuePart.equals("END"))
                    {
                        if(valuePart.equals("INPUT"))
                        {
                            continue;
                        }

                        //System.out.println(Integer.parseInt(valuePart));
                        //System.out.println(valuePart);
                        values.add(Integer.parseInt(valuePart));
                    }
                    else
                    {
                        break;
                    }
                }
                stream.values = values.stream().mapToInt(Integer::intValue).toArray();
            }
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
