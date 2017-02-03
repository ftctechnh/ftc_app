package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean O on 11/23/2016.
 */
@Autonomous(name="Blue Corner 6", group="Blue")
public class BlueCorner6 extends AutonomousBase {
    @Override
    public void gameState() {
        super.gameState();
        int i = 0;
        switch(gameState){
            case 0: //Start
                if(actualRuntime() > 3 && !gyro.isCalibrating()) {
                    gameState = 1;
                    map.setRobot(6,11.25);
                }
                break;
            case 1: //moves to shooter post
                map.setGoal(5, 9);
                moveState = MoveState.STRAFE_TOWARDS_GOAL;
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    gameState = 2;
                }
                break;
            case 2: // turns ...
                desiredAngle = 220;
                if(linedUpAngle(5)){
                    moveState = MoveState.STOP;
                    gameState = 3;
                    sTime = getRuntime();
                }else{
                    moveState = MoveState.TURN_TOWARDS_ANGLE_SLOW;
                }
                break;
            case 3: // ... and shoots
                if(i < 5){
                    if(!linedUpAngle(5)) {
                        gameState = 2;
                        i++;
                    }
                }
                 moveState = MoveState.SHOOT_WHEEL;
                 if(getRuntime() - sTime >= 3){
                     moveState = MoveState.SHOOT_CONVEYOR;
                 }
                 if(getRuntime() - sTime >= 6) {
                     moveState = MoveState.SHOOT_STOP;
                     gameState = 4;
                 }
                break;
            case 4: // Knock off cap ball and park
                map.setGoal(9,9);
                moveState = MoveState.STRAFE_TOWARDS_GOAL;
                if(map.distanceToGoal()<=.1){
                    gameState = 5;
                }
                break;
            case 5: // Knock off cap ball and park
                map.setGoal(11,11);
                moveState = MoveState.STRAFE_TOWARDS_GOAL;
                if(map.distanceToGoal()<=.1){
                    gameState = 777;
                }
                break;
            case 777:
                moveState = MoveState.FULL_STOP;
                break;
        }
    }
}
