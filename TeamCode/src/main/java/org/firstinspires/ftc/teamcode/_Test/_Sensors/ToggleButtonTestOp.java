package org.firstinspires.ftc.teamcode._Test._Sensors;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode._Libs.ToggleButton;


/**
 * Created by phanau on 11/25/16.
 * Test software toggle buttons
 */
@TeleOp(name="Test: Toggle Button Test 1", group ="Test")
//@Disabled
public class ToggleButtonTestOp extends OpMode {

    private ToggleButton toggleA;
    private ToggleButton toggleB;
    private ToggleButton toggleX;
    private ToggleButton toggleY;

    public ToggleButtonTestOp() {
    }

    public void init() {
        toggleA = new ToggleButton(gamepad1.a, 2, 0);       // 0/1 toggle on press (down) of A
        toggleB = new ToggleButton(gamepad1.b, 3, 0);       // 0/1/2 cycle toggle on press (down) of B
        toggleX = new ToggleButton(gamepad1.x, 2, 0);       // 0/1 toggle on release (up) of X
        toggleX.setTransitions(true, false);
        toggleY = new ToggleButton(gamepad1.y, 4, 0);       // 0/1/2/3 toggle advances on both press and release of Y
        toggleY.setTransitions(true, true);
    }

    public void loop() {
        // process button inputs from controller
        toggleA.process(gamepad1.a);
        toggleB.process(gamepad1.b);
        toggleX.process(gamepad1.x);
        toggleY.process(gamepad1.y);

        // log data to DriverStation
        telemetry.addData("toggleA 0/1 toggle on press (down) of A: ", toggleA.value());
        telemetry.addData("toggleB 0/1/2 cycle on press (down) of B: ", toggleB.value());
        telemetry.addData("toggleX 0/1 toggle on release (up) of X: ", toggleX.value());
        telemetry.addData("toggleY 0/1/2/3 on press and release of Y: ", toggleY.value());
    }

    public void stop() {}

}
