package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware9330;
import org.firstinspires.ftc.teamcode.subsystems.JewelArm9330;
import org.firstinspires.ftc.teamcode.subsystems.Clamps9330;
import org.firstinspires.ftc.teamcode.subsystems.RelicPickup9330;

/**
 * Created by robot on 10/30/2017.
 */

@Autonomous(name="ServoTestRun", group = "Opmode")
public class ServoTestRun extends LinearOpMode {
    Hardware9330 robotMap = new Hardware9330();
    JewelArm9330 BoopJR = new JewelArm9330(robotMap);
    Clamps9330 clamps = new Clamps9330(robotMap);
    RelicPickup9330 relicPickup = new RelicPickup9330(robotMap);


    @Override
    public void runOpMode() throws InterruptedException {
        robotMap.init(hardwareMap);
        while (robotMap.touch.getState() && opModeIsActive()) {
            BoopJR.toggleArmServo();
            clamps.toggleHighClamp();
            clamps.toggleLowClamp();
            relicPickup.toggleHand();
            relicPickup.toggleWrist();
            sleep(1000);


        }
    }
}