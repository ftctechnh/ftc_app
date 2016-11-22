package org.firstinspires.ftc.team9374;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by darwin on 9/20/16.
 */

@TeleOp(name="Basic Tank Drive!", group="Normal_Opmode")

public class Distance_Function_TankD extends OpMode {
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    MotorMatr Test = new MotorMatr();
    public void init()  {
        leftFront = hardwareMap.dcMotor.get("Motor-left");
        rightFront = hardwareMap.dcMotor.get("Motor-right");
        leftBack = hardwareMap.dcMotor.get("Motor-rear-left");
        rightBack = hardwareMap.dcMotor.get("Motor-rear-right");
        // Combine all the motors into a single Object; [[0, 1, 2, 3], ...]
        Test.addMotors(0, leftFront, rightFront);
        Test.addMotors(1, leftBack, rightBack);
    }

    public void loop() {
        float leftDC = -gamepad1.left_stick_y;
        float rightDC =  -gamepad1.right_stick_y;

        // Run all motors at [0]
        Test.runMotors(0, leftDC);
        // Run all motors at [1]
        Test.runMotors(1, rightDC);
    }
}
