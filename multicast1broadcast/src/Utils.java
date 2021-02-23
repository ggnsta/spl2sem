import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Utils {

    public static InetAddress gethostIP() throws UnknownHostException {
        InetAddress IP = InetAddress.getLocalHost();
        return IP;
    }
    public static InetAddress getBroadcastIP() throws SocketException, UnknownHostException {

        NetworkInterface nif =NetworkInterface.getByInetAddress(gethostIP());
        InetAddress br;
        br = nif.getInterfaceAddresses().get(0).getBroadcast();
        return br;
    }

    public static int getSubnet() throws UnknownHostException, SocketException {

        NetworkInterface nif =NetworkInterface.getByInetAddress(gethostIP());
        int subnet = nif.getInterfaceAddresses().get(0).getNetworkPrefixLength();
        return subnet;
    }
}
