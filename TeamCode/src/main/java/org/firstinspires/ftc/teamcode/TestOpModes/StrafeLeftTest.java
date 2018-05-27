package org.firstinspires.ftc.teamcode.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.DriveTrain;

/**
 * Created by FTC 4316 on 3/17/2018.
 */

public class StrafeLeftTest extends OpMode {

    private Robot robot;

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftRear;
    DcMotor rightRear;

    private boolean isFinished;

    public void init(){
        leftFront = hardwareMap.dcMotor.get("leftfront");
        rightFront = hardwareMap.dcMotor.get("rightfront");
        leftRear = hardwareMap.dcMotor.get("leftrear");
        rightRear = hardwareMap.dcMotor.get("rightrear");

        robot = new Robot(hardwareMap, telemetry, false);

        isFinished = false;
    }
    public void loop() {
        if(!isFinished){
            if(robot.driveTrain.encoderDrive(DriveTrain.Direction.W, 0.5, 5)){
                isFinished = true;
            }
        }
    }
}
