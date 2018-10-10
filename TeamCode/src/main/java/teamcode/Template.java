package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

// The name is the how will program will show up on the phone.
// It should be the same as your class name for clarity.
@TeleOp(name="Template", group="Linear Opmode")
public class Template extends LinearOpMode {
    // Your variables should go here.
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private Servo servo;

    private boolean servoAtZero;

    /**
     * This method will contain your robot's logic.
     * You should create separate methods/functions for any
     * reusable code and invoke them from within here to set up
     * your logic.
     * @throws InterruptedException
     */
    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        while(opModeIsActive()){

            if (this.gamepad1.a) { // 'A' button toggles the servo

                if (this.servoAtZero) {
                    // toggle to 1
                    this.servo.setPosition(1);
                    this.servoAtZero = false;
                }
                else {
                    // toggle to 0
                    this.servo.setPosition(0);
                    this.servoAtZero = true;
                }
            }

            // The right joystick moves the motors. This is an example for a 2 wheel drive robot.
            // The robot will move only when the joystick is more than halfway from the center.
            // It will only move in the Y direction. X direction joystick movements will cause the
            // robot to turn in place.
            if(gamepad1.right_stick_y > 0.5 || gamepad1.right_stick_y < -0.5) {
                double motorPower = gamepad1.right_stick_y;
                this.leftMotor.setPower(motorPower);
                this.rightMotor.setPower(motorPower);
            }
            else if(gamepad1.right_stick_x > 0.5 || gamepad1.right_stick_x < -0.5) {
                double motorPower = gamepad1.right_stick_y;

                if (motorPower < 0) {
                    // turn left
                    this.leftMotor.setPower(-motorPower);
                    this.rightMotor.setPower(motorPower);
                }
                else {
                    // turn right
                    this.leftMotor.setPower(motorPower);
                    this.rightMotor.setPower(-motorPower);
                }
            }
            else {
                // don't move
                this.leftMotor.setPower(0);
                this.rightMotor.setPower(0);
            }
        }
    }

    /**
     * A method for initializing any state required before the robot runs.
     */
    public void initialize() {
        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setDirection(DcMotor.Direction.FORWARD);

        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);

        servo = hardwareMap.get(Servo.class, "servo");
        servo.setDirection(Servo.Direction.FORWARD);

        // Initialize the state of the servo to some known values.
        servo.setPosition(0);
        servoAtZero = true;

        // Use telemetry to write print statements on the phone
        // and help you with debugging.
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }
}