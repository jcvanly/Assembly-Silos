package main;

import commands.Instruction;
import network.SiloNetwork;
import network.SiloState;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;

public class Main {
    private static SiloNetwork network;
    private static Interpreter interpreter1;
    private static Interpreter interpreter2;
    private static Interpreter interpreter3;
    private static Interpreter interpreter4;

    public static void main(String[] args) {
        setup();
        runProgram();
    }

    public static void setup() {
        // Create a network.SiloNetwork
        network = new SiloNetwork(2, 2, 4);

        // Create network.SiloState instances for two silos using the network.SiloNetwork
        SiloState siloState1 = network.createSilo( 0,0);
        SiloState siloState2 = network.createSilo(0,1);
        SiloState siloState3 = network.createSilo(1,0);
        SiloState siloState4 = network.createSilo(1,1);

        // Create and execute test programs for each silo
        String program2 = "MOVE 5 ACC\n" + "SAVE\n" + "ADD DOWN";
        String program3 = "NOOP";
        String program4 = "MOVE 5 ACC";

    }

    public static void runProgram() {
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
