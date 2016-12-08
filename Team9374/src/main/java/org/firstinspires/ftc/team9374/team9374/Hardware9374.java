package org.firstinspires.ftc.team9374.team9374;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by darwin on 12/3/16.
 *
 * 9374 Robot class.
 *
 * Here is a reference for how to une this in place of other things.
 *
 *      Hardware9374 robot = new Hardware9374();     // Use a 9K' shardware
 *
 *
 *      robot.init(hardwareMap);
 *
 */

public class Hardware9374 {
    DcMotor left_f;
    DcMotor right_f;
    DcMotor left_b;
    DcMotor right_b;
    //Shooter
    DcMotor shooter_l;
    DcMotor shooter_r;
    //Speeds
    boolean Sspeed;
    boolean Nspeed = true;
    boolean Fspeed;

    CRServo elevator;

    ColorSensor CSensor;

    //Controller vaibles
    double lStickY;
    double lStickX;
    double rStickY;
    //Power varibles
    double LFpower;
    double RFpower;
    double LBpower;
    double RBpower;

    final double wheelDiameterInInches = 2.5;
    final int tpr = 1120;
    final double wheelCorrection = 0;
    //Not yet defined, will be.

    int ticks;

    public ElapsedTime runTime = new ElapsedTime();

    /* constructor, used when ... = new Hardware9374()  */
    public Hardware9374() {
    }
    //Our init, cannot be called inside the begginning because it is finding our devices.
    public void init(HardwareMap hardwareMap) {

        //Driving motors
        left_f = hardwareMap.dcMotor.get("Eng1-left");
        right_f = hardwareMap.dcMotor.get("Eng1-right");
        left_b = hardwareMap.dcMotor.get("Eng2-left");
        right_b = hardwareMap.dcMotor.get("Eng2-right");
        //Shooter motors
        shooter_r = hardwareMap.dcMotor.get("Eng3-left");
        shooter_l = hardwareMap.dcMotor.get("Eng3-right");

        elevator = hardwareMap.crservo.get("Ser1-center");

        CSensor = hardwareMap.colorSensor.get("Col1-right");

        //This might not be true for all motors on the right side
        right_b.setDirection(DcMotorSimple.Direction.REVERSE);
        right_f.setDirection(DcMotorSimple.Direction.REVERSE);

        //shooter_r.setDirection(DcMotorSimple.Direction.REVERSE);

        //shooter_l.setDirection(DcMotorSimple.Direction.REVERSE);

        runTime.reset();

    }
    //-------------------------------------------------------
    //                      Action Functions
    //-------------------------------------------------------



    public void Turn(int degrees, double speed,boolean direction) {
        /*
        I am acutally really proud of myself for this method.
        This method moves the robot a certain amount of degrees.
        //-------------------------
        //True  = Counter-Clockwise
        //False = Clockwise
        //-------------------------
        */
        ticks = (degrees*13);   //In reality is is 13.44, but
        //everything needs to be in integers.

        //This took a lot of time to come up with one number
        //Just saying.


        if (direction){         //Going counter-clockwise
            setALLposition(ticks);

            left_b.setPower(-speed);
            left_f.setPower(-speed);
            right_b.setPower(speed);
            right_f.setPower(speed);

        } else { //Going clockwise
            setALLposition(ticks);

            left_f.setPower(speed);
            left_b.setPower(speed);
            right_f.setPower(-speed);
            right_b.setPower(-speed);
        }
        while (true) {
            //telemetry.addData("CurrentPos",left_f.getCurrentPosition());
            //Might need to find a way to get that done.
            if ((left_f.getCurrentPosition() - ticks) < 5){
                setALLpower(0);
                resetEncoders();
                break;
            }
        }


    }
    public int calcClicksForInches(double distanceInInches) {
        //Currently there are 1120 different positions on any given wheel
        double revlutions = distanceInInches / (wheelDiameterInInches * Math.PI); //Find out how many revolutations
        int clicks = (int) (revlutions * tpr); //This is a pretty big number, gonna be in the 1,000's
        return clicks; //The position to set the wheels to.
    }
    public void moveToPosition(int distanceInIN,double power){
        setALLposition(calcClicksForInches(distanceInIN));
        setALLpower(power);
        while (true){
            if (left_f.getCurrentPosition() > calcClicksForInches(distanceInIN)){
                resetEncoders();
                setALLpower(0);

                break;
            }
        }
    }
    public void translate(boolean direction, double power, int inches){
        //Currently not finished, need to confirm with camden.
        //-------------------------
        //True  = Left
        //False = Right
        //-------------------------
        /*
        Diagram!!!
          |  _ ________ _   ^
          | | |        | |  |
          v |_|        |_|  |
              |        |
              |        |
          ^  _|        |_
          | | |        | |  |
          | |_|________|_|  |
                            v

         So, to go left the two wheels on the left turn in
         and the two wheels on the right turn outward
         as shown.

         For right it is almost the exact same, except the right wheels turn in and the left wheels turn out

          ^  _ ________ _
          | | |        | | |
          | |_|        |_| |
              |        |   v
              |        |
             _|        |_  ^
          | | |        | | |
          | |_|________|_| |
          v

         */
        //Acutall code will continue

        setALLposition(calcClicksForInches(inches));
        if (direction) {
            // If ( left ),
            left_f.setPower(-power);
            left_b.setPower(power);
            right_f.setPower(power);
            right_b.setPower(-power);
        } else if (!direction) {
            left_f.setPower(power);
            left_b.setPower(-power);
            right_f.setPower(-power);
            right_b.setPower(power);
        }
        while (true) {
            if (left_f.getCurrentPosition() > calcClicksForInches(inches)){
                resetEncoders();
                break;
            }
        }


    }
    public void setALLpower(double power){
        left_b.setPower(power);
        left_f.setPower(power);
        right_b.setPower(power);
        right_f.setPower(power);
    }
    public void setALLposition(int position) {
        left_b.setTargetPosition(position);
        left_f.setTargetPosition(position);
        right_b.setTargetPosition(position);
        right_f.setTargetPosition(position);

    }
    public void resetEncoders(){
        left_b.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_f.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_b.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_f.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left_b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

}
