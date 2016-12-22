package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
/**
 * Created by Travis on 11/9/16
 */
@Autonomous(name="Color Tester", group="Test")
public class AutonomousColorTest extends AutonomousBase{
    public void gameState(){
        super.gameState();
          if(getRuntime() % 20 > 10){
              colorLeft.enableLed(false);
              colorRight.enableLed(false);
          }else{
              colorLeft.enableLed(true);
              colorRight.enableLed(true);
          }
//        if(colorLeft1.blue() > colorLeft2.blue()){
//            moveState = MoveState.SERVO_R;
//        }else{
//            moveState = MoveState.SERVO_L;
//        }
    }
}
