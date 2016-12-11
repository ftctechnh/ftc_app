package org.wheelerschool.robotics.competitionbot.SideSpecific;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.wheelerschool.robotics.competitionbot.CompetitionBotAutonomous;
import org.wheelerschool.robotics.library.vision.VuforiaLocation;

/**
 * Created by luciengaitskell on 12/6/16.
 */

@Autonomous
public class CompetitionBotAutonomousBlue extends CompetitionBotAutonomous {
    @Override
    public void runOpMode() throws InterruptedException {
        // Sensor Setup:
        this.INITAL_LOCATION = new VectorF(-56 * VuforiaLocation.MM_PER_INCH, -28 * VuforiaLocation.MM_PER_INCH, 0);
        Log.d("CompBotAutoBlue", this.INITAL_LOCATION.toString());
        this.sideUltrasonicSensor = hardwareMap.ultrasonicSensor.get("leftUltrasonicSensor");
        this.closeMotorGain = 1;
        this.closeMotors = this.leftMotors;
        this.fartherMotorGain = -1;
        this.fartherMotors = this.rightMotors;

        super.runOpMode();
    }
}
