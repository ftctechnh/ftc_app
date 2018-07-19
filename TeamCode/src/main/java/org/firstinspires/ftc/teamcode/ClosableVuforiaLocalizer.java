package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;
/**
 * Copyright (c) 2017 FTC Team 5484 Enderbots
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This is a VuforiaLocalizer replacement, as it can do everything it does, as well as detach from the camera.
 *     To use, one can replace statements like:
 *
 *         VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);
 *
 *     with
 *
 *         ClosableVuforiaLocalizer vuforia = new ClosableVuforiaLocalizer(parameters);
 *
 *     To close vuforia, simply call vuforia.close();
 */
public class ClosableVuforiaLocalizer extends VuforiaLocalizerImpl {

    boolean closed = false;

    public ClosableVuforiaLocalizer(Parameters parameters) {
        super(parameters);
    }
    @Override
    public void close() {
        if (!closed) super.close();
        closed = true;
    }
}