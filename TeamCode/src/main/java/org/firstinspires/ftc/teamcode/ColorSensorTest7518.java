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

@Autonomous(name="7518ColorTest", group="7518")

public class ColorSensorTest7518 extends LinearOpMode {

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

        while(opModeIsActive()){
            telemetry.addData("Red", colorSensor.red());
            telemetry.addData("Blue", colorSensor.blue());
            telemetry.update();
        }


    }
}
