/* Copyright (c) 2014 Qualcomm Technologies Inc
All rights reserved.
Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:
Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.
Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.
NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.teamcode.makiahsLab;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

//Edited in order to have all of the important constants as final, so that no unintentional modifications are made.
//This class should be used so that any changes made to the robot configuration propagates through all parts of the code that has been written.

public class PlayMusicIntro extends LinearOpMode
{
    // Called on initialization (once)
    @Override
    public void runOpMode() throws InterruptedException
    {

        //METHOD 1: Works, but is actually a runaway thread that plays the song in EXTREME slow motion.  Has to be fixed.
//        final Handler handler = new Handler(Looper.getMainLooper());
//        final Runnable r = new Runnable() {
//            public void run()
//            {
//                //Using the hardware map as an app context.
//                MediaPlayer mediaPlayer = MediaPlayer.create(hardwareMap.appContext, com.qualcomm.ftcrobotcontroller.R.raw.jcena);
//                mediaPlayer.start(); // no need to call prepare(); create() does that for you
//                handler.post(this);
//            }
//        };
//        handler.post(r);

        //METHOD 2: Media player technique: works, but stops even with the sleep method in effect.
//        final MediaPlayer mediaPlayer = MediaPlayer.create(hardwareMap.appContext, com.qualcomm.ftcrobotcontroller.R.raw.jcena);
//        mediaPlayer.start(); // no need to call prepare(); create() does that for you
//        sleep(50000);
//        mediaPlayer.stop();

        //METHOD 3: StackOverflow handler method (just google the media player not finalized problem).
        final MediaPlayer mediaPlayer = MediaPlayer.create(hardwareMap.appContext, com.qualcomm.ftcrobotcontroller.R.raw.jcena);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer1) {
                mediaPlayer1.release();
            }
        });

        //Wait for the start button to be pressed.
        waitForStart();
    }
}