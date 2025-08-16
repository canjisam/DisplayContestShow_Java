import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NotificationApp {

    public static void main(String[] args) {
        // 创建窗口
        JFrame frame = new JFrame("Notification App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        // 创建按钮
        JButton button = new JButton("Show Notification");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 添加按钮的点击事件
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 显示通知
                JOptionPane.showMessageDialog(frame, "Hello, this is a notification!");
            }
        });

        // 将按钮添加到窗口
        frame.add(button);

        // 显示窗口
        frame.setVisible(true);
    }
}