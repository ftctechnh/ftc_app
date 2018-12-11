package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="MallahBullRun", group="MonsieurMallah")
public class MallahBullRun extends BullRun {
    public MallahBullRun() {
        super(ChassisConfig.forMonsieurMallah());
    }
}