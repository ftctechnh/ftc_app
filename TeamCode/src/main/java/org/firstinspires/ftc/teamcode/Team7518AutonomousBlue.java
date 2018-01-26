package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * Created by Ryan Gniadek and Ben Bernstein
 */

@Autonomous(name="7518AutonomousBlue", group="7518")

public class Team7518AutonomousBlue extends LinearOpMode {

    /* Declare OpMode members. */
    private CRServo testServo;
    private Servo arm;
    private DcMotor motorLift, leftFront, rightFront, leftRear, rightRear;
    private ColorSensor colorSensor;
    private ElapsedTime     runtime = new ElapsedTime();

    private Servo colorSensorServo;
    DigitalChannel upperLimitSwitch;
    DigitalChannel lowerLimitSwitch;
    DeviceInterfaceModule cdi;

    double speed = 0.2;


    @Override
    public void runOpMode() {

        //Declare hardwareMap here,
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");
        colorSensor = hardwareMap.colorSensor.get("colorSensor");
        arm = hardwareMap.servo.get("colorSensorServo");
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        //Insert Code Below

        ElapsedTime timer = new ElapsedTime();
        int count = 0;

        runtime.reset();
        while(opModeIsActive() && runtime.seconds()<10){
            count++;
            colorSensor.enableLed(true);

            arm.setPosition(.225);
            int rColor = colorSensor.red();
            int bColor = colorSensor.blue();
            telemetry.addData("BValue", bColor);
            telemetry.addData("RValue", rColor);
            telemetry.update();

            if (count==1)
                timer.reset();
            while(opModeIsActive() && bColor>0){
                sleep(500);
                while(opModeIsActive() && timer.seconds()<1.5){
                    leftFront.setPower(speed);
                    rightFront.setPower(-speed);
                    leftRear.setPower(speed);
                    rightRear.setPower(-speed);
                }//end while
                arm.setPosition(1);
                leftFront.setPower(0);
                rightFront.setPower(0);
                leftRear.setPower(0);
                rightRear.setPower(0);

            }//end while
            while(opModeIsActive() && rColor>0) {
                sleep(500);
                while(opModeIsActive() && timer.seconds()<1.5){
                    leftFront.setPower(-speed);
                    rightFront.setPower(speed);
                    leftRear.setPower(-speed);
                    rightRear.setPower(speed);
                }//end while
                arm.setPosition(1);
                leftFront.setPower(0);
                rightFront.setPower(0);
                leftRear.setPower(0);
                rightRear.setPower(0);
            }//end while
        }//end while loop


//        Example Code from First
//        robot.leftDrive.setPower(FORWARD_SPEED);
//        robot.rightDrive.setPower(FORWARD_SPEED);
//        runtime.reset();
//        while (opModeIsActive() && (runtime.seconds() < 3.0)) {
//            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
//            telemetry.update();
//        }

        sleep(1000);
    }
}
