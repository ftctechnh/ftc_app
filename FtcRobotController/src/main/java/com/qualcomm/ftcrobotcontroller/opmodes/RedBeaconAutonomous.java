package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by sathk_000 on 11/19/2015.
 */
public class RedBeaconAutonomous extends PacmanBotHardwareBase {
    final static VersionNumber version = new VersionNumber(1,0,0);
    boolean set=false;
    ElapsedTime timer=new ElapsedTime();
    boolean checkRed=false;
    boolean drive=false;
    int red=0;
    int blue=0;
    int iterations=0;

    @Override
    public void init() {
        telemetry.addData("Program","Red Beacon");
        telemetry.addData("Version",version.string());
        telemetry.addData("Hardware Base Version", hwbVersion.string());

        setupHardware();
    }

    @Override
    public void loop(){
        if(!set){
            set=true;
            timer.reset();
        }

        if (timer.time() < 1.15) drive(1,0); //1.15

        else if (timer.time() < 1.40) drive(0,-1); //1.4

        else if (timer.time() < 3.65) drive(1,0); //3.75, 3.5

        else if (timer.time() < 4.0) drive(0,-1); //4.0, 3.75

        else if (timer.time() < 5.4) drive(0.5,0); //5.2, 5.4


        else {
            drive(0,0);
            setThrower(true);
        }

        if(iterations<5) {
            sweeper.setPosition(0.5);
            //eye.enableLed(true);
            red += eye.red();
            blue += eye.blue();
            String sr = String.format("%d", red);
            String sb = String.format("%d", blue);
            telemetry.addData("Red", sr);
            telemetry.addData("Blue", sb);
        }

        else if(iterations==5) {
            if(red<blue) {
                sweeper.setPosition(0.53);

                timer.startTime();
                drive=true;
            }
            else {
                checkRed=true;
                }
            }

        else if(iterations<11 && checkRed) {
            sweeper.setPosition(0.4);
            //eye.enableLed(true);
            red += eye.red();
            blue += eye.blue();
            String sr = String.format("%d", red);
            String sb = String.format("%d", blue);
            telemetry.addData("Red", sr);
            telemetry.addData("Blue", sb);
            }

        else if(iterations==11 && checkRed) {
            if(red<blue) {
                drive=true;
                timer.startTime();
            }
        }

        else if(timer.time()>2 && drive) {
            drive(0.3,0);
        }

        else if(timer.time()>3.5 && timer.time()<4 && drive){
            drive(0,0);
        }

        else {
            sweeper.setPosition(0.6);
            thrower.setPosition(0);
        }

//      if (red1 - blue1 > 0.25) {
//          sweeper.setPosition(0.45);
//      }
//      else{
//          sweeper.setPosition(0.53);
//      }
//
//      timer.startTime();
//      timer.startTime();
//      done=true;
        /*else if(timer.time() > 4.0)
        {
            drive(0,0);
        }
        else if(timer.time()>2.0)
        {
            drive(0.3,0);
        }*/

    iterations++;
    }
}
