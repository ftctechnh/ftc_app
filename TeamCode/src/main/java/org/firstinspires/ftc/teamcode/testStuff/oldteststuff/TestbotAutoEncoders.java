package org.firstinspires.ftc.teamcode.testStuff.oldteststuff;



import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Testbot Auto with Encoders", group = "Testbot" )
//@Disabled
public class TestbotAutoEncoders extends LinearOpMode {
    TestbotHardware robot = new TestbotHardware(DcMotor.RunMode.RUN_USING_ENCODER);
    private ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 288; //
    static final double DRIVE_GEAR_REDUCTION = 1.0;
    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    static  final double DRIVE_SPEED = 0.6;
    static  final double TURN_SPEED = 0.5;

    static final double BLUE_THRESHOLD = .5;

    @Override
    public void runOpMode(){

        robot.init(hardwareMap);

        telemetry.addData("status", "Resetting Encoders");
        telemetry.update();

        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("path0", "Starting at %7d : %7d",
                robot.leftDrive.getCurrentPosition(),
                robot.rightDrive.getCurrentPosition());
        telemetry.update();

        waitForStart();

        //AUTONOMOUS COMMANDS
        encoderDrive(DRIVE_SPEED, 48, 48,5.0);
        encoderDrive(TURN_SPEED, 12, -12, 4.0);
        encoderDrive(DRIVE_SPEED, -24, -24, 4.0);


        telemetry.addData("Alpha", robot.colorSensor.alpha());
        telemetry.addData("Red  ", robot.colorSensor.red());
        telemetry.addData("Green", robot.colorSensor.green());
        telemetry.addData("Blue ", robot.colorSensor.blue());

        robot.leftDrive.setPower(.5);
        robot.rightDrive.setPower(.5);

        while (opModeIsActive() && (robot.colorSensor.blue() < BLUE_THRESHOLD) || runtime.seconds() < 5  )
        {
            telemetry.addData("Blue ", robot.colorSensor.blue());
        }

        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);

        telemetry.addData("path", "Complete");
        telemetry.update();
    }

    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS){
        int newLeftTarget;
        int newRightTarget;

        if (opModeIsActive()){
            newLeftTarget= robot.leftDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget= robot.rightDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.leftDrive.setTargetPosition(newLeftTarget);
            robot.rightDrive.setTargetPosition(newRightTarget);

            robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            robot.leftDrive.setPower(Math.abs(speed));
            robot.rightDrive.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftDrive.isBusy() && robot.rightDrive.isBusy()))
            {}

            robot.leftDrive.setPower(0);
            robot.rightDrive.setPower(0);

            robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}
