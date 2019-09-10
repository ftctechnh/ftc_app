package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.ArrayList;

@TeleOp (name = "TeleOp")
public class Team7593TeleOp extends Team7593OpMode {

    //Declare Variables
    public ElapsedTime time = new ElapsedTime(); //a timer

    public int currEncoderVal;  //encoder values for tilt motor
    public int oldEncoderVal;

    public int cEncoderVal;  //encoder values for ext motor
    public int oEncoderVal;

    public int goldVal;
    public int silverVal;

    Orientation angles; //to use the imu (mostly for telemetry)

    //double extensionPosition = robot.EXTENSION_HOME;
    final double EXTENSION_SPEED = 0.08; //sets rate to move servo

    @Override
    //code block to that will run at the VERY beginning of Teleop
    public ArrayList<AutonStep> createAutonSteps() {
        return null;
    }

    @Override
    public void init(){
        super.init();

        //stop the motor(s) and reset the motor encoders to 0


        telemetry.addData("Say", "HELLO FROM THE OTHER SIIIIIDE");
        time.startTime();
    }

    public void loop() {

        //use super's loop so that auton steps can run
        super.loop();

        //get the current encoder value of tilt

        double leftX, rightX, leftY, hangStick, tiltStick, tiltPower, slowTilt; //declaration for the game sticks + power
        boolean spinIn, spinOut, slowDrive, goldExt, silverExt; //declaration for the buttons/bumpers
        WheelSpeeds speeds; //variable to hold speeds

        leftX = gamepad1.left_stick_x;
        rightX = gamepad1.right_stick_x;
        leftY = gamepad1.left_stick_y;
        slowDrive = gamepad1.left_bumper;

        hangStick = gamepad2.left_stick_y;
        tiltStick = gamepad2.right_stick_y;
        slowTilt = gamepad2.right_trigger;
        goldExt = gamepad2.y; //2000
        silverExt = gamepad2.x; //1500
        spinOut = gamepad2.right_bumper;
        spinIn = gamepad2.left_bumper;
        tiltPower = .6;

        goldVal = 2500 - cEncoderVal;
        silverVal = 2000 - cEncoderVal;


        //get the speeds
        if(slowDrive){
            speeds = WheelSpeeds.mecanumDrive(leftX, leftY, rightX, true);
        }else{
            speeds = WheelSpeeds.mecanumDrive(leftX, leftY, rightX, false);
        }

        //power the motors
        robot.powerTheWheels(speeds);

        //power the hang motor


        //slow the tilt motor
        if(slowTilt > 0){
            tiltPower = tiltPower/4.5;
        }




        //code to spin the small motor



        //use the imu
        angles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);


        /*
        WHOA TELEMETRY
        */
        telemetry.addLine().addData("slow mode: ", slowDrive);


        //angles on each of the axes
        telemetry.addLine().addData("IMU angle Y:", angles.secondAngle);
        telemetry.addData("IMU angle Z:", angles.firstAngle);
        telemetry.addData("IMU angle X:", angles.thirdAngle);

        //angles it's at
        telemetry.addLine();
        telemetry.addData("Current Angle: ", robot.getCurrentAngle());
        telemetry.addData("Init Angle: ", robot.initAngle);

        //gold pos should be either 1,2,3
        telemetry.addData("Gold Mineral Position:", this.getSharedInfo("Gold"));
    }
}