# Assembly Silos

This program aims to create Assembly Silos, which are independent virtual machines running a simple assembly language. Each silo operates on its own thread and communicates with other silos through a transfer region. The silos must remain in sync with each other, executing instructions in a coordinated manner.

The assembly language used in the silos includes various instructions for performing operations on registers and ports. The program requires a JavaFX GUI for user interaction, displaying the current values of ACC and BAK registers, values transferred between silos, and input/output streams. It should also have start, pause/step, and stop buttons for controlling the execution.

The program takes user input through the command line to construct the initial state, including the grid size, instructions for each silo, input/output streams, and their coordinates. It is suggested to start with a parser to convert the raw text input into an abstract syntax tree (AST), followed by an interpreter to execute the code based on the AST. Finally, the visual representation of each silo should be created in JavaFX, and threading should be handled to ensure the proper execution of each silo's code.

How to use the program:
