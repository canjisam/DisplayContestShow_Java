import java.awt.*;
import java.awt.TrayIcon;
import javax.swing.*;

public class TrayIconExample {

    public static void main(String[] args) throws AWTException {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray is not supported");
            return;
        }

        final PopupMenu popup = new PopupMenu();
        MenuItem aboutItem = new MenuItem("关于");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "这是一个托盘程序"));
        popup.add(aboutItem);

        Image image = new ImageIcon("path/to/your/icon.png").getImage();

        TrayIcon trayIcon = new TrayIcon(image, "托盘程序", popup);
        trayIcon.setImageAutoSize(true);

        // 设置托盘图标的工具提示
        trayIcon.setToolTip("这是中文工具提示");

        SystemTray tray = SystemTray.getSystemTray();
        tray.add(trayIcon);
    }
}