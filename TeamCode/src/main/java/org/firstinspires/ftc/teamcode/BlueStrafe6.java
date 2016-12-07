package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean O on 11/23/2016.
 */
@Autonomous(name="Blue Strafe 6", group="Blue")
public class BlueStrafe6 extends AutonomousBase {
    @Override
    public void gameState() {
        super.gameState();
        switch(gameState){
            case 0: //Start
                if(tDiff == 0){
                    tDiff = getRuntime();
                    map.setRobot(6,11.25);
                }
                if(getRuntime() > 5 || !gyro.isCalibrating()) {
                    gameState = 1;
                }
                break;
            case 1: //move forward a bit
                map.setGoal(6, 10);
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
            case 2: // turns to shooting angle
                desiredAngle = 195;
                if(linedUpAngle()){
                    moveState = MoveState.STOP;
                    gameState = 100;
                }else{
                    moveState = MoveState.TURN_TOWARDS_ANGLE;
                }
                break;
            case 100:
                map.setGoal(5.8,8.8);
                moveState = MoveState.STRAFE_TOWARDS_GOAL;
                if(map.distanceToGoal() <= .1){
                    moveState = MoveState.STOP;
                    gameState = 101;
                    sTime = getRuntime();
                    telemetry.addData("sTime", sTime);
                }
                break;
            case 101: // turns to shooting angle
                desiredAngle = 195;
                if(linedUpAngle()){
                    moveState = MoveState.STOP;
                    gameState = 3;
                }else{
                    moveState = MoveState.TURN_TOWARDS_ANGLE;
                }
                break;
            case 3: // ... and shoots
                 moveState = MoveState.SHOOT_WHEEL;
                 if(getRuntime() - sTime >= 1){
                     moveState = MoveState.SHOOT_CONVEYOR;
                 }
                 if(getRuntime() - sTime >= 3) {
                     moveState = MoveState.SHOOT_STOP;
                     gameState = 4;
                 }
                break;
            case 4: //MOVE TO KNOCK OFF BALL
                map.setGoal(6.5,7.5);
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
