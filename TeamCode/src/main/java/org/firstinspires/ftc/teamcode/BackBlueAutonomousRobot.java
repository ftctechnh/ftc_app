package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class BackBlueAutonomousRobot extends AutonomousRobot {
    @Override
    public void start() {
        rotationOrigin += -90.0;
        rotation = -90.0;
        targetRotation = -90.0;

        //xPosition = LX_STARTING_POSITION;
        //yPosition = BY_STARTING_POSITION;
        targetXPosition = xPosition;
        targetYPosition = yPosition;

        super.start();
    }

    @Override
    public void loop() {


        super.loop();
    }

    @Override
    public void stop() {
        rotationOrigin += 90.0;

        super.stop();
    }
}