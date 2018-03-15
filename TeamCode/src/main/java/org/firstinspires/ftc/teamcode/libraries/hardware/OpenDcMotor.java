package org.firstinspires.ftc.teamcode.libraries.hardware;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.configuration.MotorConfigurationType;

/**
 * Created by Noah on 3/14/2018.
 */

/*
 * Copyright (c) 2017 FTC team 4634 FROGbots
 *
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class OpenDcMotor extends DcMotorImplEx
{
    OpenRevDcMotorController controller;

    public OpenDcMotor(DcMotorController controller, int portNumber)
    {
        this(controller, portNumber, Direction.FORWARD);

        DcMotorControllerEx thing;
    }

    public OpenDcMotor(DcMotorController controller, int portNumber, Direction direction)
    {
        this(controller, portNumber, direction, MotorConfigurationType.getUnspecifiedMotorType());
    }

    public OpenDcMotor(DcMotorController controller, int portNumber, Direction direction, @NonNull MotorConfigurationType motorType)
    {
        super(controller, portNumber, direction, motorType);
        this.controller = (OpenRevDcMotorController) controller;
    }

    public OpenRevDcMotorController.RevSensorReading getCurrentDraw()
    {
        return controller.getMotorCurrentDraw(getPortNumber());
    }
}