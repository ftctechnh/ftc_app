package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * This will be our Autonomous and our first try at a state machine (comment one)
 * Created by Joseph Liang on 10/30/2017.
 */

@Autonomous(name="Relic Recovery: State Machine", group="Pushbot")
//@Disabled
public class RlcRcvryAutoSwitch extends OpMode{

    private int stateMachineFlow;
    RelicDrive robot       = new RelicDrive();
    private ElapsedTime     runtime = new ElapsedTime();

    int glyph = 0;
    GlyphArm gilgearmesh = new GlyphArm();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.7;
    static final double     TURN_SPEED              = 0.65;

    VuforiaLocalizer vuforia;
    int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
    VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
    VuforiaTrackable relicTemplate = relicTrackables.get(0);

    @Override
    public void init() {
        robot.init(hardwareMap);
        parameters.vuforiaLicenseKey = "Ab47Iov/////AAAAGVSivzkE2UEEoiMKAm72knw+f69pC3+FWtnwmp26yNKLBnQ7o48HaEaAIbAMmi4KE/YqAOa1hWE6uV+U5eOZyTSDhJOQQqMhHKtFymevtYLWk+CsXyFA4ipONM9Yfi06TN3sAJUDqqm3sWR8pWgTAvs2M/VoRDw9ZNwg1MzxZPmU5VVmr9ifsv0rGbcoE585jWH+jzTnnnxnRN+3i/AoE1nTthvv9KIq6ZSNpgR2hguJUcBv8B43gg122D0akqbG+pAIGp78TiMn5BZqciaHRSzvZV2JOcIMZzk5FPp96rn7sWhyHZMI5mpUpgA25CG8gTC8e+8NoxMyN277hid7VFubrb4VbsH5qUxDzfDCcmOV";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
        //need code for gripping glyph and moving arm slightly up

        gilgearmesh.clawPoss(0);
        //wait needed?
        gilgearmesh.armPoss(2);
        stateMachineFlow = 0;
        relicTrackables.activate();
    }


    @Override
    public void loop() {
        switch(stateMachineFlow){
            case 0:
                runtime.reset();
                stateMachineFlow++;
                break;
            case 1:
                RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.UNKNOWN;
                while (vuMark == RelicRecoveryVuMark.UNKNOWN) {
                    vuMark = RelicRecoveryVuMark.from(relicTemplate);
                    //viewforia stuff goes here
                    if (vuMark == center){glyph = 1;}
                    else if (vuMark == left){glyph = 2;}
                    else if (vuMark == right){glyph = 3;}
                }
                stateMachineFlow++;
                break;
            case 2:
                encoderDrive(TURN_SPEED);//180 degrees

                stateMachineFlow++;
                break;
            case 3:
                if (glyph == 1){encoderDrive(DRIVE_SPEED);}
                else if (glyph == 2) {encoderDrive(DRIVE_SPEED);}
                else if (glyph == 3) {encoderDrive(DRIVE_SPEED);}
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
                gilgearmesh.clawPoss(1);
                //if needed close claw and push the glyph in
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

