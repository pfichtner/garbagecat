/**********************************************************************************************************************
 * garbagecat                                                                                                         *
 *                                                                                                                    *
 * Copyright (c) 2008-2021 Mike Millson                                                                               *
 *                                                                                                                    * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse *
 * Public License v1.0 which accompanies this distribution, and is available at                                       *
 * http://www.eclipse.org/legal/epl-v10.html.                                                                         *
 *                                                                                                                    *
 * Contributors:                                                                                                      *
 *    Mike Millson - initial API and implementation                                                                   *
 *********************************************************************************************************************/
package org.eclipselabs.garbagecat.domain.jdk;

import static org.eclipselabs.garbagecat.util.Memory.memory;
import static org.eclipselabs.garbagecat.util.Memory.Unit.KILOBYTES;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipselabs.garbagecat.domain.BlockingEvent;
import org.eclipselabs.garbagecat.domain.CombinedData;
import org.eclipselabs.garbagecat.domain.ParallelEvent;
import org.eclipselabs.garbagecat.domain.PermMetaspaceData;
import org.eclipselabs.garbagecat.util.Memory;
import org.eclipselabs.garbagecat.util.jdk.JdkMath;
import org.eclipselabs.garbagecat.util.jdk.JdkRegEx;
import org.eclipselabs.garbagecat.util.jdk.JdkUtil;
import org.eclipselabs.garbagecat.util.jdk.unified.UnifiedRegEx;
import org.eclipselabs.garbagecat.util.jdk.unified.UnifiedUtil;

/**
 * <p>
 * SHENANDOAH_DEGENERATED_GC_MARK
 * </p>
 * 
 * <p>
 * When allocation failure occurs, degenerated GC continues the in-progress "concurrent" cycle under stop-the-world.[1].
 * 
 * [1]<a href="https://wiki.openjdk.java.net/display/shenandoah/Main">Shenandoah GC</a>
 * </p>
 * 
 * <h3>Example Logging</h3>
 * 
 * <p>
 * 1) Non-Unified:
 * </p>
 * 
 * <pre>
 * 2020-08-18T14:05:42.515+0000: 854868.165: [Pause Degenerated GC (Mark) 93058M-&gt;29873M(98304M), 1285.045 ms]
 * </pre>
 * 
 * <p>
 * 2) Unified:
 * </p>
 * 
 * <pre>
 * [52.937s][info][gc           ] GC(1632) Pause Degenerated GC (Mark) 60M-&gt;30M(64M) 53.697ms
 * </pre>
 * 
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class ShenandoahDegeneratedGcMarkEvent extends ShenandoahCollector
        implements BlockingEvent, ParallelEvent, CombinedData, PermMetaspaceData {

    /**
     * The log entry for the event. Can be used for debugging purposes.
     */
    private String logEntry;

    /**
     * The elapsed clock time for the GC event in microseconds (rounded).
     */
    private int duration;

    /**
     * The time when the GC event started in milliseconds after JVM startup.
     */
    private long timestamp;

    /**
     * Combined size at beginning of GC event.
     */
    private Memory combined;

    /**
     * Combined size at end of GC event.
     */
    private Memory combinedEnd;

    /**
     * Combined available space.
     */
    private Memory combinedAvailable;

    /**
     * Permanent generation size at beginning of GC event.
     */
    private Memory permGen;

    /**
     * Permanent generation size at end of GC event.
     */
    private Memory permGenEnd;

    /**
     * Space allocated to permanent generation.
     */
    private Memory permGenAllocation;

    /**
     * Regular expressions defining the logging.
     */
    private static final String REGEX = "^(" + JdkRegEx.DECORATOR + "|" + UnifiedRegEx.DECORATOR
            + ") [\\[]{0,1}Pause Degenerated GC \\((Mark|Outside of Cycle)\\) " + JdkRegEx.SIZE + "->" + JdkRegEx.SIZE
            + "\\(" + JdkRegEx.SIZE + "\\)[,]{0,1} " + UnifiedRegEx.DURATION + "[]]{0,1}( Metaspace: " + JdkRegEx.SIZE
            + "->" + JdkRegEx.SIZE + "\\(" + JdkRegEx.SIZE + "\\))?[ ]*$";

    private static final Pattern pattern = Pattern.compile(REGEX);

    /**
     * Create event from log entry.
     * 
     * @param logEntry
     *            The log entry for the event.
     */
    public ShenandoahDegeneratedGcMarkEvent(String logEntry) {
        this.logEntry = logEntry;
        if (logEntry.matches(REGEX)) {
            Pattern pattern = Pattern.compile(REGEX);
            Matcher matcher = pattern.matcher(logEntry);
            if (matcher.find()) {
                duration = JdkMath.convertMillisToMicros(matcher.group(45)).intValue();
                if (matcher.group(1).matches(UnifiedRegEx.DECORATOR)) {
                    long endTimestamp;
                    if (matcher.group(12).matches(UnifiedRegEx.UPTIMEMILLIS)) {
                        endTimestamp = Long.parseLong(matcher.group(23));
                    } else if (matcher.group(12).matches(UnifiedRegEx.UPTIME)) {
                        endTimestamp = JdkMath.convertSecsToMillis(matcher.group(22)).longValue();
                    } else {
                        if (matcher.group(25) != null) {
                            if (matcher.group(25).matches(UnifiedRegEx.UPTIMEMILLIS)) {
                                endTimestamp = Long.parseLong(matcher.group(27));
                            } else {
                                endTimestamp = JdkMath.convertSecsToMillis(matcher.group(26)).longValue();
                            }
                        } else {
                            // Datestamp only.
                            endTimestamp = UnifiedUtil.convertDatestampToMillis(matcher.group(12));
                        }
                    }
                    timestamp = endTimestamp - JdkMath.convertMicrosToMillis(duration).longValue();
                } else {
                    timestamp = JdkMath.convertSecsToMillis(matcher.group(11)).longValue();
                }
                combined = memory(matcher.group(36), matcher.group(38).charAt(0)).convertTo(KILOBYTES);
                combinedEnd = memory(matcher.group(39), matcher.group(41).charAt(0)).convertTo(KILOBYTES);
                combinedAvailable = memory(matcher.group(42), matcher.group(44).charAt(0)).convertTo(KILOBYTES);
                if (matcher.group(46) != null) {
                    permGen = memory(matcher.group(47), matcher.group(49).charAt(0)).convertTo(KILOBYTES);
                    permGenEnd = memory(matcher.group(50), matcher.group(52).charAt(0)).convertTo(KILOBYTES);
                    permGenAllocation = memory(matcher.group(53), matcher.group(55).charAt(0)).convertTo(KILOBYTES);
                }
            }
        }
    }

    /**
     * Alternate constructor. Create event from values.
     * 
     * @param logEntry
     *            The log entry for the event.
     * @param timestamp
     *            The time when the GC event started in milliseconds after JVM startup.
     * @param duration
     *            The elapsed clock time for the GC event in microseconds.
     */
    public ShenandoahDegeneratedGcMarkEvent(String logEntry, long timestamp, int duration) {
        this.logEntry = logEntry;
        this.timestamp = timestamp;
        this.duration = duration;
    }

    public String getLogEntry() {
        return logEntry;
    }

    public int getDuration() {
        return duration;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Memory getCombinedOccupancyInit() {
        return combined;
    }

    public Memory getCombinedOccupancyEnd() {
        return combinedEnd;
    }

    public Memory getCombinedSpace() {
        return combinedAvailable;
    }

    public Memory getPermOccupancyInit() {
        return permGen;
    }

    protected void setPermOccupancyInit(Memory permGen) {
        this.permGen = permGen;
    }

    public Memory getPermOccupancyEnd() {
        return permGenEnd;
    }

    protected void setPermOccupancyEnd(Memory permGenEnd) {
        this.permGenEnd = permGenEnd;
    }

    public Memory getPermSpace() {
        return permGenAllocation;
    }

    protected void setPermSpace(Memory permGenAllocation) {
        this.permGenAllocation = permGenAllocation;
    }

    public String getName() {
        return JdkUtil.LogEventType.SHENANDOAH_DEGENERATED_GC_MARK.toString();
    }

    /**
     * Determine if the logLine matches the logging pattern(s) for this event.
     * 
     * @param logLine
     *            The log line to test.
     * @return true if the log line matches the event pattern, false otherwise.
     */
    public static final boolean match(String logLine) {
        return pattern.matcher(logLine).matches();
    }
}
