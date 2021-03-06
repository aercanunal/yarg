/*
 * Copyright 2013 Haulmont
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.haulmont.yarg.server;

import com.haulmont.yarg.util.properties.DefaultPropertiesLoader;
import com.haulmont.yarg.util.properties.PropertiesLoader;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.haulmont.yarg.console.ConsoleRunner.PROPERTIES_PATH;

public class ServerRunner {
    private static final String SERVER_PORT = "port";
    private static final String REPORTS_DIRECTORY = "dir";
    private static final String HELP = "help";

    protected static Logger logger = LoggerFactory.getLogger(ServerRunner.class);

    public static void main(String[] args) {
        Options options = createOptions();

        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption(HELP)) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("yarg-server", options);
                System.exit(0);
            }

            PropertiesLoader propertiesLoader = new DefaultPropertiesLoader(
                    cmd.getOptionValue(PROPERTIES_PATH, DefaultPropertiesLoader.DEFAULT_PROPERTIES_PATH));
            int port = Integer.valueOf(cmd.getOptionValue(SERVER_PORT, "4567"));
            String reportsDirectory = cmd.getOptionValue(REPORTS_DIRECTORY, "./reports/");


            Server server = new Server()
                    .propertiesLoader(propertiesLoader)
                    .port(port)
                    .reportsPath(reportsDirectory);
            server.init();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("An error occurred while starting the reporting server", e);
            System.exit(1);
        }
    }

    private static Options createOptions() {
        Options options = new Options();
        options.addOption(HELP, false, "show help");
        options.addOption(PROPERTIES_PATH, true, "reporting.properties file path (default is ./reporting.properties)");
        options.addOption(SERVER_PORT, true, "reporting server port (default is 4567)");
        options.addOption(REPORTS_DIRECTORY, true, "the directory which contains report xml files (default is ./reports/)");
        return options;
    }
}
