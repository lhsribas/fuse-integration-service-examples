package br.com.ribas.fis.logging.engine;

import br.com.ribas.fis.logging.model.FixedLogParam;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class FixedLogParamEngine {

    private FixedLogParam fixedLogParam;

    public FixedLogParamEngine() {
        fixedLogParam = new FixedLogParam();
    }

    public FixedLogParam gettingFixedLogParams(){
        try {
            host();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        serverName();

        return fixedLogParam;
    }

    @SuppressWarnings("static-access")
    private void host() throws SocketException, UnknownHostException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        {
            String hostname = null;
            while (interfaces.hasMoreElements()) {
                NetworkInterface nic = interfaces.nextElement();
                Enumeration<InetAddress> addresses = nic.getInetAddresses();

                while (hostname == null && addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    fixedLogParam.setHostAddress(address.getLocalHost().getHostAddress());
                    if (!address.isLoopbackAddress()) {
                        fixedLogParam.setHostName(address.getHostName());
                    }
                }
            }
        }
    }

    private void serverName() {
        String tag = System.getenv("HOSTNAME");
        if (tag != null && !tag.isEmpty()) {
            fixedLogParam.setServerName("POD :: ".concat(tag));
        }else{
            fixedLogParam.setServerName("POD :: unknown");
        }
    }

}
