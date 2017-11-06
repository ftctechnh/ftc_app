package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.teamcode.VuForiaTest;
/**
 * This will be our Autonomous and our first try at a state machine (comment one)
 * Created by Joseph Liang on 10/30/2017.
 */

@Autonomous(name="Relic Recovery: State Machine", group="Pushbot")
//@Disabled
public class RlcRcvryAutoSwitch extends OpMode{

    private int stateMachineFlow;
    HardwarePushbot robot       = new HardwarePushbot();
    private ElapsedTime     runtime = new ElapsedTime();

    int glyph = 0;

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.7;
    static final double     TURN_SPEED              = 0.65;

    @Override
    public void init() {
        robot.init(hardwareMap);
        //need code for gripping glyph and moving arm slightly up
        stateMachineFlow = 0;
    }


    @Override
    public void loop() {
        switch(stateMachineFlow){
            case 0:
                runtime.reset();
                stateMachineFlow++;
                break;
            case 1:
                if (getRuntime()<1) {
                    //viewforia stuff goes here
                    if (viewforia = center){glyph = 1;}
                    else if (viewforia = left){glyph = 2;}
                    else if (viewforia = right){glyph = 3;}
                    break;
                }
                else stateMachineFlow++;
                break;
            case 2:
                encoderDrive(TURN_SPEED);//90 degrees

                stateMachineFlow++;
                break;
            case 3:
                if (glyph() = 1){encoderDrive(DRIVE_SPEED);}
                else if (glyph() = 2) {encoderDrive(DRIVE_SPEED);}
                else if (glyph() = 3) {encoderDrive(DRIVE_SPEED);}
                stateMachineFlow++;
                break;
            case 4:
                encoderDrive(TURN_SPEED);//face box
                stateMachineFlow++;
                break;
            case 5:
                encoderDrive(DRIVE_SPEED);//move so glyph is in box
                stateMachineFlow++;
                break;
            case 6:
                //release glyph into box
                stateMachineFlow++;
                break;
            case 7:
                //end?
                break;
        }
    }
    //encoder drive method
    public void encoderDrive(double speed,
                             double leftSide, double rightSide,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;
        telemetry.addData("Path", "before opmode is active");
        telemetry.update();
        sleep(500);

        // Ensure that the opmode is still active
        //if (opModeIsActive()) {
            telemetry.addData("Path", "inside opmode: %7d :%7d",robot.leftDrive.getCurrentPosition(),robot.rightDrive.getCurrentPosition());
            telemetry.update();
            sleep(500);

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftDrive.getCurrentPosition() + (int)(leftSide * COUNTS_PER_INCH);
            newRightTarget = robot.rightDrive.getCurrentPosition() + (int)(rightSide * COUNTS_PER_INCH);
            robot.leftDrive.setTargetPosition(newLeftTarget);
            robot.rightDrive.setTargetPosition(newRightTarget);

            telemetry.addData("after new target set",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
            telemetry.update();
            sleep(500);

            // Turn On RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftDrive.setPower(Math.abs(speed));
            robot.rightDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
           // while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftDrive.isBusy() && robot.rightDrive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.leftDrive.getCurrentPosition(),
                        robot.rightDrive.getCurrentPosition());
                telemetry.update();
            }

            //Stop all motion;
            robot.leftDrive.setPower(0);
            robot.rightDrive.setPower(0);

            telemetry.addData("PathJ", "inside opmode: %7d :%7d",robot.leftDrive.getCurrentPosition(),robot.rightDrive.getCurrentPosition());
            telemetry.update();
            sleep(500);

            // Turn off RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        } //end of encoder drive code
    }//end of class

