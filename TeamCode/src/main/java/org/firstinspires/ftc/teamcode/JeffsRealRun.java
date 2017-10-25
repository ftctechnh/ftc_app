package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

/**
 * Created by thund on 10/24/2017.
 */

@TeleOp(name="JeffsRealRun",group="Jeff" )

public class JeffsRealRun extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        JeffThePengwin jeffThePengwin = new JeffThePengwin(hardwareMap);

        //neat variables
        double drive; //turn power
        double turn; //turn direction
        double leftX; //left x: joystick
        //true means not pushed false means is pushed
        DigitalChannel upTouch;  // if it is true it is not at the bottom
        DigitalChannel downTouch;  // if it is true it is not at the top
        upTouch = hardwareMap.get(DigitalChannel.class, "upTouch");
        downTouch = hardwareMap.get(DigitalChannel.class, "downTouch");



        waitForStart();

        while (opModeIsActive()) {
            movingJeffThePengwin(jeffThePengwin);
                if (upTouch.getState());
        }
    }

    private void movingJeffThePengwin(JeffThePengwin jeffThePengwin) {
        double drive;
        double turn;
        double leftX;
        drive = gamepad1.left_stick_y; //left joystick moving up and down
        turn = gamepad1.right_stick_x; //right joystick left and right
        leftX = gamepad1.left_stick_x; //left joystick moving right and left

        double power = getPathagorus(leftX, drive); //the current joystick position

        double degreePower = 1;
        if(gamepad1.left_bumper) {
            degreePower = 0.5;
        }
        else if(gamepad1.right_bumper){
            degreePower = 0.25;
        }

        jeffThePengwin.degreeOfPower = degreePower;
        jeffThePengwin.powerInput = power;
        boolean turningRight = turn < 0; //TODO This is not working right, it should be > but it is mixing up and left and righ
        boolean notTurning = turn == 0;
        boolean movingVertical = Math.abs(drive) > Math.abs(leftX);
        boolean strafingRight = leftX > 0;
        //main if/then
        if (notTurning) {
            //no movement in right joystick
            //start of driving section
            if (movingVertical) { //forward/back or left/right?
                if (drive > 0) { //forward
                    jeffThePengwin.driveForward();
                } else { //back
                    jeffThePengwin.driveBackward();
                }
            } else {
                if (strafingRight) { //right
                    jeffThePengwin.strafeRight();
                } else { //left
                    jeffThePengwin.strafeLeft();
                }
            }
        } else if (turningRight) {
            //pushing right joystick to the right
            //turn right by left wheels going forward and right going backwards
            jeffThePengwin.turnRight();
        } else {
            //turn left
            jeffThePengwin.turnLeft();
        }
    }

    private double getPathagorus(double a, double b){//Define Pythagorean Theorem
        double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return c;

        //Finding the joystick position using pythagorean theorem (a2 + b2 = c2)
    }
}