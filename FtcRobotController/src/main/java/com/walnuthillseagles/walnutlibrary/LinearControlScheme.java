package com.walnuthillseagles.walnutlibrary;

import java.util.ArrayList;

/**
 * Created by Yan Vologzhanin on 1/23/2016.
 */
public class LinearControlScheme implements Stoppable {
    ArrayList<Stoppable> hardware;
    public LinearControlScheme(){
        hardware=new ArrayList<Stoppable>();
    }
    public void add(Stoppable item){
        hardware.add(item);
    }
    public void stop(){
        for(int i=0;i<hardware.size();i++){
            hardware.get(i).stop();
        }
    }
}
