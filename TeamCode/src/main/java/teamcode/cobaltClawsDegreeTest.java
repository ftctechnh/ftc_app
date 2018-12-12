package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.List;

import teamcode.examples.Mineral;
import teamcode.examples.TensorFlowManager;
import teamcode.kkl2.KKL2HardwareManager;

@Autonomous(name = "cobaltClawsDegreeTest", group = "Linear OpMode")

public class cobaltClawsDegreeTest extends LinearOpMode {



    double driveSpeed = 0.25;

    private static final double INCH_CONVERSION_RATIO = 55.0 / 0.39370079;
    private static final double RADIAN_CONVERSION_RATIO = 1066.15135303;
    private static final double DEGREES_TO_RADIANS = Math.PI / 180;

    private TensorFlowManager tfManager;

    public enum Direction {Forward, Backward, Left, Right}

    @Override
    public void runOpMode() {


        //run the initialize block
        KKL2HardwareManager.initialize(this);
        this.initialize();

        waitForStart();

        while (opModeIsActive()) {

            turn(Direction.Left, 90, driveSpeed);
            requestOpModeStop();

        }


    }

    private void initialize() {

        KKL2HardwareManager.driveLMotor.setDirection(DcMotor.Direction.FORWARD);
        KKL2HardwareManager.driveRMotor.setDirection(DcMotor.Direction.REVERSE);

        KKL2HardwareManager.liftBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL2HardwareManager.liftBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.tfManager = new TensorFlowManager(this.hardwareMap, this.telemetry);
        this.tfManager.initialize();
    }


    public void move(Direction direction, int inches, double speed) {

        //Changes inches to work with ticks
        inches *= INCH_CONVERSION_RATIO;

        //Resets encoder and moves the inputted ticks
        KKL2HardwareManager.driveRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL2HardwareManager.driveLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        KKL2HardwareManager.driveRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        KKL2HardwareManager.driveLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        if (direction == Direction.Forward) {

            KKL2HardwareManager.driveLMotor.setTargetPosition(inches);
            KKL2HardwareManager.driveRMotor.setTargetPosition(inches);

        } else if (direction == Direction.Backward) {

            KKL2HardwareManager.driveLMotor.setTargetPosition(-inches);
            KKL2HardwareManager.driveRMotor.setTargetPosition(-inches);

        }

        KKL2HardwareManager.driveLMotor.setPower(speed);
        KKL2HardwareManager.driveRMotor.setPower(speed);

        while (!motorsWithinTarget()) {

            //Loop body can be empty
            telemetry.update();

        }

        KKL2HardwareManager.driveLMotor.setPower(0);
        KKL2HardwareManager.driveRMotor.setPower(0);

    }

    public void turn(Direction direction, int degrees, double speed) {

        //Converts degrees to radians, then changes to work with ticks
        int radians = (int)(degrees * DEGREES_TO_RADIANS * RADIAN_CONVERSION_RATIO);


        //Resets the encoders and does a left point turn for the inputted degrees
        KKL2HardwareManager.driveRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL2HardwareManager.driveLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        KKL2HardwareManager.driveRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        KKL2HardwareManager.driveLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (direction == Direction.Left) {

            KKL2HardwareManager.driveRMotor.setTargetPosition(-radians);
            KKL2HardwareManager.driveLMotor.setTargetPosition(radians);

        }

        if (direction == Direction.Right) {

            KKL2HardwareManager.driveRMotor.setTargetPosition(radians);
            KKL2HardwareManager.driveLMotor.setTargetPosition(-radians);

        }

        KKL2HardwareManager.driveLMotor.setPower(speed);
        KKL2HardwareManager.driveRMotor.setPower(speed);

        while (!motorsWithinTarget()) {

            //Loop body can be empty
            telemetry.update();

        }

        KKL2HardwareManager.driveLMotor.setPower(0);
        KKL2HardwareManager.driveRMotor.setPower(0);

    }

    public boolean motorsBusy() {

        return (KKL2HardwareManager.driveRMotor.isBusy() || KKL2HardwareManager.driveLMotor.isBusy()) && opModeIsActive();

    }

    public boolean motorsWithinTarget() {

        int lDif = (KKL2HardwareManager.driveLMotor.getTargetPosition()
                - KKL2HardwareManager.driveLMotor.getCurrentPosition());
        int rDif = (KKL2HardwareManager.driveRMotor.getTargetPosition()
                - KKL2HardwareManager.driveRMotor.getCurrentPosition());

        return ((Math.abs(lDif) <= 10) & (Math.abs(rDif) <= 10));

    }

}

