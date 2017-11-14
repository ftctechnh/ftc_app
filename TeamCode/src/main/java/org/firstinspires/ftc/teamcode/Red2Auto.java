package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by your mother on 9/24/17.
 */

@Autonomous(name="Red2Auto", group="Pushbot")

public class Red2Auto extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareDRive robot = new HardwareDRive();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    //RelicRecoveryVuMark instance =


    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        robot.FLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.BLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.FRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.BRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        idle();

        robot.FLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.BLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.FRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.BRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        forward(1,4,10);

        // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way
            /*Vuforiastuff.vuMark();
            // Step 1:  Drive forward for 3 seconds

            if (Vuforiastuff.vuMark == RelicRecoveryVuMark.LEFT) {
                robot.Motor.setPower(FORWARD_SPEED);
                runtime.reset();
                while (opModeIsActive() && (runtime.seconds() < 3.0)) {
                    telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
                    telemetry.update();
                }
            }

            if (Vuforiastuff.vuMark == RelicRecoveryVuMark.CENTER) {
                robot.Motor.setPower(FORWARD_SPEED);
                runtime.reset();
                while (opModeIsActive() && (runtime.seconds() < 1.3)) {
                    telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
                    telemetry.update();
                }
            }
            if (Vuforiastuff.vuMark == RelicRecoveryVuMark.RIGHT) {
                // Step 3:  Drive FOrward for 1 Second
                robot.Motor.setPower(FORWARD_SPEED);
                runtime.reset();
                while (opModeIsActive() && (runtime.seconds() < 1.0)) {
                    telemetry.addData("Path", "Leg 3: %2.5f S Elapsed", runtime.seconds());
                    telemetry.update();
                }
            }

            telemetry.addData("Path", "Complete");
            telemetry.update();
            sleep(1000);
            */
    }

    public void forward(double speed, int dis, double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.FLMotor.getCurrentPosition()+dis;
            newRightTarget = robot.FRMotor.getCurrentPosition()+dis;
            robot.FLMotor.setTargetPosition(newLeftTarget);
            robot.FRMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.FLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.FRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.FLMotor.setPower(Math.abs(speed));
            robot.FRMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() ||
                    (runtime.seconds() < timeoutS) ||
                    (robot.FLMotor.isBusy() && robot.FRMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.FLMotor.getCurrentPosition(),
                        robot.FRMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.FLMotor.setPower(0);
            robot.FRMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.FLMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.FRMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

              sleep(250);   // optional pause after each move
        }
    }
}