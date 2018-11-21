package org.firstinspires.ftc.teamcode.Components
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.Utils.Logger

public class Motor(val hardwareMap: HardwareMap,val motorName:String ){
    val motor = hardwareMap.dcMotor.get(motorName)
    val l = Logger("MOTOR", motorName)

    fun setPower(power:Double){
        motor.power = power
    }

    fun useEncoder(){
        motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun prepareEncoderDrive(){
        motor.mode=DcMotor.RunMode.STOP_AND_RESET_ENCODER
        useEncoder()
    }

    fun stop(coast:Boolean=false){
        if(coast){
            motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
        }else{
            motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        }
        motor.power = 0.0
    }


    fun logInfo(){
        l.logData("Power",motor.power)
        l.logData("Position",motor.currentPosition)
        l.logData("Busy",motor.isBusy)
        l.logData("Zero Power Behaviour",motor.zeroPowerBehavior.toString())
    }

}