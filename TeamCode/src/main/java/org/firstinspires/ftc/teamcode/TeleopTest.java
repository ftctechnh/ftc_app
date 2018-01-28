package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "TeleTest")
@Disabled
public class TeleopTest extends LinearOpMode {

    private DcMotor leftDriveFront = null;
    private DcMotor rightDriveFront = null;
    public DcMotor leftDriveBack = null;
    public DcMotor rightDriveBack = null;
    public CRServo cvn1 = null;
    public CRServo cvn2 = null;
    public DcMotor elev1 = null;
    public DcMotor elev2 = null;

    public void runOpMode() throws InterruptedException {
        //telemetry.addData("Status", "Initialized");
        //telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDriveFront  = hardwareMap.get(DcMotor.class, "m1");
        rightDriveFront = hardwareMap.get(DcMotor.class, "m2");
        leftDriveBack  = hardwareMap.get(DcMotor.class, "m3");
        rightDriveBack = hardwareMap.get(DcMotor.class, "m4");
        cvn1 = hardwareMap.get(CRServo.class, "s1");
        cvn2 = hardwareMap.get(CRServo.class, "s2");
        elev1 = hardwareMap.get(DcMotor.class, "e1");
        elev2 = hardwareMap.get(DcMotor.class, "e2");


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDriveFront.setDirection(DcMotor.Direction.FORWARD);
        rightDriveFront.setDirection(DcMotor.Direction.FORWARD);
        leftDriveBack.setDirection(DcMotor.Direction.FORWARD);
        rightDriveBack.setDirection(DcMotor.Direction.FORWARD);
        cvn1.setDirection(CRServo.Direction.REVERSE);
        elev1.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive_left = -gamepad1.left_stick_y;
            double drive_right  =  gamepad1.right_stick_y;

            //double elevator_power = gamepad2.left_stick_y;
            leftPower    = Range.clip(drive_left, -0.65, 0.65) ;
            rightPower   = Range.clip(drive_right, -0.65, 0.65) ;
            //elevator_power  = Range.clip(elevator_power, -1.0, 1.0) ;
            int counter = 0;
            int counter1 = 0;

            if(gamepad2.dpad_up) {
                elev1.setPower(1);
                elev2.setPower(1);
            }
            else if(gamepad2.dpad_down) {
                elev2.setPower(-1);
                elev1.setPower(-1);
            }
            else if(gamepad2.a)
            {
                elev2.setPower(0.25);
                elev1.setPower(0.25);
            }
            else if (gamepad2.y)
            {
                elev2.setPower(-0.25);
                elev1.setPower(-0.25);
            }
            else
            {
                elev2.setPower(0);
                elev1.setPower(0);
            }

            if (gamepad1.b) {
                    cvn1.setPower(1);
                    cvn2.setPower(1);
                    //counter++;


            }
            else if (gamepad1.a)
            {
                cvn1.setPower(0);
                cvn2.setPower(0);
            }
            else if (gamepad1.x) {

                    cvn1.setPower(-1);
                    cvn2.setPower(-1);
                    //counter1++;


            }

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            // Send calculated power to wheels
            leftDriveFront.setPower(leftPower);
                leftDriveBack.setPower(leftPower);
                rightDriveFront.setPower(rightPower);
                rightDriveBack.setPower(rightPower);

            //elev1.setPower(elevator_power);
            //elev2.setPower(elevator_power);

            // Show the elapsed game time and wheel power.
            //telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            //telemetry.update();
        }
    }
}
