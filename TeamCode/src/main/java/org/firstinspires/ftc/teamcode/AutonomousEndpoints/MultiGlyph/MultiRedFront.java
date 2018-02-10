package org.firstinspires.ftc.teamcode.AutonomousEndpoints.MultiGlyph;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.AutonomousEndpoints.SoloGlyph.CompRedFront;

/**
 * Created by guberti on 12/2/2017.
 */
@Autonomous(name="Multi-glyph FRONT RED autonomous", group="A_MultiGlyph")
public class MultiRedFront extends CompRedFront {

    @Override
    public void runOpMode() {
        placeSecondGlyph = true;

        super.runOpMode();

    }
}