package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import org.firstinspires.ftc.teamcode.HardwareBruinBot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
@Autonomous (name = "AutoMineral", group = "Rohan")
public class AutoMineral extends LinearOpMode {
    HardwareBruinBot hwMap = new HardwareBruinBot();
    private ElapsedTime     runtime = new ElapsedTime();
    public void runOpMode () {
        double rotate;
        hwMap.init(hardwareMap);
        while (GoldAlignExample.found()) {
            while (runtime.seconds() < 0.5)
                hwMap.leftFrontDrive.setPower(0.05); //= drive + strafe + rotate;
            hwMap.leftRearDrive.setPower(0.05); //= drive - strafe + rotate;
            hwMap.rightFrontDrive.setPower(0.05); //= drive - strafe - rotate;
            hwMap.rightRearDrive.setPower(0.05); //= drive + strafe - rotate;
            sleep(1000);
            while (!GoldAlignDetector.class.isEnum ()) {
                rotate = Math.sin(GoldAlignExample.getX());
                hwMap.leftFrontDrive.setPower(0.1 + rotate); //= drive + strafe + rotate;
                hwMap.leftRearDrive.setPower(0.1 + rotate); //= drive - strafe + rotate;
                hwMap.rightFrontDrive.setPower(0.1 - rotate); //= drive - strafe - rotate;
                hwMap.rightRearDrive.setPower(0.1 - rotate); //= drive + strafe - rotate;
                hwMap.leftFrontDrive.setPower(0);
                hwMap.leftRearDrive.setPower(0);
                hwMap.rightFrontDrive.setPower(0);
                hwMap.rightRearDrive.setPower(0);
            }
        }
        while (runtime.seconds() < 0.2) {
            hwMap.leftFrontDrive.setPower(0.1); //= drive + strafe + rotate;
            hwMap.leftRearDrive.setPower(0.1); //= drive - strafe + rotate;
            hwMap.rightFrontDrive.setPower(0.1); //= drive - strafe - rotate;
            hwMap.rightRearDrive.setPower(0.1); //= drive + strafe - rotate;
        }

    }
}
