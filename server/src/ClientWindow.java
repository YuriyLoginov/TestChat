import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {

    private static final String ip = "192.168.31.216";
    private static final int port = 8189;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }

    private final JTextArea log = new JTextArea();
    private final JTextField nickName = new JTextField("yuriy");
    private final JTextField input = new JTextField();


    private TCPConnection connection;
    private ClientWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        log.setEditable(false);
        log.setLineWrap(true);
        input.addActionListener(this);

        add(log, BorderLayout.CENTER);
        add(input, BorderLayout.SOUTH);
        add(nickName, BorderLayout.NORTH);

        setVisible(true);

        try {
            connection = new TCPConnection(this, ip, port);
        } catch (IOException e) {
            printMes("Connection exception: " + e);
        }
    }

    private synchronized void printMes(String mes) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(mes + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = input.getText();
        if (msg.equals("")) return;
        input.setText(null);
        connection.sendString(nickName.getText() + ": " + msg);
    }


    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMes("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMes(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMes("Connection close...");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMes("Connection exception: " + e);
    }
}
