package com.alinesno.infra.business.platform.install.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
public class RuntimeUtil {

    public static String execForStr(String command) {
        StringBuilder output = new StringBuilder();
        Process p;
        try {
            // 使用 ProcessBuilder 来启动一个新的进程执行命令
            ProcessBuilder builder = new ProcessBuilder(command.split(" "));
            p = builder.start();
            
            // 读取命令执行的结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 等待命令执行完成
            p.waitFor();
        } catch (Exception e) {
            log.error("执行命令异常！" , e);
        }
        return output.toString();
    }
}