/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Autonomus.Restul;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HardwareRobot;

@Autonomous(name="Functions Iua", group="TEST")
@Disabled
public class Autonomus_Functions_Iua extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime     runtime = new ElapsedTime();
    public static int MIN_RED = 0;
    public static int MIN_BLUE = 0;


    HardwareRobot robot = new HardwareRobot();

    @Override
    public void runOpMode() {

        telemetry.addData("ABC",1);
        robot.init(hardwareMap);
        telemetry.update();

        boolean bLedOn = true;

        //robot.colorSensor = hardwareMap.get(ColorSensor.class, "sensor_color");
        //robot.colorSensor.enableLed(bLedOn);
        telemetry.addData("ABC",1);
        telemetry.update();
        waitForStart();
        while(opModeIsActive())
        {
            telemetry.addData("RED", robot.colorSensor.red() * 8);
            telemetry.addData("BLUE", robot.colorSensor.blue() * 8);
            telemetry.addData("ANSWER_R:",get_color(1));
            telemetry.addData("ANSWER_B:",get_color(0));


        telemetry.update();
        }
    }
    ///FUNCTII

    public int get_color(int TEAM)
    {
        int answer = 0;
        ///1 dreapta
        ///-1 stanga
        if(TEAM == 1) ///RED
        {
            if(robot.colorSensor.red() > MIN_RED)
                answer = 1;
            else if( robot.colorSensor.blue() > MIN_BLUE)
                answer = -1;
        }
        else if(TEAM == 0) ///BLUE
        {
            if(robot.colorSensor.red() > MIN_RED)
                answer = -1;
            else if(robot.colorSensor.blue() > MIN_BLUE)
                answer = 1;
        }
        return answer;
    }


}

