//TODO Dis i' da wight 1
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by thad on 10/24/2017.
 */

@TeleOp(name="JeffsRealRun",group="Jeff" )

public class JeffsRealRun extends LinearOpMode {
    TouchSensor up;
    TouchSensor touchy;
    PengwinArm pengwinArm;
    JeffThePengwin jeffThePengwin;
    //
    //
    @Override
    public void runOpMode() throws InterruptedException {
        //
        jeffThePengwin = new JeffThePengwin(hardwareMap);
        pengwinArm = new PengwinArm(hardwareMap);
        //
        //neat variables
        double drive; //turn power
        double turn; //turn direction
        double leftX; //left x: joystick
        //true means not pushed false means is pushed
        up = hardwareMap.touchSensor.get("up");
        touchy = hardwareMap.touchSensor.get("touchy");
        double direction = 1;//change direction if needed (TODO is fun)
        //positive direction is down, negative direction is up
        //
        //
        waitForStartify();

        startify(direction);//Startify: verb, The act of starting or calibrating code primarily written by Eric Patton or Nora Dixon ex. I will startify the code.
        //
        while (opModeIsActive()) {
            //variable
            drive = gamepad1.left_stick_y; //left joystick moving up and down
            turn = gamepad1.right_stick_x; //right joystick left and right
            leftX = gamepad1.left_stick_x; //left joystick moving right and left
            //Use Pythagoras
            double power = getPathagorus(leftX, drive); //the current joystick position
            double rotate = getPathagorus(gamepad2.right_stick_y, gamepad2.right_stick_x);
            double stretch = getPathagorus(gamepad2.left_stick_y, gamepad2.left_stick_x);
            //
            buttons();
            //Change power
            double degreeOfArmPower = 1;
            //
            double degreePower = 1;
            if(gamepad2.left_bumper) {
                degreeOfArmPower = 0.5;
            }
            else if(gamepad2.right_bumper){
                degreeOfArmPower = 0.25;
            }
            //TODO This is negative stuff in color
            int armUpDirection = gamepad2.right_stick_y > 0 ? 1 : -1;
            int extendDirection = gamepad2.left_stick_y > 0 ? 1 : -1;
            //
            //Set the power
            if((isRaised() & rotate*direction > 0 ) || (isFelled() & rotate*direction < 0 ) || (!isRaised() & !isFelled())){
                //
                pengwinArm.setUpPower(armUpDirection* degreeOfArmPower*rotate*direction);
            }
            //
            pengwinArm.setAcrossPower(extendDirection*degreeOfArmPower*stretch);
            //
            //Set power stuff
            degreePower = getDegreePower(degreePower);

            getPowerStuff(power, degreePower);
            //
            boolean turningRight = turn < 0; //TODO This is not working right, it should be > but it is mixing up and left and righ
            boolean notTurning = turn == 0;
            boolean movingVertical = Math.abs(drive) > Math.abs(leftX);
            boolean strafingRight = leftX > 0;
            //
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
                // TODO pushing right joystick to the right
                //turn right by left wheels going forward and right going backwards
                jeffThePengwin.turnRight();
            } else {
                //turn left
                jeffThePengwin.turnLeft();
            }
            telemetryJazz();

        }

    }

    private void telemetryJazz() {
        //TODO Telemetry(Fancy jazz)
        telemetry.addData("calibrate high", pengwinArm.getCalibrate());
        telemetry.addData("penguin power set", jeffThePengwin.getPowerInput());
        telemetry.addData("penguin total power", jeffThePengwin.getTotalPower());
        telemetry.addData("penguin degree of power", jeffThePengwin.getDegreeOfPower());
        telemetry.addData("penguin arm up power", pengwinArm.getUpPower());
        telemetry.addData("penguin arm extend power", pengwinArm.getAcrossPower());
        telemetry.update();
    }

    private double getDegreePower(double degreePower) {
        if(gamepad1.left_bumper) {
            degreePower = 0.5;
        }
        else if(gamepad1.right_bumper){
            degreePower = 0.25;
        }
        return degreePower;
    }

    private void getPowerStuff(double power, double degreePower) {
        jeffThePengwin.degreeOfPower = degreePower;
        jeffThePengwin.powerInput = power;
    }

    private void buttons() {
        if(gamepad2.x){
            pengwinArm.goeyHomey();
        }
        if(gamepad2.y){
            pengwinArm.goUp();
        }
    }

    private void startify(double direction) {
        if(!touchy.isPressed()){
            pengwinArm.setUpPower(.4 * direction);
            while (!touchy.isPressed()) {
                //TODO Do nothing
            }
            pengwinArm.setUpPower(0);
            pengwinArm.setHome();
        }
    }
    public void waitForStartify(){
        waitForStart();
    }

    private boolean isRaised(){
        return up.isPressed();
    }

    private boolean isFelled(){
        return touchy.isPressed();
    }

    private double getPathagorus(double a, double b){//Define Pythagorean Theorem
        double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return c;

        //Finding the joystick position using pythagorean theorem (a2 + b2 = c2)
    }
}