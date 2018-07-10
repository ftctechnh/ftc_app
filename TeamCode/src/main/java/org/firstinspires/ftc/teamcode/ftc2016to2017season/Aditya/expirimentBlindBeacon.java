package org.firstinspires.ftc.teamcode.ftc2016to2017season.Aditya;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.ftc2016to2017season.Main.beta.AutonomousGeneral;

/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "epirimentBlindBeacon")
@Disabled
public class expirimentBlindBeacon extends AutonomousGeneral {

    OpticalDistanceSensor ODSFront;
    OpticalDistanceSensor ODSBack;
    ModernRoboticsI2cRangeSensor rangeSensor;

    double baseline1;
    double baseline2;

    boolean rightDetected;
    boolean leftDetected;
    boolean second_beacon_press = false;
    String currentTeam = "red";
    String currentColor = "blank";

    @Override
    public void runOpMode() {

        initiate();

        ODSFront = hardwareMap.opticalDistanceSensor.get("ODSFront");
        ODSBack = hardwareMap.opticalDistanceSensor.get("ODSBack");
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");

        baseline1 = ODSFront.getRawLightDetected();
        baseline2 = ODSBack.getRawLightDetected();

        readNewColorLeft();

        waitForStart();
        second_beacon_press = false;

       // encoderDriveShoot(0.2, 135, 108, 8.0, 20, 2);



        currentColor = "blank";
       // lineAlignRed();

        pressBeacon();
        sleep(250);
/*
        encoderDrive(0.5, -75, -75, 6);
        encoderDrive(0.5, 10, 10, 6);
        encoderDrive(0.5, -15, -15, 6);
        */
        sleep(500);


       /* moveToNextBeaconRed();


        sleep(150);

        pressBeacon(); */


        }


    public void moveToNextBeaconRed() {
        second_beacon_press = true;
        sleep(250);
        encoderDrive(0.2, -25, -25, 6);
        sleep(450);
        encoderDrive(0.2, 29, -29, 6);

        sleep(150);
        encoderDrive(0.5, 65, 65, 6);

}



    public boolean whiteLineDetectedFront(){
        if ((ODSFront.getRawLightDetected() > (baseline1*5))){
            rightDetected = true;
            return true;
        }
        rightDetected = false;
        return false;
    }

    public boolean whiteLineDetectedBack(){
        if ((ODSBack.getRawLightDetected() > (baseline2*5))){
            leftDetected = true;
            return true;
        }
        leftDetected = false;
        return false;
    }

    public void allignRangeDist(double distInCM){

        while (rangeSensor.getDistance(DistanceUnit.CM) > distInCM){
            straightDrive(0.1);
        }
        stopMotors();
        telemetry.addData("","Stopping...");
        telemetry.update();
        while (rangeSensor.getDistance(DistanceUnit.CM) < distInCM){
            straightDrive(-0.1);
        }
        stopMotors();
    }

    public void pressBeacon() {

        /*if(currentTeam.equals("blue")) {
            lineAlignBlue();
        } else */{
            telemetry.addData("","lineAlignRed()");
            telemetry.update();
            lineAlignRed();
        }

        telemetry.addData("","allignRangeDist(12)");
        telemetry.update();
        allignRangeDist(6);
        sleep(100);
        encoderDrive(0.2, -5, -5, 5);

        sleep(5000);

        telemetry.addData("","readNewColor");
        telemetry.update();
        readNewColorLeft();


        if (currentColor.equals(currentTeam)) {

//            telemetry.addData("","correct color");
//            telemetry.update();
//            //double distance = rangeSensor.getDistance(DistanceUnit.CM);
//            encoderDrive(0.2, 3.5, -3.5, 5);
//           // encoderDrive(0.2,distance-3,distance-3,5);
//            sleep(1000);
            encoderDrive(0.2, -5, -5, 5);
            sleep(300);
//           encoderDrive(0.2, -3.5, 3.5, 5);

            sleep(300);
        } else {
            sleep(5000);
            telemetry.addData("","incorrect color");
            telemetry.update();
            //double distance = rangeSensor.getDistance(DistanceUnit.CM);
//            encoderDrive(0.2, -3.5, 3.5, 5);
//            sleep(1000);
//            //encoderDrive(0.2,distance-3,distance-3,5);
            encoderDrive(0.2, 5, 5, 5);
//            sleep(300);
//            encoderDrive(0.2, 3.5, -3.5, 5);
            sleep(300);
            encoderDrive(0.2, -5, -5, 5);
        }

    }

    public void lineAlignRed() {

        while (whiteLineDetectedFront() == false) {

            straightDrive(0.1);

            if (rangeSensor.getDistance(DistanceUnit.CM) < 20) {
                encoderDrive(0.4, -10, -10, 6);
                sleep(250);
                encoderDrive(0.6, 15, -15, 8);
            }
        }
        sleep(150);
        if (second_beacon_press == true)
        {
            encoderDrive(0.1, 5, 5, 4);
        }
        else
        {
            encoderDrive(0.1, 7, 7, 4);
        }

        sleep(150);

        while (whiteLineDetectedFront() == false) {

            newTurnLeft(0.1);
        }
        stopMotors();

        sleep(150);

    }

   /* public void lineAlignBlue() {

        while (whiteLineDetectedFront() == false) {

            straightDrive(0.4);

            if (rangeSensor.getDistance(DistanceUnit.CM) < 20) {
                encoderDrive(0.4, -10, -10, 6);
                sleep(250);
                encoderDrive(0.6, -20, 20, 8);
            }
        }

        encoderDrive(0.3, 8, 8, 4);

        while (whiteLineDetectedFront() == false) {

            newTurnLeft(0.3);
        }

        //encoderDrive(0.3, -5, 5, 6);
    }
*/
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
                if (back_left_motor.getCurrentPosition()>leftShootPosition)
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


