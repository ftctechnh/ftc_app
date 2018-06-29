package org.firstinspires.ftc.teamcode.evolve;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Eric on 6/10/2018.
 */

@TeleOp(name="teleOp2",group="evolve")
public class TeleOp2 extends LinearOpMode{

    DcMotor left;
    DcMotor right;
    TouchSensor wall;

    public void runOpMode() throws InterruptedException {

        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");
        wall = hardwareMap.touchSensor.get("wall");

        right.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()){
            /*
            Now let's add an if statement.

            example: /
            if(true){
                doSomething();
            }
            '
            It tells the program whether or not to do something.
            Let's add our own.

            Step 1 - add "if (){"' before "left.setPower"' and "}"' after "right.setPower"'

            Step 2 - add "wall.isPressed()"' in the parentheses
                example: if (wall.isPressed()){'

            Step 3 - allow backing out
                A. - add "&& ()"' after "wall.isPressed()"'

            "a > b"' means that a is great than b
            "a && b"' means that a and b must be true
            "a || b"' means that a or b must be true

            "gamepad1.right_stick_y"' means the right joystick on the first controller
            "gamepad1.left_stick_y"' means the left joystick on the first controller

            "gamepad1.left_stick_y > 0"' means the left stick is pushed forward
            "gamepad1.left_stick_y > 0 || gamepad1.right_stick_y > 0"' means the left stick or right stick are pushed forward

                B. - add a phrase to make sure at least one stick is pushed forward in the new parentheses
                    example: gamepad1.left_stick_y > 0 || gamepad1.right_stick_y > 0'

                C. - your if statement should look like this: (wall.isPressed() && (gamepad1.left_stick_y > 0 || gamepad1.right_stick_y > 0))

            Step 4 - make sure this doesn't happen by adding "!()"' around your statement inside the parentheses
                example: if (!(statement)){'
             */

            //1. - 4. /
            left.setPower(gamepad1.right_stick_y);
            right.setPower(gamepad1.left_stick_y);
            //1. /

            telemetry.addData("Wall: ", wall.isPressed());
            telemetry.update();

            /*
            Your if statement should look like this:

            example: /
            if (!(wall.isPressed()) && (gamepad1.left_stick_y > 0 || gamepad1.right_stick_y > 0)) {
                left.setPower(gamepad1.right_stick_y);
                right.setPower(gamepad1.left_stick_y);
            }
            '
             */

        }
    }
}
