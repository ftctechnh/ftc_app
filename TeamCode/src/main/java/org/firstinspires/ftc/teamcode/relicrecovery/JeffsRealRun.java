//TODO Dis i' da wight 1
package org.firstinspires.ftc.teamcode.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by thad on 10/24/2017.
 */

@TeleOp(name="JeffsRealRun",group="Jeff" )

public class JeffsRealRun extends LinearOpMode {
    //robot
    PengwinArm pengwinArm;
    JeffThePengwin jeffThePengwin;
    PengwinFin pengwinFin;

    //sensors
   double drive; //turn power


    double turn; //turn direction
    double leftX; //left x: joystick
    double power;
    double rotate;
    double stretch;
    double degreeOfArmPower = 1;
    double degreeOfRobotPower = 1;
    double ready = 1;
    double ready2 = 1;
    boolean flipify = false;
    int armUpDirection;
    int extendDirection;
    boolean turningRight;
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
        pengwinFin = new PengwinFin(hardwareMap);
        //
        //neat variables
        //true means not pushed false means is pushed
        //
        waitForStartify();
        //
        //startify();//Startify: verb, The act of starting or calibrating code primarily written by Eric Patton or Nora Dixon ex. I will startify the code.
        //smartify();
        //
        while (opModeIsActive()) {
            //variable
            drive = gamepad1.left_stick_y; //left joystick moving up and down
            turn = gamepad1.right_stick_x; //right joystick left and right
            leftX = gamepad1.left_stick_x; //left joystick moving right and left
            //Use Pythagoras
            getThePowers();
            //
            if(gamepad2.x){
                pengwinArm.open();
                startify();
            }
            if(gamepad2.y){
                pengwinArm.open();
                smartify();
            }
            //Change power
            //
            if(gamepad2.b && ready == 1){
                pengwinArm.resetify = pengwinArm.resetify * -1;
                ready = 0;
            }else if (!gamepad2.b && ready == 0){
                ready = 1;
            }
            //
            if(gamepad1.b && ready2 == 1){
                flipify = !flipify;
                ready2 = 0;
            }else if (!gamepad1.b && ready2 == 0){
                ready2 = 1;
            }
            //
            getDegreeOfArmMotor();
            //TODO This is negative stuff in color
            armUpDirection = gamepad2.right_stick_y > 0 ? 1 : -1;
            extendDirection = gamepad2.left_stick_y > 0 ? 1 : -1;
            //
            //Set the power
            setThePowerForMovingArmUpOrDown(degreeOfArmPower, armUpDirection);
            //
            pengwinArm.setAcrossPower(extendDirection*degreeOfArmPower*stretch);
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
            pengwinFin.moveFinUp();
            telemetryJazz();
            pengwinArm.retractify.setPower(-.3);
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
        strafingRight = leftX < 0;
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
                if (drive < 0 && !flipify) { //forward because when the joystick is forward, y = -1
                    jeffThePengwin.driveForward();
                } else if(drive >= 0 && !flipify || drive < 0 && flipify){ //back
                    jeffThePengwin.driveBackward();
                }else{
                    jeffThePengwin.driveForward();
                }
            } else {
                if (strafingRight && !flipify) { //right
                    jeffThePengwin.strafeLeft();
                } else if(!strafingRight && !flipify || strafingRight && flipify){ //left
                    jeffThePengwin.strafeRight();
                }else {
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
//        if((!isRaised() & armUpDirection < 0 ) || (isFelled() & armUpDirection > 0  )
//                || (isRaised() && !isFelled())){
            //
            pengwinArm.setUpPower(armUpDirection* degreeOfArmPower*rotate);
//        }
    }

    private void telemetryJazz() {
        //TODO Telemetry(Fancy jazz)
        telemetry.addData("drive", drive);
        telemetry.addData("penguin power set", jeffThePengwin.powerInput);
        telemetry.addData("penguin total power", jeffThePengwin.powerInput * jeffThePengwin.degreeOfPower);
        telemetry.addData("penguin degree of power", jeffThePengwin.degreeOfPower);
        telemetry.addData("penguin arm up power", pengwinArm.getUpPower());
        telemetry.addData("penguin arm extend power", pengwinArm.getAcrossPower());
        telemetry.addData("touchy", jeffThePengwin.touchy.getState());
        telemetry.addData("up", jeffThePengwin.touchy.getState());
        telemetry.addData("Glyphy", pengwinArm.leftGlyphy.getPosition());
        telemetry.addData("GlyphyRight", pengwinArm.rightGlyphy.getPosition());
        telemetry.addData("glyphy directions", pengwinArm.leftGlyphy.getDirection());
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

    private void startify() {
        if(!jeffThePengwin.touchy.getState()){
            pengwinArm.setUpPower(-.4);
            while (!jeffThePengwin.touchy.getState() && opModeIsActive()) {
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
        return jeffThePengwin.up.getState();
    }

    private boolean isFelled(){
        return jeffThePengwin.touchy.getState();
    }

    private double getPathagorus(double a, double b){//Define Pythagorean Theorem
        double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return c;

        //Finding the joystick position using pythagorean theorem (a2 + b2 = c2)
    }
    //
    private void smartify(){//calibrate up position
        pengwinArm.upMotor.setPower(.4);//opposite of touchy
        while(jeffThePengwin.up.getState() && opModeIsActive()){
            //TODO Wookie
        }
        pengwinArm.upMotor.setPower(0);//stop the motor
        pengwinArm.upPosition = pengwinArm.upMotor.getCurrentPosition();
    }
}