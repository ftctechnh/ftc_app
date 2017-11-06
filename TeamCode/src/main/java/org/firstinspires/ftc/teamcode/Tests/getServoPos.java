package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Stephen Ogden on 10/15/17.
 * FTC 6128 | 7935
 * FRC 1595
 */
//@Disabled
@TeleOp(name = "servo demo", group = "Test")
public class getServoPos extends LinearOpMode {
    private final double SERVOUPPOS = .5;
    private final double SERVODOWNPOS = 0;
    private ElapsedTime runtime = new ElapsedTime();
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initializing...");
        telemetry.update();
        Servo servo = hardwareMap.servo.get("servo");
        servo.setPosition(SERVOUPPOS);
        telemetry.addData("Status", "Done! Press play to start");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            runtime.reset();
            servo.setPosition(SERVODOWNPOS);
            while (runtime.seconds() <= 1){
                telemetry.addData("Servo pos", servo.getPosition());
                telemetry.update();
            }
            runtime.reset();
            servo.setPosition(SERVOUPPOS);
            while (runtime.seconds() <= 1){
                telemetry.addData("Servo pos", servo.getPosition());
                telemetry.update();
            }
        }
    }
}
