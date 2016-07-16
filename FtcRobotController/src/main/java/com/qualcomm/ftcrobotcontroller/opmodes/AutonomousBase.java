package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.Map;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
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

    DcMotor motorRT;
    DcMotor motorRB;
    DcMotor motorLT;
    DcMotor motorLB;
    DcMotor motorA;
    DcMotor motorC;
    DcMotor motorE;
    DcMotor motorR;
    GyroSensor gyro;
    Servo climber;
    Servo claw;
    Servo swingLeft;
    Servo blockRight;
    Servo blockLeft;
    Servo swingRight;
    TouchSensor touch;


    //We stateful now, boys.
    int gameState;
    int moveState;
    boolean inited = false;

    double power;
    double heading;
    int cDist, lDist;
    int dDist; //the aforementioned difference (cDist-lDist) **CAN BE NEGATIVE
    double tDiff; // getRuntime() does this really annoying thing where it counts init time, so I
    // mark the first time I exit init, and override getRuntime() to return that instead
    double climbTime;

    int startPos = 6;
    Map map = new Map(startPos); //this map object will allow for easy manipulations.


    public void init() {
        motorRT = hardwareMap.dcMotor.get("motor_RT");
        motorRB = hardwareMap.dcMotor.get("motor_RB");
        motorLT = hardwareMap.dcMotor.get("motor_LT");
        motorLB = hardwareMap.dcMotor.get("motor_LB");

        motorLT.setDirection(DcMotor.Direction.FORWARD);
        motorLB.setDirection(DcMotor.Direction.FORWARD);
        motorRT.setDirection(DcMotor.Direction.REVERSE);
        motorRB.setDirection(DcMotor.Direction.REVERSE);

        motorA = hardwareMap.dcMotor.get("motor_A");
        motorC = hardwareMap.dcMotor.get("motor_C");
        motorE = hardwareMap.dcMotor.get("motor_E");
        motorR = hardwareMap.dcMotor.get("motor_R");

        climber = hardwareMap.servo.get("climber");
        claw = hardwareMap.servo.get("claw");
        swingLeft = hardwareMap.servo.get("swing_l");
        swingRight = hardwareMap.servo.get("swing_r");
        blockRight = hardwareMap.servo.get("block_r");
        blockLeft = hardwareMap.servo.get("block_l");

        touch = hardwareMap.touchSensor.get("touch");
        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();
    }

    public void moveState(){
        switch(moveState){
            case 0:
                //Case zero is 'stop'
                motorRT.setPower(0);
                motorRB.setPower(0);
                motorLT.setPower(0);
                motorLB.setPower(0);
                break;
            //Never should we be just 'moving', always move TOWARDS something.
            case 1:
                //Case one is 'move towards' in the most literal sense. It assumes the path is
                //clear, and that there is a goal(9), and us(1) on the map somewhere.
                power = -.5; //power coefficient
                if(map.distanceToGoal()>1/12) {
                    motorRT.setPower(power);
                    motorRB.setPower(power);
                    motorLT.setPower(power);
                    motorLB.setPower(power);
                    map.moveRobot(dDist * DEGREES_TO_FEET, heading);
                }
                break;
            case 2:
                //Case Two is 'turn towards'.
                power = -0.25;
                boolean turnRight;

                if(heading<=180){
                    turnRight = heading <= map.angleToGoal() && heading + 180 >= map.angleToGoal();
                }else{
                    turnRight = !(heading >= map.angleToGoal() && heading - 180 <= map.angleToGoal());
                }

                if(!turnRight){
                    motorRT.setPower(-power);
                    motorRB.setPower(-power);
                    motorLT.setPower(power);
                    motorLB.setPower(power);
                }else{
                    motorRT.setPower(power);
                    motorRB.setPower(power);
                    motorLT.setPower(-power);
                    motorLB.setPower(-power);
                }
                break;
            case 3:
                climber.setPosition(0); //Move climber back to up position
                power = .5; //power coefficient
                if(map.distanceToGoal()>1/12) {
                    motorRT.setPower(power);
                    motorRB.setPower(power);
                    motorLT.setPower(power);
                    motorLB.setPower(power);
                    map.moveRobot(dDist * DEGREES_TO_FEET, heading);
                }
                break;
            case 5:
                if(climber.getPosition() < .5) climbTime = getRuntime();
                climber.setPosition(1);
                break;
            case 6:
                power = -.2; //power coefficient
                if(map.distanceToGoal()>1/12) {
                    motorRT.setPower(power);
                    motorRB.setPower(power);
                    motorLT.setPower(power);
                    motorLB.setPower(power);
                    map.moveRobot(dDist * DEGREES_TO_FEET, heading);
                }
                break;
        }
    }

    public void gameState(){
        heading = gyro.getHeading();
        lDist = cDist;
        cDist = ( motorLB.getCurrentPosition()
                + motorRB.getCurrentPosition()
                + motorLT.getCurrentPosition()
                + motorRT.getCurrentPosition()
        ) / 4;
        dDist = cDist - lDist;
        if(!inited){
            climber.setPosition(0);
            claw.setPosition(.5);
            swingLeft.setPosition(.4);
            swingRight.setPosition(.4);
            blockRight.setPosition(1) ;
            blockLeft.setPosition(0);
            inited = true;
        }
        //if(getRuntime() > 29) gameState = 777;  //robot death switch
    }

    public void telemetry(){
        telemetry.addData("Runtime ",getRuntime());
        telemetry.addData("States ",gameState + " " + moveState);
        telemetry.addData("heading ",heading);
        telemetry.addData("goal x,y ",map.getGoalX()+","+map.getGoalY());
        telemetry.addData("robot x,y ",map.getRobotX()+","+map.getRobotY());
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
