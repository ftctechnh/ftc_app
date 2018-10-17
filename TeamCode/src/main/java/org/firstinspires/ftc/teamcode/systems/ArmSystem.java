package org.firstinspires.ftc.teamcode.systems;/* Copyright (c) 2017 FIRST. All rights reserved.
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
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT ffefef AND CONTRIBUTORS
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


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name = "ArmSystem", group = "System")
public class ArmSystem extends LinearOpMode {

    DigitalChannel limitTop;
    DigitalChannel limitMiddle;
    DigitalChannel limitBottom;
    DcMotor motor1;
    DcMotor motor2;
    DcMotor winch;

    boolean isAtTop = false;

    boolean debouncing = false;
    boolean middleIsPressed = false;
    boolean topIsPressed = false;

    public void initialize() {
        ElapsedTime time = new ElapsedTime();
        motor1 = hardwareMap.get(DcMotor.class, "parrellelM1");
        motor2 = hardwareMap.get(DcMotor.class, "parrellelM2");
        winch = hardwareMap.get(DcMotor.class, "winch");
        limitTop = hardwareMap.get(DigitalChannel.class, "limitTop");
        limitMiddle = hardwareMap.get(DigitalChannel.class, "limitMiddle");
        //limitBottom = hardwareMap.get(DigitalChannel.class, "limitBot");

        telemetry.addData("motor1 encoder: ", motor1.getCurrentPosition());
        telemetry.addData("motor2 encoder: ", motor2.getCurrentPosition());

        motor1.setMode(DcMotor.RunMode.RESET_ENCODERS);
        motor2.setMode(DcMotor.RunMode.RESET_ENCODERS);
        winch.setMode(DcMotor.RunMode.RESET_ENCODERS);

        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        winch.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        limitTop.setMode(DigitalChannel.Mode.INPUT);
        limitMiddle.setMode(DigitalChannel.Mode.INPUT);
        //limitBottom.setMode(DigitalChannel.Mode.INPUT);
    }

    public void runOpMode() {
        initialize();
        waitForStart();

        telemetry.addData("Top", limitTop.getState());
        telemetry.addData("Middle", limitMiddle.getState());

        telemetry.addData("Encoder motor 1: ", motor1.getCurrentPosition());
        telemetry.addData("Encoder motor 2: ", motor2.getCurrentPosition());
        telemetry.addData("Encoder winch: ", winch.getCurrentPosition());

        sleep(1000);
        parallelUp();
        telemetry.update();
        sleep(3000);
        parallelDown(2000);
        telemetry.update();
    }

//    public void debouncing(){
//        telemetry.addData("Elapsed Time:", time);
//    }
  
    public void parallelUp() {

        while(limitTop.getState() && limitMiddle.getState()) {
            telemetry.addData("Top", limitTop.getState());
            telemetry.addData("Middle", limitMiddle.getState());
        
            motor1.setPower(0.8);
            motor2.setPower(-0.8);
        }

        motor1.setPower(0.0);
        motor2.setPower(0.0);
    }

    public void parallelUp(int encoderTicks) {
        if(limitTop.getState()) {
            motor1.setTargetPosition(encoderTicks);
            motor2.setTargetPosition(encoderTicks);       
            motor1.setPower(0.8);
            motor2.setPower(-0.8);
        }
    }

    public void parallelDown(){
        while(!limitMiddle.getState()) {
            motor1.setPower(-0.05);
            motor2.setPower(0.05);
        }

        motor1.setPower(-0.0);
        motor2.setPower(0.0);
    }



    public void parallelDown(int encoderTicks){
        if (!limitMiddle.getState()) {
            motor1.setTargetPosition(encoderTicks);
            motor2.setTargetPosition(encoderTicks);
            motor1.setPower(-0.05);
            motor2.setPower(0.05);               
        }
    }
}
