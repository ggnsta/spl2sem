import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Chat  implements Runnable{
    protected DatagramSocket udpSocket = null;
    protected int serverPort = 49005;
    protected InetAddress broadcastIP = null;
    protected MyGUI gui;



    public Chat(InetAddress broadcastIP, MyGUI gui) {
        this.broadcastIP = broadcastIP;
        this.gui=gui;
    }

    @Override
    public void run ()
    {

        createUDPSocket();
        selfDiscovery();

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
