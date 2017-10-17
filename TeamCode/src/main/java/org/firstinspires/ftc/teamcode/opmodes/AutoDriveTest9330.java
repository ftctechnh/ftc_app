package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 10/16/2017.
 */
@Autonomous(name="LeftAuto9330", group = "Opmode")
public class AutoDriveTest9330 extends LinearOpMode{
        Hardware9330 robotMap = new Hardware9330();
    //Diameter = 3.78
    //Circumference = Pi * 3.78

    //Any comments regarding leftMotor can be uncommented when we hook up a second motor

        @Override
        public void runOpMode() throws InterruptedException {
            robotMap.init(hardwareMap);
                //Resetting Encoders
            //robotMap.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robotMap.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                //Setting the mode of the motor
            //robotMap.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robotMap.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //Goes forward
            telemetry.addData("Program", "Driving Forward");
        }

        public void encoderDrive

}

