package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "kobaltKlawsBaseProgram", group = "Linear OpMode")

public class kobaltKlawsBaseProgram extends LinearOpMode{

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motor1;
    private DcMotor motor2;
    private DcMotor motor3;
    private DcMotor motor4;
    private Servo servo1;
    private Servo servo2;

    @Override
    public void runOpMode() {

        //run the initialize block
        this.initialize();

        waitForStart();
        runtime.reset();

        while(opModeIsActive()){


            //Drive System

            //double is a variable type that supports decimals
            double leftPower;
            double rightPower;
            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;


            if (drive !=0){
                leftPower = (-drive/2);
                rightPower = (-drive/2);
                //if drive is not 0, set both motor powers to the value of drive
            }
            else if (turn !=0){
                leftPower = (drive - (turn/2));
                rightPower = (drive + (turn/2));
                //if the drive if reports false, it runs this turn block
            }
            else {
                leftPower = 0;
                rightPower = 0;
                //if neither of these if statements report true it sets the motor powers to 0
                // if you don't push a stick, the robot don't move
            }
            this.motor1.setPower(leftPower);
            this.motor2.setPower(rightPower);

            if (gamepad1.x) {
                double grabberPosition = this.servo1.getPosition();
                if (grabberPosition == 0) {
                    this.servo1.setPosition(0.5);
                    this.servo2.setPosition(0.5);
                    //If grabber is closed, open grabber
                } else if (grabberPosition == 0.5) {
                    this.servo1.setPosition(0);
                    this.servo2.setPosition(0);
                    //If grabber is open, close grabber
                }
            }

            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }


    private void initialize(){

        //giving internal hardware an external name for the app config
        //also initializing the hardware?
        this.motor1 = hardwareMap.get (DcMotor.class,"LeftDriveMotor");
        this.motor2 = hardwareMap.get (DcMotor.class, "RightDriveMotor");
        this.motor3 = hardwareMap.get (DcMotor.class, "ArmMotorExtend");
        this.motor4 = hardwareMap.get (DcMotor.class, "ArmMotorSwing");
        this.servo1 = hardwareMap.get (Servo.class, "GrabberServoLeft");
        this.servo2 = hardwareMap.get (Servo.class, "GrabberServoRight");

        motor1.setDirection(DcMotor.Direction.FORWARD);
        motor2.setDirection(DcMotor.Direction.FORWARD);

        servo1.setDirection(Servo.Direction.FORWARD);
        servo2.setDirection(Servo.Direction.REVERSE);

        this.servo1.setPosition(0);
        this.servo2.setPosition(0);

        telemetry.addData("Status", "Online");
        telemetry.update();
    }
}
