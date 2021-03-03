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
package org.eclipselabs.garbagecat.domain.jdk;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipselabs.garbagecat.util.jdk.JdkUtil;



/**
 * @author <a href="mailto:mmillson@redhat.com">Mike Millson</a>
 * 
 */
public class TestHeaderVersionEvent {

    @Test
    public void testLine() {
        String logLine = "Java HotSpot(TM) 64-Bit Server VM (24.85-b08) for linux-amd64 JRE (1.7.0_85-b34), built on "
                + "Sep 29 2015 08:44:21 by \"java_re\" with gcc 4.3.0 20080428 (Red Hat 4.3.0-8)";
        assertTrue("Log line not recognized as " + JdkUtil.LogEventType.HEADER_VERSION.toString() + ".",
                HeaderVersionEvent.match(logLine));
    }

    @Test
    public void testLineOpenJdk() {
        String logLine = "OpenJDK 64-Bit Server VM (24.95-b01) for linux-amd64 JRE (1.7.0_95-b00), built on "
                + "Jan 18 2016 21:57:50 by \"mockbuild\" with gcc 4.8.5 20150623 (Red Hat 4.8.5-4)";
        assertTrue("Log line not recognized as " + JdkUtil.LogEventType.HEADER_VERSION.toString() + ".",
                HeaderVersionEvent.match(logLine));
    }

    @Test
    public void testNotBlocking() {
        String logLine = "Java HotSpot(TM) 64-Bit Server VM (24.85-b08) for linux-amd64 JRE (1.7.0_85-b34), built on "
                + "Sep 29 2015 08:44:21 by \"java_re\" with gcc 4.3.0 20080428 (Red Hat 4.3.0-8)";
        assertFalse(JdkUtil.LogEventType.HEADER_VERSION.toString() + " incorrectly indentified as blocking.",
                JdkUtil.isBlocking(JdkUtil.identifyEventType(logLine)));
    }
}
