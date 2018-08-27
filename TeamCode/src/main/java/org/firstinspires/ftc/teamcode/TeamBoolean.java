package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TeamBoolean", group="Basic OP Mode")
@Disabled

public class TeamBoolean extends LinearOpMode {

    private DcMotor motor1 = null;
    private DcMotor motor2 = null;
    private DcMotor motor3 = null;
    private DcMotor motor4 = null;

    public void runOpMode(){
        telemetry.addData(  "Status",   "Initialized");
        telemetry.update();

        motor1 = hardwareMap.get(DcMotor.class,                "motor1");
        motor2 = hardwareMap.get(DcMotor.class,                "motor2");
        motor3 = hardwareMap.get(DcMotor.class,                "motor3");
        motor4 = hardwareMap.get(DcMotor.class,                "motor4");

        waitForStart();

    while(opModeIsActive()){
        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;
        if ((x < 0 ) && (y < 0)) {
            motor1.setPower(-1);
            motor2.setPower(1);
            motor3.setPower(-1);
            motor4.setPower(1);
        } else if ((x > 0) && (y == 0)){
            motor1.setPower(1);
            motor3.setPower(1);
        } else if ((x == 0) && (y < 0)){
            motor2.setPower(1);
            motor4.setPower(1);
        } else if ((x > 0) && (y < 0)){
            motor4.setPower(1);
            motor2.setPower(1);
            motor1.setPower(1);
            motor3.setPower(1);
        } else if ((x < 0) && (y == 0)){
            motor3.setPower(-1);
            motor1.setPower(-1);
        } else if ((x == 0) && (y > 0)){
            motor2.setPower(1);
            motor4.setPower(1);
        } else if ((x < 0) && (y > 0)) {
            motor4.setPower(1);
            motor2.setPower(1);
            motor1.setPower(-1);
            motor3.setPower(-1);
        } else if ((x > 0) && (y > 0)){
            motor3.setPower(1);
            motor1.setPower(1);
            motor2.setPower(1);
            motor4.setPower(1);
        } else if ((x == 0) && (y == 0)){
            motor4.setPower(0);
            motor2.setPower(0);
            motor3.setPower(0);
            motor1.setPower(0);
        }

    }
    }
}
