# Assembly Silos

This program creates Assembly Silos, which are independent virtual machines running a simple assembly language. Each silo operates on its own thread and communicates with other silos through a transfer region. The silos must remain in sync with each other, executing instructions in a coordinated manner. The assembly language used in the silos includes various instructions for performing operations on registers and ports. The program features a JavaFX GUI for user interaction, displaying the current values of ACC and BAK registers, values transferred between silos, and input/output streams. It should also have start, pause/step, and stop buttons for controlling the execution.

To use the program you need to only run it and then select your initial setup file. The setup file will feature the silo grid size, instructions for each silo, input/output streams, and their coordinates. Once the initial state is set up, you can press the start button and watch the program run. You can press the stop button to reset the program or, you can press the puase button and then step the program one instruction at a time. You can also add or remove instructions at will by writing them in to a silo or by deleteing already present commands. If there is input or output present in the program , it will displayed in columns in the left quadrant of the screen.

There are some issues with silo transfer labels which are sometimes delayed or don't show up at all.

Jack Vanlyssel:
Worked on parser and implementation of the interpreter to execute the code. Tested logic of the language implementation to ensure correct function. Worked on the GUI implementation. Worked on commands.

Luke McDougal:
Handled the threading aspect of the project, ensuring that each interpreter runs on its own thread and each transfer region operates correctly. Setup the silo network and connected the streams. Worked on the GUI implementaion. Worked on commands.

Spoorthi Menta:
Integrated the parser, interpreter, and JavaFX components, ensuring that they work together seamlessly. Worked on commands. Manged documentation and team organization.
