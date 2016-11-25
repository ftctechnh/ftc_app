package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean O on 11/23/2016.
 */
@Autonomous(name="Blue Ball", group="Blue")
public class AutoB2 extends AutonomousBase {
    @Override
    public void gameState() {
        super.gameState();
        switch(gameState){
            case 0: //Start
                if(tDiff == 0){tDiff = getRuntime();}
                if(getRuntime() > 5 || !gyro.isCalibrating()) {
                    gameState = 1;
                    sTime = getRuntime();
                }
                break;
            case 1: //moves to shooter post
                map.setGoal(6,8);
                if(linedUp()){
                    moveState = MoveState.FORWARD;
                }else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    gameState = 2;
                }
                break;
            case 2: // turns and shoots
                map.setGoal(4, 10);
                if(linedUp()){
                    moveState = MoveState.STOP;
                    moveState = MoveState.SHOOT;
                    if(getRuntime() - sTime >= 3){
                        moveState = MoveState.SHOOT_STOP;
                        gameState = 3;
                    }
                }else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                break;
            case 3: //MOVE TO KNOCK OFF BALL
                map.setGoal(6.8,6.8);
                if(linedUp()){
                    moveState = MoveState.FORWARD;
                }else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                }
                break;
            case 777:
                moveState = MoveState.STOP;
                break;
        }
    }
}
