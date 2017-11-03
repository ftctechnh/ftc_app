package org.firstinspires.ftc.teamcode.autonomii;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robotplus.hardware.ColorSensorWrapper;
import org.firstinspires.ftc.teamcode.robotplus.hardware.Drivetrain;
import org.firstinspires.ftc.teamcode.robotplus.hardware.IMUWrapper;
import org.firstinspires.ftc.teamcode.robotplus.hardware.MecanumDrive;
import org.firstinspires.ftc.teamcode.robotplus.hardware.Robot;

/**
 * Created by BAbel on 11/2/2017.
 */

@Autonomous (name = "Blake tryina code", group = "Competition OpModes")
public class ThiccAuto extends LinearOpMode {

    private Robot robot;
    private DcMotor raiser;
    private Servo grabber;
    private IMUWrapper imuWrapper;
    private MecanumDrive drivetrain;

    private Servo colorRod;
    private ColorSensorWrapper colorSensorWrapper;

    @Override
    public void runOpMode(){

        robot = new Robot(hardwareMap);
        drivetrain = (MecanumDrive)robot.getDrivetrain();
        raiser = hardwareMap.dcMotor.get("raiser");
        grabber = hardwareMap.servo.get("grabber");
        imuWrapper = new IMUWrapper(hardwareMap);

        colorRod = hardwareMap.servo.get("rod");
        colorRod.setPosition(0); // up position

        colorSensorWrapper = new ColorSensorWrapper(hardwareMap);

        //Hitting the ball
        colorRod.setPosition(0.5); //out position

        //Checks that blue jewel is closer towards the cryptoboxes
        if (colorSensorWrapper.getRGBValues()[2] > colorSensorWrapper.getRGBValues()[0]){
            drivetrain.complexDrive(MecanumDrive.Direction.UP.angle(), 0.2, 0);
        }

    }
}
