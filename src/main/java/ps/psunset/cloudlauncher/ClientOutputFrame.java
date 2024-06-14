package ps.psunset.cloudlauncher;

import javax.swing.*;
import java.awt.*;

public class ClientOutputFrame{
    private JFrame frame;
    private JPanel panel;
    private JLabel output;
    public static StringBuilder outputMsg = new StringBuilder();

    public ClientOutputFrame() {
        initialize();
    }

    public void initialize(){
        frame = new JFrame();
        panel = new JPanel();
        output = new JLabel();

        frame.setTitle("JRE Output");
        frame.setSize(1063, 620);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(Color.BLACK);

        output.setText("");
        output.setFont(new Font("Sans-serif", Font.PLAIN, 24));
        output.setForeground(Color.WHITE);
        panel.add(output);

        frame.add(panel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void addMsg(String msg){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                outputMsg.append(msg).append("<br>");
                output.setText("<html><div>" + outputMsg.toString() + "</div></html>");
                initialize();
            }
        });
    }

    public void exit(){
        frame.setVisible(false);
    }
}
