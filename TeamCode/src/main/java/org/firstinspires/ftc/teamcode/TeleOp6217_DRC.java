/* Team 6217
    The Fellowship
    2016 - 2017 Velocity Vortex Robot Code */

package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.PWMOutputController;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.lang.Math;

import static java.lang.Math.abs;

@TeleOp(name="Preciousss: TeleOp6217_DRC 1654", group="Preciousss")

public class TeleOp6217_DRC extends OpMode
{
/*FR = Front Right Wheel, FL = Front Left Wheel, BR = Back Righ Wheelt, BL = Back Left Wheel, Con1= Conveyor 1, Con2= Conveyor 2*/
    DcMotor motorFR;
    DcMotor motorFL;
    DcMotor motorBR;
    DcMotor motorBL;
    DcMotor motorConL;
    DcMotor motorConR;
    CRServo servoConL;
    CRServo servoConR;
    DcMotor motorRocker1;
    DcMotor motorRocker2;
    Servo Jack;

   /* IntegratingGyroscope gyro;
    ModernRoboticsI2cGyro modernRoboticsI2cGyro;*/

    //static final double     COUNTS_PER_MOTOR_REV    = 420 ; // PPR for NeveRest 400
    //static final double     limitRocker = COUNTS_PER_MOTOR_REV / 4 ; // 90 degrees
    private ElapsedTime     runtime = new ElapsedTime();

    private boolean RunLauncher = false;
    private int DelayCounter = 0;
    private int DelayTimer = 400 ;
    public TeleOp6217_DRC() {}

    @Override
    public void init()
    {
      /*
       * Wheels: controller 1, motors 0,1,2,3
       */
        motorFL = hardwareMap.dcMotor.get("motorFL");
        motorFL.setDirection(DcMotor.Direction.FORWARD);
        motorFR = hardwareMap.dcMotor.get("motorFR");
        motorFR.setDirection(DcMotor.Direction.REVERSE);
        motorBL = hardwareMap.dcMotor.get("motorBL");
        motorBL.setDirection(DcMotor.Direction.FORWARD);
        motorBR = hardwareMap.dcMotor.get("motorBR");
        motorBR.setDirection(DcMotor.Direction.REVERSE);

        // Conveyor

        motorConL = hardwareMap.dcMotor.get("motorConL");
        motorConL.setDirection(DcMotor.Direction.FORWARD);
        motorConR = hardwareMap.dcMotor.get("motorConR");
        motorConR.setDirection(DcMotor.Direction.FORWARD);
        servoConL = hardwareMap.crservo.get("servoConL");
        servoConL.setDirection(CRServo.Direction.FORWARD);
        servoConR = hardwareMap.crservo.get("servoConR");
        servoConR.setDirection(CRServo.Direction.REVERSE);

        // Rocker Motors

        motorRocker1 = hardwareMap.dcMotor.get("motorRocker1");
        motorRocker1.setDirection(DcMotor.Direction.FORWARD);
        motorRocker2 = hardwareMap.dcMotor.get("motorRocker2");
        motorRocker2.setDirection(DcMotor.Direction.REVERSE);
        Jack = hardwareMap.servo.get ("Jack");


       /* modernRoboticsI2cGyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
        gyro = (IntegratingGyroscope)modernRoboticsI2cGyro;
       telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update()
        telemetry.addData("WBG",  "Starting at %7d",
               // motorWBT1.getCurrentPosition());
        telemetry.update());*/
    }

    /*
    * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

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
        float posyR = gamepad1.right_stick_y;
        float posxR = gamepad1.right_stick_x;
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
        posxR = Range.clip(posxR, -1,1);
        posyR = Range.clip(posyR, -1,1);

        posx = (float) powerCurve(posx);
        posy = (float) powerCurve(posy);
        posxR = (float) pCurve(posxR);
        posyR = (float) pCurve(posyR);


        LT = (float) powerCurve(LT);
        RT = (float) powerCurve(RT);

        //  Loading Glyphs
        // Directions will require testing
        if (dpad_up) {
            motorConL.setPower(-.7);
            motorConR.setPower(.7);

        }
        else if (dpad_down)  {
            motorConL.setPower(.7);
            motorConR.setPower(-.7);

        }
        else {
            motorConL.setPower(0);
            motorConR.setPower(0);

        }
        if(rightPad)    {
            servoConL.setPower(-1);
            servoConR.setPower(-1);
        }

        else if (leftPad)   {
            servoConL.setPower(1);
            servoConR.setPower(1);
        }

        else    {
            servoConL.setPower(0);
            servoConR.setPower(0);
        }

        // Rocker
        if (posyR <= -.2) {
            motorRocker1.setPower(posyR);
            motorRocker2.setPower(posyR);
        }
        else if (posyR >= .2) {
            motorRocker1.setPower(posyR);
            motorRocker2.setPower(posyR);
        }
        else if (x)  {
            motorRocker1.setPower(.15);
            motorRocker2.setPower(-.15);
        }

        else    {
            motorRocker1.setPower(0);
            motorRocker2.setPower(0);
        }
        // Jack
        if(a) {
            Jack.setPosition(1.0);
        }
        else if(b) {
            Jack.setPosition(0.2);
        }

        else if (y) {
            Jack.setPosition(0.6);
        }



//  pivot left

        if (LT != 0)  {

            motorFL.setPower(LT);
            motorBL.setPower(LT);
            motorFR.setPower(-LT);
            motorBR.setPower(-LT);
        }

        //

        //  pivot right

        else if (RT != 0)  {

            motorFL.setPower(-RT);
            motorBL.setPower(-RT);
            motorFR.setPower(RT);
            motorBR.setPower(RT);
        }
        //  Driving

        else if ( ( posy != 0) || ( posx != 0 ) ) {

            FRBLPower = posy - posx;
            FLBRPower = posy + posx;
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
       double max = .5;
       if (LB == true && RB == false){
           // Turbo with left bumper
           max = 1;
       } else if (LB == false && RB == true){
           // Turtle with right bumper
           max = .3;
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
