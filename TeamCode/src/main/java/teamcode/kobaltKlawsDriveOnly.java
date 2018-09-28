package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "kobaltKlawsDriveOnly", group = "Linear OpMode")

public class kobaltKlawsDriveOnly extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor LeftDriveMotor; //motor 0
    private DcMotor RightDriveMotor; //motor 1


    @Override
    public void runOpMode() {

        //run the initialize block
        this.initialize();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {


            //Drive System

            //double is a variable type that supports decimals
            double leftPower;
            double rightPower;
            double drive = gamepad1.left_stick_y;
            double turn = gamepad1.left_stick_x;


            if (drive != 0) {
                leftPower = (drive);
                rightPower = (drive);
                //if drive is not 0, set both motor powers to the value of drive
            } else if (turn != 0) {
                leftPower = (drive - turn);
                rightPower = (drive + turn);
                //if the drive if reports false, it runs this turn block
            } else {
                leftPower = 0;
                rightPower = 0;
                //if neither of these if statements report true it sets the motor powers to 0
                // if you don't push a stick, the robot don't move
            }
            this.LeftDriveMotor.setPower(leftPower);
            this.RightDriveMotor.setPower(rightPower);


            //Gives stats and updates
            telemetry.addData("Status", "Running");
            telemetry.addData("Left Power: ", leftPower);
            telemetry.addData("Right Power: ", rightPower);
            telemetry.addData("Left Wheel: ", LeftDriveMotor.getCurrentPosition());
            telemetry.addData("Right Wheel: ", RightDriveMotor.getCurrentPosition());
            telemetry.update();
        }
    }


    private void initialize() {

        //giving internal hardware an external name for the app config
        this.LeftDriveMotor = hardwareMap.get(DcMotor.class, "LeftDriveMotor");
        this.RightDriveMotor = hardwareMap.get(DcMotor.class, "RightDriveMotor");


        //Sets correct directions for motors and servos
        LeftDriveMotor.setDirection(DcMotor.Direction.REVERSE);
        RightDriveMotor.setDirection(DcMotor.Direction.FORWARD);


        //Tells the driver station the arm motor positions and that the robot is ready.
        telemetry.addData("Status", "Online");
        telemetry.update();
    }
}

