/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode.opmodes.old2017;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardwareOld;

@Autonomous(name="Shoot Auto (Shoot Balls)", group="Main")
@Disabled
public class ShootAuto extends OpMode {

    BotHardwareOld robot = new BotHardwareOld();

    AutoLib.Sequence mShoot;

    boolean bDone = false;

    boolean redColor;

    OpMode modePointer;

    // parameters of the PID controller for this sequence's first part
    float Kp = 0.05f;        // degree heading proportional term correction per degree of deviation
    //float Ki = 0.02f;         // ... integrator term
    float Ki = 0.00f;
    float Kd = 0;             // ... derivative term
    float KiCutoff = 3.0f;    // maximum angle error for which we update integrator

    SensorLib.PID gPid = new SensorLib.PID(Kp, Ki, Kd, KiCutoff);

    public ShootAuto(OpMode mode, boolean isRed){
        modePointer = mode;
        redColor = isRed;
    }

    @Override
    public void init(){
        //init hardware objects
        final boolean debug = false;
        robot = new BotHardwareOld();
        robot.init(modePointer, debug, true);
        robot.setMaxSpeedAll(2500);

        mShoot = new AutoLib.LinearSequence();

        mShoot.add(new AutoLib.LogTimeStep(modePointer, "Wait", 15.0));
        mShoot.add(new AutoLib.MoveByEncoderStep(robot.getMotorArray(), 0.4f, 1450, true));
        mShoot.add(new AutoLib.LogTimeStep(modePointer, "YAY", 0.5));
        mShoot.add(new AutoLib.EncoderMotorStep(robot.launcherMotor, 1.0f,  1500, true, modePointer));
        mShoot.add(new AutoLib.TimedServoStep(robot.ballServo, 0.6f, 0.8, false));
        mShoot.add(new AutoLib.EncoderMotorStep(robot.launcherMotor, 1.0f, 1500, true, modePointer));

        int direction;
        if(redColor) direction = -50;
        else direction = 50;

        //mShoot.add(new AutoLib.GyroTurnStep(modePointer, direction, robot.getNavXHeadingSensor(), robot.getMotorArray(), 0.4f, 3.0f, true));
        mShoot.add(new AutoLib.MoveByEncoderStep(robot.getMotorArray(), 0.5f, 2750, true));
    }

    @Override
    public void init_loop(){
        modePointer.telemetry.addData("NavX Connected", robot.navX.isConnected());
        modePointer.telemetry.addData("NavX Ready", robot.startNavX());
    }

    @Override
    public void start(){
        robot.navX.zeroYaw();
    }

    @Override
    public void loop(){
        // until we're done, keep looping through the current Step(s)
        if (!bDone)
            bDone = mShoot.loop();       // returns true when we're done
        else
            modePointer.telemetry.addData("sequence finished", "");
    }

    @Override
    public void stop(){
        super.stop();
    }

}
