package org.firstinspires.ftc.simulator;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.autonomous.TestPurePursuitTracking;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SimulatedOpMode {
    double FRAMERATE = 20;
    final static int INIT_ITERATIONS = 40;
    final static int RUN_ITERATIONS = 400;

    OpMode opMode;

    public SimulatedOpMode(Class c) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = c.getConstructor();
        this.opMode = (OpMode) constructor.newInstance();

        opMode.init();
        opMode.init_loop();
    }

    public void simulateInitLoop() {opMode.init_loop();}
    public void start() {opMode.start();}
    public void loop() {opMode.loop();}

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        SimulatedOpMode simulated = new SimulatedOpMode(TestPurePursuitTracking.class);
        for (int i = 0; i < INIT_ITERATIONS; i++) {
            simulated.simulateInitLoop();
        }

        simulated.start();
        simulated.simulateInitLoop();
    }
}
