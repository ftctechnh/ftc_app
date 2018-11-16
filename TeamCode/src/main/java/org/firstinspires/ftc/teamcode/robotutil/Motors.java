package org.firstinspires.ftc.teamcode.robotutil;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Motors {


    public static void useEncoders(DcMotor[] motorArray){
        for(DcMotor motor : motorArray){
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public static void dontUseEncoders(DcMotor[] motorArray){
        for(DcMotor motor : motorArray){
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }
    public static void setBrake(DcMotor[] motorArray){
        for(DcMotor motor : motorArray){
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    public static void setCoast(DcMotor[] motorArray){
        for(DcMotor motor : motorArray){
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
    }

    public static void resetEncoders(DcMotor[] motorArray){
        for(DcMotor motor: motorArray) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
    public static void runToPosition(DcMotor[] motorArray){
        for(DcMotor motor: motorArray) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    public static void stopAll(DcMotor[] motorArray){
        for(DcMotor motor : motorArray){
            motor.setPower(0);
        }
    }

}
