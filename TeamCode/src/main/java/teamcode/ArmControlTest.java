
package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="ArmControlTest", group="Linear Opmode")
public class ArmControlTest extends LinearOpMode {
    //hardware
    //private DcMotor armMotorBase;
    private Servo armServoBase;
    private Servo armServoTop;
    //private Servo armServoClaw;

    //private int armMotorBasePosition;
    private double armServoBasePosition;
    private double armServoTopPosition;

    @Override
    //runop calls other methods to control arm- main method


    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        while(opModeIsActive()){

            if (gamepad1.right_stick_y != 0) {
                int val = gamepad1.right_stick_y > 0 ? 5 : -5;
                //armMotorBasePosition += val;
                //setMotor(armMotorBasePosition, .25);
            }
            else if (gamepad1.x) {
                armServoBasePosition += 0.1;
                //armServoBase.setPosition(armServoBasePosition);
                armServoBase.setPosition(1);
                sleep(200);
            }
            else if (gamepad1.a) {
                armServoBasePosition -= 0.1;
                //armServoBase.setPosition(armServoBasePosition);
                armServoBase.setPosition(0);
                sleep(200);
            }
            else if (gamepad1.y) {
                armServoTopPosition -= 0.1;
                //armServoTop.setPosition(armServoTopPosition);
                armServoTop.setPosition(0);
                sleep(200);
            }
            else if (gamepad1.b) {
                armServoTopPosition += 0.1;
                //armServoTop.setPosition(armServoTopPosition);
                armServoTop.setPosition(1);
                sleep(200);
            }
        }
    }

    /*
    public void setMotor (int position, double speed) {
        armMotorBase.setTargetPosition(position);
        armMotorBase.setPower(speed);
    }
    */

    public void initialize() {
        //armMotorBase = hardwareMap.get(DcMotor.class, "armMotorBase");
        armServoBase = hardwareMap.get(Servo.class, "armServoBase");
        armServoTop = hardwareMap.get(Servo.class, "armServoTop");
        //armServoClaw = hardwareMap.get(Servo.class, "armServoClaw");
        //armMotorBase.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //armMotorBase.setDirection(DcMotor.Direction.FORWARD);
        //armMotorBase.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //armMotorBase.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armServoBase.setDirection(Servo.Direction.REVERSE);
        armServoTop.setDirection(Servo.Direction.FORWARD);
        //armServoClaw.setDirection(Servo.Direction.FORWARD);

        //armMotorBasePosition = 0;
        armServoBasePosition = 0;
        armServoTopPosition = 0;
    }
}
