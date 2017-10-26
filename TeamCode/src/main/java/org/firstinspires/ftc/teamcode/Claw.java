package org.firstinspires.ftc.teamcode;

/**
 * Created by gstaats on 21/09/17.
 */
//Hello!
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

//Declaration of Servos
public class Claw {
        private Servo leftClawMotor = null;
        private Servo rightClawMotor = null;
        private double leftClawPosition = 0.0;
        private double rightClawPosition = 0.0;

        public Claw( HardwareMap ahwMap )
        {
                //Hardware Map
                rightClawMotor = ahwMap.servo.get( "rightClawMotor" );
                leftClawMotor = ahwMap.servo.get( "leftClawMotor" );

                //Setting direction of Servos
                rightClawMotor.setDirection( Servo.Direction.FORWARD );
                leftClawMotor.setDirection( Servo.Direction.FORWARD );
        }

        //
        //Claw inward motion
        public void claw_Inward(){

                leftClawPosition += 0.1;
                rightClawPosition += -0.1;
                leftClawMotor.setPosition( leftClawPosition );
                rightClawMotor.setPosition( rightClawPosition );
        }

        //Claw outward motion
        public void claw_Outward(){

                leftClawPosition += -0.1;
                rightClawPosition += +0.1;
                leftClawMotor.setPosition( leftClawPosition );
                rightClawMotor.setPosition( rightClawPosition );
        }

}
