package org.firstinspires.ftc.teamcode.libraries.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Noah on 3/6/2018.
 * Library created to make blinky effects with the underglow
 * Cuz if life gives you LEDs...
 */

public class BlinkyGlowLib {
    private final DcMotor glowMotor;
    private final float defaultPower;
    private long lastTime;
    private int delta = 0;
    private BlinkyEffect effect;

    BlinkyGlowLib(DcMotor blinkme, float defaultPower) {
        this.glowMotor = blinkme;
        this.defaultPower = defaultPower;
    }

    public void setAnimation(BlinkyEffect effect) {
        this.effect = effect;
    }

    public boolean loop() {
        if(effect != null) {
            long millis = System.currentTimeMillis();
            if(delta <= 0) {
                effect.setMotor(glowMotor);
                lastTime = millis;
                delta = effect.loop(0);
            }
            else if(millis - lastTime >= delta) {
                delta = effect.loop((int)(millis - lastTime));
                lastTime = millis;
                if(delta <= 0) {
                    effect = null;
                    glowMotor.setPower(defaultPower);
                }
            }
            return true;
        }
        return false;
    }
}
