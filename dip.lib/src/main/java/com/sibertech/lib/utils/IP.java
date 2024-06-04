package com.sibertech.lib.utils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IP {

    protected static Logger logback = LoggerFactory.getLogger(IP.class);

    public    static ArrayList<String> getIps() {

        ArrayList<String> ipList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while(interfaces .hasMoreElements()) {
                NetworkInterface iface  = (NetworkInterface) interfaces.nextElement();

                if (iface.isLoopback() || !iface.isUp())
                    continue; // filters out 127.0.0.1 and inactive interfaces

                Enumeration<InetAddress> eInetAddresses = iface.getInetAddresses();
                while(eInetAddresses.hasMoreElements()) {
                    InetAddress addr = eInetAddresses.nextElement();
                    if (addr instanceof Inet6Address)
                        continue;

                    String ip = addr.getHostAddress();
                    ipList.add(ip);
                }
            }
        }
        catch (SocketException ex) {
            logback.info(ex.getMessage());
        }
        return ipList;
    }

}
