package org.swerverobotics.library;

import com.qualcomm.ftccommon.FtcEventLoopHandler;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robocol.Telemetry;
import org.swerverobotics.library.interfaces.*;
import org.swerverobotics.library.internal.*;
import java.util.*;

/**
 * TelemetryDashboardAndLog is a telemetry helper class that makes it easier write 
 * telemetry to the driver station. It is primarily intended to be used within
 * SynchronousOpModes, where its use is necessary to avoid possibly garbled output on
 * the driver station, but it can be used in any OpMode.
 *
 * <p> The telemetry is divided into two parts: a <em>dashboard</em>, and a <em>log</em>.
 * The dashboard, typically consisting of a fixed set of lines of data, is shown in the driver
 * station at the top of the telemetry output. The log, containing a series of messages in
 * chronological or reverse chronological order, is shown below that.</p>
 *
 * <p>The telemetry is sent to the driver station when {@link #update()} is called. It is
 * <em>not</em> transmitted automatically: if you don't call update(), you won't see any telemetry. Usually
 * you call update() at the bottom of your {@link SynchronousOpMode#opModeIsActive()
 * while(opModeIsActive())} loop. And only a subset of update() calls actually transmit:
 * by default, the update rate is limited to twice per second, which can be adjusted with
 * {@link #setUpdateIntervalMs(int)}. This helps both reduce network traffic and reduce
 * the cost on the robot controller itself of acquiring telemetry data for transmission,
 * as the acquisition of the data itself can sometimes be quite expensive, depending on what
 * is being shown.</p>
 *
 * <p>Data can be shown in the dashboard in one (or both) of two ways:</p>
 * <ol>
 *  <li>As with the telemetry in the robot controller runtime, you can call
 *      {@link #addData(String, String)} to add a message you have composed. And as in
 *      the robot controller runtime telemetry, addData() needs to be called on every display
 *      cycle, that is, here, before every call to {@link #update()}, as the display of
 *      these pre-composed lines is a one-time event.
 *  </li><li>
 *      Lines in the dashboard can be configured in a one-time setup setup with the unevaluated
 *      computations needed for their display. Only when a transmission is actually to be made
 *      to the driver station are these expressions evaluated and the lines composed. This is
 *      accomplished using a series of {@link #addLine() addLine()} calls, each containing a
 *      number of {@link #item(String, IFunc) item()} invocations.
 * </li></ol>
 *
 * <p>The log simply contains a serial history of the messages it has been asked to display.
 * When new additions are made to the log, they are conveyed to the driver station in an
 * expeditious manner. The dashboard is also updated at such times. The order of the display
 * of log messages can be controlled with {@link org.swerverobotics.library.TelemetryDashboardAndLog.Log#setDisplayOldToNew(boolean) setDisplayOldToNew()}.</p>
 */
public class TelemetryDashboardAndLog
    {
    //------------------------------------------------------------------------------------------
    // Public State
    //------------------------------------------------------------------------------------------

    /**
     * 'log' provides a means by which a scrolling history of events can be recorded on
     * the driver station
     */
    public final Log                log;

    /**
     * Retrieves the current delimiter used to separate dashboard items on a single line
     *
     * @return the current delimiter
     * @see #setItemDelimiter(String)
     */
    public String getItemDelimiter()
        {
        return itemDelimiter;
        }

    /**
     * Sets the delimiter used to separate dashboard items on a single line
     * @param itemDelimiter the new delimiter to use
     */
    public void setItemDelimiter(String itemDelimiter)
        {
        this.itemDelimiter = itemDelimiter;
        }

    /**
     * Advanced: retrieves the interval in milliseconds at which the drive station
     * is refreshed with new contents of the dashboard. If updates aren't happening
     * frequently enough for you, you can change this value.
     *
     * Note that updates happen more frequently if messages are written to the log.
     *
     * @return  the current update interval, in milliseconds
     *
     * @see #setUpdateIntervalMs(int)
     */
    public int getUpdateIntervalMs()
        {
        return msUpdateInterval;
        }

    /**
     * Advanced: sets the update interval at which the driver station is refreshed.
     *
     * @param msUpdateInterval  the new update interval for telemetry, in milliseconds
     *
     * @see #getUpdateIntervalMs()
     */
    public void setUpdateIntervalMs(int msUpdateInterval)
        {
        this.msUpdateInterval = msUpdateInterval;
        }

    //------------------------------------------------------------------------------------------
    // Private State
    //------------------------------------------------------------------------------------------

    private String itemDelimiter    = " | ";
    private int    msUpdateInterval = 500;

    private Vector<Runnable>        actions = null;
    private Vector<Line>            composableLines = null;
    private Vector<String>          composedLines = null;
    private boolean                 updateSinceAddComposedLine = false;

    private long                    nanoLastUpdate = 0;
    private EventLoopManager        eventLoopManager;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    /**
     * Instantiate a new telemetry dashboard and log for use within a given OpMode
     *
     * @param notUsed the previous telemetry object that the new one is to take over from
     */
    @Deprecated
    public TelemetryDashboardAndLog(Telemetry notUsed)
        {
        this();
        }

    /**
     * Instantiate a new telemetry dashboard and log
     */
    public TelemetryDashboardAndLog()
        {
        SwerveThreadContext context = SwerveThreadContext.getThreadContext();
        this.eventLoopManager = context.swerveFtcEventLoop.getEventLoopManager();
        this.log = new Log();
        //
        this.clearDashboard();
        }

    //------------------------------------------------------------------------------------------
    // Robot controller runtime Telemetry API support
    //------------------------------------------------------------------------------------------

    /**
     * Add a one-time message to the dashboard. This message is erased after
     * update() is called and must be reissued if it is to be shown
     * in subsequent update() cycles.
     *
     * @param caption   the caption to put on the message
     * @param msg       the message to display
     *
     * @see Telemetry#addData(String, String)
     */
    public synchronized void addData(String caption, String msg)
        {
        this.addComposedLine(caption, msg);
        }

    /**
     * Add a one-time message to the dashboard. This message is erased after
     * update() is called and must be reissued if it is to be shown
     * in subsequent update() cycles.
     *
     * @param caption   the caption to put on the message
     * @param value     the value to be formatted and displayed
     *
     * @see #addData(String, String)
     * @see Telemetry#addData(String, Object)
     */
    public synchronized void addData(String caption, Object value)
        {
        this.addComposedLine(caption, value.toString());
        }

    /**
     * Add a one-time message to the dashboard. This message is erased after
     * update() is called and must be reissued if it is to be shown
     * in subsequent update() cycles.
     *
     * @param caption   the caption to put on the message
     * @param value     the value to be formatted and displayed
     *
     * @see #addData(String, String)
     * @see Telemetry#addData(String, float)
     */
    public synchronized void addData(String caption, float value)
        {
        this.addData(caption, (double)value);
        }

    /**
     * Add a one-time message to the dashboard. This message is erased after
     * update() is called and must be reissued if it is to be shown
     * in subsequent update() cycles.
     *
     * @param caption   the caption to put on the message
     * @param value     the value to be formatted and displayed
     *
     * @see #addData(String, String)
     * @see Telemetry#addData(String, double)
     */
    public synchronized void addData(String caption, double value)
        {
        this.addComposedLine(caption, Double.toString(value));
        }

    private void addComposedLine(String caption, String value)
        {
        if (this.updateSinceAddComposedLine)
            this.composedLines.clear();

        // We display both the key and the value for these, as that's what the
        // raw telemetry does. However, we do NOT sort on the caption, as that
        // has little utility.
        this.composedLines.add(String.format("%s : %s", caption, value));
        this.updateSinceAddComposedLine = false;
        }

    //------------------------------------------------------------------------------------------
    // Updating
    //------------------------------------------------------------------------------------------

    /**
     * (Re)initialize the dashboard to contain no lines or items
     */
    public synchronized void clearDashboard()
        {
        this.actions         = new Vector<Runnable>();
        this.composableLines = new Vector<Line>();
        clearComposedLines();
        }

    private void clearComposedLines()
        {
        this.composedLines    = new Vector<String>();
        }

    private static String getKey(int iLine)
        {
        // At present (Aug 8, 2015), the driver station both sorts by the key we return here
        // but also DISPLAYS it! Ugh. So we try to conserve space. And we use Unicode characters
        // that don't actually take up space on the display.
        return String.format("%c", 0x180 + iLine);
        }

     /**
      * Call this function often to send your telemetry to the driver station.
      *
      * Note that telemetry isn't *actually* transmitted on each call. Rather, transmission
      * to the driver station is throttled to normally be sent at the end of every update
      * interval. However, when a message is added to the log, the driver station is always
      * updated on the next call to update().
      *
      * @return whether an update to the driver station was made or not
      * @see #getUpdateIntervalMs()
      * @see #update(int)
      */
    public synchronized boolean update()
        {
        return update(getUpdateIntervalMs());
        }

    /**
     * A variant on {@link #update()} in which one can explicitly specify the update interval
     * to be used.
     *
     * @param msUpdateInterval the update interval to use, in milliseconds
     * @return whether an update to the driver station was made or not
     * @see #update()
     */
    public synchronized boolean update(int msUpdateInterval)
        {
        return update(msUpdateInterval, true);
        }

    private synchronized boolean update(int msUpdateInterval, boolean userRequest)
        {
        boolean result = false;

        // Don't actually put out updates too often so as to avoid excessive pointless
        // computation in the robot controller and (to a lesser extent) reduced network
        // traffic to the driver station.
        long nanoNow = System.nanoTime();
        if (nanoLastUpdate == 0
                || nanoNow > nanoLastUpdate + (long)msUpdateInterval * SynchronousOpMode.NANO_TO_MILLI
                || log.newLogMessagesAvailable
                )
            {
            // Ok, we're going to update the telemetry

            // Evaluate any delayed actions we've been asked to do
            for (Runnable action : this.actions)
                {
                action.run();
                }

            // Get a copy of all the data
            final Vector<String> keys = new Vector<String>();
            final Vector<String> values = new Vector<String>();
            int iLine = 0;

            // Compose each of the composable lines
            for (Line line : this.composableLines)
                {
                keys.add(getKey(iLine));
                values.add(composableLines.elementAt(iLine).compose());
                iLine++;
                }

            // Copy each of the composed lines
            for (String line : this.composedLines)
                {
                keys.add(getKey(iLine));
                values.add(line);
                iLine++;
                }

            // Add on the log
            int size = this.log.logQueue.size();
            for (int i = 0; i < size; i++)
                {
                String s = this.log.isDisplayOldToNew() ? this.log.logQueue.elementAt(i) : this.log.logQueue.elementAt(size - 1 - i);
                keys.add(getKey(iLine));
                values.add(s);
                iLine++;
                }

            // Build an object to carry our telemetry data.
            // Transmit same to the driver station.
            Telemetry transmitter = new Telemetry();
            //
            for (int i = 0; i < keys.size(); i++)
                {
                transmitter.addData(
                        keys.elementAt(i),
                        values.elementAt(i));
                }
            //
            if (transmitter.hasData())
                this.eventLoopManager.sendTelemetryData(transmitter);

            // Update our state for the next time around
            this.nanoLastUpdate = nanoNow;
            this.log.newLogMessagesAvailable = false;
            result = true;
            }

        // We ALWAYS clear the composed lines, as the user, generally,
        // has no idea which update() calls actually transmit.
        if (userRequest)
            this.updateSinceAddComposedLine = true;

        return result;
        }

    //------------------------------------------------------------------------------------------
    // Items
    //------------------------------------------------------------------------------------------

    /**
     * Create a new dashboard item with the indicated caption and value computation.
     *
     * @param itemCaption   the string with which the item value is to be labelled
     * @param itemValue     a lambda expression that when evaluated will provide the
     *                      then-current value of the item
     * @return              the newly created item
     */
    public Item item(final String itemCaption, final IFunc<Object> itemValue)
        {
        Item result = new Item(itemCaption, new IFunc<String>()
                {
                @Override public String value()
                    {
                    return itemValue.value().toString();
                    }
                }
            );
        return result;
        }

    //------------------------------------------------------------------------------------------
    // Actions
    //------------------------------------------------------------------------------------------

    /**
     * In addition to lines, a dashboard may also contain a list of actions.
     * When the dashboard is to be updated, these actions are evaluated before
     * the dashboard lines are composed. A typical use of such actions is to
     * initialize some state variable, parts of which are subsequently displayed
     * in dashboard lines and items. This can help avoid needless re-evaluation.
     * @param action    the action to execute before composing the lines of the dashboard
     * @see #addLine()
     * @see #update()
     */
    public synchronized void addAction(Runnable action)
        {
        this.actions.add(action);
        }
        
    //------------------------------------------------------------------------------------------
    // Lines
    //------------------------------------------------------------------------------------------

    /**
     * Add an empty line to the dashboard
     * @see #addLine(Item[])
     */
    public void addLine()
        {
        this.addLine(new Item[]{});
        }
    /**
     * Add a line to the dashboard containing the indicated item
     *
     * @param item      the item to be contained in the line
     * @see #addLine(Item[])
     */
    public void addLine(Item item)
        {
        this.addLine(new Item[]{item});
        }
    /**
     * Add a line to the dashboard containing the indicated items
     *
     * @param item0     the first item to be contained in the line
     * @param item1     the second item to be contained in the line
     * @see #addLine(Item[])
     */
    public void addLine(Item item0, Item item1)
        {
        this.addLine(new Item[]{item0, item1});
        }
    /**
     * Add a line to the dashboard containing the indicated items
     *
     * @param item0     the first item to be contained in the line
     * @param item1     the second item to be contained in the line
     * @param item2     the third item to be contained in the line
     * @see #addLine(Item[])
     */
    public void addLine(Item item0, Item item1, Item item2)
        {
        this.addLine(new Item[]{item0, item1, item2});
        }
    /**
     * Add a line to the dashboard containing the indicated items

     * @param item0     the first item to be contained in the line
     * @param item1     the second item to be contained in the line
     * @param item2     the third item to be contained in the line
     * @param item3     the fourth item to be contained in the line
     * @see #addLine(Item[])
     */
    public void addLine(Item item0, Item item1, Item item2, Item item3)
        {
        this.addLine(new Item[]{item0, item1, item2, item3});
        }
    /**
     * Add a line to the dashboard containing the indicated items
     *
     * @param item0     the first item to be contained in the line
     * @param item1     the second item to be contained in the line
     * @param item2     the third item to be contained in the line
     * @param item3     the fourth item to be contained in the line
     * @param item4     the fifth item to be contained in the line
     * @see #addLine(Item[])
     */
    public void addLine(Item item0, Item item1, Item item2, Item item3, Item item4)
        {
        this.addLine(new Item[]{item0, item1, item2, item3, item4});
        }
    /**
     * Add a line to the dashboard containing the indicated items.
     *
     * @param items     the list of items to be contained in the line
     * @see #addAction(Runnable)
     */
    public synchronized void addLine(Item[] items)
        {
        Line line = new Line(items);
        this.composableLines.add(line);
        }

    //==============================================================================================

    public class Log
        {
        //------------------------------------------------------------------------------------------
        // Public state
        //------------------------------------------------------------------------------------------

        /**
         * Returns the order in which the log is displayed.
         * <p>
         * If true, older log messages are displayed at the top of the log, newer messages at the bottom.
         * If false, newer messages are on top, older messages at the bottom.
         *
         * @return whether the log is displayed in old-to-new order
         * @see #setDisplayOldToNew(boolean)
         */
        public boolean isDisplayOldToNew()
            {
            return displayOldToNew;
            }

        /**
         * Sets the order in which the log is displayed.
         * @param displayOldToNew   whether the log is to be displayed in old-to-new order
         * @see #isDisplayOldToNew()
         */
        public void setDisplayOldToNew(boolean displayOldToNew)
            {
            this.displayOldToNew = displayOldToNew;
            }

        /**
         * Returns the maximum number of entries used by the log on the telemetry display
         *
         * @return the maximum number of lines retained by the og
         * @see #setCapacity(int)
         */
        public int getCapacity()
            {
            return this.capacity;
            }

        /**
         * Sets the maximum number of entries used by the log on the telemetry display.
         *
         * @param capacity  the maximum number of lines to retain
         * @see #getCapacity()
         */
        public void setCapacity(int capacity)
            {
            this.capacity = capacity;
            this.prune();
            }

        //------------------------------------------------------------------------------------------
        // Private State
        //------------------------------------------------------------------------------------------

        private Vector<String>  logQueue = new Vector<String>();
        private boolean         newLogMessagesAvailable = false;
        private int             capacity = 9;

        // We just use the outer class so as to *mindlessly* avoid any potential deadlocks
        private Object getLock() { return TelemetryDashboardAndLog.this; }

        private boolean displayOldToNew = true;

        //------------------------------------------------------------------------------------------
        // Operations
        //------------------------------------------------------------------------------------------

        /**
         * Add a new log message to be transmitted to the driver station as soon as we can.
         *
         * @param msg   the message to display in the log
         */
        public void add(String msg)
            {
            synchronized (this.getLock())
                {
                this.logQueue.add(msg);
                this.newLogMessagesAvailable = true;
                this.prune();
                }

            TelemetryDashboardAndLog.this.update(getUpdateIntervalMs(), false);
            }

        /**
         * Clear the contents of the log
         */
        public void clear()
            {
            synchronized (this.getLock())
                {
                this.logQueue.clear();
                this.newLogMessagesAvailable = true;
                }
            }

        private void prune()
            {
            synchronized (this.getLock())
                {
                while (this.logQueue.size() > this.capacity)
                    {
                    this.logQueue.remove(0);
                    }
                }
            }

        }

    //------------------------------------------------------------------------------------------
    // Types
    //------------------------------------------------------------------------------------------

    class Item
        {
        String        caption;
        IFunc<String> value;

        Item(String caption, IFunc<String> value)
            {
            this.caption = caption;
            this.value   = value;
            }

        void composeTo(StringBuilder builder)
            {
            builder.append(this.caption);
            builder.append(this.value.value());
            }
        }

    class Line
        {
        final Item[] items;

        Line(Item[] items)
            {
            this.items = items;
            }

        String compose()
            {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Item item : this.items)
                {
                // Separate the items with the delimiter
                if (!first)
                    {
                    result.append(getItemDelimiter());
                    }
                item.composeTo(result);
                first = false;
                }
            return result.toString();
            }
        }

    }
