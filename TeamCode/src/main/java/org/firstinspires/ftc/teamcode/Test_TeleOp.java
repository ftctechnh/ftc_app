package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by johnb on 9/26/2017.
 */


@TeleOp(name="Test: Tank Drive", group="Linear Opmode")

//Comment out @Disabled to add this OpMode to the Driver Station List!
@Disabled

public class Test_TeleOp extends LinearOpMode{

    //Declare OpMode members/variables (Motors, servos, etc.)

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();


        //Initialize the hardware variables. Strings used a parameters to "get"
        // must correspond to the names assigned during robot config on the FTC Robot Controller
        //App
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");


        //Reverse the motor that runs backwards when given positive power.
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        //Wait for game to start/ Wait for driver to press PLAY
        waitForStart();
        runtime.reset();

        //Runs until the end of the match (Driver presses stop)
        while (opModeIsActive()) {

            //Setup speed variables for each drive Motor/wheel to save power level
            double leftPower;
            double rightPower;

            //Choose to drive using either Tank Mod, or POV Mode.

            //POV Mode uses left tick to go forward, and right stick to turn
            //Uses basic math to combine motions and is easier to drive stright
            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
            leftPower = Range.clip(drive + turn, -1.0, 1.0);
            rightPower = Range.clip(drive - turn, -1.0, 1.0);


            //Tank Mode  - Simpler to program, harder to drive straight and slow.
            //  leftPower = -gamepad1.left_stick_y;
            //  rightPower = -gamepad1.right_stick_y;


            //Send calculated power (set in Power variables) to the actual motors
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);



            //Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (0.2f), right (0.2f)", leftPower, rightPower);
            telemetry.addLine("Robotics is awesome! John was here :P");
            telemetry.update();






        }
    }

}
