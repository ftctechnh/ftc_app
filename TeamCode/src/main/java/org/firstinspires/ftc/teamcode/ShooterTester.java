package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean Ovens on 10/26/2016.
 */
@Autonomous(name="Shooter Tester", group="Test")
public class ShooterTester extends AutonomousBase{
    private double cumDist;
    @Override
    public void gameState() {
        super.gameState();
        switch(gameState){
            case 0: //Start
                if(getRuntime() > 5 || !gyro.isCalibrating()) {
                    gameState = 1;
                    sTime = getRuntime();
                }
                break;
            case 1:
                cumDist += dDistW;
                moveState = MoveState.SHOOT_WHEEL;
                if(sTime > 1){
                    moveState = MoveState.SHOOT;
                }
                telemetry.addData("Wheel Speed", cumDist/(getRuntime()-sTime)/1440);
                break;
        }
   }
}
