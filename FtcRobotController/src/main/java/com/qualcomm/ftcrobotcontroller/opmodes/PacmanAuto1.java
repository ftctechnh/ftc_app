package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PacmanAuto1 extends PacmanBotHardwareBase {
    final static VersionNumber version = new VersionNumber(1,0,0);
    boolean done=false;
    ElapsedTime timer=new ElapsedTime();
    double timeDiffs[];

    @Override
    public void init() {
        telemetry.addData("Program", "Pacman Auto");
        telemetry.addData("Version", version.string());
        telemetry.addData("Hardware Base Version", hwbVersion.string());
        setupHardware();
        timeDiffs = new double[3];//amount of time distances
        timeDiffs[0]=2;//time operating 1st instruction
        timeDiffs[1]=1;//time operating 2nd instruction
        timeDiffs[2]=2;//time operating 3rd instruction
        int x=0;
        for(int i=0;i<2;i++){
            x+=timeDiffs[i];
            timeDiffs[i]=x;
        }
        //setSweeperPosition(0);
    }

    @Override
    public void loop() {
        //Drive the robot
        /*double drive_rate = 100;
        double turn_rate  = 0;*/

        if(!done)
        {
            done=true;
            timer.startTime();
        }
        if(timer.time()<timeDiffs[0])//1st instuction
        {
            drive(1,0);
        }
        else if(timer.time()<timeDiffs[1])//2nd instruction
        {
            drive(0,1);
        }
        else if(timer.time()<timeDiffs[2])//3rd instruction
        {
            drive(1,0);
        }
        else
        {
            drive(0,0);
        }
    }
}

