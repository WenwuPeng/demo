/*
 *
 *  Copyright 2013 Justin Santa Barbara.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.demo.chaos;


import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.common.net.HostAndPort;
import org.jclouds.compute.domain.ExecResponse;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.http.handlers.BackoffLimitedRetryHandler;
import org.jclouds.proxy.internal.GuiceProxyConfig;
import org.jclouds.ssh.SshClient;
import org.jclouds.ssh.jsch.JschSshClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * Executes a CPU intensive program on the node, using up all available CPU.
 *
 * This simulates either a noisy CPU neighbor on the box or just a general issue with the CPU.
 */
public class BurnCpuChaos {

    private String ip;
    private String user;
    private String password;

    private static final Logger LOGGER = LoggerFactory.getLogger(BurnCpuChaos.class);

    public BurnCpuChaos(String ip, String user, String password) {
        this.ip = ip;
        this.user = user;
        this.password = password;
    }

    public void apply(){
        LOGGER.info("Running script for {} on instance {}", "test", "test");

        SshClient ssh = connectSsh();

        String filename = "burncpu.sh";
        URL url = Resources.getResource(BurnCpuChaos.class, "/scripts/" + filename);
        String script;
        try {
            script = Resources.toString(url, Charsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Error reading script resource", e);
        }

        ssh.put("/tmp/" + filename, script);
        ExecResponse response = ssh.exec("/bin/bash /tmp/" + filename);
        if (response.getExitStatus() != 0) {
            LOGGER.warn("Got non-zero output from running script: {}", response);
        }
        ssh.disconnect();
    }

    public SshClient connectSsh(){
        JschSshClient ssh = null;
        try {
            HostAndPort hap = HostAndPort.fromParts(ip,22);
            LoginCredentials cre = LoginCredentials.builder().user(user).password(password).build();
            BackoffLimitedRetryHandler bk = new BackoffLimitedRetryHandler();
            GuiceProxyConfig proxy = new GuiceProxyConfig();
            ssh = new JschSshClient(proxy, bk, hap, cre,1000);
            ssh.connect();
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return ssh;
    }
}
