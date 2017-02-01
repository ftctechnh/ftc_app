package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean O on 11/23/2016.
 */
@Autonomous(name="TestRun6", group="Test")
public class    TestRun6 extends AutonomousBase {
    @Override
    public void gameState() {
        super.gameState();
        switch(gameState){
            case 0: //Start
                if(actualRuntime() > 3 && !gyro.isCalibrating()) {
                    gameState = 1;
                    map.setRobot(6,11.25);
                }
                break;
            case 1: //moves to beacon post
                map.setGoal(10.5, 7);
                if(linedUp()){
                    moveState = MoveState.FORWARD;
                }else{
                    moveState = MoveState.STRAFE_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    //gameState = 2;
                }
                break;
            /*case 2: // HIT BEACON
                map.setGoal(11, 7);
                if(linedUp()){
                    moveState = MoveState.FORWARD;
                }else{
                    moveState = MoveState.STRAFE_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    gameState = 2;
                }
                break;
            case 3: // ... and shoots
                moveState = MoveState.SHOOT_WHEEL;
                if(getRuntime() - sTime >= 2) {
                    moveState = MoveState.SHOOT_CONVEYOR;
                    moveState = MoveState.SERVO_C;
                }
                if(getRuntime() - sTime >= 5) {
                    moveState = MoveState.SHOOT_STOP;
                    gameState = 100;
                    sTime = getRuntime();
                }
                break;
            case 100:
                if (getRuntime() - sTime > 1) {
                    gameState = 4;
                }
                moveState = MoveState.STOP;
                break;
            case 4: //MOVE TO KNOCK OFF BALL
                map.setGoal(7,7);
                if(linedUp()){
                    moveState = MoveState.BACKWARD;
                }else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                }
                break;*/
            case 777:
                moveState = MoveState.STOP;
                break;
        }
    }
}

