package com.fellowshipoftheloosescrews.utilities.opmode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.HashMap;

/**
 * Created by FTC7123A on 9/1/2015.
 *
 * This is the master OpMode for our main robot. This adds methods to the standard
 * LinearOpMode specific to Legoless, like sensor setups and motor controls. This also
 * helps control the many threads we'll use for sensors.
 *
 * This corresponds to a hardware config file on the phones, it must be used with
 * the correct hardware.
 */
public abstract class FellowshipOpMode extends LinearOpMode {

    /**
     * Run method from LinearOpMode, don't override.
     * @throws InterruptedException
     */
    @Override
    public final void runOpMode() throws InterruptedException
    {
        thisOpMode = this;

        // create ModuleManager
        currentModuleManager = new ModuleManager();
        registerModules(currentModuleManager);

        // init the modules
        currentModuleManager.init();

        // init the user opmode
        fellowshipInit();

        // wait for the start of the match
        waitForStart();

        currentModuleManager.runModuleThread();
        fellowshipRunOpMode();
        fellowshipStop();
        currentModuleManager.stopModuleThread();
    }

    public abstract void fellowshipInit();

    public abstract void fellowshipRunOpMode();

    public void fellowshipStop() {}

    /**
     * A reference to the currently running OpMode
     */
    private static FellowshipOpMode thisOpMode = null;

    /**
     * Getter for the currently running OpMode,
     * @return The currently running FellowshipOpMode reference
     */
    public static FellowshipOpMode getOpMode()
    {
        return thisOpMode;
    }

    /**
     * Lets any thread delay one hardware cycle
     * NOTE: Different from waitOneHardwareCycle()
     */
    public static void waitForHardwareUpdate()
    {
        try {
            FellowshipOpMode currentOM = getOpMode();
            if(currentOM == null) {
                new IllegalAccessException("getOpMode() returned null").printStackTrace();
                return;
            }
            currentOM.waitOneFullHardwareCycle();
        } catch (Exception e) {
            Log.e("FellowshipOpMode", "Couldn't wait one hardware cycle");
        }
    }

    //========  Start Module Code  ========//

    /**
     * put code here to register modules with the module manager
     */
    public abstract void registerModules(ModuleManager manager);

    private ModuleManager currentModuleManager;

    public ModuleManager getModuleManager()
    {
        return currentModuleManager;
    }

    public class ModuleManager implements Runnable
    {
        private HashMap<String, Module> moduleMap;

        public ModuleManager()
        {
            moduleMap = new HashMap<String, Module>();
        }

        public void registerModule(String key, Module m)
        {
            if(moduleMap.containsKey(key))
                Log.e("ModuleManager", "Module map already contains key " + key + ", replacing "
                + "with the new one.");
            moduleMap.put(key, m);
            m.onAddToMap(this, getOpMode(), key);
        }

        public void init()
        {
            for(String s : moduleMap.keySet())
            {
                Module module = moduleMap.get(s);
                module.init();
            }
        }

        //========  Start Theading  ========//

        Thread thread;

        public void runModuleThread()
        {
            if(isRunning)
                return;
            thread = new Thread(this);
            thread.start();
        }

        public void stopModuleThread()
        {
            isRunning = false;
        }

        private boolean isRunning = false;

        public Module getModule(String key)
        {
            return moduleMap.get(key);
        }

        @Override
        public void run() {
            isRunning = true;

            for(String s : moduleMap.keySet())
            {
                moduleMap.get(s).start();
            }

            while(isRunning)
            {
                for(String s : moduleMap.keySet())
                {
                    moduleMap.get(s).update();
                }
                waitForHardwareUpdate();
            }

            for(String s : moduleMap.keySet())
            {
                moduleMap.get(s).stop();
            }
            isRunning = false;
        }

        //========  Stop Threading  ========//
    }

    //========  End Module Code ========//
}
