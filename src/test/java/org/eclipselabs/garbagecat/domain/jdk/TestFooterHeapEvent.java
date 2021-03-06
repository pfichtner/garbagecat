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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipselabs.garbagecat.util.jdk.JdkUtil;
import org.eclipselabs.garbagecat.util.jdk.JdkUtil.LogEventType;
import org.eclipselabs.garbagecat.util.jdk.unified.UnifiedUtil;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
class TestFooterHeapEvent {

    @Test
    void testLineJdk8Heap() {
        String logLine = "Heap";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedHeap() {
        String logLine = "[25.016s][info][gc,heap,exit  ] Heap";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedHeapUptimeMillis() {
        String logLine = "[2019-02-05T15:10:08.998-0200][1357910ms] Heap";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedHeap1SpaceAfterExit() {
        String logLine = "[69.946s][info][gc,heap,exit ] Heap";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedHeap3SpacesAfterExit() {
        String logLine = "[32.839s][info][gc,heap,exit   ] Heap";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testIdentityEventType() {
        String logLine = "[25.016s][info][gc,heap,exit  ] Heap";
        assertEquals(JdkUtil.LogEventType.FOOTER_HEAP, JdkUtil.identifyEventType(logLine),
                JdkUtil.LogEventType.FOOTER_HEAP + "not identified.");
    }

    @Test
    void testParseLogLine() {
        String logLine = "[25.016s][info][gc,heap,exit  ] Heap";
        assertTrue(JdkUtil.parseLogLine(logLine) instanceof FooterHeapEvent,
                JdkUtil.LogEventType.FOOTER_HEAP.toString() + " not parsed.");
    }

    @Test
    void testNotBlocking() {
        String logLine = "[25.016s][info][gc,heap,exit  ] Heap";
        assertFalse(JdkUtil.isBlocking(JdkUtil.identifyEventType(logLine)),
                JdkUtil.LogEventType.FOOTER_HEAP.toString() + " incorrectly indentified as blocking.");
    }

    @Test
    void testReportable() {
        assertFalse(JdkUtil.isReportable(JdkUtil.LogEventType.FOOTER_HEAP),
                JdkUtil.LogEventType.FOOTER_HEAP.toString() + " incorrectly indentified as reportable.");
    }

    @Test
    void testUnified() {
        List<LogEventType> eventTypes = new ArrayList<LogEventType>();
        eventTypes.add(LogEventType.FOOTER_HEAP);
        assertFalse(UnifiedUtil.isUnifiedLogging(eventTypes),
                JdkUtil.LogEventType.FOOTER_HEAP.toString() + " incorrectly indentified as unified.");
    }

    @Test
    void testLineUnifiedGarbageFirst() {
        String logLine = "[25.016s][info][gc,heap,exit  ]  garbage-first heap   total 59392K, used 38015K "
                + "[0x00000000fc000000, 0x0000000100000000)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedShenandoah() {
        String logLine = "[69.946s][info][gc,heap,exit ] Shenandoah Heap";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedShenandoahUptimeMillis() {
        String logLine = "[2019-02-05T15:10:08.998-0200][1357910ms] Shenandoah Heap";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineJdk8ShenandoahTotalCommittedUsed() {
        String logLine = " 128M total, 128M committed, 102M used";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedShenandoahTotalCommittedUsed() {
        String logLine = "[69.946s][info][gc,heap,exit ]  65536K total, 65536K committed, 55031K used";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedShenandoahTotalCommittedUsedUptimeMillis() {
        String logLine = "[2019-02-05T15:10:08.998-0200][1357910ms]  1334272K total, 107008K committed, 80727K used";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineJdk8ShenandoahRegions() {
        String logLine = " 512 x 256K regions";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedShenandoahRegions() {
        String logLine = "[69.946s][info][gc,heap,exit ]  256 x 256K regions";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedShenandoahRegionsUptimeMillis() {
        String logLine = "[2019-02-05T15:10:08.998-0200][1357910ms]  2606 x 512K regions";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedStatus() {
        String logLine = "[69.946s][info][gc,heap,exit ] Status: cancelled";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedStatusUptimeMillis() {
        String logLine = "[2019-02-05T15:10:08.998-0200][1357910ms] Status: cancelled";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineJdk8StatusHasForwarded() {
        String logLine = "Status: has forwarded objects, cancelled";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedStatusHasForwarded() {
        String logLine = "[103.682s][info][gc,heap,exit ] Status: has forwarded objects, cancelled";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineJdk8ReservedRegion() {
        String logLine = "Reserved region:";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedReservedRegion() {
        String logLine = "[69.946s][info][gc,heap,exit ] Reserved region:";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedReservedRegionUptimeMillis() {
        String logLine = "[2019-02-05T15:10:08.998-0200][1357910ms] Reserved region:";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineJdk8ReservedRegionAddress() {
        String logLine = " - [0x00000000f8000000, 0x0000000100000000)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedReservedRegionAddress() {
        String logLine = "[69.946s][info][gc,heap,exit ]  - [0x00000000fc000000, 0x0000000100000000)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedReservedRegionAddressUptimeMillis() {
        String logLine = "[2019-02-05T15:10:08.998-0200][1357910ms]  - [0x00000000ae900000, 0x0000000100000000) ";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedRegion() {
        String logLine = "[25.016s][info][gc,heap,exit  ]   region size 1024K, 13 young (13312K), 1 survivors (1024K)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedRegion3DigitYoung2DigutSurvivors() {
        String logLine = "[2020-03-12T13:13:49.821-0400][26578ms]   region size 1024K, 260 young (266240K), 26 "
                + "survivors (26624K)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedMetaspace() {
        String logLine = "[25.016s][info][gc,heap,exit  ]  Metaspace       used 11079K, capacity 11287K, "
                + "committed 11520K, reserved 1060864K";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedMetaspaceUptimeMillis() {
        String logLine = "[2019-02-05T15:10:08.998-0200][1357910ms]  Metaspace       used 80841K, capacity 89293K, "
                + "committed 89600K, reserved 331776K";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedMetaspace1SpaceAfterExit() {
        String logLine = "[69.946s][info][gc,heap,exit ]  Metaspace       used 4066K, capacity 7271K, committed "
                + "7296K, reserved 1056768K";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedMetaspace3SpacesAfterExit() {
        String logLine = "[32.839s][info][gc,heap,exit   ]  Metaspace       used 4109K, capacity 7271K, committed "
                + "7296K, reserved 1056768K";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedClass() {
        String logLine = "[25.016s][info][gc,heap,exit  ]   class space    used 909K, capacity 995K, committed 1024K, "
                + "reserved 1048576K";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedClassUptimeMillis() {
        String logLine = "[2019-02-05T15:10:08.998-0200][1357910ms]   class space    used 10193K, capacity 13027K, "
                + "committed 13056K, reserved 253952K";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedClass1SpaceAfterExit() {
        String logLine = "[69.946s][info][gc,heap,exit ]   class space    used 299K, capacity 637K, committed 640K, "
                + "reserved 1048576K";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedDefNew() {
        String logLine = "[32.839s][info][gc,heap,exit   ]  def new generation   total 11456K, used 4604K "
                + "[0x00000000fc000000, 0x00000000fcc60000, 0x00000000fd550000)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedEden() {
        String logLine = "[32.839s][info][gc,heap,exit   ]   eden space 10240K,  43% used [0x00000000fc000000, "
                + "0x00000000fc463ed8, 0x00000000fca00000)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedEdenNoSpacesBetweenAddresses() {
        String logLine = "[37.098s][info][gc,heap,exit   ]   eden space 20480K, 33% used [0x00000000feb00000,"
                + "0x00000000ff1cb940,0x00000000fff00000)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedFromSpace() {
        String logLine = "[32.839s][info][gc,heap,exit   ]   from space 1216K,   8% used [0x00000000fca00000, "
                + "0x00000000fca1b280, 0x00000000fcb30000)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedToSpace() {
        String logLine = "[32.839s][info][gc,heap,exit   ]   to   space 1216K,   0% used [0x00000000fcb30000, "
                + "0x00000000fcb30000, 0x00000000fcc60000)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedTenured() {
        String logLine = "[32.839s][info][gc,heap,exit   ]  tenured generation   total 25240K, used 24218K "
                + "[0x00000000fd550000, 0x00000000fedf6000, 0x0000000100000000)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedTheSpace() {
        String logLine = "[32.839s][info][gc,heap,exit   ]    the space 25240K,  95% used [0x00000000fd550000, "
                + "0x00000000fecf6b58, 0x00000000fecf6c00, 0x00000000fedf6000)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedPsYoungGen() {
        String logLine = "[37.098s][info][gc,heap,exit   ]  PSYoungGen      total 20992K, used 7054K "
                + "[0x00000000feb00000, 0x0000000100000000, 0x0000000100000000)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedPsOldGen() {
        String logLine = "[37.098s][info][gc,heap,exit   ]  PSOldGen        total 32768K, used 27239K "
                + "[0x00000000fc000000, 0x00000000fe000000, 0x00000000feb00000)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedParOldGen() {
        String logLine = "[37.742s][info][gc,heap,exit   ]  ParOldGen       total 30720K, used 27745K "
                + "[0x00000000fc000000, 0x00000000fde00000, 0x00000000feb00000)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedObjectSpace() {
        String logLine = "[37.098s][info][gc,heap,exit   ]   object space 32768K, 83% used [0x00000000fc000000,"
                + "0x00000000fda99f58,0x00000000fe000000)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedParNewGeneration() {
        String logLine = "[59.713s][info][gc,heap,exit ]  par new generation   total 1152K, used 713K "
                + "[0x00000000fc000000, 0x00000000fc140000, 0x00000000fd550000)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedParCmsGeneration() {
        String logLine = "[59.713s][info][gc,heap,exit ]  concurrent mark-sweep generation total 31228K, used 25431K "
                + "[0x00000000fd550000, 0x00000000ff3cf000, 0x0000000100000000)";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineJdk8Collection() {
        String logLine = "Collection set:";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedCollection() {
        String logLine = "[103.682s][info][gc,heap,exit ] Collection set:";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineJdk8MapVanilla() {
        String logLine = " - map (vanilla): 0x00007f271b2e5e00";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedMapVanilla() {
        String logLine = "[103.683s][info][gc,heap,exit ]  - map (vanilla): 0x00007fa7ea119f00";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineJdk8MapBiased() {
        String logLine = " - map (biased):  0x00007f271b2e2000";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineUnifiedMapBiased() {
        String logLine = "[103.683s][info][gc,heap,exit ]  - map (biased):  0x00007fa7ea116000";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }

    @Test
    void testLineShenandoahSoftMax() {
        String logLine = "[2021-01-25T17:44:28.636-0500]  98304K max, 98304K soft max, 98304K committed, 58219K used";
        assertTrue(FooterHeapEvent.match(logLine),
                "Log line not recognized as " + JdkUtil.LogEventType.FOOTER_HEAP.toString() + ".");
    }
}
