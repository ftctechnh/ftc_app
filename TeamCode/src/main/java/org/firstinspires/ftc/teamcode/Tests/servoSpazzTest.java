package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Stephen Ogden on 9/18/17.
 * FTC 6128 | 7935
 * FRC 1595
 */
@Disabled
@Autonomous(name = "servo test", group = "Test")

public class servoSpazzTest extends LinearOpMode {
    private static final double INCREMENT = 0.1;     // amount to slew servo each CYCLE_MS cycle
    private static final int CYCLE_MS = 50;     // period of each cycle
    private static final double MAX_POS = 1.0;     // Maximum rotational position
    private static final double MIN_POS  = 0.0;     // Minimum rotational position
    Servo servo = null;
    DcMotor tomMotor = null;
    double  position = (MAX_POS - MIN_POS) / 2; // Start at halfway position
    boolean rampUp = true;
    @Override
    public void runOpMode() {
        Servo servo = hardwareMap.servo.get("servo");
        tomMotor = hardwareMap.dcMotor.get("tom");
        tomMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        tomMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        tomMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // Wait for the start button
        telemetry.addData("Status", "Press Start to scan Servo." );
        telemetry.update();
        waitForStart();
        while(opModeIsActive()) {
            tomMotor.setPower(.5);
            if (rampUp) {
                position += INCREMENT;
                if (position >= MAX_POS) {
                    position = MAX_POS;
                    rampUp = !rampUp;   // Switch ramp direction
                }
            } else {
                position -= INCREMENT;
                if (position <= MIN_POS) {
                    position = MIN_POS;
                    rampUp = !rampUp;  // Switch ramp direction
                }
            }
            telemetry.addData("Servo Position", "%5.2f", position);
            telemetry.addData("Status", "Running. Press Stop to end test." );
            telemetry.update();
            servo.setPosition(position);
            sleep(CYCLE_MS);
            idle();
        }
        telemetry.addData("Status", "Done");
        telemetry.update();
    }
}
