import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.io.File;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.*;


public class MyGUI {
    private JButton button = new JButton("Press");
    private JTextField input = new JTextField("", 5);
    private JLabel label_host_ip;
    private JLabel label_br_ip;
    private JLabel label_subnet;
    private JRadioButton radio_broadcast = new JRadioButton("Broadcast");
    private JRadioButton radio_multicast = new JRadioButton("Multicast");
    private JCheckBox check = new JCheckBox("Check", false);
    protected  JTextArea chatArea;
    protected JScrollPane chatScroll;
    protected JTextArea jtfMessage;
    protected JScrollPane messageScroll;
    protected JScrollPane contactsScroll;

    protected DefaultListModel listModel = new DefaultListModel();
    protected JList list;
    protected ArrayList<InetAddress> contactList = new ArrayList<InetAddress>();


    public MyGUI() throws UnknownHostException, SocketException {

        Chat chat = new Chat(Utils.getBroadcastIP(),this);
        new Thread(chat).start();



        JFrame frame = new JFrame("My First GUI");
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,600);

        //поле чата
        chatArea = new JTextArea();
        chatArea.setEditable(false);//делаем нередактируемым
        chatArea.setLineWrap(true);// разрешаем перенос строк
        chatArea.setWrapStyleWord(true);// и перенос слов


        chatScroll = new JScrollPane(chatArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);//добавляем его к ScrollPane
        chatScroll.setBounds(215, 5, 355, 400);

        frame.add(chatScroll);

        // радио кнопки
        ButtonGroup group = new ButtonGroup();
        group.add(radio_broadcast);
        group.add(radio_multicast);
        radio_broadcast.setSelected(true);
        radio_broadcast.setBounds(215,410,100,15);
        frame.add(radio_broadcast);
        radio_multicast.setBounds(315,410,100,15);
        frame.add(radio_multicast);

        //поле ввода
        jtfMessage = new JTextArea("Введите ваше сообщение: ");
        messageScroll = new JScrollPane(jtfMessage, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jtfMessage.setEditable(true);
        jtfMessage.setLineWrap(true);
        jtfMessage.setWrapStyleWord(true);

        messageScroll.setBounds(215, 430, 355, 100);
        frame.add(messageScroll);

        //лейблы с ip
        label_host_ip= new JLabel("IP: "+Utils.gethostIP().getHostAddress() + " /" +Utils.getSubnet());
        label_host_ip.setBounds(5,5,130,15);
        frame.add(label_host_ip);

        label_br_ip= new JLabel("Broadcast: " + Utils.getBroadcastIP());
        label_br_ip.setBounds(5,20,180,15);
        frame.add(label_br_ip);


        list = new JList(listModel);
        list.setSelectedIndex(0);
        list.setFocusable(false);
        list.setFont(new Font("Monospaced", Font.PLAIN, 15));
        contactsScroll = (new JScrollPane(list));
        contactsScroll.setBounds(5, 35, 201, 300);

        frame.add(contactsScroll);

        frame.setVisible(true);

        jtfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {

                    if (radio_broadcast.isSelected()) {
                        chat.send(jtfMessage.getText());
                        jtfMessage.setText(null);
                    }
                    else {
                        chat.send_multi(jtfMessage.getText());
                        chatArea.append(jtfMessage.getText());
                        jtfMessage.setText(null);
                    }
                }
            }
        });


        //listners
    }


}
