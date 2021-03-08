package com.cere.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by CheRevir on 2021/3/1
 */
public class NetworkUtils {

    /**
     * 不能在UI线程调用
     *
     * @return 网络是否可用
     */
    public static boolean isAvailable() {
        return isAvailableByDns();
    }

    private static boolean isAvailableByDns() {
        try {
            return InetAddress.getAllByName("www.baidu.com") != null;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isAvailableByPing() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("ping -c 3 223.5.5.5");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            runtime.gc();
        }
        return false;
    }
}
