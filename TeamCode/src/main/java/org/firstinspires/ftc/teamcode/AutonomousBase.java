package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
/**
 * Created by minds on 1/23/2016.
 */
public abstract class AutonomousBase extends OpMode {
    public final double HEADING_TOLERANCE = 12; //tolerance for heading calculations
    public final double DISTANCE_TOLERANCE = 1.0/12; //tolerance for heading calculations
    public final double DEGREES_TO_FEET = 4*Math.PI/1120/12;
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
      public static final int SERVO_STARBOARD_R = 7;
      public static final int SERVO_STARBOARD_L = 8;
      public static final int SERVO_PORT_R = 9;
      public static final int SERVO_PORT_L = 10;
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
    TouchSensor touchRight;
    TouchSensor touchLeft;
    ColorSensor colorLeft1;
    ColorSensor colorLeft2;
    ColorSensor colorRight1;
    ColorSensor colorRight2;
    GyroSensor gyro;


    //We stateful now, boys.
    int gameState;
    int moveState;
    boolean inited = false;

    double power;
    double heading;
    int cDistF, lDistF, dDistF; //Forward distance variables
    int cDistS, lDistS, dDistS; //Sideways distance variables
    double tDiff; // getRuntime() does this really annoying thing where it counts init time, so I
    // mark the first time I exit init, and override getRuntime() to return that instead
    double climbTime;

    int startPos = 6;
    Map map = new Map(startPos); //this map object will allow for easy manipulations.


    public void init() {
        motorUp = hardwareMap.dcMotor.get("front");
        motorDown = hardwareMap.dcMotor.get("back");
        motorLeft = hardwareMap.dcMotor.get("left");
        motorRight = hardwareMap.dcMotor.get("right");

        motorDown.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setDirection(DcMotor.Direction.REVERSE);

        motorRightShooter = hardwareMap.dcMotor.get("r_shoot");
        motorLeftShooter = hardwareMap.dcMotor.get("l_shoot");
        motorConveyor = hardwareMap.dcMotor.get("conveyor");

        servoCollector = hardwareMap.servo.get("collector");
        servoLeftButton = hardwareMap.servo.get("l_button");
        servoRightButton = hardwareMap.servo.get("r_button");

        touchRight = hardwareMap.touchSensor.get("right_touch");
        touchLeft = hardwareMap.touchSensor.get("left_touch");

        colorLeft1 = hardwareMap.colorSensor.get("color_left_1");
        colorLeft2 = hardwareMap.colorSensor.get("color_left_2");
        colorLeft1.enableLed(false);
        colorLeft2.enableLed(false);

        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();
    }

    public void moveState(){
        switch(moveState){
            case MoveState.STOP:
                //Case zero is 'stop'
                motorUp.setPower(0);
                motorDown.setPower(0);
                motorLeft.setPower(0);
                motorRight.setPower(0);
                break;
            case MoveState.FORWARD:
                //Case one is 'move towards' in the most literal sense. It assumes the path is
                //clear, and that there is a goal(9), and us(1) on the map somewhere.
                power = .75; //power coefficient
                if(map.distanceToGoal()>DISTANCE_TOLERANCE) {
                    motorLeft.setPower(power);
                    motorRight.setPower(power);
                    map.moveRobot(dDistF * DEGREES_TO_FEET, heading);
                }
                break;
            case MoveState.BACKWARD:
                //Case one is 'move towards' in the most literal sense. It assumes the path is
                //clear, and that there is a goal(9), and us(1) on the map somewhere.
                power = -.75; //power coefficient
                if(map.distanceToGoal()>DISTANCE_TOLERANCE) {
                    motorLeft.setPower(power);
                    motorRight.setPower(power);
                    map.moveRobot(dDistF * DEGREES_TO_FEET, heading);
                }
                break;
            case MoveState.LEFT:
                power = -.75; //power coefficient
                if(map.distanceToGoal()>DISTANCE_TOLERANCE) {
                    motorUp.setPower(power);
                    motorDown.setPower(power);
                    map.moveRobot(-dDistS * DEGREES_TO_FEET, heading);
                }
                break;

           case MoveState.RIGHT:
                power = .75; //power coefficient
                if(map.distanceToGoal()>DISTANCE_TOLERANCE) {
                    motorUp.setPower(power);
                    motorDown.setPower(power);
                    map.moveRobot(-dDistS * DEGREES_TO_FEET, heading);
                }
                break;

            case MoveState.TURN_TOWARDS_GOAL:
                //Case Three is 'turn towards'.
                power = .1;
                boolean turnRight;

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
            case MoveState.SERVO_STARBOARD_R:
                servoRightButton.setPosition(1);
                break;
            case MoveState.SERVO_STARBOARD_L:
                servoRightButton.setPosition(0);
                break;
            case MoveState.SERVO_PORT_R:
                servoLeftButton.setPosition(1);
                break;
            case MoveState.SERVO_PORT_L:
                servoLeftButton.setPosition(0);
                break;
            case MoveState.SHOOT:
                motorLeftShooter.setPower(1);
                motorRightShooter.setPower(1);
                motorConveyor.setPower(1);
                break;
        } 
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

        if(!inited){
            tDiff = getRuntime();
            inited = true;
        }
        //if(getRuntime() > 29) gameState = 777;  //robot death switch
    }

    public void telemetry(){
        telemetry.addData("angle to goal ",map.angleToGoal());
        telemetry.addData("dist from goal ",map.distanceToGoal());
        telemetry.addData("goal (x,y) ","(" +
          map.getGoalX() + "," + 
          map.getGoalY() + ")");
        telemetry.addData("Robot(x,y) ","(" +
          map.getRobotX() + "," + 
          map.getRobotY() + ")");
        telemetry.addData("robot theta",heading);
        telemetry.addData("Am I lined up?", linedUp());
        telemetry.addData("Color Sensor 1 blue", colorLeft1.blue());
        telemetry.addData("Color Sensor 2 blue", colorLeft2.blue());
        telemetry.addData("moveState", moveState);
        telemetry.addData("gameState", gameState);
        telemetry.addData("distance_tolerance", DISTANCE_TOLERANCE);
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
    public boolean linedUpRev() {
        if (Math.abs(heading - map.angleToGoalRev()) < HEADING_TOLERANCE || (heading > 360 - HEADING_TOLERANCE && map.angleToGoalRev() < HEADING_TOLERANCE || (heading < HEADING_TOLERANCE && map.angleToGoalRev() > 360 - HEADING_TOLERANCE))) {
            return true;
        } else {
            return false;
        }
    }

    public double getRuntime() {
        return super.getRuntime() - tDiff;
    }

}
