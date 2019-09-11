package org.firstinspires.ftc.simulator;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.common.SimulatableMecanumOpMode;
import org.firstinspires.ftc.teamcode.common.math.Pose;
import org.firstinspires.ftc.teamcode.robot.mecanum.VirtualMecanumHardware;
import org.mockito.Mockito;

public class SimulatedOpModeFactory {
    double FRAMERATE = 20;
    final static int INIT_ITERATIONS = 40;
    final static int RUN_ITERATIONS = 400;

    public SimulatableMecanumOpMode opMode; // Pointer to our op mode
    public VirtualMecanumHardware robot; // Pointer to our virtual robot

    public SimulatedOpModeFactory(Class c) {
        // Assert we were passed an opmode we can
        assert c.getSuperclass() == SimulatableMecanumOpMode.class;

        // Create a simulated mecanum robot
        this.robot = new VirtualMecanumHardware(new Pose(0, 0, 0));
        opMode = (SimulatableMecanumOpMode) Mockito.spy(c);
        Mockito.doReturn(robot).when(opMode).getRobot();

        // Mock the gamepads
        opMode.gamepad1 = new Gamepad();
        opMode.gamepad2 = new Gamepad();

        opMode.init();
        opMode.init_loop();
    }

    public void elapseCycles(double seconds, double loopHertz) {
        int iterations = (int) Math.round(seconds * loopHertz);
        for (int i = 0; i < iterations; i++) {
            opMode.loop();
            robot.elapse(seconds / iterations);
        }
    }

    /*public static void main(String[] args) {
        SimulatedOpModeFactory simulated = new SimulatedOpModeFactory(TestPurePursuitTracking.class);
        for (int i = 0; i < INIT_ITERATIONS; i++) {
            simulated.simulateInitLoop();
        }

        simulated.start();
        simulated.simulateInitLoop();
    }*/
}
