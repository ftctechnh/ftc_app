package org.firstinspires.ftc.teamcode.AutonomousEndpoints.MultiGlyph;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.AutonomousEndpoints.SoloGlyph.CompBlueFront;

/**
 * Created by guberti on 12/2/2017.
 */
@Autonomous(name="Multi-glyph FRONT BLUE autonomous", group="A_MultiGlyph")
public class MultiBlueFront extends CompBlueFront {

    @Override
    public void runOpMode() {
        placeSecondGlyph = true;

        super.runOpMode();

    }
}