package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class ArmControlTest extends LinearOpMode {
    //hardware
    private DcMotor armMotorBase;
    private Servo armServoBase;
    private Servo armServoTop;
    private Servo armServoClaw;
    //set positions

    @Override
    //runop calls other methods to control arm- main method


    public void runOpMode() throws InterruptedException {
        initialize();
        while(opModeIsActive()){
            if (gamepad1.a) { //folded
                armServoBase.setPosition(0);
                armServoTop.setPosition(0);

            } else if (gamepad1.b) { //straight
                armServoBase.setPosition(1);
                armServoTop.setPosition(1);
            }
        }
    }

    public void setMotor () {
        armMotorBase.setTargetPosition(100);
        armMotorBase.setPower(0.5);
    }

    public void initialize() {
        armMotorBase = hardwareMap.get(DcMotor.class, "armMotorBase");
        armServoBase = hardwareMap.get(Servo.class, "armServoBase");
        armServoTop = hardwareMap.get(Servo.class, "armServoTop");
        armServoClaw = hardwareMap.get(Servo.class, "armServoClaw");
        armMotorBase.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotorBase.setDirection(DcMotor.Direction.FORWARD);
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