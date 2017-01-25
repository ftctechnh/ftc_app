package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
/**
 * Created by minds on 1/23/2016.
 */
public abstract class AutonomousBase extends OpMode {
    public final double HEADING_TOLERANCE = 7; //tolerance for heading calculations
    public final double DISTANCE_TOLERANCE = 1.0/12; //tolerance for heading calculations
    public final double DEGREES_TO_FEET = 3.96*Math.PI/1120/12;
    //EXPLAINATION:
    // (wheel diameter) * pi / (encoder ticks per rotation) /(inches in a foot)
    // This converts encoder ticks into feet.
    //**WARNING** Always calculate distance CHANGED, since encoders have no
    // concept of direction, and we are moving across a 2D plane.
    
    public static class MoveState{
      public static final int STOP = 0;
      public static final int FORWARD = 1;
      public static final int BACKWARD = 2;
      public static final int LEFT = 3;
      public static final int RIGHT = 4;
      public static final int TURN_TOWARDS_GOAL = 5;
      public static final int SHOOT = 6;
      public static final int SERVO_R = 7;
      public static final int SERVO_L = 8;
      public static final int BACKWARD_SLOW = 9;
      public static final int SERVO_M = 10;
      public static final int SHOOT_STOP = 11;
      public static final int FULL_STOP = 12;
      public static final int SHOOT_CONVEYOR = 13;
      public static final int SHOOT_WHEEL = 14;
      public static final int STRAFE_TOWARDS_GOAL = 15;
      public static final int TURN_TOWARDS_ANGLE = 16;
      public static final int LEFT_SLOW = 17;
      public static final int RIGHT_SLOW = 18;
      public static final int TURN_TOWARDS_ANGLE_SLOW= 19;
      public static final int SERVO_DEPLOY = 20;
      public static final int SERVO_C = 21;
    }


    DcMotor motorUp;
    DcMotor motorDown;
    DcMotor motorLeft;
    DcMotor motorRight;
    DcMotor motorRightShooter;
    DcMotor motorLeftShooter;
    DcMotor motorConveyor;
    Servo servoCollector;
    Servo servoLeftButton;
    Servo servoRightButton;
    Servo servoBeaconDeploy;
    TouchSensor touchRight;
    TouchSensor touchWall;
    ColorSensor colorLeft;
    ColorSensor colorRight;
    GyroSensor gyro;


    //We stateful now, boys.
    int gameState;
    int moveState;

    double power;
    double heading;
    double desiredAngle;
    boolean turnRight;
    int cDistF, lDistF, dDistF; //Forward distance variables
    int cDistS, lDistS, dDistS; //Sideways distance variables
    int cDistW, lDistW, dDistW; //Sideways distance variables
    double sTime; //Shooting timer
    double pTime; //Button presser timer
    double tDiff;

    int startPos = 6;
    Map map = new Map(startPos); //this map object will allow for easy manipulations.


    public void init() {
        motorUp = hardwareMap.dcMotor.get("front");
        motorDown = hardwareMap.dcMotor.get("back");
        motorLeft = hardwareMap.dcMotor.get("left");
        motorRight = hardwareMap.dcMotor.get("right");
        
        motorUp.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorDown.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        motorDown.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setDirection(DcMotor.Direction.REVERSE);

        motorRightShooter = hardwareMap.dcMotor.get("r_shoot");
        motorLeftShooter = hardwareMap.dcMotor.get("l_shoot");
        motorConveyor = hardwareMap.dcMotor.get("conveyor");
        motorLeftShooter.setDirection(DcMotor.Direction.REVERSE);
        motorRightShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motorLeftShooter.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        servoCollector = hardwareMap.servo.get("collector");
        servoLeftButton = hardwareMap.servo.get("l_button");
        servoRightButton = hardwareMap.servo.get("r_button");
        servoBeaconDeploy = hardwareMap.servo.get("b_servo");

        touchRight = hardwareMap.touchSensor.get("right_touch");
        touchWall = hardwareMap.touchSensor.get("wall_touch");

        I2cAddr colorAddrLeft = I2cAddr.create8bit(0x3C);
        I2cAddr colorAddrRight = I2cAddr.create8bit(0x4C);
        colorLeft = hardwareMap.colorSensor.get("color_l");
        colorRight = hardwareMap.colorSensor.get("color_r");
        colorLeft.setI2cAddress(colorAddrLeft);
        colorRight.setI2cAddress(colorAddrRight);
        colorLeft.enableLed(false);
        colorRight.enableLed(false);

        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();
    }

    public void moveState(){
        heading = gyro.getHeading();
        switch(moveState){
            case MoveState.STOP:
                // Halts all drivetrain movement of the robot
                motorUp.setPower(0);
                motorDown.setPower(0);
                motorLeft.setPower(0);
                motorRight.setPower(0);
                break;
            case MoveState.FORWARD:
                // Moves the bot forward at half speed
                power = .5; //power coefficient
                if(map.distanceToGoal()>DISTANCE_TOLERANCE) {
                    motorUp.setPower(0);
                    motorDown.setPower(0);
                    motorLeft.setPower(power);
                    motorRight.setPower(power);
                }
                break;
            case MoveState.BACKWARD:
                // Moves the bot backwards at half speed
                power = -.5; //power coefficient
                if(map.distanceToGoal()>DISTANCE_TOLERANCE) {
                    motorUp.setPower(0);
                    motorDown.setPower(0);
                    motorLeft.setPower(power);
                    motorRight.setPower(power);
                }
                break;
            case MoveState.BACKWARD_SLOW:
                // Moves the bot backwards at minimum speed
                power = -.2; //power coefficient
                if(map.distanceToGoal()>DISTANCE_TOLERANCE) {
                    motorLeft.setPower(power);
                    motorRight.setPower(power);
                    motorUp.setPower(0);
                    motorDown.setPower(0);
                }
                servoLeftButton.setPosition(.5); // HACK
                break;               
            case MoveState.LEFT:
                // Moves the bot left at half speed
                power = -.5; //power coefficient
                if(map.distanceToGoal()>DISTANCE_TOLERANCE) {
                    motorLeft.setPower(0);
                    motorRight.setPower(0);
                    motorUp.setPower(power);
                    motorDown.setPower(power);
                }
                break;
            case MoveState.LEFT_SLOW:
                // Moves the bot left at half speed
                power = -.2; //power coefficient
                if(map.distanceToGoal()>DISTANCE_TOLERANCE) {
                    motorLeft.setPower(0);
                    motorRight.setPower(0);
                    motorUp.setPower(power);
                    motorDown.setPower(power);
                }
                break;
            case MoveState.RIGHT:
                // Moves the bot right at half speed
                power = .5; //power coefficient
                if(map.distanceToGoal()>DISTANCE_TOLERANCE) {
                    motorLeft.setPower(0);
                    motorRight.setPower(0);
                    motorUp.setPower(power);
                    motorDown.setPower(power);
                }
                break;
            case MoveState.RIGHT_SLOW:
                // Moves the bot right at half speed
                power = .2; //power coefficient
                if(map.distanceToGoal()>DISTANCE_TOLERANCE) {
                    motorLeft.setPower(0);
                    motorRight.setPower(0);
                    motorUp.setPower(power);
                    motorDown.setPower(power);
                }
                break;
            case MoveState.STRAFE_TOWARDS_GOAL:
                // Moves the bot towards the goal, while always pointing at desiredAngle
                double P = .5;
                double H = Math.toRadians(heading);
                double Ht = Math.toRadians(map.angleToGoal());

                motorUp.setPower(-P * Math.sin(H - Ht));
                motorDown.setPower(-P * Math.sin(H - Ht));
                motorLeft.setPower(P * Math.cos(H - Ht));
                motorRight.setPower(P * Math.cos(H - Ht));
                break;
            case MoveState.TURN_TOWARDS_GOAL:
                // Orients the bot to face the goal
                power = .25;
                if(heading<=180){
                    turnRight = heading <= map.angleToGoal() && heading + 180 >= map.angleToGoal();
                }else{
                    turnRight = !(heading >= map.angleToGoal() && heading - 180 <= map.angleToGoal());
                }

                if(turnRight){
                    motorUp.setPower(power);
                    motorDown.setPower(-power);
                    motorLeft.setPower(power);
                    motorRight.setPower(-power);
                }else{
                    motorUp.setPower(-power);
                    motorDown.setPower(power);
                    motorLeft.setPower(-power);
                    motorRight.setPower(power);
                }
                break;
            case MoveState.TURN_TOWARDS_ANGLE:
                // Orients the bot to face at desiredAngle.
                power = .3;
                if(heading<=180){
                    turnRight = heading <= desiredAngle && heading + 180 >= desiredAngle;
                }else{
                    turnRight = !(heading >= desiredAngle && heading - 180 <= desiredAngle);
                }

                if(turnRight){
                    motorUp.setPower(power);
                    motorDown.setPower(-power);
                    motorLeft.setPower(power);
                    motorRight.setPower(-power);
                }else{
                    motorUp.setPower(-power);
                    motorDown.setPower(power);
                    motorLeft.setPower(-power);
                    motorRight.setPower(power);
                }
                break;
            case MoveState.TURN_TOWARDS_ANGLE_SLOW:
                // Orients the bot to face at desiredAngle.
                power = .2;
                if(heading<=180){
                    turnRight = heading <= desiredAngle && heading + 180 >= desiredAngle;
                }else{
                    turnRight = !(heading >= desiredAngle && heading - 180 <= desiredAngle);
                }

                if(turnRight){
                    motorUp.setPower(power);
                    motorDown.setPower(-power);
                    motorLeft.setPower(power);
                    motorRight.setPower(-power);
                }else{
                    motorUp.setPower(-power);
                    motorDown.setPower(power);
                    motorLeft.setPower(-power);
                    motorRight.setPower(power);
                }
                break;
            case MoveState.SERVO_R:
                // Hits right button with wumbo
                servoLeftButton.setPosition(1);
                break;
            case MoveState.SERVO_L:
                // Hits left button with wumbo
                servoLeftButton.setPosition(0);
                break;
            case MoveState.SERVO_DEPLOY:
                servoBeaconDeploy.setPosition(1);
                break;
             case MoveState.SERVO_M:
                // Retracts wumbo
                servoLeftButton.setPosition(.5);
                break;
            case MoveState.SERVO_C:
                 servoCollector.setPosition(1);
                 break;
             case MoveState.SHOOT:
                // Shoots ball out of conveyor
                motorLeftShooter.setPower(.4);
                motorRightShooter.setPower(.4);
                motorConveyor.setPower(1);
                break;
            case MoveState.FULL_STOP:
                // Stop ALL robot movement, and resets servo to default pos
                servoLeftButton.setPosition(.5);
                servoCollector.setPosition(.5);
                motorUp.setPower(0);
                motorDown.setPower(0);
                motorLeft.setPower(0);
                motorRight.setPower(0);
                motorLeftShooter.setPower(0);
                motorRightShooter.setPower(0);
                motorConveyor.setPower(0);
                break;
              case MoveState.SHOOT_STOP:
                // Stop shooter mechanism
                motorLeftShooter.setPower(0);
                motorRightShooter.setPower(0);
                motorConveyor.setPower(0);
                  servoCollector.setPosition(.5);
                break;
           case MoveState.SHOOT_WHEEL:
                // Spins fly-wheels
                motorLeftShooter.setPower(.4);
                motorRightShooter.setPower(.4);
                break;
           case MoveState.SHOOT_CONVEYOR:
                // Pushed ball towards flywheel
                motorConveyor.setPower(1);
                servoCollector.setPosition(1);
                break;
        }
        map.moveRobot(dDistS * DEGREES_TO_FEET, (heading+90%360));
        map.moveRobot(dDistF * DEGREES_TO_FEET, heading);
    }

    public void gameState(){
        heading = gyro.getHeading();

        lDistF = cDistF;
        cDistF = ( motorLeft.getCurrentPosition()
                 + motorRight.getCurrentPosition()
        ) / 2;
        dDistF = cDistF - lDistF;

        lDistS = cDistS;
        cDistS = ( motorUp.getCurrentPosition()
                 + motorDown.getCurrentPosition()
        ) / 2;
        dDistS = cDistS - lDistS;

        lDistW = cDistW;
        cDistW = ( motorLeftShooter.getCurrentPosition()
                 + motorRightShooter.getCurrentPosition()
        ) / 2;
        dDistW = cDistW - lDistW;

        if(tDiff == 0){
            tDiff = getRuntime();
        }
    }

    public void telemetry(){
        telemetry.addData("angle to goal ",map.angleToGoal());
        telemetry.addData("Runtime ",getRuntime());
        telemetry.addData("colorLeft ","Left R: " + colorLeft.red() + " G: " + colorLeft.green() + " B: " + colorLeft.blue() + " A: " + colorLeft.alpha() + " RGBA: " + colorLeft.argb());
        telemetry.addData("colorRight ","Right R: " + colorRight.red() + " G: " + colorRight.green() + " B: " + colorRight.blue() + " A: " + colorRight.alpha() + " RGBA: " + colorRight.argb());
        telemetry.addData("dist from goal ",map.distanceToGoal());
        telemetry.addData("goal (x,y) ","(" +
          map.getGoalX() + "," + 
          map.getGoalY() + ")");
        telemetry.addData("Robot(x,y) ","(" +
          map.getRobotX() + "," + 
          map.getRobotY() + ")");
        telemetry.addData("robot theta",heading);
        telemetry.addData("Am I lined up?", linedUp());
        telemetry.addData("moveState", moveState);
        telemetry.addData("gameState", gameState);
    }

    @Override
    public void loop(){
        gameState();
        moveState();
        telemetry();
    }
    public boolean linedUp() {
        if (Math.abs(heading - map.angleToGoal()) < HEADING_TOLERANCE || (heading > 360 - HEADING_TOLERANCE && map.angleToGoal() < HEADING_TOLERANCE || (heading < HEADING_TOLERANCE && map.angleToGoal() > 360 - HEADING_TOLERANCE))) {
            return true;
        } else {
            return false;
        }
    }
    public boolean linedUpAngle() {
        if (Math.abs(heading - desiredAngle) < HEADING_TOLERANCE || (heading > 360 - HEADING_TOLERANCE && desiredAngle < HEADING_TOLERANCE || (heading < HEADING_TOLERANCE && desiredAngle > 360 - HEADING_TOLERANCE))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean linedUpAngle(int HEADING_TOLERANCE) {
        if (Math.abs(heading - desiredAngle) < HEADING_TOLERANCE || (heading > 360 - HEADING_TOLERANCE && desiredAngle < HEADING_TOLERANCE || (heading < HEADING_TOLERANCE && desiredAngle > 360 - HEADING_TOLERANCE))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean linedUpRev() {
        if (Math.abs(heading - map.angleToGoalRev()) < HEADING_TOLERANCE || (heading > 360 - HEADING_TOLERANCE && map.angleToGoalRev() < HEADING_TOLERANCE || (heading < HEADING_TOLERANCE && map.angleToGoalRev() > 360 - HEADING_TOLERANCE))) {
            return true;
        } else {
            return false;
        }
    }

    public double actualRuntime() {
        return getRuntime() - tDiff;
    }
}
