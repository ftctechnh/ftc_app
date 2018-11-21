package org.firstinspires.ftc.teamcode.Models

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import android.provider.SyncStateContract.Helpers.update
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.Utils.Logger


public abstract class Task(var opMode: LinearOpMode,taskName:String):Thread(){
    public var running:Boolean = true
    private val l:Logger = Logger(taskName)

    fun stopThread() {
        this.running = false
        l.log("Thread stopped.")
    }

    fun runWhileActive(doForever:()-> Unit){
        while(opMode.opModeIsActive() && !opMode.isStopRequested && this.running){
            doForever()
        }
    }

    abstract override fun run()
}