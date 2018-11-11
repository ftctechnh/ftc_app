package org.firstinspires.ftc.teamcode.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractTeleop extends AbstractOpMode {

    private boolean threadRunning = false;

    private List<Exception> exceptions = Collections.synchronizedList(new ArrayList<Exception>());

    //Setup gamepad
    private Emitter emitter = new Emitter();

    private boolean a_down = false, b_down = false, x_down = false, y_down = false, lb_down = false,
            rb_down = false, dpl_down = false, dpr_down = false, dpu_down = false, dpd_down = false,
            start_down = false, back_down = false;

    private double lt_down = 0, rt_down = 0, lsx_down = 0, lsy_down = 0, rsx_down = 0, rsy_down = 0;

    public AbstractTeleop(){

    }

    @Override
    public void runOpMode() {

        ExecutorService service = Executors.newSingleThreadExecutor();

        Thread InitThread = new Thread(new initThread());
        Thread InitLoopThread = new Thread(new initloopThread());
        Thread StartThread = new Thread(new startThread());
        Thread RunThread = new Thread(new runThread());

        //sets up emitter
        initEmitter();

        //calls user init
        service.execute(InitThread);

        while (!isStopRequested() && !isStarted()){
            checkException();

            if(!threadRunning) {
                threadRunning = true;
                service.execute(InitLoopThread);
            }
        }

        while (!isStopRequested() && threadRunning);

        if(!isStopRequested()) {
            checkException();

            threadRunning = true;
            service.execute(StartThread);
        }

        while (opModeIsActive()) {
            checkException();

            //checks the gamepad for changes
            checkGamepad();

            //calls user loop
            if(!threadRunning) {
                threadRunning = true;
                service.execute(RunThread);
            }
        }

        Stop();
    }

    public abstract void Init();

    public void InitLoop(){

    }

    public void Start(){

    }

    public abstract void Loop();

    public void Stop(){

    }

    private void throwException(Exception e){
        exceptions.add(e);
    }

    private void checkException() {
        for (Exception e : exceptions) {
            telemetry.update();
            for (StackTraceElement element : e.getStackTrace()) {
                if (element.toString().contains("org.firstinspires.ftc.teamcode")) {
                    telemetry.addData(element.toString().replace("org.firstinspires.ftc.teamcode.", ""));
                    break;
                }
            }
            switch (e.getClass().getSimpleName()) {
                case "NullPointerException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    NullPointerException exception = (NullPointerException) e;
                    throw exception;
                }
                case "IllegalArgumentException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    IllegalArgumentException exception = (IllegalArgumentException) e;
                    throw exception;
                }
                case "ArrayIndexOutOfBoundsException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    ArrayIndexOutOfBoundsException exception = (ArrayIndexOutOfBoundsException) e;
                    throw exception;
                }
                case "ConcurrentModificationException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    ConcurrentModificationException exception = (ConcurrentModificationException) e;
                    throw exception;
                }
                default: {
                    telemetry.addData(e.getClass().getSimpleName());
                    telemetry.update();
                    AbstractOpMode.delay(2000);
                }
            }
        }
    }

    class initThread implements Runnable{
        public void run(){
            try {
                Init();
            } catch (Exception e){
                throwException(e);
            }
            threadRunning = false;
        }
    }

    class initloopThread implements Runnable{
        public void run(){
            try {
                InitLoop();
            } catch (Exception e){
                throwException(e);
            }
            threadRunning = false;
        }
    }

    class startThread implements Runnable{
        public void run(){
            try{
                Start();
            } catch (Exception e){
                throwException(e);
            }
            threadRunning = false;
        }
    }

    class runThread implements Runnable{
        public void run(){
            try{
                Loop();
            } catch (Exception e){
                throwException(e);
            }
            threadRunning = false;
        }
    }

    class a_up implements Runnable{
        public void run(){
            try{
                a_up();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class a_down implements Runnable{
        public void run(){
            try{
                a_down();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class b_up implements Runnable{
        public void run(){
            try{
                b_up();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class b_down implements Runnable{
        public void run(){
            try{
                b_down();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class x_up implements Runnable{
        public void run(){
            try{
                x_up();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class x_down implements Runnable{
        public void run(){
            try{
                x_down();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class y_up implements Runnable{
        public void run(){
            try{
                y_up();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class y_down implements Runnable{
        public void run(){
            try{
                y_down();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class lb_up implements Runnable{
        public void run(){
            try{
                lb_up();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class lb_down implements Runnable{
        public void run(){
            try{
                lb_down();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class rb_up implements Runnable{
        public void run(){
            try{
                rb_up();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class rb_down implements Runnable{
        public void run(){
            try{
                rb_down();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class dpl_up implements Runnable{
        public void run(){
            try{
                dpl_up();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class dpl_down implements Runnable{
        public void run(){
            try{
                dpl_down();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class dpr_up implements Runnable{
        public void run(){
            try{
                dpr_up();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class dpr_down implements Runnable{
        public void run(){
            try{
                dpr_down();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class dpu_up implements Runnable{
        public void run(){
            try{
                dpu_up();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class dpu_down implements Runnable{
        public void run(){
            try{
                dpu_down();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class dpd_up implements Runnable{
        public void run(){
            try{
                dpd_up();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class dpd_down implements Runnable{
        public void run(){
            try{
                dpd_down();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class start_up implements Runnable{
        public void run() {
            try{
                start_up();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class start_down implements Runnable{
        public void run(){
            try{
                start_down();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class back_up implements Runnable{
        public void run(){
            try{
                back_up();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class back_down implements Runnable{
        public void run(){
            try{
                back_down();
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class lt_change implements Runnable{
        public void run(){
            try{
                lt_change(lt_down);
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class rt_change implements Runnable{
        public void run(){
            try{
                rt_change(rt_down);
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class lsx_change implements Runnable{
        public void run(){
            try{
                lsx_change(lsx_down);
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class lsy_change implements Runnable{
        public void run(){
            try{
                lsy_change(-lsy_down);
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class rsx_change implements Runnable{
        public void run(){
            try{
                rsx_change(rsx_down);
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    class rsy_change implements Runnable{
        public void run(){
            try{
                rsy_change(-rsy_down);
            } catch (Exception e){
                throwException(e);
            }
        }
    }

    private void initEmitter(){
        //a
        emitter.on("a_up", new a_up());
        emitter.on("a_down", new a_down());

        //b
        emitter.on("b_up", new b_up());
        emitter.on("b_down", new b_down());

        //x
        emitter.on("x_up", new x_up());
        emitter.on("x_down", new x_down());

        //y
        emitter.on("y_up", new y_up());
        emitter.on("y_down", new y_down());

        //lb
        emitter.on("lb_up", new lb_up());
        emitter.on("lb_down", new lb_down());

        //rb
        emitter.on("rb_up", new rb_up());
        emitter.on("rb_down", new rb_down());

        //dpl
        emitter.on("dpl_up", new dpl_up());
        emitter.on("dpl_down", new dpl_down());

        //dpr
        emitter.on("dpr_up", new dpr_up());
        emitter.on("dpr_down", new dpr_down());

        //dpu
        emitter.on("dpu_up", new dpu_up());
        emitter.on("dpu_down", new dpu_down());

        //dpd
        emitter.on("dpd_up", new dpd_up());
        emitter.on("dpd_down", new dpd_down());

        //start
        emitter.on("start_up", new start_up());
        emitter.on("start_down", new start_down());

        //back
        emitter.on("back_up", new back_up());
        emitter.on("back_down", new back_down());

        //lt
        emitter.on("lt_change", new lt_change());

        //rt
        emitter.on("rt_change", new rt_change());

        //lsx
        emitter.on("lsx_change", new lsx_change());

        //lsy
        emitter.on("lsy_change", new lsy_change());

        //rsx
        emitter.on("rsx_change", new rsx_change());

        //rsy
        emitter.on("rsy_change", new rsy_change());
    }

    private void checkGamepad(){
        //a
        boolean a = gamepad1.a;
        if(a != a_down){
            if(a) emitter.emit("a_down");
            else emitter.emit("a_up");
            a_down = a;
        }

        //b
        boolean b = gamepad1.b;
        if(b != b_down) {
            if (b) emitter.emit("b_down");
            else emitter.emit("b_up");
            b_down = b;
        }

        //x
        boolean x = gamepad1.x;
        if(x != x_down){
            if(x) emitter.emit("x_down");
            else emitter.emit("x_up");
            x_down = x;
        }

        //y
        boolean y = gamepad1.y;
        if(y != y_down){
            if(y) emitter.emit("y_down");
            else emitter.emit("y_up");
            y_down = y;
        }

        //lb
        boolean lb = gamepad1.left_bumper;
        if(lb != lb_down){
            if(lb) emitter.emit("lb_down");
            else emitter.emit("lb_up");
            lb_down = lb;
        }

        //rb
        boolean rb = gamepad1.right_bumper;
        if(rb != rb_down){
            if(rb) emitter.emit("rb_down");
            else emitter.emit("rb_up");
            rb_down = rb;
        }

        //dpl
        boolean dpl = gamepad1.dpad_left;
        if(dpl != dpl_down){
            if(dpl) emitter.emit("dpl_down");
            else emitter.emit("dpl_up");
            dpl_down = dpl;
        }

        //dpr
        boolean dpr = gamepad1.dpad_right;
        if(dpr != dpr_down){
            if(dpr) emitter.emit("dpr_down");
            else emitter.emit("dpr_up");
            dpr_down = dpr;
        }

        //dpu
        boolean dpu = gamepad1.dpad_up;
        if(dpu != dpu_down){
            if(dpu) emitter.emit("dpu_down");
            else emitter.emit("dpu_up");
            dpu_down = dpu;
        }

        //dpd
        boolean dpd = gamepad1.dpad_down;
        if(dpd != dpd_down){
            if(dpd) emitter.emit("dpd_down");
            else emitter.emit("dpd_up");
            dpd_down = dpd;
        }

        //start
        boolean start = gamepad1.start;
        if(start != start_down){
            if(start) emitter.emit("start_down");
            else emitter.emit("start_up");
            start_down = start;
        }

        //back
        boolean back = gamepad1.back;
        if(back != back_down){
            if(back) emitter.emit("back_down");
            else emitter.emit("back_up");
            back_down = back;
        }

        //lt
        double lt = gamepad1.left_trigger;
        if(lt != lt_down){
            lt_down = lt;
            emitter.emit("lt_change");
        }

        //rt
        double rt = gamepad1.right_trigger;
        if(rt != rt_down){
            rt_down = rt;
            emitter.emit("rt_change");
        }

        //lsx
        double lsx = gamepad1.left_stick_x;
        if(lsx != lsx_down){
            lsx_down = lsx;
            emitter.emit("lsx_change");
        }

        //lsy
        double lsy = gamepad1.left_stick_y;
        if(lsy != lsy_down){
            lsy_down = lsy;
            emitter.emit("lsy_change");
        }

        //rsx
        double rsx = gamepad1.right_stick_x;
        if(rsx != rsx_down){
            rsx_down = rsx;
            emitter.emit("rsx_change");
        }

        //rsy
        double rsy = gamepad1.right_stick_y;
        if(rsy != rsy_down){
            rsy_down = rsy;
            emitter.emit("rsy_change");
        }
    }

    /*public abstract void a_up();
    public abstract void a_down();
    public abstract void b_up();
    public abstract void b_down();
    public abstract void x_up();
    public abstract void x_down();
    public abstract void y_up();
    public abstract void y_down();
    public abstract void lb_up();
    public abstract void lb_down();
    public abstract void rb_up();
    public abstract void rb_down();
    public abstract void dpl_up();
    public abstract void dpl_down();
    public abstract void dpr_up();
    public abstract void dpr_down();
    public abstract void dpu_up();
    public abstract void dpu_down();
    public abstract void dpd_up();
    public abstract void dpd_down();
    public abstract void start_up();
    public abstract void start_down();
    public abstract void back_up();
    public abstract void back_down();
    public abstract void lt_change(double lt);
    public abstract void rt_change(double rt);
    public abstract void lsx_change(double lsx);
    public abstract void lsy_change(double lsy);
    public abstract void rsx_change(double rsx);
    public abstract void rsy_change(double rsy);*/

    public void a_up(){}

    public void a_down(){}

    public void b_up(){}

    public void b_down(){}

    public void x_up(){}

    public void x_down(){}

    public void y_up(){}

    public void y_down(){}

    public void lb_up(){}

    public void lb_down(){}

    public void rb_up(){}

    public void rb_down(){}

    public void dpl_up(){}

    public void dpl_down(){}

    public void dpr_up(){}

    public void dpr_down(){}

    public void dpu_up(){}

    public void dpu_down(){}

    public void dpd_up(){}

    public void dpd_down(){}

    public void start_up(){}

    public void start_down(){}

    public void back_up(){}

    public void back_down(){}

    public void lt_change(double lt){}

    public void rt_change(double rt){}

    public void lsx_change(double lsx){}

    public void lsy_change(double lsy){}

    public void rsx_change(double rsx){}

    public void rsy_change(double rsy){}
}