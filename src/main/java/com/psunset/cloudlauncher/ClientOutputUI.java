package com.psunset.cloudlauncher;

import javax.swing.*;
import java.awt.*;

public class ClientOutputUI {
    public static ClientOutputUI INSTANCE;

    public static ClientOutputUI getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new ClientOutputUI();
        }

        return INSTANCE;
    }

    private JFrame frame;
    private JPanel panel;
    private JLabel output;
    private JScrollPane scrollPane;
    public static StringBuilder outputMsg;

    public ClientOutputUI() {
        init();
    }

    public void init(){
        SwingUtilities.invokeLater(() -> {
            outputMsg = new StringBuilder("");

            frame = new JFrame();
            frame.setTitle("Client Output");
            frame.setSize(1063, 620);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        });
    }

    public void addMsg(String msg){
        SwingUtilities.invokeLater(() -> {

            // Add message
            StringBuilder sb = new StringBuilder();
            for (String s : msg.split(" ")){
                sb.append(s).append("&ensp;");
            }
            outputMsg.append(sb).append("<br/>");
            output.setText("<html><p>" + outputMsg.toString() + "</p></html>");

            // Make scroll bar auto scroll down
            int sPaneMax = scrollPane.getVerticalScrollBar().getMaximum();
            if(scrollPane.getVerticalScrollBar().getValue() >= (sPaneMax - 800)){
                scrollPane.getVerticalScrollBar().setValue(sPaneMax);
            }
        });
    }

    public void exit(){
        frame.setVisible(false);
        frame.dispose();

        INSTANCE = null;
    }
}
