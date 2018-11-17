package org.firstinspires.ftc.teamcode;

public class House {
    WashingMachine washingMachineA;

    String welcome = "hello";
    int x = "hello".length(); // = 5

    java.lang.String;
    double rulerLength = 12.3; //inches


    House(int size)
    {
        washingMachineA = new WashingMachine(6);
        washingMachineA.runRemotely();

        if(size < 100 )
            welcome = "go away";

    }

    telemetry.addData(welcome + "Machine A's number is", washingMachineA.modelNumber);
}
