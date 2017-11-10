//TODO Dis i' da wight 1
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by thad on 10/24/2017.
 */

@TeleOp(name="JeffsRealRun",group="Jeff" )

public class JeffsRealRun extends LinearOpMode {
    //robot
    PengwinArm pengwinArm;
    JeffThePengwin jeffThePengwin;

    //sensors
    DigitalChannel up;
    DigitalChannel touchy;    double drive; //turn power


    double turn; //turn direction
    double leftX; //left x: joystick
    double power;
    double rotate;
    double stretch;
    double degreeOfArmPower = 1;
    double degreeOfRobotPower = 1;
    double servoPosition =.8;
    double directionOfArmMotor = 1;//change direction if needed (TODO is fun)
    double go;
    int armUpDirection;
    int extendDirection;
    boolean turningRight; //TODO This is not working right, it should be > but it is mixing up and left and righ
    boolean notTurning;
    boolean movingVertical;
    boolean strafingRight;
    //
    //
    @Override
    public void runOpMode() throws InterruptedException {
        //
        jeffThePengwin = new JeffThePengwin(hardwareMap);
        pengwinArm = new PengwinArm(hardwareMap);
        //
        //neat variables
        //true means not pushed false means is pushed
        up = hardwareMap.digitalChannel.get("up");
        touchy = hardwareMap.digitalChannel.get("touchy");
        //positive direction is down, negative direction is up
        //
        waitForStartify();

        startify();//Startify: verb, The act of starting or calibrating code primarily written by Eric Patton or Nora Dixon ex. I will startify the code.
        smartify();
        //
        while (opModeIsActive()) {
            //variable
            drive = gamepad1.left_stick_y; //left joystick moving up and down
            turn = gamepad1.right_stick_x; //right joystick left and right
            leftX = gamepad1.left_stick_x; //left joystick moving right and left
            //Use Pythagoras
            getThePowers();
            //
            buttons();
            //Change power
            //
            getDegreeOfArmMotor();
            //TODO This is negative stuff in color
            armUpDirection = gamepad2.right_stick_y > 0 ? 1 : -1;
            extendDirection = gamepad2.left_stick_y > 0 ? 1 : -1;
            //
            //Set the power
            moveTheArm(degreeOfArmPower, armUpDirection, extendDirection);
            //
            //Set power stuff
            setDegreePower();

            getPowerStuff(power, degreeOfRobotPower);
            //
            figureOutMovementOfRobot();
            //
            //main if/then
            moveTheRobot(turningRight, notTurning, movingVertical, strafingRight);
            //
            //
            if(gamepad2.dpad_right == true){
                pengwinArm.close();
            }else if (gamepad2.dpad_left == true){
                pengwinArm.open();
            }
            //
            telemetryJazz();

        }

    }

    private void getDegreeOfArmMotor() {
        if(gamepad2.left_bumper) {
            degreeOfArmPower = 0.5;
        }
        else if(gamepad2.right_bumper){
            degreeOfArmPower = 0.25;
        }else{
            degreeOfArmPower = 1.0;
        }
    }

    private void figureOutMovementOfRobot() {
        turningRight = turn > 0; //TODO This is not working right, it should be > but it is mixing up and left and righ
        notTurning = turn == 0;
        movingVertical = Math.abs(drive) >=Math.abs(leftX);
        strafingRight = leftX > 0;
    }

    private void moveTheArm(double degreeOfArmPower, int armUpDirection, int extendDirection) {
        setThePowerForMovingArmUpOrDown(degreeOfArmPower, armUpDirection);
        //
        //pengwinArm.setAcrossPower(extendDirection*degreeOfArmPower*stretch);
    }

    private void getThePowers() {
        power = getPathagorus(leftX, drive); //the current joystick position
        rotate = (getPathagorus(gamepad2.right_stick_y, gamepad2.right_stick_x) * .50);
        stretch = getPathagorus(gamepad2.left_stick_y, gamepad2.left_stick_x);
    }

    private void moveTheRobot(boolean turningRight, boolean notTurning, boolean movingVertical, boolean strafingRight) {
        if (notTurning) {
            //no movement in right joystick
            //start of driving section
            if (movingVertical) { //forward/back or left/right?
                if (drive < 0) { //forward because when the joystick is forward, y = -1
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
    }

    private void setThePowerForMovingArmUpOrDown(double degreeOfArmPower, int armUpDirection) {
        //if((isRaised() & rotate > 0 ) || (isFelled() & rotate < 0 ) || (!isRaised() & !isFelled())){
            //
            pengwinArm.setUpPower(armUpDirection* degreeOfArmPower*rotate);
        //}
    }

    private void telemetryJazz() {
        //TODO Telemetry(Fancy jazz)
        telemetry.addData("drive", drive);
        telemetry.addData("calibrate high", pengwinArm.getCalibrate());
        telemetry.addData("penguin power set", jeffThePengwin.getPowerInput());
        telemetry.addData("penguin total power", jeffThePengwin.getTotalPower());
        telemetry.addData("penguin degree of power", jeffThePengwin.getDegreeOfPower());
        telemetry.addData("penguin arm up power", pengwinArm.getUpPower());
        telemetry.addData("penguin arm extend power", pengwinArm.getAcrossPower());
        telemetry.addData("Left Stick X", gamepad1.left_stick_x);
        telemetry.addData("Left Stick Y", gamepad1.left_stick_y);
        telemetry.addData("Right Stick X", gamepad1.right_stick_x);
        telemetry.addData("touchy", touchy.getState());
        telemetry.addData("up", up.getState());
        telemetry.addData("Glyphy", pengwinArm.glyphy.getPosition());
        telemetry.addData("dpad right", gamepad2.dpad_right);
        telemetry.addData("dpad left", gamepad2.dpad_left);
        telemetry.addData("glyphy directions", pengwinArm.glyphy.getDirection());
        if (gamepad2.dpad_up == true){
            //pengwinArm.upMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            telemetry.addData("position", pengwinArm.getCalibrate());
           // pengwinArm.upMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        telemetry.addData("Unicorn Crossing", "Always a danger WATCH OUT");
        telemetry.update();
    }

    private void setDegreePower() {
        if(gamepad1.left_bumper) {
            degreeOfRobotPower = 0.5;
        }
        else if(gamepad1.right_bumper){
            degreeOfRobotPower = 0.25;
        }else{
            degreeOfRobotPower = 1.0;
        }
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

    private void startify() {
        if(!touchy.getState()){
            pengwinArm.setUpPower(-.4);
            while (!touchy.getState()) {
                //TODO Do nothing
            }
            pengwinArm.setUpPower(0);
            //pengwinArm.setHome();
        }
    }
    public void waitForStartify(){
        waitForStart();
    }

    private boolean isRaised(){
        return up.getState();
    }

    private boolean isFelled(){
        return touchy.getState();
    }

    private double getPathagorus(double a, double b){//Define Pythagorean Theorem
        double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return c;

        //Finding the joystick position using pythagorean theorem (a2 + b2 = c2)
    }
    //
    private void smartify(){//calibrate up position
        pengwinArm.upMotor.setPower(.3);//opposite of touchy
        while(up.getState()){
            //TODO Wookie
        }
        pengwinArm.upMotor.setPower(0);//stop the motor
        pengwinArm.upPosition = pengwinArm.upMotor.getCurrentPosition();
    }
}