
package org.firstinspires.ftc.teamcode;

//import all needed classes
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;


@TeleOp

public class POV extends LinearOpMode {

    private DcMotor m1, m2,m3,m4; //define DcMotor type motors (4 in this case, can use 1)

    private double rightPower = 0; //initialize power for right side motors at 0 power
    private double leftPower = 0; //initialize power for left side motor at 0 power


    @Override
    public void runOpMode() {
        //display that the OpMode has been initialized on the DS phone
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //initialize the motors as hardware types, deviceName is what is configured.
        m1 = hardwareMap.get(DcMotor.class, "Motor1");
        m2 = hardwareMap.get(DcMotor.class, "Motor2");
        m3 = hardwareMap.get(DcMotor.class,"Motor3");
        m4 = hardwareMap.get(DcMotor.class,"Motor4");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //display that the OpMode is running in the DS phone
            telemetry.addData("Status", "Running");
            telemetry.update();

            //set rightPower based on y axis of left stick, and x axis of left stick
            rightPower = Range.clip(gamepad1.left_stick_y - gamepad1.left_stick_x,-0.5,0.5);
            //set leftPower based on y axis of left stick, and x axis of left stick
            leftPower = Range.clip(gamepad1.left_stick_y + gamepad1.left_stick_x,-0.5,0.5);

            //notice that if driver only moves the x axis of the stick, bot will rotate

            //sets the powers of all motors
            m1.setPower(-rightPower);
            m2.setPower(-rightPower);
            m3.setPower(leftPower);
            m4.setPower(leftPower);



        }
    }
}
