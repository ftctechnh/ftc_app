package org.firstinspires.ftc.team9374.team9374;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 *      ******Not Complete********
 * Assuming that the robot is in position
 * (IE: does not include the code to get the robot in place)
 *
 * The Color sensor is on the right, so we can
 * use logic to tell which side is which color.
 *
 * To be used with TB2
 *
 * Must be used with encoded Motors
 */
//@Disabled
@Autonomous(name = "Degrees test", group = "Null")
@Disabled
public class RedTeam_AutoBeacon extends OpMode {
    ColorSensor CSensor;    //Devices
    //Servo Slapper;
    DcMotor left;
    DcMotor right;

    final int tpr = 1120;   //Ticks per Rotation
    final int wheelDiameterInInches = 4;    // All of out wheels will be inches this year
                                            // Please note that this needs to be changed for any wheel size that we decide to use

    int ticks;  //To Be used for later. Just have to init it here
    public ElapsedTime runTime = new ElapsedTime(); //Handy dandy timer in case that we will be needing it

    public void init(){
        CSensor = hardwareMap.colorSensor.get("CSensor");   //Finding Devices
        //Slapper = hardwareMap.servo.get("Servo");
        left = hardwareMap.dcMotor.get("Motor-left");
        right = hardwareMap.dcMotor.get("Motor-right");

        left.setDirection(DcMotorSimple.Direction.REVERSE); //Due to the fact that one motor is opposite the other
                                                            //It is nessissary to reverse a motor

        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);    //Commend out if not true
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        runTime.reset();                                    //We dont need to keep track of all that. Reset the timer

    }

    public void loop(){
        telemetry.addData("Color Sensor RED:", CSensor.red());  //Mostly just for the drive team.
        telemetry.addData("Color Sensor BLUE", CSensor.blue());
        /*
        if (CSensor.red() >= 4) {                               //The max is 5 for any color.
            //Code to press button if red is on the right       //Theis logic can be inproved
            Slapper.setPosition(1.0);                           //Move the slapper
        } else {
            //This will run if red is greater than or equeal to 4
            Slapper.setPosition(0);                             //If the right side is not red, it must be blue.
                                                                //So we press the other side.
        }
        */
        Turn(90,0.5,false);
    }
    public void Turn(int degrees, double speed,boolean direction) {
        /*
        I am acutally really proud of myself for this method.
        This method moves the robot a certain amount of degrees.
        //True  = Counter-Clockwise
        //False = Clockwise
        */
        ticks = (degrees*13);   //In reality is is 13.44, but
                                //everything needs to be in integers.

                                //This took a lot of time to come up with one number
                                //Just saying.


        if (direction){         //Going counter-clockwise
            left.setTargetPosition(ticks);
            right.setTargetPosition(ticks);

            left.setPower(-speed);
            right.setPower(speed);
        } else { //Going clockwise
            left.setTargetPosition(ticks);
            right.setTargetPosition(ticks);

            left.setPower(speed);
            right.setPower(-speed);
        }
        while (true) {
            telemetry.addData("CurrentPos",left.getCurrentPosition());
            if ((left.getCurrentPosition() - ticks) < 5){
                break;
            }
        }


    }
    private int calcClicksForInches(double distanceInInches) {
        //Currently there are 1120 different positions on any given wheel
        double revlutions = distanceInInches / (wheelDiameterInInches * Math.PI); //Find out how many revolutations
        int clicks = (int) (revlutions * tpr); //This is a pretty big number, gonna be in the 1,000's
        return clicks; //The position to set the wheels to.
        }


    }

