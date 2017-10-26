package org.firstinspires.ftc.teamcode;

/**
 * Created by gstaats on 21/09/17.
 */
//Hello!
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap

//Declaration of Servos
public class claw {
private Servo leftClawMotor = null;
private Servo rightClawMotor = null;

        public claw( HardwareMap ahwMap )
        {
                //Hardware Map
                rightClawMotor = ahwMap.servo.get( "rightClawMotor" );
                leftClawMotor = ahwMap.servo.get( "leftClawMotor" );

                //Setting direction of Servos
                rightClawMotor.setDirection( Servo.Direction.FORWARD );
                leftClawMotor.setDirection( Servo.Direction.FORWARD );

                //Setting power to 0
                rightClawMotor.setPower( 0 );
                leftClawMotor.setPower( 0 );
        }

//
//Claw inward motion
public void claw_Inward(){
        leftClawMotor.setPower(1);
        rightClawMotor.setPower(-1);
        }
//Claw outward motion
public void claw_Outward(){
        leftClawMotor.setPower(-1);
        rightClawMotor.setPower(1);

        }

        }
