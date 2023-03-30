import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String code = "1 2\n" + "ADD 1\n" + "MOVE ACC RIGHT\n" + "ADD\n" + "END\n" +
                "ADD 10\n" + "SAVE\n" + "MOVE LEFT ACC\n" + "END\n" +
                "INPUT -1 1 10 11 12 13 END OUTPUT 1 1 END";

        Program program = Parser.parseCode(code);
        System.out.println("Number of rows: " + program.numRows);
        System.out.println("Number of columns: " + program.numCols);

        System.out.println("\nSilos:");
        for (int i = 0; i < program.silos.size(); i++) {
            System.out.println("\nSilo " + (i + 1) + ":");
            for (Instruction instruction : program.silos.get(i)) {
                System.out.println("  " + instruction.name + " " + String.join(" ", instruction.args));
            }
        }

        System.out.println("\nStreams:");
        for (Stream stream : program.streams) {
            System.out.print("  " + "INPUT" + "  " + Arrays.toString(stream.input));
            System.out.print("\n  OUTPUT " + Arrays.toString(stream.output));
            System.out.println();
        }
    }
}
