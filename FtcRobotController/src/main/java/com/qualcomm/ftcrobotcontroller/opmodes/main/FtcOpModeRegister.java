/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

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

package com.qualcomm.ftcrobotcontroller.opmodes.main;

import com.powerstackers.resq.opmodes.autonomous.ProtoBlue;
import com.powerstackers.resq.opmodes.autonomous.TedbotAuto;
import com.powerstackers.resq.opmodes.autonomous.ProtoAuto;
import com.powerstackers.resq.opmodes.teleop.testbot1;
import com.qualcomm.ftcrobotcontroller.opmodes.NullOp;
import com.qualcomm.ftcrobotcontroller.opmodes.k9.K9IrSeeker;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;

import org.swerverobotics.library.internal.AnnotatedOpModeRegistrar;

/**
 * Register Op Modes
 */
public class FtcOpModeRegister implements OpModeRegister {
    /**
     * The Op Mode Manager will call this method when it wants a list of all available op modes.
     * OpModes which are registered during this call will form the contents of the OpMode
     * list on the driver station.
     * <p/>
     * There are two ways you can register an OpMode.
     * <p/>
     * 1)   Manually, by calling manager.register(displayName, class) here, where
     * displayName is the name you want to show up in on the driver station
     * and class is the class which should be instantiated to service that OpMode,
     * <p/>
     * 2)   Dynamically, by annotating your OpModes with @Autonomous or @TeleOp annotations,
     * or using a static @OpModeRegistrar method.
     *
     * @param manager the manager to use for manual registration
     * @see org.swerverobotics.library.interfaces.Autonomous
     * @see org.swerverobotics.library.interfaces.TeleOp
     * @see org.swerverobotics.library.interfaces.Disabled
     * @see org.swerverobotics.library.interfaces.OpModeRegistrar
     * @see org.swerverobotics.library.examples.SynchTeleOp
     */
    @Override
    public void register(OpModeManager manager) {
        AnnotatedOpModeRegistrar.register(manager);

    /*
     * Uncomment any of the following lines if you want to register an op mode,
     * or do that registration in a static method annotated as @OpModeRegistrar.
     */
    /*
     * Uncomment any of the following lines if you want to register an op mode.
     */
        /**
         * OTHER OPMODES:
         */
        manager.register("NullOp", NullOp.class);

        /**
         * K9 OPMODES:
         */
        manager.register("K9IrSeeker", K9IrSeeker.class);
        //manager.register("K9Line", K9Line.class);
        //
        //manager.register("K9TeleOp", K9TeleOp.class);
        //manager.register("MatrixK9TeleOp", MatrixK9TeleOp.class);

        /**
         * SENSOR OPMODES:
         */
        //manager.register("AdafruitRGBExample", AdafruitRGBExample.class);
        //manager.register("ColorSensorDriver", ColorSensorDriver.class);
        //manager.register("CompassCalibration", CompassCalibration.class);
        //
        //manager.register("IrSeekerOp", IrSeekerOp.class);
        //
        //
        //manager.register("OpticalTelemetry", OpticalTelemetry.class);
        //manager.register("NxtTeleOp", NxtTeleOp.class);

        /**
         * LINEAR OPMODES:
         */
        //manager.register("I2cAddressChangeExample", LinearI2cAddressChange.class);
        //manager.register("LinearIrExample", LinearIrExample.class);
        //manager.register("LinearK9TeleOp", LinearK9TeleOp.class);

        /**
         * PUSHBOT OPMODES:
         */
        //manager.register("PushBotAuto", PushBotAuto.class);
        //manager.register ("PushBotAutoSensors", PushBotAutoSensors.class);
        //manager.register("PushBotDriveTouch", PushBotDriveTouch.class);
        //
        //
        //manager.register ("PushBotIrEvent", PushBotIrEvent.class);
        //manager.register("PushBotIrSeek", PushBotIrSeek.class);
        // manager.register("PushBotManual", PushBotManual.class);
        //manager.register ("PushBotManual1", PushBotManual1.class);
        //manager.register ("PushBotManualSensors", PushBotManualSensors.class);
        //manager.register ("PushBotOdsDetectEvent", PushBotOdsDetectEvent.class);
        //manager.register ("PushBotOdsFollowEvent", PushBotOdsFollowEvent.class);
        //manager.register("PushBotSquare", PushBotSquare.class);
        //
        //
        //manager.register ("PushBotTouchEvent", PushBotTouchEvent.class);

        /**
         * POWERSTACKERS OPMODES:
         */
        // All of our opmodes make use of the annotations provided by the Swerve library.
        // You should use those on any class you want to register, rather than registering it here.
//        manager.register("BlueAllianceOP", BlueAlianceOP.class);
//        manager.register("RedAllianceOP", RedAlianceOP.class);
//        manager.register("ColorSensorSense", ColorSensorSense.class);
//        manager.register("ProtobotOP", ProtobotTeleop.class);
//      manager.register("Purple Pencil", PurplePencil.class);
//        manager.register("Tedbot", Tedbot.class);
        manager.register("protored", ProtoBlue.class);
        manager.register("Auto Ted", TedbotAuto.class);
        manager.register("testbot1", testbot1.class);
//        manager.register("protoAuto", ProtoAuto.class);
    }
}