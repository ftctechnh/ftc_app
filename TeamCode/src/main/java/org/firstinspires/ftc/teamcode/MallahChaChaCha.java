package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="MallahChaChaCHa", group="MonsieurMallah")
public class MallahChaChaCha extends BullRunChaChaCha {
    public MallahChaChaCha() {
        super(ChassisConfig.forMonsieurMallah());
    }
}