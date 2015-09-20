package org.swerverobotics.library;

import com.qualcomm.robotcore.robocol.Telemetry;
import org.swerverobotics.library.interfaces.*;
import org.swerverobotics.library.internal.*;
import java.util.*;

/**
 * TelemetryDashboardAndLog is a telemetry helper class that makes it easier write 
 * telemetry to the driver station. It is primarily intended to be used within
 * SynchronousOpModes.
 * <p>
 * The telemetry is provided in two parts: a dashboard, and a log, instances of which 
 * reside in fields of the same names within the TelemetryDashboardAndLog object.
 * <p>
 * The dashboard is configured once, using a series of {@link #addLine() line()}
 * calls each containing a series of {@link #item item()} invocations.
 * You then call {@link #update() update()} on at a relatively high
 * rate of speed, as often as you like, usually at the bottom of your while(opModeIsActive())
 * loop. Periodically, at a rate controlled by the {@link #msUpdateInterval msUpdateInterval}
 * field, which defaults to one second, these {@link #update() update()} calls will actually
 * cause reevaluation of the dashboard line items and transmission of the data to the driver station. 
 * So: call it often, but the transmission traffic is kept to a reasonable amount.
 * <p>
 * The log simply contains a serial history of the messages it has been asked to display. 
 * When new additions are made to the log, they are conveyed to the driver station in an
 * expeditious manner. The dashboard is also updated at such times.
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
     * Sets the current delimiter used to separate dashboard items on a single line
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
     * @see #setUpdateIntervalMs(double)
     */
    public double getUpdateIntervalMs()
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
    public void setUpdateIntervalMs(double msUpdateInterval)
        {
        this.msUpdateInterval = msUpdateInterval;
        }

    /**
     * Advanced: 'target' is the lower level robot-controller-runtime-provided telemetry object
     */
    public final Telemetry target;

    //------------------------------------------------------------------------------------------
    // Private State
    //------------------------------------------------------------------------------------------

    private String itemDelimiter    = " | ";
    private double msUpdateInterval = 1000;  // default is 1s

    /** the list of actions that are evaluated before the lines are composed */
    private Vector<IAction>         actions = null;
    /** the lines that are composed to form the dashboard contents */
    private Vector<Line>            lines = null;

    private long                    nanoLastUpdate = 0;
    private final int               singletonKey = SynchronousOpMode.staticGetNewSingletonKey();

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    /**
     * Instantiate a new telemetry dashboard and log
     * @param telemetry the robot controller runtime telemetry object
     */
    public TelemetryDashboardAndLog(Telemetry telemetry)
        {
        this.target    = telemetry;
        this.log       = new Log();
        //
        this.clearDashboard();
        }

    //------------------------------------------------------------------------------------------
    // Robot controller runtime Telemetry API support
    //------------------------------------------------------------------------------------------

    public synchronized void addData(String key, String msg)
        {
        // this.b.put(key, msg);
        }

    public synchronized void addData(String key, Object msg)
        {
        // this.b.put(key, msg.toString());
        }

    public synchronized void addData(String key, float msg)
        {
        // this.c.put(key, Float.valueOf(msg));
        }

    public synchronized void addData(String key, double msg)
        {
        // this.c.put(key, Float.valueOf((float) msg));
        }

    //------------------------------------------------------------------------------------------
    // Updating
    //------------------------------------------------------------------------------------------

    /**
     * (Re)initialize the dashboard to contain no lines or items
     */
    public synchronized void clearDashboard()
        {
        this.actions = new Vector<IAction>();
        this.lines   = new Vector<Line>();
        }

    private static String getKey(int iLine)
        {
        // At present (Aug 8, 2015), the driver station both sorts by the key we return here
        // but also DISPLAYS it! Ugh. So we try to conserve space. And we use Unicode characters
        // that don't actually take up space on the display.
        return String.format("%c", 0x180 + iLine);
        }

     /**
     * If sufficient time has elapsed since the last driver station refresh,
     * evaluate all the items on all the dashboard lines and update the driver
     * station screen.
     *
     * @see #getUpdateIntervalMs()
     */
    public synchronized void update()
        {
        // Don't actually put out updates too often in order to avoid flooding. So,
        // log additions flow as soon as we can, but otherwise, we only send things to
        // the driver station at periodic intervals.
        long nanoNow = System.nanoTime();
        if (nanoLastUpdate == 0
                || nanoNow > nanoLastUpdate + getUpdateIntervalMs() * SynchronousOpMode.NANO_TO_MILLI
                || log.newLogMessagesAvailable
                )
            {
            // Ok, we're going to update the telemetry: get a copy of all the data to output.
            // We only use strings as values. Keys we make up in alphabetical order so as
            // to maintaining the ordering in which they are created.

            for (IAction action : this.actions)
                {
                action.doAction();
                }

            final Vector<String> keys = new Vector<String>();
            final Vector<String> values = new Vector<String>();
            int iLine = 0;

            for (Line line : this.lines)
                {
                keys.add(getKey(iLine));
                values.add(lines.elementAt(iLine).compose());
                iLine++;
                }

            int size = this.log.logQueue.size();
            for (int i = 0; i < size; i++)
                {
                String s = this.log.displayOldToNew ? this.log.logQueue.elementAt(i) : this.log.logQueue.elementAt(size - 1 - i);
                keys.add(getKey(iLine));
                values.add(s);
                iLine++;
                }

            IAction action = new IAction()
                {
                @Override public void doAction()
                    {
                    for (int i = 0; i < keys.size(); i++)
                        {
                        TelemetryDashboardAndLog.this.target.addData(
                                keys.elementAt(i),
                                values.elementAt(i));
                        }
                    }
                };

            if (SynchronousThreadContext.isSynchronousThread())
                {
                // Head on over to the loop() thread and add these messages to the
                // (unthunked) telemetry. However, we only do that once per loop() call;
                // if we attempt two of these within one loop() quantum (by, e.g., issuing
                // a bunch of log.add() calls), then only the last one will actually manifest
                // itself and thus get back to the driver station.
                SynchronousOpMode.getThreadThunker().executeSingletonOnLoopThread(singletonKey, action);
                }
            else
                {
                // We're not on a synchronous thread. Presumably, we're on the loop() thread,
                // though we can't confirm that. In any case, update the unthunked telemetry
                // here, directly on this thread, and we'll live with the consequences.
                action.doAction();
                }

            // Update our state for the next time around
            this.nanoLastUpdate = nanoNow;
            this.log.newLogMessagesAvailable = false;
            }
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
     * @param action
     */
    public synchronized void action(IAction action)
        {
        this.actions.add(action);
        }
        
    //------------------------------------------------------------------------------------------
    // Lines
    //------------------------------------------------------------------------------------------

    /**
     * Add an empty line to the dashboard
     */
    public void addLine()
        {
        this.addLine(new Item[]{});
        }
    /**
     * Add a line to the dashboard containing the indicated item
     *
     * @param item      the item to be contained in the line
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
     */
    public void addLine(Item item0, Item item1, Item item2, Item item3, Item item4)
        {
        this.addLine(new Item[]{item0, item1, item2, item3, item4});
        }
    /**
     * Add a line to the dashboard containing the indicated items
     *
     * @param items     the list of items to be contained in the line
     */
    public synchronized void addLine(Item[] items)
        {
        Line line = new Line(items);
        this.lines.add(line);
        }

    //==============================================================================================

    public class Log
        {
        //------------------------------------------------------------------------------------------
        // Public state
        //------------------------------------------------------------------------------------------

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

        /**
         * Is the log shown in reversed order instead of normal order?
         * <p></p>
         * If true, older log messages are displayed at the top of the log, newer messages at the bottom.
         * <p></p>
         * If false, newer messages are on top, older messages at the bottom.
         */
        public boolean displayOldToNew = true;

        //------------------------------------------------------------------------------------------
        // Operations
        //------------------------------------------------------------------------------------------

        /**
         * Add a new log message to be emitted to the telemetry as soon as we can
         */
        public void add(String msg)
            {
            synchronized (this.getLock())
                {
                this.logQueue.add(msg);
                this.newLogMessagesAvailable = true;
                this.prune();
                }

            TelemetryDashboardAndLog.this.update();
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
