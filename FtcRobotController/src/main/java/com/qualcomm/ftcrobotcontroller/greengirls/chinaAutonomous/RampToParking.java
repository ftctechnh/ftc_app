package com.greengirls.chinaAutonomous;

import com.greengirls.RobotHardwareChina;

import java.util.concurrent.TimeUnit;


/**
 * Created by Dell User on 10/15/2015.
 */
public class RampToParking extends RobotHardwareChina {
    
    //set state to zero
    int state = 0;

    @Override
    public void loop() {

        switch (state){
            case 0:
                //start facing backward
                runWithEncoders();
                //
                setRightMotors(-1);
                setLeftMotors(-1);

                //
                if(encoderCountReached(160)){

                    resetEncoders();

                    setRightMotors(0);
                    setLeftMotors(0);

                    state++;
                }
                break;
            case 1:
                runWithEncoders();
                setRightMotors(-1);
                setLeftMotors(1);

                if(encoderCountReached(160)){

                    resetEncoders();

                    setRightMotors(0);
                    setLeftMotors(0);

                    state++;
                }
                break;

            case 2:
                runWithEncoders();
                setRightMotors(1);
                setLeftMotors(1);

                if(encoderCountReached(160)){

                    resetEncoders();

                    setRightMotors(0);
                    setLeftMotors(0);

                    state++;
                }
                break;
            case 3:
                closeDinoArms();
                openDeflector();
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                }
                catch (InterruptedException e){

                }

                shootBalls();
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                }
                catch (InterruptedException e){

                }
                stopShootBalls();
                state++;
                break;
            case 4:
                runWithEncoders();
                setRightMotors(-1);
                setLeftMotors(1);

                if(encoderCountReached(160)){

                    resetEncoders();

                    setRightMotors(0);
                    setLeftMotors(0);

                    state++;
                }
                break;
            case 5:
                runWithEncoders();
                setRightMotors(1);
                setLeftMotors(1);

                if(encoderCountReached(160)){

                    resetEncoders();

                    setRightMotors(0);
                    setLeftMotors(0);

                    state++;
                }
                break;
            case 6:
                runWithEncoders();
                setRightMotors(-1);
                setLeftMotors(1);

                if(encoderCountReached(160)){

                    resetEncoders();

                    setRightMotors(0);
                    setLeftMotors(0);

                    state++;
                }
                break;
        }
    }
}
