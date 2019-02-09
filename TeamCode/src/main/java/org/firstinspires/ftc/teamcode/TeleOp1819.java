/* Team 6217
    The Fellowship
    2016 - 2017 Velocity Vortex Robot Code */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import static java.lang.Math.abs;

@TeleOp(name="Preciousss: TeleOp1819", group="Preciousss")

public class TeleOp1819 extends OpMode
{
/*FR = Front Right Wheel, FL = Front Left Wheel, BR = Back Righ Wheelt, BL = Back Left Wheel, Con1= Conveyor 1, Con2= Conveyor 2*/
    DcMotor motorFR;
    DcMotor motorFL;
    DcMotor motorBR;
    DcMotor motorBL;
    DcMotor slideMotor;
    DcMotor mineralLiftR;
    DcMotor robotLift;
    DcMotor sweeperMotor;
    Servo binLeveler;
    CRServo binLifter;
    Servo servo_U;
    Servo servo_V;
    /*Servo slide;
    CRServo outake1;
    CRServo outake2;*/


   /* IntegratingGyroscope gyro;
    ModernRoboticsI2cGyro modernRoboticsI2cGyro;*/

    //static final double     COUNTS_PER_MOTOR_REV    = 420 ; // PPR for NeveRest 400
    //static final double     limitRocker = COUNTS_PER_MOTOR_REV / 4 ; // 90 degrees
    private ElapsedTime     runtime = new ElapsedTime();

    private boolean RunLauncher = false;
    private int DelayCounter = 0;
    private int DelayTimer = 400 ;
    public TeleOp1819() {}

    @Override
    public void init()
    {
      /*
       * Wheels: controller 1, motors 0,1,2,3
       */
        motorFL = hardwareMap.dcMotor.get("motorFL");
        motorFL.setDirection(DcMotor.Direction.REVERSE);
        motorFR = hardwareMap.dcMotor.get("motorFR");
        motorFR.setDirection(DcMotor.Direction.REVERSE);
        motorBL = hardwareMap.dcMotor.get("motorBL");
        motorBL.setDirection(DcMotor.Direction.FORWARD);
        motorBR = hardwareMap.dcMotor.get("motorBR");
        motorBR.setDirection(DcMotor.Direction.FORWARD);
        sweeperMotor = hardwareMap.dcMotor.get("sweeperMotor");
        sweeperMotor.setDirection(DcMotor.Direction.FORWARD);
        slideMotor= hardwareMap.dcMotor.get("slideMotor");
        slideMotor.setDirection(DcMotor.Direction.FORWARD);
        mineralLiftR = hardwareMap.dcMotor.get("mineralLiftR");
        mineralLiftR.setDirection(DcMotor.Direction.REVERSE);
        robotLift = hardwareMap.dcMotor.get("robotLift");
        robotLift.setDirection(DcMotor.Direction.FORWARD);
        robotLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        binLeveler = hardwareMap.servo.get("binLeveler");
        binLifter = hardwareMap.crservo.get("binLifter");
        servo_U = hardwareMap.servo.get("servo_U");
        servo_V = hardwareMap.servo.get("servo_V");
        /*servo = hardwareMap.servo.get("servo");
        slide = hardwareMap.servo.get("slide"); //
        outake1 = hardwareMap.crservo.get("outake1");
        outake2 = hardwareMap.crservo.get("outake2");*/

    }

    /*
    * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    */
    @Override
    public void init_loop() {
        // Start wrist in stowed position
        //servoWrist.setPosition(.85);
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */


    @Override
    public void loop() {

        /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            Read values from Game Controller
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

        // Stick values range from -1 to 1, where -1 is full up, and 1 is full down
        // Trigger values range from 0 to 1

        /* posx = Position X on Left Joystick, posy = Position Y on Left Joystick, posxR = Position X on Right Joystick,
        posyR = Position Y on Right Joystick, LT = Left Trigger, RT = Right Trigger, a = Button a, y = Button y, x = Button x,
         b = Button b, UP = on Dpad Loads Glyph, DOWN = on Dpad Shoots Glyph, rightPad = Right on Dpad, leftPad = Left on Dpad */
        float FLBRPower = 0.f;
        float FRBLPower = 0.f;
        float posx = gamepad1.left_stick_x;
        float posy = gamepad1.left_stick_y;
        float intake = gamepad1.right_stick_y;
        float posyFlip = 0;
        float posxFlip = gamepad1.right_stick_x;
        float LT = gamepad1.left_trigger;
        float RT = gamepad1.right_trigger;
        boolean a = gamepad1.a;
        boolean b = gamepad1.b;
        boolean y = gamepad1.y;
        boolean x = gamepad1.x;
        boolean rightPad = gamepad1.dpad_right;
        boolean leftPad = gamepad1.dpad_left;
        boolean dpad_up = gamepad1.dpad_up;
        boolean dpad_down = gamepad1.dpad_down;
         /* ~~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            Limit x and y values and adjust to power curve
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

        posx = Range.clip(posx, -1, 1);
        posy = Range.clip(posy, -1, 1);
        posxFlip = Range.clip(posxFlip, -1, 1);
        posyFlip = Range.clip(posyFlip, -1, 1);

        posx = (float) powerCurve(posx);
        posy = (float) powerCurve(posy);
        posxFlip = (float) pCurve(posxFlip);
        posyFlip = (float) pCurve(posyFlip);

        LT = (float) powerCurve(LT);
        RT = (float) powerCurve(RT);

        if(x&&b)
            binLifter.setPower(1);
        else if(y&&a)
            binLifter.setPower(-1);
        else
            binLifter.setPower(0);
        if(b && (!x)) {
            servo_U.setPosition(1);
            servo_V.setPosition(1);
        }
        if(x && (!b)){
            servo_U.setPosition(0);
            servo_V.setPosition(0);
        }
        /*if()
            servo_U.setPosition(1);
        if()
            servo_V.setPosition(1);*/
        //Robot Lifting
        if(y && !a) {
            robotLift.setPower(1);//up
        }
        else if (a &&!y)
            robotLift.setPower(-1);//down
        else
            robotLift.setPower(0);

        if(dpad_up)
           mineralLiftR.setPower(1);
        else if(dpad_down)
            mineralLiftR.setPower(-1);
        else
            mineralLiftR.setPower(0);

        if(leftPad)
            slideMotor.setPower(1);
        else if(rightPad)
            slideMotor.setPower(-1);
        else
            slideMotor.setPower(0);


        // pivot left
        if (LT != 0)  {

            motorFL.setPower(1);
            motorBL.setPower(1);
            motorFR.setPower(-1);
            motorBR.setPower(-1);
        }


        //  pivot right
        else if (RT != 0)  {

            motorFL.setPower(-1);
            motorBL.setPower(-1);
            motorFR.setPower(1);
            motorBR.setPower(1);
        }


        //  Intake
        if ( ( intake != 0) ) {
            //sweeperMotor.setPower(powerCurve(intake));
        }


            //  Driving
        if ( ( posy != 0) || ( posx != 0 ) ) {

            FRBLPower = -posy - posx;
            FLBRPower = -posy + posx;
            motorFR.setPower( FRBLPower );
            motorFL.setPower( FLBRPower );
            motorBR.setPower( FLBRPower );
            motorBL.setPower( FRBLPower );

        }

        else {

            motorFR.setPower(0);
            motorFL.setPower(0);
            motorBR.setPower(0);
            motorBL.setPower(0);
        }


        /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            Write telemetry back to driver station
            NOTE: Output is sorted by the first field, hence the numbering
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */


        telemetry.addData("1", "Power, FRBL/FLBR: " +
                String.format("%.2f", FRBLPower) + " " +
                String.format("%.2f", FLBRPower));

        telemetry.addData("2", "Raw Joystick X, Y: " +
                String.format("%.2f", gamepad1.left_stick_x) + " " +
                String.format("%.2f", gamepad1.left_stick_y));

        telemetry.addData("3", "D-Pad U, R, D, L: " +
                String.format("%b", gamepad1.dpad_up) + " " +
                String.format("%b", gamepad1.dpad_right) + " " +
                String.format("%b", gamepad1.dpad_down) + " " +
                String.format("%b", gamepad1.dpad_left));

        telemetry.addData("0", "Trigger: " +
                String.format("%.2f", gamepad1.left_trigger));
        telemetry.addData("0", "Trigger: " +
                String.format("%.2f", gamepad1.right_trigger));
        telemetry.addData("4", "Right Bumper Left Bumper" +
                String.format("%b", gamepad1.right_bumper)+ " " +
                String.format("%b", gamepad1.left_bumper));

        telemetry.addData("5", "Actuator A, B,: " +
                String.format("%b", gamepad1.a) + " " +
                String.format("%b", gamepad1.b));
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {
    }

   double powerCurve ( float dVal ) {
    /*
     * This method adjusts the joystick input to apply a power curve that
     * is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    // LB = left bumper, RB = right bumper.
       boolean LB = gamepad1.left_bumper;
       boolean RB = gamepad1.right_bumper;


       double dead = .2;

       // Regular
       double max = 1;
       /*if (LB == true && RB == false){
           // Turbo with left bumper
           max = 1;
       } else */if (LB == false && RB == true){
           // Turtle with right bumper
           max = .6;
       }

       int direction = 1;
       if (dVal < 0) {
           direction = -1;
       }

       double dScale = 0.0;
       if (abs(dVal) < dead) {
           dScale = 0.0;
       } else {
           dScale = ((dVal * dVal) * max) * direction;
       }

       return (dScale);
   }

   //NOT USED
    double pCurve ( float dVal ) {
    /*
     * This method adjusts the joystick input to apply a power curve that
     * is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    // LB = left bumper, RB = right bumper.



        double dead = .2;

        // Regular
        double max = 1;

        int direction = 1;
        if (dVal < 0) {
            direction = -1;
        }

        double dScale = 0.0;
        if (abs(dVal) < dead) {
            dScale = 0.0;
        } else {
            dScale = ((dVal * dVal) * max) * direction;
        }

        return (dScale);
    }
}
