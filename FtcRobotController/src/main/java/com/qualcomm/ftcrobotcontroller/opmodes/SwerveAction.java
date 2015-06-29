package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Steve on 6/26/2015.
 */
public interface SwerveAction {

    public void Start(ElapsedTime currentTime);
    public void Update(ElapsedTime currentTime);
    public boolean IsDone();
    public boolean IsStarted();
    public String ToString();
}
