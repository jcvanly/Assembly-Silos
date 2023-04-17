# CS 351 Project 4 Assembly Silos

## Project Overview

The Assembly Silos project aims to create a grid of virtual machines called silos, each running a simple assembly language. Each silo runs on its own thread and communicates with other silos through a transfer region. They must remain in sync, executing one instruction at a time and waiting for others to finish their current instruction.


## Group Work

This project was a collaborative effort of our team members, who contributed to the following parts:
### Team Members:

- Jack Vanlyssel:
  - Worked on parser and implementation of the interpreter to execute the code. Tested logic of the language implementation to ensure correct function. Worked on the GUI implementation. Worked on commands.

- Luke McDougall:
  - Handled the threading aspect of the project, ensuring that each interpreter runs on its own thread and each transfer region operates correctly. Setup the silo network and connected the streams. Worked on the GUI implementaion. Worked on commands.

- Spoorthi Menta:
  - Integrated the parser, interpreter, and JavaFX components, ensuring that they work together seamlessly. Worked on commands. Manged documentation and team organization.

## Architecture Overview

Each silo contains a program written in a custom assembly language. Silos can communicate with each other through ports (UP, DOWN, LEFT, RIGHT) and store data in registers (ACC, BAK, NIL).

The Assembly language supports various instructions such as NOOP, MOVE, SWAP, SAVE, ADD, SUB, NEGATE, JUMP, JEZ, JNZ, JGZ, JLZ, and JRO.

## GUI

The GUI is written in JavaFX and allows users to:

- Input a program via a .txt file
- Display the current value of ACC and BAK registers
- Show values being transferred between silos
- Start, pause/step, and stop the execution of each silo's code
- View the current value of each input stream and all previous values written to an output stream


## Getting Started

To run the Assembly Silos project, follow these steps:

1. Download the executable jar file `assembly-silos.jar`
2. Run the program with the console command `java -jar assembly-silos.jar [input].txt`
3. Use the provided controls to start, pause, step, and stop the execution of silo programs.

### Input file format:

```html
[numRows] [numCols] 
[instruction]
[instruction] 
. . .
END
[instruction]
[instruction]
. . .
END
. . . 
END 
INPUT
-1 1 
10
11 
12 
13 
END 
OUTPUT
1 1 
END
```
- The first line tells you how many rows and columns the grid is going to have. The subsequent lines after that are the instructions which must be loaded into the first silo. 
- Note the silos will be given row by row. Then when you see END that means the instructions for that silo are done. 
- You then move onto the next silo and so on and so forth. Next you must read in the input/output streams.
  An input stream will follow the keyword INPUT and will then be followed the
  coordinates of the stream. Note these coordinates will have 1 out of bound
  coordinate. This is to signify that is not by of the normal silo grid but rather
  adjacent to it. 
- The values of the stream are given after these
  coordinates. An output stream will follow the keyword OUTPUT and only
  coordinates will be given. Both stream will end with END

## Bugs

- Values being transferred shown on the GUI:
    - Since the javafx is updated after the transfer is done, the value being transferred is not shown on the GUI. However, the value is transferred correctly and is displayed after the transfer is done.





