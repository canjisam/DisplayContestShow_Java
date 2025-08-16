import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.TrayIcon;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.util.concurrent.Executors.*;

/**
 * @author Jisam
 */

public class TrayContestFetcher extends JFrame {
    private JEditorPane editorPane;
    private Point initialClick;
    private double zoomFactor = 1.0;

    public TrayContestFetcher(String text) {
        this.editorPane = new JEditorPane();
        initializeWindow();
        initializeEditorPane(text);
    }

    private double opacity = 0.8; // 初始透明度
    private static final double OPACITY_INCREMENT = 0.05; // 透明度变化量

    private void adjustOpacity(int rotation) {
        opacity += rotation * OPACITY_INCREMENT;
        opacity = Math.max(0.1, Math.min(opacity, 1.0)); // 设置透明度范围在 0.1 到 1.0 之间

        setBackground(new Color(1,1,1, (float) opacity)); // 设置背景透明度
        getContentPane().setBackground(new Color(1,1,1, (float) opacity)); // 设置内容面板背景透明度
        repaint();
    }

    private void initializeWindow() {
        setTitle("比赛信息");
        setUndecorated(true); // 无边框
        setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.2),(int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.4));
        setLocation(0, 0);
        setBackground(new Color(122, 115, 116, 80)); // 背景透明
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(150,300));
        // 屏幕大小的0.8
        setMaximumSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.4), (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.8)));
        // 使窗口可拖动
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint(); // 记录鼠标点击的点
                grabFocus(); // 聚焦窗口
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // 获取当前鼠标位置
                Point currentPoint = e.getPoint();
                // 计算窗口移动的距离
                int xMoved = currentPoint.x - initialClick.x;
                int yMoved = currentPoint.y - initialClick.y;
                // 获取当前窗口的位置
                Point currentLocation = getLocation();
                // 设置窗口新的位置
                setLocation(currentLocation.x + xMoved, currentLocation.y + yMoved);
                initialClick = currentPoint; // 更新点击的点
            }
        });

        // 使窗口可缩放和透明
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown()) {
                    zoom(e.getWheelRotation());
                } else if (e.isShiftDown()){
//                    改变字体大小
                    checkFontSize(e.getWheelRotation());
                }else {
                    adjustOpacity(e.getWheelRotation());
                }
            }
        });

//         设置窗口穿透效果
//        setIgnoreRepaint(true); // 忽略重绘



        // 自动调整大小
        editorPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setSize(editorPane.getPreferredSize());
            }
        });
    }

    private void checkFontSize(int wheelRotation) {
        int fontSize = editorPane.getFont().getSize();
        int newFontSize = fontSize + wheelRotation;
        editorPane.setFont(new Font(editorPane.getFont().getName(), editorPane.getFont().getStyle(), newFontSize));
    }

    private void grabFocus() {
        Toolkit.getDefaultToolkit().sync();
        Window window = SwingUtilities.getWindowAncestor(this);
        window.toFront();
        window.requestFocus();
    }

    public void setVis(boolean visible) {
        super.setVisible(visible);
    }

    private void initializeEditorPane(String text) {
        editorPane = new JEditorPane("text/html", "<html><body></body></html>");
        editorPane.setEditable(false);
        editorPane.setBackground(new Color(122, 115, 116, 80)); // 背景透明
        editorPane.setOpaque(false);
        editorPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    openLink(e.getURL().toString());
                }
            }
        });

        editorPane.setText(text);
        add(editorPane, BorderLayout.CENTER);

        // 设置窗口穿透效果
//        editorPane.setIgnoreRepaint(true); // 忽略重绘
    }

    private void zoom(int rotation) {
        zoomFactor += 0.01 * rotation * 0.1;
        // 限制缩放因子的最大值为 1.1，最小值为 0.5
//        opacity = Math.max(0.5, Math.min(opacity, 1.1));
//        zoomFactor = Math.min(1.1, Math.max(0.5, zoomFactor));
        if(zoomFactor >= 1.1){
            zoomFactor = 1.1;
            return;
        }else if(zoomFactor <= 0.5){
            zoomFactor = 0.5;
            return;
        }
        Dimension d = getSize();
        setSize((int) (d.width * zoomFactor), (int) (d.height * zoomFactor));
        if(d.width * zoomFactor <= 150 || d.height * zoomFactor <= 300){
            zoomFactor = 1.0;
            setSize(150,300);
        }else if(d.width * zoomFactor >= (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.4) || d.height * zoomFactor >= (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.8)){
            zoomFactor = 1.0;
            setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.4),(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.8));
        }
        repaint();
    }

    private static void openLink(String url) {
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private static void updateCountdown(TrayIcon trayIcon, String name, String startTime, String endTime) {
        try {
            // 解析开始时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = sdf.parse(startTime);
            Date now = new Date();

            // 计算倒计时
            long millisUntilStart = start.getTime() - now.getTime();
            long hours = millisUntilStart / (1000 * 60 * 60);
            long minutes = (millisUntilStart % (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (millisUntilStart % (1000 * 60)) / 1000;

            // 构建倒计时字符串
            String countdown = String.format("%d小时%d分钟%d秒", hours, minutes, seconds);
            String toolTip = "即将开始的比赛: " + name + "\n" + startTime + " 至 " + endTime + "\n" + countdown;

            // 更新托盘图标的工具提示
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    trayIcon.setToolTip(toolTip);
                }
            });

            // 如果时间已经过去，则停止计时器
            if (millisUntilStart <= 0) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        trayIcon.setToolTip("比赛已经开始: " + name);
                    }
                });
                System.exit(0); // 退出程序
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String apiUrl = "https://algcontest.rainng.com/contests/acm";
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                StringBuilder response = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }
                connection.disconnect();

                return response.toString();
            }

            @Override
            protected void done() {
                try {
                    String jsonResponse = get();
                    JSONArray contests = new JSONArray(jsonResponse);
                    JSONObject latestContest = contests.getJSONObject(0); // 获取最近的比赛
                    // 遍历比赛数据并按平台和类型分类后添加到HTML中
                    StringBuilder sb = new StringBuilder("<html><body style='font-family: Arial; font-size: 12pt; color: black;'>");

// 定义一个Map来存储不同平台的比赛列表
                    Map<String, List<JSONObject>> contestsByPlatform = new HashMap<>();

// 遍历比赛数据
                    for (int i = 0; i < contests.length(); i++) {
                        JSONObject contest = contests.getJSONObject(i);
                        String oj = contest.getString("oj");
                        boolean oiContest = contest.getBoolean("oiContest");

                        // 使用平台名称作为键
                        List<JSONObject> platformContests = contestsByPlatform.computeIfAbsent(oj, k -> new ArrayList<>());
                        platformContests.add(contest);
                    }

// 遍历每个平台的比赛列表
                    for (Map.Entry<String, List<JSONObject>> entry : contestsByPlatform.entrySet()) {
                        String platform = entry.getKey();
                        List<JSONObject> platformContests = entry.getValue();

                        sb.append("<h2>").append(platform).append("</h2>");
                        sb.append("<ul>");

                        // 再次遍历每个比赛，根据是否为OI比赛进行分组显示
                        for (JSONObject contest : platformContests) {
                            String name = contest.getString("name");
                            String startTime = contest.getString("startTime");
                            String endTime = contest.getString("endTime");
                            String link = contest.getString("link").replace("leetcode.com", "leetcode.cn");
                            boolean isOiContest = contest.getBoolean("oiContest");

                            sb.append("<span>")
                                    .append(isOiContest ? "<strong>(OI) </strong>" : "")
                                    .append("</span>")
                                    .append("<a style='font-size: 11px;' href='").append(link).append("' target='_blank'>")
                                    .append(name)
                                    .append("</a>")
                                    .append("<br>")
                                    .append("<span style='font-size: 7px;'>Start Time: ").append(startTime).append("</span>")
                                    .append("<br>")
                                    .append("<span style='font-size: 7px;'>End Time: ").append(endTime).append("</span>")
                                    .append("<br>").append("<br>");


                        }

                        sb.append("</ul>");
                    }

                    sb.append("</body></html>");

                    displayInTray(sb.toString(), latestContest);

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error fetching data: " + e.getMessage());
                }
            }
        }.execute();
    }


    private static void displayInTray(String text, JSONObject latestContest) throws JSONException {
//        // 创建一个JFrame来显示信息
//        JFrame frame = new JFrame("比赛信息");
//        frame.setLayout(new BorderLayout());
////        frame.add(new JLabel(text), BorderLayout.CENTER);
////        frame.setSize(400, 300);
//        frame.setUndecorated(true); // 无边框
//        frame.setSize(400, 300);
//        frame.setBackground(new Color(255, 255, 255, 20)); // 完全透明
////        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(false); // 初始不可见
//
//        // 创建一个JEditorPane来显示HTML内容
//        JEditorPane editorPane = new JEditorPane("text/html", "<html><body></body></html>");
//        editorPane.setEditable(false);
//        editorPane.setBackground(new Color(255, 255, 255, 20)); // 背景透明
//        editorPane.setOpaque(false);
////        // 创建一个JEditorPane来显示HTML内容
////        JEditorPane editorPane = new JEditorPane("text/html", "<html><body></body></html>");
////        editorPane.setEditable(false);
////        editorPane.setBackground(new Color(240, 240, 240)); // 淡灰色背景
//        editorPane.addHyperlinkListener(new HyperlinkListener() {
//            @Override
//            public void hyperlinkUpdate(HyperlinkEvent e) {
//                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
//                    openLink(e.getURL().toString());
//                }
//            }
//        });
//
//        // 设置HTML内容
//        editorPane.setText(text);
//
//        // 添加JEditorPane到JFrame
//        frame.getContentPane().add(editorPane, BorderLayout.CENTER);
//
//        JOptionPane.showMessageDialog(null, text);
        TrayContestFetcher window = new TrayContestFetcher(text);


        String name = latestContest.getString("name");
        String startTime = latestContest.getString("startTime");
        String endTime = latestContest.getString("endTime");
        String link = latestContest.getString("link");

        if (!SystemTray.isSupported()) {
            JOptionPane.showMessageDialog(null, "System tray is not supported!");
            return;
        }

        ImageIcon icon = new ImageIcon("icon.png"); // 确保你有一个名为icon.png的图标文件
        TrayIcon trayIcon = new TrayIcon(icon.getImage());
        trayIcon.setImageAutoSize(false);

        final PopupMenu popup = new PopupMenu();
        MenuItem openItem = new MenuItem("open closest contest");
        openItem.addActionListener(e -> openLink(link));
        MenuItem showItem = new MenuItem("show all current contest");
//        showItem.addActionListener(e -> JOptionPane.showMessageDialog(null,text));
        showItem.addActionListener(e -> {
            window.setVisible(true);
        });
//        showItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "Name: " + name + "\nStart Time: " + startTime + "\nEnd Time: " + endTime + "\nLink: " + link));
        MenuItem exitItem = new MenuItem("exit");
        exitItem.addActionListener(e -> System.exit(0));
        popup.add("-"); // 分隔符
        popup.add(openItem);
        popup.add(showItem);
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);

        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "无法添加托盘图标！", "错误", JOptionPane.ERROR_MESSAGE);
        }
//        // 设置托盘图标的工具提示
//        trayIcon.setToolTip("即将开始的比赛: " + name + "\n" + startTime + " 至 " + endTime);
        trayIcon.setImageAutoSize(true);
// 添加双击事件监听器
        trayIcon.addMouseListener(new MouseAdapter() {
//            boolean isVisible = false;

            @Override
            public void mouseClicked(MouseEvent ee) {
                if (ee.getClickCount() == 2) { // 双击事件
//                    frame.setVisible(true); // 显示窗口
                    if (window.isVisible() == false) {
                        window.setVisible(true);
                    } else {
                        window.setVisible(false);
                    }
//                    if(!isVisible){

//                        JOptionPane.showMessageDialog(null,text);
//                    }else{
//                        isVisible = false;
//                    }
//                    showItem.addActionListener(e -> JOptionPane.showMessageDialog(null,text));
                }
            }
        });
        // 创建 ScheduledExecutorService
        ScheduledExecutorService scheduler = newSingleThreadScheduledExecutor();

        // 创建一个任务用于更新倒计时
        Runnable updateCountdownTask = new Runnable() {
            @Override
            public void run() {
                updateCountdown(trayIcon, name, startTime, endTime);
            }
        };

        // 立即执行任务，然后每隔1秒执行一次
        scheduler.scheduleAtFixedRate(updateCountdownTask, 0, 1, TimeUnit.SECONDS);
    }


}