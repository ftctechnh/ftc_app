package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
@Autonomous(name = "tavis", group = "Test")
public class travis extends LinearOpMode {
    public void runOpMode() {
        double power = 0.1;
        boolean rampUp = true;
        DcMotor motor =hardwareMap.dcMotor.get("bob");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        waitForStart();
        while(opModeIsActive()){
          motor.setPower(power);
            if (rampUp) {
                power += 0.1;
                if (power >= 1.0) {
                    rampUp = !rampUp;
                    power = 1.0;
                }
            }
            if (!rampUp) {
                power -= 0.1;
                if (power <= 0.0) {
                    rampUp = !rampUp;
                    power = 0.0;
                }
            }
            sleep(100);
            idle();
        }
    }
}
