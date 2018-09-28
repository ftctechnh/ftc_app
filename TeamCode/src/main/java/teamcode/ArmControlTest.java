package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class ArmControlTest extends LinearOpMode {
    //hardware
    private DcMotor armBaseMotor;
    private Servo armServoBase;
    private Servo armServoTop;
    private Servo armServoClaw;
    //set positions

    private double craterPosition = ..25;
    private double scorePosition = .5;
    @Override
    //runop calls other methods to control arm- main method


    public void runOpMode() throws InterruptedException {
        initialize();
        while(opModeIsActive()){

        }
    }
    public void initialize() {
        armBaseMotor = hardwareMap.get(DcMotor.class, "armBaseMotor");
        armServoBase = hardwareMap.get(Servo.class, "armServoBase");
        armServoTop = hardwareMap.get(Servo.class, "armServoTop");
        armServoClaw = hardwareMap.get(Servo.class, "armServoClaw");
        armBaseMotor.setDirection(DcMotor.Direction.FORWARD);
        armServoBase.setDirection(Servo.Direction.FORWARD);
        armServoTop.setDirection(Servo.Direction.FORWARD);
        armServoClaw.setDirection(Servo.Direction.FORWARD);






    }
}

/*
public class LinearSlideTest extends LinearOpMode {
    private DcMotor motor1;

    public void runOpMode() {
        initialize();

        //Always True
        while (opModeIsActive() ) {
            float joystickY = gamepad1.right_stick_y;
            motor1.setPower(joystickY);
        }
    }

    public void initialize() {
        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor1.setDirection(DcMotor.Direction.FORWARD);
    }
}
*/