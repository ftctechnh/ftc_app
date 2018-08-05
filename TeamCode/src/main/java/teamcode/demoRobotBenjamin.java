package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name="demoRobotBenjamin", group="Linear Opmode")
public class demoRobotBenjamin extends LinearOpMode {
    private DcMotor motorLeft;
    private DcMotor motorRight;
    private Servo armServo;

    private void initialize() {

        motorLeft = hardwareMap.get(DcMotor.class, "motorLeft");
        motorRight = hardwareMap.get(DcMotor.class, "motorRight");


        armServo = hardwareMap.get(Servo.class, "armServo");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

    }


    @Override
    public void runOpMode() {

        this.initialize();

        // run until the end of the match (driver presses STOP)
        double tgtPowerLeft = 0;
        double tgtPowerRight = 0;
        while (opModeIsActive()) {
            tgtPowerLeft = -this.gamepad1.left_stick_y;
            tgtPowerRight = -this.gamepad1.right_stick_y;
            motorLeft.setPower(tgtPowerLeft);
            motorRight.setPower(tgtPowerRight);
            //check to see if we need to move the servo.
            if(gamepad1.y) {
                //move to 0 degrees
                armServo.setPosition(0);
            } else if (gamepad1.x){
                //move to 90 degrees
                armServo.setPosition(0.5);
            } else if (gamepad1.a) {
                //move to 180 degrees
                armServo.setPosition(1);
            } else if (gamepad1.b) {
                //move to -90 degrees
                armServo.setPosition(-0.5);
            }

            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }

}
