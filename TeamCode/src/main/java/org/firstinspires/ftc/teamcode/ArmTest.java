/* Team 6217
    The Fellowship
    2016 - 2017 Velocity Vortex Robot Code */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp(name="Preciousss: ArmTest ", group="Preciousss")

public class ArmTest extends OpMode
{
/*FR = Front Right Wheel, FL = Front Left Wheel, BR = Back Righ Wheelt, BL = Back Left Wheel,
BG = Ball Grabber, WBT = Wiffle Ball Thrower, BGL = Ball Grabber Lifter, BP = Blast Plate, CS1 = color sensor 1*/
    DcMotor motorFR;
    DcMotor motorFL;
    DcMotor motorBR;
    DcMotor motorBL;
    Servo ServoGlyphGrabber1;
    Servo ServoGlyphGrabber2;
    DcMotor GlyphGrabberLifter;


    private ElapsedTime     runtime = new ElapsedTime();

    private boolean RunLauncher = false;
    private int DelayCounter = 0;
    private int DelayTimer = 400 ;

    public ArmTest() {}

    @Override
    public void init()
    {
      /*
       * Wheels: controller 1, motors 1,2,3,4
       */
        motorFL = hardwareMap.dcMotor.get("motorFL");
        motorFL.setDirection(DcMotor.Direction.FORWARD);
        motorFR = hardwareMap.dcMotor.get("motorFR");
        motorFR.setDirection(DcMotor.Direction.REVERSE);
        motorBL = hardwareMap.dcMotor.get("MotorBL");
        motorBL.setDirection(DcMotor.Direction.FORWARD);
        motorBR = hardwareMap.dcMotor.get("MotorBR");
        motorBR.setDirection(DcMotor.Direction.REVERSE);
        GlyphGrabberLifter = hardwareMap.dcMotor.get("GlyphGrabberLifter");
        ServoGlyphGrabber1 = hardwareMap.servo.get("ServoGlyphGrabber1");
        ServoGlyphGrabber2 = hardwareMap.servo.get("ServoGlyphGrabber2");
        ServoGlyphGrabber2.setDirection(Servo.Direction.REVERSE);




       /* telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();
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

        /* posx = Position X, posy = Position Y, LT = Left Trigger, RT = Right Trigger,
         a = Button a, y = Button y, x = Button x, b = Button b, UP = BGL Going Up, DOWN = BGL Going Down,
         AA = Arm Angle, Close = BG closing, Open = BG opening, BPD = Blast Protector Down, BPU = Blast Protector Up,
         GP = Grab Particles, PPIWBT = Put Partciles Into Wiffle Ball Thrower,Beacon = Push Beacons. */
        float FLBRPower = 0.f;
        float FRBLPower = 0.f;
        float posx = gamepad1.left_stick_x;
        float posy = gamepad1.left_stick_y;
        float LT = gamepad1.left_trigger;
        float RT = gamepad1.right_trigger;
        boolean a = gamepad1.a;
        boolean y = gamepad1.y;
        boolean x = gamepad1.x;
        boolean Close = gamepad1.dpad_right;
        boolean Open = gamepad1.dpad_left;
        boolean UP = gamepad1.dpad_up;
        boolean DOWN = gamepad1.dpad_down;

         /* ~~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            Limit x and y values and adjust to power curve
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

        posx = Range.clip(posx, -1, 1);
        posy = Range.clip(posy, -1, 1);

        posx = (float) powerCurve(posx);
        posy = (float) powerCurve(posy);

        LT = (float) powerCurve(LT);
        RT = (float) powerCurve(RT);


        /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            Write power to wheels
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */





        // Glyph Grabber 0 out
        if (a) {
            ServoGlyphGrabber1.setPosition(0);
            ServoGlyphGrabber2.setPosition(0);
        }


        else {
         
            
        }
        // opens the Glyph grabber
        if ( y ) {
            ServoGlyphGrabber1.setPosition(100);
            ServoGlyphGrabber2.setPosition(100);
            } else {
                
        }


        if (x) {
            ServoGlyphGrabber1.setPosition(80);
            ServoGlyphGrabber2.setPosition(80);


        }

        else {


        }


        if ( UP )  {
            GlyphGrabberLifter.setPower(.5);
        }

        else {
            GlyphGrabberLifter.setPower(0);
        }


        if ( DOWN )  {
            GlyphGrabberLifter.setPower(-0.5);
        }

        else {
            GlyphGrabberLifter.setPower(0);
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
       double max = .75;

       if (LB == true && RB == false){
           max = 1;
       }

       if (LB == false && RB == true){
           max = .3;
       }

       int direction = 1;
       if (dVal < 0) {
           direction = -1;
       }

       double dScale = 0.0;
       if (Math.abs(dVal) < dead) {
           dScale = 0.0;
       } else {
           dScale = ((dVal * dVal) * max) * direction;
       }

       return (dScale);
   }




}
