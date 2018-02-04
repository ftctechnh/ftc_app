

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/*
This program contains all the autnomous programs. The varaible sign changes if it is red orr blue for the turns.
You need to turn more on front / facing the crowd becuase it is too close to the glyph pit.
All other programs extended driveAutonomous.
Two deciding variables red or blue/alliance which decides what sign is and close to the crowd or not decides the front.
 */

@Autonomous(name="Pushbot: Drive Autonomous" , group="Pushbot")
//@Disabled
public abstract class driveAutonomous extends LinearOpMode {

    /* Declare OpMode members. */
    MyHardwarePushbot         robot   = new MyHardwarePushbot();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 835;    // eg: Neverest 40
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    int             target = 0;
    double          clawOffset ; // starts claw closed on block
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.25;
    double          ballArmUp = .7; // makes ball Arm a variable
    double          ballArmDown = 0.05;

    String alliance = "";
    String position = "";
    int    sign = 1;
//    int    sign2 = 1;

    NormalizedColorSensor colorSensor; //This line creates a NormalizedColorSensor variable called colorSensor
    VuforiaLocalizer vuforia;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         * Set up Vuforia
         */
        robot.init(hardwareMap);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AQnPJDP/////AAAAmUS73qR6gk0zhRdrQV8z5sQcsIZOLNFEeBu51S0z1IDJ2mjzjQakuSkKjPPN5P0OU1xLzIllx8BNZ1+sUF0TMno1opOTWRq7LHOx1FwfiYL9OWCB+nti7esVQTjWHAat6af1DAX0wpzvL+YSjcTptliDr1r4mQ5jZvuypJDv2F/o0F2sGVAM0GZNczjmp3mconmIdNFt/ZkuhtEmqSYNEiRO5vp049huTdMQ+wF63ZO9naWQgNf/sD4u/6YydHzKNGg9BneTYjWrdXnfLMrUlPCFzVZ+WOWh6HnbV8OxA+AEMGSJCe9dZwkVouBgfAMXmTLkWxerr195jyo/pVPAZ2euUnqVOCbZwKS0S/jdZapO";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        relicTrackables.activate();

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();

        // set up dc motors
        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                robot.leftDrive.getCurrentPosition(),
                robot.rightDrive.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color");

        robot.ballArm.setPosition(ballArmDown);// lowers ballArm all the way down

        //grab and slightly lift glyph
        clawOffset =robot.CLOSE_offset;
        clawOffset = Range.clip(clawOffset, -0.5, 0.5);
        robot.leftClaw.setPosition(robot.MID_SERVO + clawOffset + robot.leftclawcorrection);
        robot.leftClawup.setPosition(robot.MID_SERVO - clawOffset + robot.leftclawupcorrection);
        robot.rightClaw.setPosition(robot.MID_SERVO - clawOffset + robot.rightclawcorrection);
        robot.rightClawup.setPosition(robot.MID_SERVO + clawOffset + robot.rightclawupcorrection);
        runtime.reset();
        while (runtime.seconds() < .4) {    //wait for claw to finsh open or close
        }
        target = robot.lift.getCurrentPosition() + 650;
        robot.lift.setTargetPosition(target);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setPower(0.6);
        while (robot.lift.isBusy() && (robot.lift.getCurrentPosition() < robot.maxlift)) {}   //wait for lift to stop
        robot.lift.setPower(0.0);


        NormalizedRGBA colors = colorSensor.getNormalizedColors(); // reads color sensor and puts it in the variable colors

        //  checks if you are red or blue to decided sign
        //when you turn you multiply by sign
        if (alliance == "Red") {
            sign = 1;
        }
        else {
            sign = -1;
        }

        if (colors.red < colors.blue){ //It checks if the ball is bluecthen turns to knock of the correct ball

            telemetry.addData("Status", "Sensed Blue ");    //
            telemetry.update();
            encoderDrive(TURN_SPEED, 2*sign, -2*sign, 5 );
            robot.ballArm.setPosition(ballArmUp);
            encoderDrive(TURN_SPEED, -2*sign, 2*sign, 5 );

        }
        else  { //The ball facing the color sensor is red
            telemetry.addData("Status", "Sensed Red ");
            encoderDrive(TURN_SPEED, -2*sign, 2*sign, 5 );
            robot.ballArm.setPosition(ballArmUp);
            encoderDrive(TURN_SPEED,  2*sign, -2*sign, 5 );
        }

        //turn toward and view pictograph
        encoderDrive(TURN_SPEED, 1.5*sign, -1.5*sign, 5);
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        do {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            telemetry.addData("VuMark", "%s visible", vuMark);
            telemetry.update();
        }while (runtime.seconds() < 1);
        encoderDrive(TURN_SPEED, -1.5*sign, 1.5*sign, 5);

        if (position == "Front") {
            // drive to cryptobox
            encoderDrive(DRIVE_SPEED, -29, -29, 5);      // drives off balancing stone
            encoderDrive(TURN_SPEED, -14*sign, 14*sign, 30);
            encoderDrive(DRIVE_SPEED, -18.5,  -18.5, 5);  //  turns twice to avoid glyph  pit

            //turns acording to pictograph reading
            if ((alliance == "Red" && vuMark == RelicRecoveryVuMark.RIGHT)|| (alliance == "Blue" && vuMark == RelicRecoveryVuMark.LEFT)){
                telemetry.addData("VuMark", "%s visible", vuMark);
                telemetry.update();
                encoderDrive(TURN_SPEED, -6.75*sign,6.75*sign,5);
            }
            else if (vuMark == RelicRecoveryVuMark.CENTER) {
                telemetry.addData("VuMark", "%s visible", vuMark);
                telemetry.update();
                encoderDrive(TURN_SPEED, -5.5*sign,5.5*sign,5);
            }
            else if ((alliance == "Blue" && vuMark == RelicRecoveryVuMark.RIGHT)|| (alliance == "Red" && vuMark == RelicRecoveryVuMark.LEFT)  || vuMark == RelicRecoveryVuMark.UNKNOWN) {
                telemetry.addData("VuMark", "%s visible", vuMark);
                telemetry.update();
                encoderDrive(TURN_SPEED, -4.5*sign,4.5*sign,5);
            }
            encoderDrive(DRIVE_SPEED, -35,-35,5);   // drive into cryptobox

        }
        else {
            encoderDrive(DRIVE_SPEED, -32.5, -32.5, 5); // Drive off balance board
            //turns acording to pictograph reading
            if ((alliance == "Red" && vuMark == RelicRecoveryVuMark.RIGHT )|| (alliance == "Blue" && vuMark == RelicRecoveryVuMark.LEFT ) ||  vuMark == RelicRecoveryVuMark.UNKNOWN) {
                telemetry.addData("VuMark", "%s visible", vuMark);
                telemetry.update();
                encoderDrive(TURN_SPEED, -15*sign, 15*sign, 5);
            }
            else if (vuMark == RelicRecoveryVuMark.CENTER) {
                telemetry.addData("VuMark", "%s visible", vuMark);
                telemetry.addData("Turn", "14", vuMark);
                telemetry.update();
                encoderDrive(TURN_SPEED, -14*sign, 14*sign, 5);
            }
            else if ((alliance == "Blue" && vuMark == RelicRecoveryVuMark.RIGHT)|| (alliance == "Red" && vuMark == RelicRecoveryVuMark.LEFT)) {
                telemetry.addData("VuMark", "%s visible", vuMark);
                telemetry.update();
                encoderDrive(TURN_SPEED, -13*sign, 13*sign, 5);
            }
            encoderDrive(DRIVE_SPEED, -40, -40, 5);

        }
        // release glyph and lower lift
        clawOffset = robot.OPEN_offset;
        robot.leftClaw.setPosition(robot.MID_SERVO + clawOffset + robot.leftclawcorrection);
        robot.leftClawup.setPosition(robot.MID_SERVO - clawOffset + robot.leftclawupcorrection);
        robot.rightClaw.setPosition(robot.MID_SERVO - clawOffset + robot.rightclawcorrection);
        robot.rightClawup.setPosition(robot.MID_SERVO + clawOffset + robot.rightclawupcorrection);
        runtime.reset();
        while (runtime.seconds() < .4) {    //wait for claw to finsh open or close
        }
        robot.lift.setTargetPosition(50);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setPower(0.3);
        while (robot.lift.isBusy() && (robot.lift.getCurrentPosition() < robot.maxlift)) {}   //wait for lift to stop
        robot.lift.setPower(0.0);


        encoderDrive(DRIVE_SPEED, 2.5, 2.5, 5);    // back up
        if (position == "Front") {
            encoderDrive(TURN_SPEED, -3 * sign, 3 * sign, 1);   // turn to push glyph into cryptobox
        }
        else{
            encoderDrive(TURN_SPEED, 3 * sign, -3 * sign,1);
        }
        encoderDrive(DRIVE_SPEED, 3.5,3.5,1);   // backup away from glyph
        encoderDrive(DRIVE_SPEED, -3.5,-3.5,1);   // drive towards glyph
        encoderDrive(DRIVE_SPEED, 3.5,3.5,1);   // backup away from glyph

    }

    /*========================================================================================================
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.rightDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.leftDrive.setTargetPosition(newLeftTarget);
            robot.rightDrive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftDrive.setPower(Math.abs(speed));
            robot.rightDrive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftDrive.isBusy() || robot.rightDrive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.leftDrive.getCurrentPosition(),
                        robot.rightDrive.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.leftDrive.setPower(0);
            robot.rightDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
}
