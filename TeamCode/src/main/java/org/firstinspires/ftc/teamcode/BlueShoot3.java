package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean O on 11/23/2016.
 */
@Autonomous(name="Blue Shoot 3", group="Blue")
public class BlueShoot3 extends AutonomousBase {
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
                if(getRuntime() > 5 || !gyro.isCalibrating()) {
                    gameState = 1;
                }
                break;
            case 1: //moves to shooter post
                map.setGoal(6, 8);
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
                map.setGoal(6.6, 10 );
                if(linedUp()){
                    moveState = MoveState.STOP;
                    gameState = 3;
                    sTime = getRuntime();
                    telemetry.addData("sTime", sTime);
                }else{
                    moveState = MoveState.TURN_TOWARDS_GOAL;
                }
                break;
            case 3:
                moveState = MoveState.SHOOT;
                if(getRuntime() - sTime >= 3) {
                    moveState = MoveState.SHOOT_STOP;
                    gameState = 4;
                }
                break;
            case 777:
                moveState = MoveState.STOP;
                break;
        }
    }
}
