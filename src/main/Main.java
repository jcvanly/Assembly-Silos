package main;

import commands.Instruction;
import network.SiloNetwork;
import network.SiloState;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;

public class Main {
    public static void main(String[] args) {
        // Create a network.SiloNetwork
        SiloNetwork network = new SiloNetwork(2, 2, 4);

        // Create network.SiloState instances for two silos using the network.SiloNetwork
        SiloState siloState1 = network.createSilo("Silo1", 0,0);
        SiloState siloState2 = network.createSilo("Silo2",0,1);
        SiloState siloState3 = network.createSilo("Silo3",1,0);
        SiloState siloState4 = network.createSilo("Silo4",1,1);

        // Create and execute test programs for each silo
        Parser parser = new Parser();

        // Test program for silo1
        String program1 =
                "ADD 1\n" +
                "MOVE ACC RIGHT\n" +
                "SAVE";
        List<Instruction> instructions1 = parser.parse(program1);

        // Test program for silo2
        String program2 =
                "ADD 10\n" +
                "SAVE\n" +
                "MOVE RIGHT ACC";
        List<Instruction> instructions2 = parser.parse(program2);

        // Test program for silo3
        String program3 = "NOOP";
        List<Instruction> instructions3 = parser.parse(program3);

        // Test program for silo4
        String program4 = "NOOP";
        List<Instruction> instructions4 = parser.parse(program4);

        // Create interpreters for each network.SiloState
        Interpreter interpreter1 = new Interpreter(siloState1, instructions1);
        Interpreter interpreter2 = new Interpreter(siloState2, instructions2);
        Interpreter interpreter3 = new Interpreter(siloState3, instructions3);
        Interpreter interpreter4 = new Interpreter(siloState4, instructions4);

        // Execute the programs in silos concurrently
        Thread silo1 = new Thread(() -> {
            try {
                interpreter1.run();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        Thread silo2 = new Thread(() -> {
            try {
                interpreter2.run();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        Thread silo3 = new Thread(() -> {
            try {
                interpreter3.run();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        Thread silo4 = new Thread(() -> {
            try {
                interpreter4.run();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        silo1.start();
        silo2.start();
        silo3.start();
        silo4.start();

        // Wait for the threads to finish (if needed)
        try {
            silo1.join();
            silo2.join();
            silo3.join();
            silo4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
