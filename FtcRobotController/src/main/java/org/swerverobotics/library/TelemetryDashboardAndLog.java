package org.swerverobotics.library;

import com.qualcomm.robotcore.robocol.Telemetry;
import org.swerverobotics.library.interfaces.*;
import org.swerverobotics.library.thunking.*;
import java.util.*;

/**
 *
 */
public class TelemetryDashboardAndLog
    {
    //----------------------------------------------------------------------------------------------
    // Types
    //----------------------------------------------------------------------------------------------

    //==============================================================================================

    public class Dashboard
        {
        //------------------------------------------------------------------------------------------
        // State
        //------------------------------------------------------------------------------------------

        /**
         * msUpdateInterval is the interval in milliseconds at which the drive station
         * is refreshed with new contents of the dashboard.
         *
         * Note that updates might happen more frequently if messages are written to the log.
         */
        public double msUpdateInterval = 1000;  // default is 1s

        /**
         * itemDelimeter is the text used to separate dashboard items on a single line
         */
        public String itemDelimeter = " | ";

        // We just use the outer class so as to *mindlessly* avoid any potential deadlocks
        private Object getLock() { return TelemetryDashboardAndLog.this; }
        private Vector<Dashboard.Line> lines = null;

        //------------------------------------------------------------------------------------------
        // Types
        //------------------------------------------------------------------------------------------

        public class Item
            {
            String        caption;
            IFunc<String> value;

            public void composeTo(StringBuilder builder)
                {
                builder.append(this.caption);
                builder.append(this.value.value());
                }
            }

        public class Line
            {
            List<Item> items;

            public String compose()
                {
                StringBuilder result = new StringBuilder();
                boolean first = true;
                for (Item item : this.items)
                    {
                    // Separate the items with the delimeter
                    if (!first)
                        {
                        result.append(itemDelimeter);
                        }
                    item.composeTo(result);
                    first = false;
                    }
                return result.toString();
                }
            }

        //------------------------------------------------------------------------------------------
        // Evaluation
        //------------------------------------------------------------------------------------------

        /**
         * (Re)initialize the dashboard to contain no lines or items
         */
        public void clear()
            {
            synchronized (this.getLock())
                {
                this.lines = new Vector<Dashboard.Line>();
                }
            TelemetryDashboardAndLog.this.updateLogCapacity();
            }

        //------------------------------------------------------------------------------------------
        // Items
        //------------------------------------------------------------------------------------------

        /**
         * Create a new dashboard item with the indicated caption and value computation.
         */
        public Item item(final String itemCaption, final IFunc<Object> itemValue)
            {
            Item result = new Item();
            result.caption = itemCaption;
            result.value = new IFunc<String>()
                {
                @Override public String value()
                    {
                    return itemValue.value().toString();
                    }
                };
            return result;
            }

        //------------------------------------------------------------------------------------------
        // Lines
        //------------------------------------------------------------------------------------------

        /**
         * Add an empty line to the dashboard
         */
        public void line()
            {
            this.line(new ArrayList<Item>());
            }
        /**
         * Add a line to the dashboard containing the indicated item
         */
        public void line(Item item)
            {
            ArrayList<Item> items = new ArrayList<Item>();
            items.add(item);
            this.line(items);
            }
        /**
         * Add a line to the dashboard containing the indicated items
         */
        public void line(Item item0, Item item1)
            {
            ArrayList<Item> items = new ArrayList<Item>();
            items.add(item0);
            items.add(item1);
            this.line(items);
            }
        /**
         * Add a line to the dashboard containing the indicated items
         */
        public void line(Item item0, Item item1, Item item2)
            {
            ArrayList<Item> items = new ArrayList<Item>();
            items.add(item0);
            items.add(item1);
            items.add(item2);
            this.line(items);
            }
        /**
         * Add a line to the dashboard containing the indicated items
         */
        public void line(Item item0, Item item1, Item item2, Item item3)
            {
            ArrayList<Item> items = new ArrayList<Item>();
            items.add(item0);
            items.add(item1);
            items.add(item2);
            items.add(item3);
            this.line(items);
            }
        /**
         * Add a line to the dashboard containing the indicated items
         */
        public void line(Item item0, Item item1, Item item2, Item item3, Item item4)
            {
            ArrayList<Item> items = new ArrayList<Item>();
            items.add(item0);
            items.add(item1);
            items.add(item2);
            items.add(item3);
            items.add(item4);
            this.line(items);
            }
        /**
         * Add a line to the dashboard containing the indicated items
         */
        public void line(List<Item> items)
            {
            synchronized (this.getLock())
                {
                Line line = new Line();
                line.items = items;
                this.lines.add(line);
                }
            TelemetryDashboardAndLog.this.updateLogCapacity();
            }

        //------------------------------------------------------------------------------------------
        // Emitting
        //------------------------------------------------------------------------------------------

        /**
         * Update the driver station view of the dashboard.
         */
        public void update()
            {
            TelemetryDashboardAndLog.this.update();
            }
        }

    //==============================================================================================

    public class Log
        {
        //------------------------------------------------------------------------------------------
        // State
        //------------------------------------------------------------------------------------------

        private Vector<String> logQueue = new Vector<String>();
        private boolean       newLogMessagesAvailable = false;
        private int           capacity = 0;     // this gets automatically computed

        // We just use the outer class so as to *mindlessly* avoid any potential deadlocks
        private Object getLock() { return TelemetryDashboardAndLog.this; }

        /**
         * Is the log shown in reversed order instead of normal order?
         */
        public boolean        displayOldToNew = true;

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
                //
                this.prune();
                }

            TelemetryDashboardAndLog.this.update();
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

    //==============================================================================================

    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private long                    nanoLastUpdate = 0;
    private int                     singletonKey = SynchronousOpMode.getNewExecuteSingletonKey();

    /**
     * 'dashboard' provides a means to declaratively indicate telemetry items of interest.
     *
     * One should fairly often call 'update' on the dashboard from a synchronized thread
     * in order to update the driver station view of the dashboard.
     */
    public final Dashboard          dashboard;
    /**
     * 'log' provides a means by which a scrolling history of events can be recorded on
     * the driver station
     */
    public final Log                log;
    /**
     * Advanced: 'raw' provides access to the lower level (ie: non-dashboard/log) telemetry
     * API.
     *
     * The ThunkedTelemetry object here can only be called from a synchronous thread; the
     * robot-controller-runtime-provided object, callable on the loop() thread, can be retrieved using
     * the raw.getTarget() method.
     */
    public final ThunkedTelemetry   raw;
    /**
     * 'telemetryDisplayLineCount' is the number of visible lines we have room for on the
     * driver station.
     */
    public int                      telemetryDisplayLineCount = 9;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public TelemetryDashboardAndLog(Telemetry telemetry)
        {
        this.raw       = ThunkedTelemetry.create(telemetry);
        this.dashboard = new Dashboard();
        this.log       = new Log();
        //
        this.dashboard.clear();
        }

    //----------------------------------------------------------------------------------------------
    // Emitting
    //----------------------------------------------------------------------------------------------

    /**
     * If you want to acquire both the dashboard and the log lock, then this method
     * does it in the right order. For safety's sake, it is preferable that neither
     * lock be held when calling; however (with the current implementation) it is in
     * fact permissible to hold the dashboard lock when calling.
     *
     * Note that at the present time, this is moot, since the log and the dashboard
     * in fact do not have separate locks. But we keep the API here for (future) semantic
     * clarity in case we might wish to change our minde about that.
     */
    private void synchronizeDashboardAndLog(IAction action)
        {
        synchronized (this.dashboard.getLock())
            {
            synchronized (this.log.getLock())
                {
                action.doAction();
                }
            }
        }

    private void updateLogCapacity()
        {
        this.synchronizeDashboardAndLog(new IAction()
            {
            @Override public void doAction()
                {
                log.capacity = telemetryDisplayLineCount - dashboard.lines.size();
                log.prune();
                }
            });
        }

    private static String getKey(int iLine)
        {
        // At present (Aug 8, 2015), the driver station both sorts by the key we return here
        // but also DISPLAYS it! Ugh. So we try to conserve space.
        return String.format("%c", 'a' + iLine);
        }

    /**
     * At the next available loop() thread call, update the telemetry to reflect the state
     * of the dashboard and log.
     */
    private void update()
        {
        // We need both the dashboard and log locked while we copy their contents
        this.synchronizeDashboardAndLog(new IAction()
            {
            @Override public void doAction()
                {
                // Don't actually put out updates too often in order to avoid flooding. So,
                // log additions flow as soon as we can, but otherwise, we only send things to
                // the driver station at periodic intervals.
                long nanoNow = System.nanoTime();
                if (nanoLastUpdate == 0
                        || nanoNow > nanoLastUpdate + dashboard.msUpdateInterval * SynchronousOpMode.NANO_TO_MILLI
                        || log.newLogMessagesAvailable
                        )
                    {
                    // Ok, we're going to update the telemetry: get a copy of all the data to output.
                    // We only use strings as values. Keys we make up in alphabetical order so as
                    // to maintaining the ordering in which they are created.
                    final Vector<String> keys = new Vector<String>();
                    final Vector<String> values = new Vector<String>();
                    int iLine = 0;

                    for (Dashboard.Line line : dashboard.lines)
                        {
                        keys.add(getKey(iLine));
                        values.add(dashboard.lines.elementAt(iLine).compose());
                        iLine++;
                        }

                    int size = log.logQueue.size();
                    for (int i=0; i < size; i++)
                        {
                        String s = log.displayOldToNew ? log.logQueue.elementAt(i) : log.logQueue.elementAt(size-1-i);
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
                                TelemetryDashboardAndLog.this.raw.target.addData(
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
                        // here, directly on this thread.
                        action.doAction();
                        }

                    // Update our state for the next time around
                    nanoLastUpdate = nanoNow;
                    log.newLogMessagesAvailable = false;
                    }
                }
            });
        }
    }
