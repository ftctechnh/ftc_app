package org.firstinspires.ftc.teamcode.Aditya;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.AutonomousGeneral;

/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "newBlueBeaconPress_auto")
public class newblueBeaconPress_auto extends AutonomousGeneral {

    boolean second_beacon_press = false;
    String currentTeam = "blue";


    @Override
    public void runOpMode() {

        initiate();

        readNewColor();

        waitForStart();

        second_beacon_press = false;

        encoderDriveShoot(0.2, -135, -108, 8.0, -20, 2);

        pressBeaconBlue();
        sleep(250);

        moveToNextBeaconBlue();

        sleep(150);

        pressBeaconBlue();
        }




    public void moveToNextBeaconBlue() {
        second_beacon_press = true;

        sleep(250);
        encoderDrive(0.4, -25, -25, 6);
        sleep(450);
        encoderDrive(0.4, 32, -32, 6);

        sleep(150);
        encoderDrive(0.5, -65, -65, 6);
    }



    public void pressBeaconBlue() {

            lineAlignBlue();

        allignRangeDist(7);

        readNewColor();
        if (currentColor.equals(currentTeam)) {
            telemetry.addData("","correct color");
            telemetry.update();

            encoderDrive(0.2, 3.5, -3.5, 5);
            sleep(1000);
            encoderDrive(0.2, -5, -5, 5);
            sleep(300);
           encoderDrive(0.2, -3.5, 3.5, 5);

            sleep(300);
        } else {
            telemetry.addData("","incorrect color");
            telemetry.update();

            encoderDrive(0.2, -3.5, 3.5, 5);
            sleep(1000);
            encoderDrive(0.2, -5, -5, 5);
            sleep(300);
            encoderDrive(0.2, 5, -5, 5);
            sleep(300);
        }
    }



    public void lineAlignBlue() {

        while (whiteLineDetectedBack() == false) {

            straightDrive(-0.1);

           /* if (rangeSensor.getDistance(DistanceUnit.CM) < 20) {
                encoderDrive(0.4, -15, -15, 6);
                sleep(250);
                encoderDrive(0.6, 15, -15, 8);
            }*/
        }
        sleep(150);
        if (second_beacon_press == true)
        {
            encoderDrive(0.1, -15, -15, 4);
        }
        else
        {
            encoderDrive(0.1, -15, -15, 4);
        }

        sleep(150);

        while (whiteLineDetectedFront() == false) {

            newTurnLeft(0.1);
        }
        stopMotors();
        if (second_beacon_press == true)
        {
            encoderDrive(0.1, -3, 3, 4);
        }
        else
        {
          //  encoderDrive(0.1, -3, 3, 4);
        }
    }

    public void encoderDriveShoot(double speed,
                             double leftInches, double rightInches,
                             double timeoutS, double left_motor_shoot_position, int num_shoot) {
        int newLeftTarget;
        int newRightTarget;
        int leftShootPosition;
        int shoot_count = 0;
        double leftSpeed;
        double rightSpeed;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = back_left_motor.getCurrentPosition() + (int) (leftInches * getCountsPerCm());
            leftShootPosition = back_left_motor.getCurrentPosition() + (int) (left_motor_shoot_position * getCountsPerCm());
            newRightTarget = back_right_motor.getCurrentPosition() + (int) (rightInches * getCountsPerCm());
            back_left_motor.setTargetPosition(newLeftTarget);
            back_right_motor.setTargetPosition(newRightTarget);
            front_left_motor.setTargetPosition(newLeftTarget);
            front_right_motor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            back_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            back_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            front_left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            front_right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            if (Math.abs(leftInches) > Math.abs(rightInches)) {
                leftSpeed = speed;
                rightSpeed = (speed * rightInches) / leftInches;
            } else {
                rightSpeed = speed;
                leftSpeed = (speed * leftInches) / rightInches;
            }
            runtime.reset();
            //if(leftInches != -rightInches)
            {
                back_left_motor.setPower(Math.abs(leftSpeed));
                back_right_motor.setPower(Math.abs(rightSpeed));
            }
            front_left_motor.setPower(Math.abs(leftSpeed));
            front_right_motor.setPower(Math.abs(rightSpeed));


            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (back_left_motor.isBusy() && back_right_motor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        back_left_motor.getCurrentPosition(),
                        back_right_motor.getCurrentPosition());
                telemetry.update();
                if (back_left_motor.getCurrentPosition()< leftShootPosition)
                {
                    if (shoot_count < num_shoot)
                    {
                        shoot_count++;
                        shootingDrive(0.8,850);

                       // sleep(500);     // pause for servos to move
                        if (shoot_count < (num_shoot))
                        {
                            intakeDrive(0.8, 1800);
                        }
                    }
                }
            }

            // Stop all motion;
            back_left_motor.setPower(0);
            back_right_motor.setPower(0);
            front_left_motor.setPower(0);
            front_right_motor.setPower(0);


            // Turn off RUN_TO_POSITION
            back_left_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            back_right_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            front_left_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            front_right_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }

    }
}
//-------------------------------------------------------------------//


