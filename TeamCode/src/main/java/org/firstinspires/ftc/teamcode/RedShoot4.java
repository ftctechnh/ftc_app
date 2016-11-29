package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean O on 11/23/2016.
 */
@Autonomous(name="Red Shoot 4", group="Red")
public class RedShoot4 extends AutonomousBase {
    @Override
    public void gameState() {
        super.gameState();
        switch(gameState){
            case 0: //Start
                if(tDiff == 0){tDiff = getRuntime();}
                if(getRuntime() > 5 || !gyro.isCalibrating()) {
                    gameState = 1;
                }
                break;
            case 1: //moves to shooter post
                map.setGoal(4, 8);
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
            case 2: // turns ...
                map.setGoal(2, 10);
                if(linedUp()){
                    moveState = MoveState.STOP;
                    gameState = 3;
                    sTime = getRuntime();
                    telemetry.addData("sTime", sTime);
                }else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                break;
            case 3: // ... and shoots
                 moveState = MoveState.SHOOT_WHEEL;
                 if(getRuntime() - sTime >= 1){
                     moveState = MoveState.SHOOT_CONVEYER;
                 }
                 if(getRuntime() - sTime >= 3) {
                     moveState = MoveState.SHOOT_STOP;
                     gameState = 4;
                 }
                break;
            case 4: //MOVE TO LINE UP TO KNOCK OFF BALL
                map.setGoal(8,10);
                if(linedUp()){
                    moveState = MoveState.FORWARD;
                }else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    gameState = 5;
                }
            case 5: //MOVE TO ACTUALLY KNOCK OFF BALL
                map.setGoal(5,6);
                if(linedUp()){
                    moveState = MoveState.FORWARD;
                }else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                }
        }
    }
}
