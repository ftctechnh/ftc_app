
package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="ArmControlTest", group="Linear Opmode")
public class ArmControlTest extends LinearOpMode {
    //hardware
    private DcMotor armMotorBase;
    private Servo armServoBase;
    private Servo armServoTop;
    //private Servo armServoClaw;

    private int armMotorBasePosition;
    private double armServoBasePosition;
    private double armServoTopPosition;
    private int ticks = 10;
    private double step = 0.1;
    private long sleepTime = 200;

    @Override
    //runop calls other methods to control arm- main method


    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        while(opModeIsActive()){

            if (gamepad1.dpad_up) {
                armMotorBasePosition += ticks;
                setMotor(armMotorBasePosition, .2);
                sleep(sleepTime);
            }
            else if (gamepad1.dpad_down) {
                armMotorBasePosition -= ticks;
                setMotor(armMotorBasePosition, .2);
                sleep(sleepTime);
            }
            else if (gamepad1.x) {
                armServoBasePosition = getNewServoPosition(armServoBase.getPosition(), step);
                armServoBase.setPosition(armServoBasePosition);
                sleep(sleepTime);
            }
            else if (gamepad1.a) {
                armServoBasePosition = getNewServoPosition(armServoBase.getPosition(), -step);
                armServoBase.setPosition(armServoBasePosition);
                sleep(sleepTime);
            }
            else if (gamepad1.y) {
                armServoTopPosition = getNewServoPosition(armServoTop.getPosition(), -step);
                armServoTop.setPosition(armServoTopPosition);
                sleep(sleepTime);
            }
            else if (gamepad1.b) {
                armServoTopPosition = getNewServoPosition(armServoTop.getPosition(), step);
                armServoTop.setPosition(armServoTopPosition);
                sleep(sleepTime);
            }
        }
    }

    public void setMotor (int position, double speed) {
        armMotorBase.setTargetPosition(position);
        armMotorBase.setPower(speed);
    }

    public void initialize() {
        armMotorBase = hardwareMap.get(DcMotor.class, "armMotorBase");
        armServoBase = hardwareMap.get(Servo.class, "armServoBase");
        armServoTop = hardwareMap.get(Servo.class, "armServoTop");
        //armServoClaw = hardwareMap.get(Servo.class, "armServoClaw");
        armMotorBase.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotorBase.setDirection(DcMotor.Direction.FORWARD);
        armMotorBase.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotorBase.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armServoBase.setDirection(Servo.Direction.REVERSE);
        armServoTop.setDirection(Servo.Direction.FORWARD);
        //armServoClaw.setDirection(Servo.Direction.FORWARD);

        armMotorBasePosition = 0;
        armServoBasePosition = 0;
        armServoTopPosition = 0;
    }

    public double getNewServoPosition(double currentPosition, double delta) {
        double newPosition = currentPosition + delta;

        if (newPosition > 1.0) {
            newPosition = 1.0;
        }
        else if (newPosition < 0) {
            newPosition = 0;
        }

        return newPosition;
    }

}
