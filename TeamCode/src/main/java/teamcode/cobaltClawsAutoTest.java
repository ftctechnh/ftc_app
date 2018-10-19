package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.R;

@Autonomous(name = "cobaltClawsAutoTest", group = "Linear OpMode")

public class cobaltClawsAutoTest extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor LeftDriveMotor; //motor 0
    private DcMotor RightDriveMotor; //motor 1

    //1000 ticks is about 26 inches

    @Override
    public void runOpMode() {


        //run the initialize block
        this.initialize();

        waitForStart();
        runtime.reset();

        while(opModeIsActive()) {

            move("F", 1000, 0.6);

            requestOpModeStop();
            
        }


    }

    private void initialize(){

        //giving internal hardware an external name for the app config
        this.LeftDriveMotor = hardwareMap.get (DcMotor.class,"LeftDriveMotor");
        this.RightDriveMotor = hardwareMap.get (DcMotor.class, "RightDriveMotor");


        //Sets correct directions for motors and servos
        LeftDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        RightDriveMotor.setDirection(DcMotor.Direction.FORWARD);

        //Tells the driver station the arm motor positions and that the robot is ready.
        telemetry.addData("Status", "Online");
        telemetry.update();
    }

    public void move(String direction, int distance, double speed){


        //Resets encoder and moves the inputted degrees
        RightDriveMotor.setMode    (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftDriveMotor.setMode     (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        RightDriveMotor.setMode    (DcMotor.RunMode.RUN_TO_POSITION);
        LeftDriveMotor.setMode     (DcMotor.RunMode.RUN_TO_POSITION);


        if(direction == "F") {

            LeftDriveMotor.setTargetPosition(distance);
            RightDriveMotor.setTargetPosition(distance);

        } else if(direction == "B") {

            LeftDriveMotor.setTargetPosition(-distance);
            RightDriveMotor.setTargetPosition(-distance);

        }

        LeftDriveMotor.setPower    (speed);
        RightDriveMotor.setPower   (speed);

        while(!motorsWithinTarget()) {

            //Loop body can be empty
            telemetry.update();

        }

        LeftDriveMotor.setPower    (0);
        RightDriveMotor.setPower   (0);

    }

    public void turn(String direction, int distance, double speed){


        //Resets the encoders and does a left point turn for the inputted degrees
        RightDriveMotor.setMode    (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftDriveMotor.setMode     (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        RightDriveMotor.setMode    (DcMotor.RunMode.RUN_TO_POSITION);
        LeftDriveMotor.setMode     (DcMotor.RunMode.RUN_TO_POSITION);

        if(direction == "L"){// left

            RightDriveMotor.setTargetPosition  (-distance);
            LeftDriveMotor.setTargetPosition   (distance);

        }

        if(direction == "R"){// right

            RightDriveMotor.setTargetPosition  (distance);
            LeftDriveMotor.setTargetPosition   (-distance);

        }

        LeftDriveMotor.setPower    (speed);
        RightDriveMotor.setPower   (speed);

        while(!motorsWithinTarget()) {

            //Loop body can be empty
            telemetry.update();

        }

        LeftDriveMotor.setPower    (0);
        RightDriveMotor.setPower   (0);

    }

    public boolean motorsBusy(){

        return (RightDriveMotor.isBusy() || LeftDriveMotor.isBusy()) && opModeIsActive();

    }

    public boolean motorsWithinTarget(){

        int lDif = (LeftDriveMotor.getTargetPosition() - LeftDriveMotor.getCurrentPosition());
        int rDif = (RightDriveMotor.getTargetPosition() - RightDriveMotor.getCurrentPosition());

        return ((Math.abs(lDif) <= 10) & (Math.abs(rDif) <= 10));

    }

}

