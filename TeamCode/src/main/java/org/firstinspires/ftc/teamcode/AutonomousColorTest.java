package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
/**
 * Created by Travis on 11/9/16
 */
@Autonomous(name="Color Tester", group="Test")
public class AutonomousColorTest extends AutonomousBase{
    public void gameState(){
        super.gameState();
        switch(gameState){
            case 0:
                moveState = MoveState.BACKWARD_SLOW;
                if(colorLeft.alpha() > 2 && colorRight.alpha() > 2){
                    moveState = MoveState.STOP;
                    gameState = 1;
                }
            break;
            case 1: 
                if(colorLeft.red() > colorLeft.blue() && colorRight.red() < colorRight.blue()){
                    telemetry.addData("Right color:", "Blue");
                    telemetry.addData("Left color:", "Red");
                }else{
                    telemetry.addData("Right color:", "Red");
                    telemetry.addData("Left color:", "Blue");
                }
            break;
        }
    }
}
