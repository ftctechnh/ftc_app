package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean O on 11/23/2016.
 */
@Autonomous(name="Red Wait 6", group="Red")
public class RedWait6 extends AutonomousBase {
    @Override
    public void gameState() {
        super.gameState();
        switch(gameState){
            case 0: //Start
                if(actualRuntime() > 15 && !gyro.isCalibrating()) {
                    gameState = 1;
                    map.setRobot(6,11.25);
                }
                break;
            case 1: //moves to shooter post
                map.setGoal(8, 10);
                moveState = MoveState.STRAFE_TOWARDS_GOAL;
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    gameState = 2;
                }
                break;
            case 2: // turns ...
                desiredAngle = 135;
                if(linedUpAngle(5)){
                    moveState = MoveState.STOP;
                    gameState = 3;
                    sTime = getRuntime();
                }else{
                    moveState = MoveState.TURN_TOWARDS_ANGLE_SLOW;
                }
                break;
            case 3: // ... and shoots
                if(!linedUpAngle(5)){
                    gameState = 2;
                }
                moveState = MoveState.SHOOT_WHEEL;
                if(getRuntime() - sTime >= 2){
                    moveState = MoveState.SHOOT_CONVEYOR;
                }
                if(getRuntime() - sTime >= 5) {
                    moveState = MoveState.SHOOT_STOP;
                    gameState = 4;
                }
                break;
            case 4: // Line up with cap ball
                map.setGoal(5,6);
                moveState = MoveState.STRAFE_TOWARDS_GOAL;
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    gameState = 777;
                }
                break;
            case 777:
                moveState = MoveState.FULL_STOP;
                break;
        } 
    }
}
