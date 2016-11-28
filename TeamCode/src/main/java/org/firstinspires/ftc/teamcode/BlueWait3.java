package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean O on 11/23/2016.
 */
@Autonomous(name="Blue Wait 3", group="Blue")
public class BlueWait3 extends AutonomousBase {
    boolean init;
    @Override
    public void gameState() {
        super.gameState();
        if(!init){
            init = true;
            map.setRobot(3,10.25);
        }
        switch(gameState){
            case 0: //Start
                if(tDiff == 0){tDiff = getRuntime();}
                if(getRuntime() > 15 & !gyro.isCalibrating()) {
                    gameState = 8;
                    sTime = getRuntime();
                }
                break;
            case 8:
                if(getRuntime() - sTime >= .6) {
                    moveState = MoveState.FORWARD;
                    gameState = 1;
                }
                break;
            case 1: //moves to shooter post
                map.setGoal(6, 11);
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
                map.setGoal(5, 10);
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
                moveState = MoveState.SHOOT;
                if(getRuntime() - sTime >= 3) {
                    moveState = MoveState.SHOOT_STOP;
                    gameState = 4;
                }
                break;
            case 4: //MOVE TO KNOCK OFF BALL
                map.setGoal(6.5,6.5);
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
