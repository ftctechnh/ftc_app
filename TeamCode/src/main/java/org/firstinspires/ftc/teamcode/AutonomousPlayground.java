package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by connorespenshade on 8/3/17.
 */

@Autonomous(name = "AutonPlayground")
public class AutonomousPlayground extends LinearOpMode {

    // Jason's ready to commit!
    private DcMotor motorLeft;
    private DcMotor motorRight;

    @Override
    public void runOpMode() throws InterruptedException {

        initDCMotors();

        waitForStart();

        int thing = 0;

        while (thing < 4) {

            move(1,1);
            sleep(1000);

            move(0, 1);
            sleep(1000);

            thing += 1;
        }

    }

    public void initDCMotors() {

        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");

        motorRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void move(float leftPower, float rightPower) {
        motorLeft.setPower(leftPower);
        motorRight.setPower(rightPower);
    }

    public int add(int number1, int number2) {

        return number1 + number2;

    }

}
