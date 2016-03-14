/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.stratos.tools.stratos.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.stratos.tools.stratos.event.receiver.EventReceiver;

import java.util.concurrent.Executors;

/**
 * Run this main class to send a set of sample topology events.
 */
public class Main {

    private static final Log log = LogFactory.getLog(Main.class);
    private static EventReceiver receiver;

    public static void main(String[] args) {

        // Configure log4j properties
        String topicName = System.getProperty("topic.name");
        if (topicName == null) {
            topicName = "topology.>";
        }

        PropertyConfigurator.configure(System.getProperty("log4j.properties.file.path", "src/main/conf/log4j.properties"));
        System.setProperty("jndi.properties.dir", System.getProperty("jndi.properties.dir", "src/main/conf"));
        receiver = new EventReceiver("topology-1", topicName);
        Executors.newFixedThreadPool(1).submit(receiver);

        class Shutdownhook extends Thread {
            public void run() {
                log.info("Shutting down Event Receiver...");
                receiver.terminate();
            }
        }

        Runtime.getRuntime().addShutdownHook(new Shutdownhook());

    }

}
