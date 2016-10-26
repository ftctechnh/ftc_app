package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by minds on 1/23/2016.
 */
public abstract class AutonomousBase extends OpMode {
    public final double TOL = 7; //tolerance for heading calculations
    public final double DEGREES_TO_FEET = 0.0007257875336263644793718177649;
    //EXPLAINATION:
    //204.5=65(diameter of sprocket in mm)*PI
    //.03937 = millimeter to inch conversion
    //(1440*12) converts encoder reading, where one rotation is 1440, to feet.
    //**WARNING** Always calculate distance CHANGED, since encoders have no
    // concept of direction, and we are moving across a 2D plane.
    
    public static class MoveState{
      public static final int STOP = 0;
      public static final int FORWARD = 1;
      public static final int BACKWARD = 2;
      public static final int LEFT = 3;
      public static final int RIGHT = 4;
      public static final int TURN_TOWARDS_GOAL= 5;
    }


    DcMotor motorUp;
    DcMotor motorDown;
    DcMotor motorLeft;
    DcMotor motorRight;
    DcMotor motorRightShooter;
    DcMotor motorLeftShooter;
    DcMotor motorConveyer;
    Servo servoCollector;
    Servo servoLeftButton;
    Servo servoRightButton;
    TouchSensor touchRight;
    TouchSensor touchLeft;
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

        motorUp.setDirection(DcMotor.Direction.REVERSE);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        motorRightShooter = hardwareMap.dcMotor.get("r_shoot");
        motorLeftShooter = hardwareMap.dcMotor.get("l_shoot");
        motorConveyer = hardwareMap.dcMotor.get("convyer");

        servoCollector = hardwareMap.servo.get("collector");
        servoLeftButton = hardwareMap.servo.get("l_button");
        servoRightButton = hardwareMap.servo.get("r_button");

        touchRight = hardwareMap.touchSensor.get("right_touch");
        touchLeft = hardwareMap.touchSensor.get("left_touch");
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
                if(map.distanceToGoal()>1/12) {
                    motorLeft.setPower(power);
                    motorRight.setPower(power);
                    map.moveRobot(dDistF * DEGREES_TO_FEET, heading);
                }
                break;
            case MoveState.BACKWARD:
                //Case one is 'move towards' in the most literal sense. It assumes the path is
                //clear, and that there is a goal(9), and us(1) on the map somewhere.
                power = -.75; //power coefficient
                if(map.distanceToGoal()>1/12) {
                    motorLeft.setPower(power);
                    motorRight.setPower(power);
                    map.moveRobot(dDistF * DEGREES_TO_FEET, heading);
                }
                break;
            case MoveState.LEFT:
                power = .75; //power coefficient
                if(map.distanceToGoal()>1/12) {
                    motorUp.setPower(power);
                    motorDown.setPower(power);
                    map.moveRobot(dDistS * DEGREES_TO_FEET, heading);
                }
                break;

           case MoveState.RIGHT:
                power = -.75; //power coefficient
                if(map.distanceToGoal()>1/12) {
                    motorUp.setPower(power);
                    motorDown.setPower(power);
                    map.moveRobot(dDistS * DEGREES_TO_FEET, heading);
                }
                break;

            case MoveState.TURN_TOWARDS_GOAL:
                //Case Three is 'turn towards'.
                power = .25;
                boolean turnRight;

                if(heading<=180){
                    turnRight = heading <= map.angleToGoal() && heading + 180 >= map.angleToGoal();
                }else{
                    turnRight = !(heading >= map.angleToGoal() && heading - 180 <= map.angleToGoal());
                }

                if(turnRight){
                    motorUp.setPower(power);
                    motorDown.setPower(power);
                    motorLeft.setPower(power);
                    motorRight.setPower(power);
                }else{
                    motorUp.setPower(-power);
                    motorDown.setPower(-power);
                    motorLeft.setPower(-power);
                    motorRight.setPower(-power);
                }
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
    }

    @Override
    public void loop(){
        gameState();
        moveState();
        telemetry();
    }

    public void linedUp(int o, int n) {
        if (Math.abs(heading - map.angleToGoal()) < TOL || (heading > 360 - TOL && map.angleToGoal() < TOL || (heading < TOL && map.angleToGoal() > 360 - TOL))) {
            moveState = o;
        } else {
            moveState = n;
        }
    }
    public void linedUpRev(int o, int n) {
        if (Math.abs(heading - map.angleToGoalRev()) < TOL || (heading > 360 - TOL && map.angleToGoalRev() < TOL || (heading < TOL && map.angleToGoalRev() > 360 - TOL))) {
            moveState = o;
        } else {
            moveState = n;
        }
    }

    public double getRuntime() {
        return super.getRuntime() - tDiff;
    }

}
