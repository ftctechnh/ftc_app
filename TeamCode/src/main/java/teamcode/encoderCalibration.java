package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.sqrt;

@TeleOp(name="encoderCalibration", group="Linear Opmode")

public class encoderCalibration extends LinearOpMode {
    //teleOPFinal

    // Declare OpMode members.

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motor1; // back motor
    private DcMotor motor2; // left motor
    private DcMotor motor3; // right motor
    private Servo armServo;
    private Servo leftClampServo;
    private Servo rightClampServo;
    final double armLiftChange = 0.0015;
    final double cooldown = 0.5;
    private boolean isClamped = true;
    private double row1Position = 0.2;
    private double row2Position = 0.4;
    private double row3Position = 0.6;
    private double testSpeed = 0.3;

    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");

        telemetry.update();

        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor3 = hardwareMap.get(DcMotor.class, "motor3");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");

        armServo = hardwareMap.get(Servo.class, "armServo");
        rightClampServo = hardwareMap.get(Servo.class, "rightClampServo");

        motor1.setDirection(DcMotor.Direction.FORWARD);
        motor3.setDirection(DcMotor.Direction.FORWARD);
        motor2.setDirection(DcMotor.Direction.FORWARD);

        armServo.setDirection(Servo.Direction.FORWARD);
        rightClampServo.setDirection(Servo.Direction.REVERSE);


        //At Start
        waitForStart();
        runtime.reset();
        double clawActiveTimeInSec = 0;
        boolean clawActive = false;
        double elapsedTime;
        double robotYSpeedRight = 0;
        double robotXSpeedRight = 0;

        //Always True
        while (opModeIsActive() ) {
            elapsedTime = runtime.time();
            // on gamepad movement (controls robot wheel movment)
            if (gamepad1.dpad_up) {
                drive(0, testSpeed);
            }
            else if (gamepad1.dpad_down) {
                drive(0, -testSpeed);
            }
            else if (gamepad1.dpad_left) {
                drive(-testSpeed, 0);
            }
            else if (gamepad1.dpad_right) {
                drive(testSpeed, 0);
            }
            else if (gamepad1.left_bumper) {
                testSpeed -= .001;
            }
            else if (gamepad1.right_bumper) {
                testSpeed += .001;
            }
            else
            {
                turnOffMotors();
            }


            updateTelemetry();
        }
    }

    public void updateTelemetry(){

        telemetry.addData("Status", "Run Time   : " + runtime.toString());
        telemetry.addData("speed", testSpeed);
        telemetry.addData("back wheel encoder values", motor1.getCurrentPosition());
        telemetry.addData("left wheel encoder values", motor2.getCurrentPosition());
        telemetry.addData("right wheel encoder values", motor3.getCurrentPosition());
        telemetry.update();
    }
    public double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    //drive method that accepts two values, x and y motion

    public void driveForwardDistance(double power, int distance)
    {
        //reset encoders
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //set target position
        motor1.setTargetPosition(distance);
        motor2.setTargetPosition(distance);

        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        drive(0,power);

        while (motor2.isBusy() && motor3.isBusy())
        {

        }

        turnOffMotors();
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void drive(double x, double y)
    {
        double scale1 = 1.0;
        double scale2 = 1.0;
        double scale3 = 1.0;
        double power1 = scale1 * x;
        double power2 = (scale2 * (((-.5) * x) - (sqrt(3)/2) * y)) ;
        double power3 = (scale3 * (((-.5) * x) + (sqrt(3)/2) * y)) ;

        motor1.setPower(power1);
        motor2.setPower(power2);
        motor3.setPower(power3);
    }

    private void turnOffMotors()
    {
        motor1.setPower(0);
        motor3.setPower(0);
        motor2.setPower(0);
    }

    private void turn(double speed)
    {
        double divisor = 2;
        // of the bumper is being held than make the robot turn slower
        if (gamepad1.left_bumper) {
            divisor = 4;
        }
        motor1.setPower(-speed/divisor);
        motor3.setPower(-speed/divisor);
        motor2.setPower(-speed/divisor);
    }



}

