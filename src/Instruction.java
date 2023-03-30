import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Instruction {
    String name;
    List<String> args = new ArrayList<>();

    Instruction(String name, String... args) {
        this.name = name;
        Collections.addAll(this.args, args);
    }
}