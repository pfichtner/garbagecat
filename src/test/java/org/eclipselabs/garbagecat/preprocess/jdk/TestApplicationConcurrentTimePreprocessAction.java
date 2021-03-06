/**********************************************************************************************************************
 * garbagecat                                                                                                         *
 *                                                                                                                    *
 * Copyright (c) 2008-2020 Mike Millson                                                                               *
 *                                                                                                                    * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse *
 * Public License v1.0 which accompanies this distribution, and is available at                                       *
 * http://www.eclipse.org/legal/epl-v10.html.                                                                         *
 *                                                                                                                    *
 * Contributors:                                                                                                      *
 *    Mike Millson - initial API and implementation                                                                   *
 *********************************************************************************************************************/
package org.eclipselabs.garbagecat.preprocess.jdk;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import org.eclipselabs.garbagecat.util.jdk.JdkUtil;



/**
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class TestApplicationConcurrentTimePreprocessAction {

    @Test
    public void testLine1Timestamp() {
        String priorLogLine = "";
        String logLine = "1122748.949Application time: 0.0005210 seconds";
        assertTrue("Log line not recognized as "
                + JdkUtil.PreprocessActionType.APPLICATION_CONCURRENT_TIME.toString() + ".",
                ApplicationConcurrentTimePreprocessAction.match(logLine, priorLogLine));
    }

    @Test
    public void testLine1CmsConcurrent() {
        String priorLogLine = "";
        String logLine = "1987600.604: [CMS-concurrent-preclean: 0.016/0.017 secs]Application time: "
                + "4.5432350 seconds";
        assertTrue("Log line not recognized as "
                + JdkUtil.PreprocessActionType.APPLICATION_CONCURRENT_TIME.toString() + ".",
                ApplicationConcurrentTimePreprocessAction.match(logLine, priorLogLine));
    }

    @Test
    public void testLine2CmsConcurrent() {
        String priorLogLine = "1122748.949Application time: 0.0005210 seconds";
        String logLine = ": [CMS-concurrent-mark-start]";
        assertTrue("Log line not recognized as "
                + JdkUtil.PreprocessActionType.APPLICATION_CONCURRENT_TIME.toString() + ".",
                ApplicationConcurrentTimePreprocessAction.match(logLine, priorLogLine));
    }

    @Test
    public void testLine1PrecleanLine2TimesBlock() {
        String priorLogLine = "1987600.604: [CMS-concurrent-preclean: 0.016/0.017 secs]Application time: "
                + "4.5432350 seconds";
        String logLine = " [Times: user=0.02 sys=0.00, real=0.02 secs]";
        assertTrue("Log line not recognized as "
                + JdkUtil.PreprocessActionType.APPLICATION_CONCURRENT_TIME.toString() + ".",
                ApplicationConcurrentTimePreprocessAction.match(logLine, priorLogLine));
    }

    @Test
    public void testLine1AbortablePrecleanLine2TimesBlock() {
        String priorLogLine = "235820.289: [CMS-concurrent-abortable-preclean: 0.049/1.737 secs]Application "
                + "time: 0.0001370 seconds";
        String logLine = " [Times: user=0.90 sys=0.05, real=1.74 secs]";
        assertTrue("Log line not recognized as "
                + JdkUtil.PreprocessActionType.APPLICATION_CONCURRENT_TIME.toString() + ".",
                ApplicationConcurrentTimePreprocessAction.match(logLine, priorLogLine));
    }

    @Test
    public void testLine1MarkLine2TimesBlock() {
        String priorLogLine = "408365.532: [CMS-concurrent-mark: 0.476/10.257 secs]Application time: "
                + "0.0576080 seconds";
        String logLine = " [Times: user=6.00 sys=0.28, real=10.26 secs]";
        assertTrue("Log line not recognized as "
                + JdkUtil.PreprocessActionType.APPLICATION_CONCURRENT_TIME.toString() + ".",
                ApplicationConcurrentTimePreprocessAction.match(logLine, priorLogLine));
    }

    @Test
    public void testLine1MarkLine2TimesBlockWhitespaceAtEnd() {
        String priorLogLine = "408365.532: [CMS-concurrent-mark: 0.476/10.257 secs]Application time: "
                + "0.0576080 seconds";
        String logLine = " [Times: user=6.00 sys=0.28, real=10.26 secs]       ";
        assertTrue("Log line not recognized as "
                + JdkUtil.PreprocessActionType.APPLICATION_CONCURRENT_TIME.toString() + ".",
                ApplicationConcurrentTimePreprocessAction.match(logLine, priorLogLine));
    }
}
