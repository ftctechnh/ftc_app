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
 * The dashboard is configured once, using a series of {@link Dashboard#line() line()} 
 * calls each containing a series of {@link org.swerverobotics.library.TelemetryDashboardAndLog.Dashboard#item item()} invocations.
 * You then call {@link Dashboard#update() update()} on the dashboard at a relatively high
 * rate of speed, as often as you like, usually at the bottom of your while(opModeIsActive())
 * loop. Periodically, at a rate controlled by the {@link org.swerverobotics.library.TelemetryDashboardAndLog.Dashboard#msUpdateInterval msUpdateInterval}
 * field, which defaults to one second, these {@link Dashboard#update() update()} calls will actually 
 * cause reevaluation of the dashboard line items and transmission of the data to the driver station. 
 * So: call it often, but the transmission traffic is kept to a reasonable amount.
 * <p>
 * The log simply contains a serial history of the messages it has been asked to display. 
 * When new additions are made to the log, they are conveyed to the driver station in an
 * expeditious manner. The dashboard is also updated at such times.
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
         * is refreshed with new contents of the dashboard. If updates aren't happening
         * frequently enough for you, you can change this value.
         *
         * Note that updates might happen more frequently if messages are written to the log.
         */
        public double msUpdateInterval = 1000;  // default is 1s

        /**
         * itemDelimeter is the text used to separate dashboard items on a single line
         */
        public String itemDelimiter = " | ";

        // We just use the outer class so as to *mindlessly* avoid any potential deadlocks
        private Object getLock() { return TelemetryDashboardAndLog.this; }

        /** the list of actions that are evaluated before the lines are composed */
        private Vector<IAction> actions = null;
        /** the lines that are composed to form the dashboard contents */
        private Vector<Dashboard.Line> lines = null;

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
                        result.append(itemDelimiter);
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
                this.actions = new Vector<IAction>();
                this.lines   = new Vector<Dashboard.Line>();
                }
            TelemetryDashboardAndLog.this.updateLogCapacity();
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
        public void action(IAction action)
            {
            synchronized (this.getLock())
                {
                this.actions.add(action);
                }
            }
        
        //------------------------------------------------------------------------------------------
        // Lines
        //------------------------------------------------------------------------------------------

        /**
         * Add an empty line to the dashboard
         */
        public void line()
            {
            this.line(new Item[] {});
            }
        /**
         * Add a line to the dashboard containing the indicated item
         * 
         * @param item      the item to be contained in the line
         */
        public void line(Item item)
            {
            this.line(new Item[] { item });
            }
        /**
         * Add a line to the dashboard containing the indicated items
         * 
         * @param item0     the first item to be contained in the line
         * @param item1     the second item to be contained in the line
         */
        public void line(Item item0, Item item1)
            {
            this.line(new Item[] { item0, item1 });
            }
        /**
         * Add a line to the dashboard containing the indicated items
         *
         * @param item0     the first item to be contained in the line
         * @param item1     the second item to be contained in the line
         * @param item2     the third item to be contained in the line
         */
        public void line(Item item0, Item item1, Item item2)
            {
            this.line(new Item[] { item0, item1, item2 });
            }
        /**
         * Add a line to the dashboard containing the indicated items

         * @param item0     the first item to be contained in the line
         * @param item1     the second item to be contained in the line
         * @param item2     the third item to be contained in the line
         * @param item3     the fourth item to be contained in the line
         */
        public void line(Item item0, Item item1, Item item2, Item item3)
            {
            this.line(new Item[] { item0, item1, item2, item3 });
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
        public void line(Item item0, Item item1, Item item2, Item item3, Item item4)
            {
            this.line(new Item[] {item0, item1, item2, item3, item4});
            }
        /**
         * Add a line to the dashboard containing the indicated items
         * 
         * @param items     the list of items to be contained in the line
         */
        public void line(Item[] items)
            {
            synchronized (this.getLock())
                {
                Line line = new Line(items);
                this.lines.add(line);
                }
            TelemetryDashboardAndLog.this.updateLogCapacity();
            }
        
        //------------------------------------------------------------------------------------------
        // Emitting
        //------------------------------------------------------------------------------------------

        /**
         * If sufficient time has elapsed since the last driver station refresh,
         * evaluate all the items on all the dashboard lines and update the driver
         * station screen.
         * 
         * @see #msUpdateInterval
         * @see TelemetryDashboardAndLog#update() 
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
    private int                     telemetryMaxLineCount = 9;
    private final int               singletonKey = SynchronousOpMode.staticGetNewSingletonKey();

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
     * Advanced: 'target' is the lower level (ie: non-dashboard/log) telemetry
     * object from the robot controller runtime.
     */
    public final Telemetry          target;

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
        this.dashboard = new Dashboard();
        this.log       = new Log();
        //
        this.dashboard.clear();
        }

    /**
     * 'telemetryDisplayLineCount' is the number of visible on the driver station that
     * we use in rendering the dashboard plus accumulated log. This method returns the
     * current value of that variable.
     * 
     * @return the current maximum number of lines displayed by the log
     * @see #setDisplayMaxLineCount(int) 
     */
    public int getTelemetryMaxLineCount() 
        { 
        return this.telemetryMaxLineCount; 
        }
    /**
     * Set the maximum number of lines displayed in the log.
     * 
     * @param count the maximum number of lines to display in the log
     * @see #getTelemetryMaxLineCount() 
     */
    public void setDisplayMaxLineCount(int count)
        {
        this.telemetryMaxLineCount = count;
        this.updateLogCapacity();
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
                log.capacity = telemetryMaxLineCount - dashboard.lines.size();
                log.prune();
                }
            });
        }

    private static String getKey(int iLine)
        {
        // At present (Aug 8, 2015), the driver station both sorts by the key we return here
        // but also DISPLAYS it! Ugh. So we try to conserve space. And we use Unicode characters
        // that don't actually take up space on the display.
        return String.format("%c", 0x180 + iLine);
        }

    /**
     * Equivalent to {@link Dashboard#update() Dashboard.update()} 
     * 
     * @see Dashboard#update()
     */
    public void update()
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

                    for (IAction action : dashboard.actions)
                        {
                        action.doAction();
                        }

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
