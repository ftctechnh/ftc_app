package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Sean Ovens on 10/26/2016.
 */
@Autonomous(name="Blue 6", group="Blue")
public class AutoB1 extends AutonomousBase{
    @Override
    public void gameState() {
        super.gameState();
        double sTime = 0;
        switch(gameState){
            case 0: //Start
                if(tDiff == 0){tDiff = getRuntime();}
                // todo                  REMOVE?
                if(getRuntime() > 5 || !gyro.isCalibrating()) {
                    gameState = 1;
                    sTime = getRuntime();
                }
                break;
            case 1: //Shoot
                moveState = MoveState.SHOOT;
                if(getRuntime() - sTime >= 3){gameState = 2;}
                break;
            case 2: //Move to beacon A push pos.
                map.setGoal(11,6);
                linedUp(1,2);
                if(map.distanceToGoal()<=.1){
                    moveState = MoveState.STOP;
                    gameState = 3;
                }
                break;
            case 3: //Move pear. to wall
                map.setGoal(11,0);
                linedUp(MoveState.STOP, MoveState.TURN_TOWARDS_GOAL);
                gameState = 4;
                break;
            case 4: //Move to wall and back up and button press A
                map.setGoal(12,6);
                moveState = MoveState.RIGHT;
                if(map.distanceToGoal()<=.1) {moveState = MoveState.STOP;}
                while(!touchRight.isPressed()){moveState = MoveState.BACKWARD;}
                moveState = MoveState.STOP;
                if(true) { //// TODO: 10/26/2016 ADD COLOR LOGIC
                    moveState = MoveState.SERVO_PORT_L;
                }
                else{
                    moveState = MoveState.SERVO_PORT_R;
                }
                gameState = 5;
                break;
            case 5: //move up and button press B
                while(!touchLeft.isPressed()){moveState = MoveState.FORWARD;}
                moveState = MoveState.STOP;
                if(true) { //// TODO: 10/26/2016 ADD COLOR LOGIC
                    moveState = MoveState.SERVO_STARBOARD_L;
                }
                else{
                    moveState = MoveState.SERVO_STARBOARD_R;
                }
                gameState = 6;
                break;
            case 6: //Moves to tHe center and knocks off cap ball
                map.setGoal(6.8,5.5);
                linedUp(1,2);
                if(map.distanceToGoal()<=.1){moveState = MoveState.STOP;}
                break;
            case 777:
                moveState = 0;
                break;
        }
    }
}
