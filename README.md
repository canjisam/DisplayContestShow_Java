# DisplayContestShow_Java

一个用于显示编程竞赛信息的Java桌面应用程序，支持系统托盘通知和比赛倒计时功能。

## 项目简介

DisplayContestShow_Java是一个轻量级的Java应用程序，用于获取和显示各大编程竞赛平台的比赛信息。该应用程序会在系统托盘中运行，提供即将开始的比赛倒计时，并允许用户查看所有当前比赛的详细信息。

## 功能特点

- **系统托盘集成**：应用程序在系统托盘中运行，不占用桌面空间
- **比赛倒计时**：实时显示最近一场比赛的倒计时
- **多平台支持**：获取并显示多个竞赛平台的比赛信息
- **分类显示**：按平台和比赛类型（ACM/OI）分类显示比赛信息
- **可交互界面**：
  - 支持窗口拖动
  - 支持鼠标滚轮调整透明度
  - 支持Ctrl+滚轮缩放窗口大小
  - 支持Shift+滚轮调整字体大小
  - 支持点击链接直接打开比赛页面

## 技术栈

- Java 17
- Swing/AWT (用户界面)
- JSoup (网页解析)
- JSON 处理 (org.json)
- Maven (项目管理)

## 系统要求

- Java 17 或更高版本
- 支持系统托盘的操作系统

## 安装与使用

### 从源码构建

1. 克隆仓库：
   ```
   git clone [仓库URL]
   cd DisplayContestShow_Java
   ```

2. 使用Maven构建项目：
   ```
   mvn clean package
   ```

3. 运行应用程序：
   ```
   java -jar target/DisplayContestShow_Java-1.0-SNAPSHOT.jar
   ```

### 直接运行

1. 下载最新的发布版本
2. 双击JAR文件运行，或使用命令：
   ```
   java -jar DisplayContestShow_Java-1.0-SNAPSHOT.jar
   ```

## 使用说明

1. 启动应用程序后，它将在系统托盘中显示一个图标
2. 双击托盘图标可以显示/隐藏比赛信息窗口
3. 右键点击托盘图标可以打开菜单：
   - "open closest contest"：打开最近的比赛链接
   - "show all current contest"：显示所有当前比赛信息
   - "exit"：退出应用程序
4. 托盘图标的提示信息会显示最近一场比赛的倒计时

## 主要类说明

- **TrayContestFetcher**：主类，负责获取比赛数据、创建系统托盘图标和显示比赛信息
- **ContestFetcher**：简化版的比赛数据获取类
- **SimpleWebCrawler**：网页爬虫示例类
- **NotificationApp**：通知应用示例类
- **TrayIconExample**：系统托盘示例类

## 数据来源

应用程序从以下API获取比赛数据：
```
https://algcontest.rainng.com/contests/acm
```

## 贡献指南

欢迎提交问题报告和功能请求。如果您想贡献代码，请遵循以下步骤：

1. Fork 仓库
2. 创建您的特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交您的更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 打开一个 Pull Request

## 许可证

[MIT]
