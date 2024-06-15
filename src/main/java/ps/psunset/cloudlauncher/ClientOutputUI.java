package ps.psunset.cloudlauncher;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;

public class ClientOutputUI {
    private JFrame frame;
    private JPanel panel;
    private JLabel output;
    private JScrollPane scrollPane;
    public static StringBuilder outputMsg;

    public ClientOutputUI() {
        initialize();
    }

    public void initialize(){
        outputMsg = new StringBuilder("");

        frame = new JFrame();
        frame.setTitle("JRE Output");
        frame.setSize(1063, 620);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        output = new JLabel(outputMsg.toString());
        output.setFont(new Font("Sans-serif", Font.PLAIN, 18));
        output.setForeground(Color.WHITE);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.BLACK);
        panel.add(output);

        scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public void addMsg(String msg){
        // Add message
        StringBuilder sb = new StringBuilder();
        for (String s : msg.split(" ")){
            sb.append(s).append("&ensp;");
        }
        outputMsg.append(sb).append("<br/>");
        System.out.println(msg);
        output.setText("<html><p>" + outputMsg.toString() + "</p></html>");

        // Make scroll bar auto scroll down
        int sPaneMax = scrollPane.getVerticalScrollBar().getMaximum();
        if(scrollPane.getVerticalScrollBar().getValue() >= (sPaneMax - 850)){
            scrollPane.getVerticalScrollBar().setValue(sPaneMax);
        }
    }

    public void exit(){
        frame.setVisible(false);
    }
}
