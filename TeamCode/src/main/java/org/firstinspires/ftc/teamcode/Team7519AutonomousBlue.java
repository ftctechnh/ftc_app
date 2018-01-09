package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * Created by Ryan Gniadek and Ben Bernstein
 */

@Autonomous(name="7519Autonomous", group="7519")
@Disabled
public class Team7519AutonomousBlue extends LinearOpMode {

    /* Declare OpMode members. */
    private CRServo testServo;
    private DcMotor motorLift, leftFront, rightFront, leftRear, rightRear;
    private ColorSensor colorSensor;
    private ElapsedTime     runtime = new ElapsedTime();


    double speed = 0.5;


    @Override
    public void runOpMode() {

        //Declare hardwareMap here,
        motorLift = hardwareMap.dcMotor.get("motorLift");
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");
        testServo = hardwareMap.crservo.get("testServo");
        colorSensor = hardwareMap.colorSensor.get("colorSensor");

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        //Insert Code Below

        int bColor = colorSensor.blue();
        int rColor = colorSensor.red();
        boolean color = false;


        //in event of searching for blue

        runtime.reset();
        while(opModeIsActive() && runtime.seconds()<3){
            if(bColor > rColor)
                color = true;
            if(color){//Replace true with Team Colored Jewel
                leftFront.setPower(speed);
                rightFront.setPower(speed);
                leftRear.setPower(speed);
                rightRear.setPower(speed);
            }//end if
            else{
                leftFront.setPower(-speed);
                rightFront.setPower(-speed);
                leftRear.setPower(-speed);
                rightRear.setPower(-speed);
            }
            telemetry.addData("Status", "Displacing Team Colored Jewel", runtime.seconds());
        }//end while loop


//        Example Code from First
//        robot.leftDrive.setPower(FORWARD_SPEED);
//        robot.rightDrive.setPower(FORWARD_SPEED);
//        runtime.reset();
//        while (opModeIsActive() && (runtime.seconds() < 3.0)) {
//            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
//            telemetry.update();
//        }



        telemetry.addData("Status", "Complete");
        telemetry.update();
        sleep(1000);
    }
}
