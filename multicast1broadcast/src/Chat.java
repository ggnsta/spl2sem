import java.io.IOException;
import java.net.*;

public class Chat  implements Runnable{
    protected DatagramSocket udpSocket = null;
    protected int serverPort = 49005;
    protected InetAddress broadcastIP = null;
    protected MyGUI gui;
    protected MulticastSocket socket = null;
    protected String groupAddress =  "232.0.0.0";
    protected int multiPort = 49007;



    public Chat(InetAddress broadcastIP, MyGUI gui)  {
        this.broadcastIP = broadcastIP;
        this.gui=gui;
    }

    @Deprecated
    @Override
    public void run ()
    {
        try {
            socket = new MulticastSocket(49007);
            InetAddress group = InetAddress.getByName(groupAddress);
            socket.joinGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
        createUDPSocket();
        selfDiscovery();
        listenMulti();

        while (true){
            get();
            System.out.println("1111"+Thread.currentThread());
        }

    }
    public void selfDiscovery()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(5000);
                        System.out.println("0000"+Thread.currentThread());
                        send("");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public void  send(String msg)
    {
        byte[] sendingDataBuffer = new byte[ 512];
        sendingDataBuffer=msg.getBytes();
        DatagramPacket outputPacket = new DatagramPacket(
                sendingDataBuffer, sendingDataBuffer.length,
                broadcastIP,serverPort
        );
        try {
            udpSocket.send(outputPacket);
            //zakrut socket gde-nibud
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    public void send_multi(String msg) {
        try {
        byte[] sendingDataBuffer = new byte[ 512];
        sendingDataBuffer=msg.getBytes();
        DatagramPacket outputPacket = new DatagramPacket(
                sendingDataBuffer, sendingDataBuffer.length,
                InetAddress.getByName(groupAddress),49006
        );

            socket.send(outputPacket);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    public void listenMulti(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    get_multi();

                }
            }
        }).start();
    }
    public String get_multi()
    {
        try {
            byte[] receivingDataBuffer = new byte[512];
            DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
            socket.receive(receivingPacket);
            String msg = new String (receivingPacket.getData());
            gui.chatArea.append("!!!"+msg);
        }catch (Exception x) {
        x.printStackTrace();
    }
        return null;
    }
    public String get() {
        try {
            byte[] receivingDataBuffer = new byte[512];
            DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer,receivingDataBuffer.length);
            udpSocket.receive(receivingPacket);
            String msg = new String (receivingPacket.getData());

            if (msg.trim().length()>0) {
                  gui.chatArea.append("\n" + receivingPacket.getAddress() +": "+msg);

            }
            else {

                if (receivingPacket.getAddress().getHostAddress().equals(Utils.gethostIP().getHostAddress())==false) {
                    {
                        if (gui.contactList.contains(receivingPacket.getAddress())==false) {
                            gui.listModel.addElement(receivingPacket.getAddress());
                            gui.contactList.add(receivingPacket.getAddress());
                        }

                    }
                }
            }



            return msg;

        } catch (Exception x) {
            x.printStackTrace();
        }
        return null;
    }

    public void createUDPSocket()
    {
        try {
            this.udpSocket = new DatagramSocket(serverPort);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
