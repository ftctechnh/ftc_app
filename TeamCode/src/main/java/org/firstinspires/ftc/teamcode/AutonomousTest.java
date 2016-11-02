package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
/**
 * Created by USER on 10/26/2016.
 */
@Autonomous(name="Autonomous Tester", group="Test")
public class AutonomousTest extends AutonomousBase{
    public void gameState() {
        super.gameState();

        switch(gameState){
            case 0:
              map.setGoal(6,9);
              linedUp(MoveState.FORWARD,MoveState.TURN_TOWARDS_GOAL);
              break;
        }
    }
}
