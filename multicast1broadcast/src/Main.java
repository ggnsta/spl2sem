import java.net.*;

import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws SocketException, UnknownHostException {

        MyGUI app = new MyGUI();


        System.out.println(Utils.gethostIP());
        System.out.println(Utils.getBroadcastIP());
        System.out.println(Utils.getSubnet());

    }


}

