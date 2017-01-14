package com.borsch.sim;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.TelemetryInternal;

/**
 * Created by Max on 12/18/2016.
 */

public class TelemetrySim implements Telemetry, TelemetryInternal {

    public TelemetrySim (OpMode opMode) {

    }

    @Override
    public Item addData(String s, String s1, Object... objects) {
        return null;
    }

    @Override
    public Item addData(String s, Object o) {
        return null;
    }

    @Override
    public <T> Item addData(String s, Func<T> func) {
        return null;
    }

    @Override
    public <T> Item addData(String s, String s1, Func<T> func) {
        return null;
    }

    @Override
    public boolean removeItem(Item item) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public void clearAll() {

    }

    @Override
    public Object addAction(Runnable runnable) {
        return null;
    }

    @Override
    public boolean removeAction(Object o) {
        return false;
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public Line addLine() {
        return null;
    }

    @Override
    public Line addLine(String s) {
        return null;
    }

    @Override
    public boolean removeLine(Line line) {
        return false;
    }

    @Override
    public boolean isAutoClear() {
        return false;
    }

    @Override
    public void setAutoClear(boolean b) {

    }

    @Override
    public int getMsTransmissionInterval() {
        return 0;
    }

    @Override
    public void setMsTransmissionInterval(int i) {

    }

    @Override
    public String getItemSeparator() {
        return null;
    }

    @Override
    public void setItemSeparator(String s) {

    }

    @Override
    public String getCaptionValueSeparator() {
        return null;
    }

    @Override
    public void setCaptionValueSeparator(String s) {

    }

    @Override
    public Log log() {
        return null;
    }

    @Override
    public boolean tryUpdateIfDirty() {
        return false;
    }

    @Override
    public void resetTelemetryForOpMode() {

    }
}
