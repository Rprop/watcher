/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jd.ump.log4j.helpers;


import com.jd.ump.log4j.Appender;
import com.jd.ump.log4j.spi.AppenderAttachable;
import com.jd.ump.log4j.spi.LoggingEvent;

import java.util.Enumeration;
import java.util.Vector;

/**
 * A straightforward implementation of the {@link AppenderAttachable}
 * interface.
 *
 * @author Ceki G&uuml;lc&uuml;
 * @since version 0.9.1
 */
public class AppenderAttachableImpl implements AppenderAttachable {

    /**
     * appender
     */
    protected Appender appender;

    /**
     * Attach an appender. If the appender is already in the list in
     * won't be added again.
     */
    public void addAppender(Appender newAppender) {
        // Null values for newAppender parameter are strictly forbidden.
        if (newAppender == null)
            return;

        appender = newAppender;
    }

    /**
     * Call the <code>doAppend</code> method on all attached appenders.
     */
    public int appendLoopOnAppenders(LoggingEvent event) {
        Appender aa = appender;
        if (aa != null) {
            aa.doAppend(event);
        }

        return 1;
    }


    /**
     * Get all attached appenders as an Enumeration. If there are no
     * attached appenders <code>null</code> is returned.
     *
     * @return Enumeration An enumeration of attached appenders.
     */
    public Appender getAppender() {
        Appender aa = appender;
        return aa;
    }

    /**
     * Look for an attached appender named as <code>name</code>.
     * <p/>
     * <p>Return the appender with that name if in the list. Return null
     * otherwise.
     */
    public Appender getAppender(String name) {
        Appender aa = appender;

        if (name == null || aa == null) {
            return null;
        }

        if (name.equals(aa.getName())) {
            return aa;
        }

        return null;
    }


    /**
     * Returns <code>true</code> if the specified appender is in the
     * list of attached appenders, <code>false</code> otherwise.
     *
     * @since 1.2
     */
    public boolean isAttached(Appender appender) {
        if (appender == null) {
            return false;
        }

        return this.appender == appender;
    }


    /**
     * Remove and close all previously attached appenders.
     */
    public void removeAllAppenders() {
        appender = null;
    }


    /**
     * Remove the appender passed as parameter form the list of attached
     * appenders.
     */
    public void removeAppender(Appender appender) {
        if (appender == null) {
            return;
        }
        if (this.appender == appender) {
            this.appender = null;
        }
    }


    /**
     * Remove the appender with the name passed as parameter form the
     * list of appenders.
     */
    public void removeAppender(String name) {
        if (name == null || appender == null) {
            return;
        }

        if (name.equals(appender.getName())) {
            appender = null;
        }
    }

    @Override
    public Enumeration getAllAppenders() {
        Appender aa = appender;
        if (aa == null) {
            return null;
        }
        Vector list = new Vector();
        list.add(aa);

        return list.elements();
    }
}
