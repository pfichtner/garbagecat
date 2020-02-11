/**********************************************************************************************************************
 * garbagecat                                                                                                         *
 *                                                                                                                    *
 * Copyright (c) 2008-2020 Red Hat, Inc.                                                                              *
 *                                                                                                                    * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse *
 * Public License v1.0 which accompanies this distribution, and is available at                                       *
 * http://www.eclipse.org/legal/epl-v10.html.                                                                         *
 *                                                                                                                    *
 * Contributors:                                                                                                      *
 *    Red Hat, Inc. - initial API and implementation                                                                  *
 *********************************************************************************************************************/
package org.eclipselabs.garbagecat.domain.jdk.unified;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipselabs.garbagecat.domain.JvmRun;
import org.eclipselabs.garbagecat.service.GcManager;
import org.eclipselabs.garbagecat.util.Constants;
import org.eclipselabs.garbagecat.util.jdk.JdkRegEx;
import org.eclipselabs.garbagecat.util.jdk.JdkUtil;
import org.eclipselabs.garbagecat.util.jdk.JdkUtil.LogEventType;
import org.eclipselabs.garbagecat.util.jdk.Jvm;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class TestUnifiedG1YoungPrepareMixedEvent extends TestCase {

    public void testLogLinePreprocessed() {
        String logLine = "[15.108s][info][gc            ] GC(1194) Pause Young (Prepare Mixed) (G1 Evacuation Pause) "
                + "24M->13M(31M) 0.285ms User=0.00s Sys=0.00s Real=0.00s";
        Assert.assertTrue(
                "Log line not recognized as " + JdkUtil.LogEventType.UNIFIED_G1_YOUNG_PREPARE_MIXED.toString() + ".",
                UnifiedG1YoungPrepareMixedEvent.match(logLine));
        UnifiedG1YoungPrepareMixedEvent event = new UnifiedG1YoungPrepareMixedEvent(logLine);
        Assert.assertEquals("Event name incorrect.", JdkUtil.LogEventType.UNIFIED_G1_YOUNG_PREPARE_MIXED.toString(),
                event.getName());
        Assert.assertEquals("Time stamp not parsed correctly.", 15108 - 0, event.getTimestamp());
        Assert.assertTrue("Trigger not parsed correctly.",
                event.getTrigger().matches(JdkRegEx.TRIGGER_G1_EVACUATION_PAUSE));
        Assert.assertEquals("Combined begin size not parsed correctly.", 24 * 1024, event.getCombinedOccupancyInit());
        Assert.assertEquals("Combined end size not parsed correctly.", 13 * 1024, event.getCombinedOccupancyEnd());
        Assert.assertEquals("Combined allocation size not parsed correctly.", 31 * 1024, event.getCombinedSpace());
        Assert.assertEquals("Duration not parsed correctly.", 285, event.getDuration());
        Assert.assertEquals("User time not parsed correctly.", 0, event.getTimeUser());
        Assert.assertEquals("Real time not parsed correctly.", 0, event.getTimeReal());
        Assert.assertEquals("Parallelism not calculated correctly.", 100, event.getParallelism());
    }

    public void testIdentityEventType() {
        String logLine = "[15.108s][info][gc            ] GC(1194) Pause Young (Prepare Mixed) (G1 Evacuation Pause) "
                + "24M->13M(31M) 0.285ms User=0.00s Sys=0.00s Real=0.00s";
        Assert.assertEquals(JdkUtil.LogEventType.UNIFIED_G1_YOUNG_PREPARE_MIXED + "not identified.",
                JdkUtil.LogEventType.UNIFIED_G1_YOUNG_PREPARE_MIXED, JdkUtil.identifyEventType(logLine));
    }

    public void testParseLogLine() {
        String logLine = "[15.108s][info][gc            ] GC(1194) Pause Young (Prepare Mixed) (G1 Evacuation Pause) "
                + "24M->13M(31M) 0.285ms User=0.00s Sys=0.00s Real=0.00s";
        Assert.assertTrue(JdkUtil.LogEventType.UNIFIED_G1_YOUNG_PREPARE_MIXED.toString() + " not parsed.",
                JdkUtil.parseLogLine(logLine) instanceof UnifiedG1YoungPrepareMixedEvent);
    }

    public void testIsBlocking() {
        String logLine = "[15.108s][info][gc            ] GC(1194) Pause Young (Prepare Mixed) (G1 Evacuation Pause) "
                + "24M->13M(31M) 0.285ms User=0.00s Sys=0.00s Real=0.00s";
        Assert.assertTrue(
                JdkUtil.LogEventType.UNIFIED_G1_YOUNG_PREPARE_MIXED.toString() + " not indentified as blocking.",
                JdkUtil.isBlocking(JdkUtil.identifyEventType(logLine)));
    }

    public void testHydration() {
        LogEventType eventType = JdkUtil.LogEventType.UNIFIED_G1_YOUNG_PREPARE_MIXED;
        String logLine = "[15.108s][info][gc            ] GC(1194) Pause Young (Prepare Mixed) (G1 Evacuation Pause) "
                + "24M->13M(31M) 0.285ms User=0.00s Sys=0.00s Real=0.00s";
        long timestamp = 15108;
        int duration = 0;
        Assert.assertTrue(JdkUtil.LogEventType.UNIFIED_G1_YOUNG_PREPARE_MIXED.toString() + " not parsed.",
                JdkUtil.hydrateBlockingEvent(eventType, logLine, timestamp,
                        duration) instanceof UnifiedG1YoungPrepareMixedEvent);
    }

    public void testReportable() {
        Assert.assertTrue(
                JdkUtil.LogEventType.UNIFIED_G1_YOUNG_PREPARE_MIXED.toString() + " not indentified as reportable.",
                JdkUtil.isReportable(JdkUtil.LogEventType.UNIFIED_G1_YOUNG_PREPARE_MIXED));
    }

    public void testUnified() {
        List<LogEventType> eventTypes = new ArrayList<LogEventType>();
        eventTypes.add(LogEventType.UNIFIED_G1_YOUNG_PREPARE_MIXED);
        Assert.assertTrue(
                JdkUtil.LogEventType.UNIFIED_G1_YOUNG_PREPARE_MIXED.toString() + " not indentified as unified.",
                JdkUtil.isUnifiedLogging(eventTypes));
    }

    public void testLogLineWhitespaceAtEnd() {
        String logLine = "[15.108s][info][gc            ] GC(1194) Pause Young (Prepare Mixed) (G1 Evacuation Pause) "
                + "24M->13M(31M) 0.285ms User=0.00s Sys=0.00s Real=0.00s          ";
        Assert.assertTrue(
                "Log line not recognized as " + JdkUtil.LogEventType.UNIFIED_G1_YOUNG_PREPARE_MIXED.toString() + ".",
                UnifiedG1YoungPrepareMixedEvent.match(logLine));
    }

    public void testPreprocessing() {
        // TODO: Create File in platform independent way.
        File testFile = new File("src/test/data/dataset168.txt");
        GcManager gcManager = new GcManager();
        File preprocessedFile = gcManager.preprocess(testFile, null);
        gcManager.store(preprocessedFile, false);
        JvmRun jvmRun = gcManager.getJvmRun(new Jvm(null, null), Constants.DEFAULT_BOTTLENECK_THROUGHPUT_THRESHOLD);
        Assert.assertEquals("Event type count not correct.", 1, jvmRun.getEventTypes().size());
        Assert.assertFalse(JdkUtil.LogEventType.UNKNOWN.toString() + " collector identified.",
                jvmRun.getEventTypes().contains(LogEventType.UNKNOWN));
        Assert.assertTrue(
                "Log line not recognized as " + JdkUtil.LogEventType.UNIFIED_G1_YOUNG_PREPARE_MIXED.toString() + ".",
                jvmRun.getEventTypes().contains(JdkUtil.LogEventType.UNIFIED_G1_YOUNG_PREPARE_MIXED));
    }

}