package com.qualcomm.ftcrobotcontroller.FreshClasses;

import com.qualcomm.robotcore.hardware.DcMotor;
/**
 * Created by Naisan on 4/16/2016.
 */
public class FreshMotors {
    DcMotor M_backLeft, //andy
            M_backRight, //barry
            M_frontLeft, //carl
            M_frontRight, //daniel
            M_rackPinion,
            M_sweeper;

        public FreshMotors(DcMotor M_backLeft, DcMotor M_backRight, DcMotor M_frontLeft, DcMotor M_frontRight, DcMotor M_rackPinion, DcMotor M_sweeper) {
            this.M_backLeft = M_backLeft;
            this.M_backRight = M_backRight;
            this.M_frontLeft = M_frontLeft;
            this.M_frontRight = M_frontRight;
            this.M_rackPinion = M_rackPinion;
            this.M_sweeper = M_sweeper;
        }
    }

