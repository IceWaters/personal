package com.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

import com.function.SendFileThread;
import com.function.SocketListenerThread;

/**
 * @author 郭雄
 * @version 1.0
 * @file mainPage.java
 * @date 2014-12-5 to 2015-1-
 * */
public class MainPage {
	// 定义变量
	public final int mainPageWidth = 300;
	public final int mainPageHeight = 516;
	private int xx, yy;
	private boolean isDraging = false;// 是否允许拖动标识
	boolean isSetMenuVisible = false;// 设置对话框是否显示标识
	public boolean isConnected = false; // 是否已连接到其他客户端
	private int CloseOption = 1; // 设置关闭按钮选择
	boolean isSetDialogVisible = false;// 设置对话框是否显示标准
	public String DefaultFileSavePath = "E:\\";
	public String IP; // 连接对方的IP地址
	public String MyNickname = "me"; // 自己的昵称
	Socket talkingSocket; // 网络套接字
	PrintWriter output = null; // 输出流
	BufferedReader input = null; // 输入流
	public HashMap<String, Integer> isFileTransferWaitMap = new HashMap<String, Integer>();
	// 联系人<IP地址,昵称>哈希表
	public HashMap<String, String> ContactsMap = new HashMap<String, String>();

	public JFrame MainUI; // 主窗体
	/* 组件定义 */
	private JButton drag; // 用于拖拽图形界面
	private JLabel softName; // 软件名
	private JButton BLogo; // 软件图标
	private SetButton set; // 软件的设置按钮
	private MinSizeButton minSize;// 软件最小化按钮
	private CloseButton close; // 软件关闭按钮
	private JPanel pConnect; // 显示连接按钮实现与其他客户端连接
	private JPanel pOperation; // 文件传输下载操作组件
	private JPanel pMain; // Home操作界面
	public JPanel pCenter; // 中间显示的面板
	public JPanel pHome; // 底部主要操作面板
	public ChatPanel pMessage; // 消息面板
	JPanel pSysSet;
	public ContactsPanel pContacts;// 联系人面板
	public JTabbedPane pTask; // 任务面板
	public TaskPanel pDownloadPanel; // 下载列表面板
	public TaskPanel pUploadPanel; // 上传列表面板
	public TaskPanel pComplishedPanel; // 已完成任务面板
	public CardLayout card;
	JButton upload; // 发送文件按钮
	JButton download; // 接收文件按钮
	JButton suggest; // 反馈建议
	public JButton BConnect; // 链接其他客户端按钮
	public JLabel ConnectState; // 显示连接状态标签
	public SetMenu setMenu; // 系统设置菜单栏
	public SetDialog sd;
	public MessageButton mb; // 消息按钮
	public HomeButton hb; // 主页按钮
	public ContactsButton cb; // 联系人按钮
	private PopMenu ContactsPopMenu;
	private TaskPopMenu downloadTaskPopMenu;// 下载列表弹出菜单
	private TaskPopMenu complishedTaskPopMenu;// 已完成列表弹出菜单
	public TaskButton tb;
	InputStream in;
	AddContactsDialog acd;
	// 线程监听类
	public SocketListenerThread slt;
	Image image;

	public MainPage() {
		MainUI = new JFrame();
		MainUI.setSize(mainPageWidth, mainPageHeight);
		MainUI.setResizable(false);
		/* 设置界面居中 */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		MainUI.setLocation((screenSize.width - mainPageWidth) / 2,
				(screenSize.height - mainPageHeight) / 2);
		MainUI.setUndecorated(true);/* 设置无边框 */
		MainUI.setOpacity(0.93f);
		MainUI.setIconImage(new ImageIcon("Image\\SystemLogo.png").getImage());
		//MainUI.setIconImage(new ImageIcon(getClass().getResource("/Image\\SystemLogo.png")).getImage());
		// 添加组件移动的代码
		MainUI.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				if (isSetMenuVisible) {
					setMenu.setVisible(false);
					isSetMenuVisible = false;
				}
				if (isSetDialogVisible) {
					sd.shake();
				}
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent e) {
				if (isSetDialogVisible) {
					sd.shake();
				} else {
					isDraging = true;
					xx = e.getX();
					yy = e.getY();
				}
			}

			public void mouseReleased(MouseEvent arg0) {
				isDraging = false;
			}
		});
		MainUI.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				if (isDraging) {
					int left = MainUI.getLocation().x;
					int top = MainUI.getLocation().y;
					MainUI.setLocation(left + e.getX() - xx, top + e.getY()
							- yy);
					if (isSetMenuVisible) {
						setMenu.setVisible(false);
						isSetMenuVisible = false;
					}
				}
			}

			public void mouseMoved(MouseEvent arg0) {
			}
		});

		// 添加系统设置组件
		// 顶层设置按钮项
		image = new ImageIcon("Image\\BackGround\\hua.png").getImage();
		pSysSet = new JPanel() {
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, mainPageWidth, 300, pSysSet);
			}
		};

		pSysSet.setBackground(new Color(0.2824f, 0.549f, 0.7843f));
		pSysSet.setBorder(null);
		BLogo = new JButton();
		BLogo.setPreferredSize(new Dimension(20, 20));
		BLogo.setBorderPainted(false);
		BLogo.setFocusable(false);
		BLogo.setContentAreaFilled(false);
		BLogo.setIcon(new ImageIcon("Image\\SystemLogo_Small.png"));
		softName = new JLabel("TCP/IP断点续传");
		softName.setForeground(Color.WHITE);
		set = new SetButton();
		set.addActionListener(new ClickActionListener());
		setMenu = new SetMenu(MainUI.getX() + 200, MainUI.getY() + 25);
		setMenu.setSystem.addMouseListener(new SetSystemMouseListener());
		setMenu.upDate.addMouseListener(new UpdateMouseListener());
		setMenu.echo.addMouseListener(new EchoMouseListener());
		setMenu.about.addMouseListener(new AboutMouseListener());
		minSize = new MinSizeButton();
		minSize.addActionListener(new ClickActionListener());
		close = new CloseButton();
		close.addActionListener(new ClickActionListener());
		drag = new JButton();
		drag.setPreferredSize(new Dimension(90, 20));
		drag.setBorderPainted(false);
		drag.setForeground(Color.BLUE);
		drag.setContentAreaFilled(false);
		drag.setFocusPainted(false);
		drag.addMouseListener(new systemDragListener());
		drag.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				if (isSetDialogVisible) {
					sd.shake();
				} else if (isDraging) {
					int left = MainUI.getLocation().x;
					int top = MainUI.getLocation().y;
					MainUI.setLocation(left + e.getX() - xx, top + e.getY()
							- yy);
					if (isSetMenuVisible) {
						setMenu.setVisible(false);
						isSetMenuVisible = false;
					}
				}
			}

			public void mouseMoved(MouseEvent e) {
			}
		});
		// 添加软件图标，软件名，空白区，设置，最小化，关闭控件
		pSysSet.add(BLogo);
		pSysSet.add(softName);
		pSysSet.add(drag);
		pSysSet.add(set);
		pSysSet.add(minSize);
		pSysSet.add(close);
		// 中间连接项
		pMain = new JPanel();

		pConnect = new JPanel() {
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, -30, mainPageWidth, 300, this);
			}
		};
		ConnectState = new JLabel("传输文件");
		ConnectState.setFont(new Font("楷体", 14, 14));
		ConnectState.setForeground(Color.WHITE);

		pConnect.setPreferredSize(new Dimension(mainPageWidth, 267));
		pConnect.setBorder(null);
		BConnect = new JButton("连接客户端");
		BConnect.setContentAreaFilled(false);
		BConnect.setPreferredSize(new Dimension(110, 20));
		BConnect.setBorderPainted(false);
		BConnect.setFocusPainted(false);
		BConnect.setFont(new Font("楷体", Font.PLAIN, 14));
		BConnect.setForeground(Color.WHITE);
		BConnect.addActionListener(new ClickActionListener());

		BConnect.setBounds(50, 200, 110, 20);
		ConnectState.setBounds(165, 200, 200, 20);

		pConnect.setLayout(null);
		pConnect.setBorder(null);
		pConnect.add(BConnect);
		pConnect.add(ConnectState);

		pOperation = new JPanel();
		JPanel pFileOperation = new JPanel();

		upload = new JButton("发送文件");
		upload.setFont(new Font("楷体", 12, 20));
		upload.setBorderPainted(false);
		upload.setPreferredSize(new Dimension(mainPageWidth, 52));
		upload.setContentAreaFilled(false);
		upload.setFocusPainted(false);

		upload.addMouseListener(new UploadMouseListener(this));
		download = new JButton("接收文件");
		download.setBorderPainted(false);
		download.setFont(new Font("楷体", 12, 20));
		download.setContentAreaFilled(false);
		download.setPreferredSize(new Dimension(mainPageWidth, 52));
		download.setFocusPainted(false);
		download.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (isSetDialogVisible) {
					sd.shake();
				} else if (isSetMenuVisible) {
					setMenu.setVisible(false);
					isSetMenuVisible = false;
				} else {
					card.show(pCenter, "pTask");
				}
			}

			public void mouseEntered(MouseEvent e) {
				download.setForeground(Color.BLUE);
			}

			public void mouseExited(MouseEvent e) {
				download.setForeground(Color.BLACK);
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}
		});

		suggest = new JButton("新版本建议，全心为您服务");
		suggest.setBorderPainted(false);
		suggest.setFont(new Font("楷体", 12, 20));
		suggest.setContentAreaFilled(false);
		suggest.setPreferredSize(new Dimension(mainPageWidth, 52));
		suggest.setFocusPainted(false);
		suggest.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (isSetDialogVisible) {
					sd.shake();
				} else if (isSetMenuVisible) {
					setMenu.setVisible(false);
					isSetMenuVisible = false;
				}
			}

			public void mouseEntered(MouseEvent e) {
				suggest.setForeground(Color.GREEN);
			}

			public void mouseExited(MouseEvent e) {
				suggest.setForeground(Color.BLACK);
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}
		});

		pOperation.setPreferredSize(new Dimension(mainPageWidth, 220));
		pOperation.setBackground(new Color(0.1f, 0.5f, 0.9f));
		pFileOperation.setPreferredSize(new Dimension(mainPageWidth, 156));
		pFileOperation.setLayout(new BorderLayout());
		pFileOperation.setBackground(new Color(1.0f, 1.0f, 1.0f));
		pFileOperation.add(upload, BorderLayout.NORTH);
		pFileOperation.add(download, BorderLayout.CENTER);
		pFileOperation.add(suggest, BorderLayout.SOUTH);

		pMain.setLayout(new BorderLayout());
		pMain.setBorder(null);
		pMain.add(pConnect, BorderLayout.NORTH);
		pMain.add(pFileOperation, BorderLayout.CENTER);

		// 底部操作面板
		pHome = new JPanel();
		pHome.setPreferredSize(new Dimension(mainPageWidth, 64));
		mb = new MessageButton();
		mb.addActionListener(new ClickActionListener());
		hb = new HomeButton();
		hb.addActionListener(new ClickActionListener());
		cb = new ContactsButton();
		cb.addActionListener(new ClickActionListener());
		tb = new TaskButton();
		tb.addActionListener(new ClickActionListener());

		pHome.setLayout(new GridLayout(1, 4));
		pHome.add(mb);
		pHome.add(hb);
		pHome.add(cb);
		pHome.add(tb);

		/**
		 * 中间的面板设置为卡片布局，分别装载信息面板，主控面板， 联系人面板，任务进程面板，点击不同的按钮则切换为想应 的显示面板*
		 */
		// 消息面板
		pMessage = new ChatPanel();
		pMessage.MessageSendButton.addActionListener(new ClickActionListener());
		pMessage.MessageSendField.addActionListener(new ClickActionListener());
		
		// 联系人面板
		pContacts = new ContactsPanel();
		pContacts.addContact.addActionListener(new ClickActionListener());
		pContacts.deleteContact.addActionListener(new ClickActionListener());
		pContacts.table.addMouseListener(new ContactsMouseListener());
		pContacts.setBackground(Color.blue);
		ContactsPopMenu = new PopMenu();
		pContacts.add(ContactsPopMenu);
		ContactsPopMenu.addItem.addActionListener(new ClickActionListener());
		ContactsPopMenu.deleteItem.addActionListener(new ClickActionListener());

		// 任务面板
		pTask = new JTabbedPane();
		pTask.setBorder(null);
		pTask.setFont(new Font("楷体", Font.PLAIN, 16));
		pTask.setBackground(new Color(189, 252, 201));
		pDownloadPanel = new TaskPanel();
		pUploadPanel = new TaskPanel();
		pComplishedPanel = new TaskPanel();
		pTask.addTab("  发送  ", pUploadPanel);
		pTask.addTab("  接收  ", pDownloadPanel);
		pTask.addTab("  完成  ", pComplishedPanel);
		// 添加下载任务及其点击事件
		downloadTaskPopMenu = new TaskPopMenu();
		downloadTaskPopMenu.openFile
				.addActionListener(new ClickActionListener());
		downloadTaskPopMenu.waitTask
				.addActionListener(new ClickActionListener());
		downloadTaskPopMenu.continueTask
				.addActionListener(new ClickActionListener());
		downloadTaskPopMenu.deleteTask
				.addActionListener(new ClickActionListener());
		pDownloadPanel.add(downloadTaskPopMenu);
		pDownloadPanel.table.addMouseListener(new DownloadMouserListener());
		// 已完成任务及添加点击事件
		complishedTaskPopMenu = new TaskPopMenu();
		complishedTaskPopMenu.openFile
				.addActionListener(new ClickActionListener());
		complishedTaskPopMenu.waitTask
				.addActionListener(new ClickActionListener());
		complishedTaskPopMenu.continueTask
				.addActionListener(new ClickActionListener());
		complishedTaskPopMenu.deleteTask
				.addActionListener(new ClickActionListener());
		pComplishedPanel.add(complishedTaskPopMenu);
		pComplishedPanel.table.addMouseListener(new ComplishedMouserListener());

		pCenter = new JPanel();
		card = new CardLayout();
		pCenter.setLayout(card);
		pCenter.add("pMessage", pMessage);
		pCenter.add("pMain", pMain);
		pCenter.add("pContacts", pContacts);
		pCenter.add("pTask", pTask);
		card.show(pCenter, "pMain");

		// 整体集合
		MainUI.setLayout(new BorderLayout());
		MainUI.add(pSysSet, BorderLayout.NORTH);
		MainUI.add(pCenter, BorderLayout.CENTER);
		MainUI.add(pHome, BorderLayout.SOUTH);

		MainUI.setVisible(true);
		slt = new SocketListenerThread(this);
		slt.start();
	}

	// MainPage方法
	/** 窗口抖动 */
	public void shake() {
		MainUI.setAlwaysOnTop(true);
		int x = MainUI.getX();
		int y = MainUI.getY();
		for (int i = 0; i < 10; i++) {
			if ((i & 1) == 0) {
				x += 3;
				y += 3;
			} else {
				x -= 3;
				y -= 3;
			}
			MainUI.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		MainUI.setAlwaysOnTop(false);
	}

	// 设置背景图片
	public void setBackImage(Image im) {
		image = im;
	}

	// 根据序号设置默认背景图片
	public void setBackImage(int index) {
		switch (index) {
		case 0:
			image = new ImageIcon("Image\\BackGround\\hua.png").getImage();
			break;
		case 1:
			image = new ImageIcon("Image\\BackGround\\ali.png").getImage();
			break;
		case 2:
			image = new ImageIcon("Image\\BackGround\\hua2.png").getImage();
			break;
		case 3:
			image = new ImageIcon("Image\\BackGround\\pugongying.png")
					.getImage();
			break;
		case 4:
			image = new ImageIcon("Image\\BackGround\\qipao.png").getImage();
			break;
		case 5:
			image = new ImageIcon("Image\\BackGround\\zhifeiji.png").getImage();
			break;
		default:
			image = null;
		}
		this.pConnect.repaint();
		this.pSysSet.repaint();
	}

	// 判断输入的IP地址是否符合标准
	private static boolean isIPAddress(String ipaddr) {
		boolean flag = false;
		Pattern pattern = Pattern
				.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		if (ipaddr == null)
			return false;
		else {
			Matcher m = pattern.matcher(ipaddr);
			flag = m.matches();
		}
		return flag;
	}

	/** 事件响应内部类 */
	private class systemDragListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (isSetMenuVisible) {
				setMenu.setVisible(false);
				isSetMenuVisible = false;
			}
		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			isDraging = true;
			xx = e.getX();
			yy = e.getY();
		}

		public void mouseReleased(MouseEvent e) {
			isDraging = false;
		}
	}

	// 设置系统事件处理
	private class SetSystemMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			setMenu.setVisible(false);
			isSetMenuVisible = false;
			isSetDialogVisible = true;
			sd = new SetDialog(MainUI.getX(), MainUI.getY() + 100);
			getClass();
			if (CloseOption == 1)
				sd.b1.setSelected(false);
			else
				sd.b1.setSelected(true);
			sd.tf.setText(DefaultFileSavePath);
			sd.b1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					CloseOption = sd.Close_MinSize;
				}
			});
			sd.b2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					CloseOption = sd.Close_Exit;
				}
			});
			sd.cb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					DefaultFileSavePath = sd.tf.getText().trim();
					isSetDialogVisible = false;
					sd.jd.dispose();
				}
			});
			sd.tf.setText(DefaultFileSavePath);
			if (CloseOption == sd.Close_MinSize) {
				sd.b1.setSelected(true);
			} else {
				sd.b1.setSelected(false);
			}
		}

		public void mouseEntered(MouseEvent arg0) {
			setMenu.setSystem.setForeground(Color.BLUE);
		}

		public void mouseExited(MouseEvent arg0) {
			setMenu.setSystem.setForeground(Color.BLACK);
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}
	}

	// 设置软件更新事件处理
	private class UpdateMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent arg0) {
			setMenu.setVisible(false);
			isSetMenuVisible = false;
			JOptionPane.showMessageDialog(MainUI, "已经是最新版本!", "软件更新",
					JOptionPane.INFORMATION_MESSAGE);
		}

		public void mouseEntered(MouseEvent arg0) {
			setMenu.upDate.setForeground(Color.BLUE);
		}

		public void mouseExited(MouseEvent arg0) {
			setMenu.upDate.setForeground(Color.BLACK);
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}
	}

	// 设置反馈信息事件处理
	private class EchoMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent arg0) {
			setMenu.setVisible(false);
			isSetMenuVisible = false;
		}

		public void mouseEntered(MouseEvent arg0) {
			setMenu.echo.setForeground(Color.BLUE);
		}

		public void mouseExited(MouseEvent arg0) {
			setMenu.echo.setForeground(Color.BLACK);
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}
	}

	// 设置关于事件处理
	private class AboutMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent arg0) {
			setMenu.setVisible(false);
			isSetMenuVisible = false;
			CopyRightFrame crf = new CopyRightFrame(MainUI.getX(),
					MainUI.getY() + 100);
			crf.setVisible(true);
		}

		public void mouseEntered(MouseEvent arg0) {
			setMenu.about.setForeground(Color.BLUE);
		}

		public void mouseExited(MouseEvent arg0) {
			setMenu.about.setForeground(Color.BLACK);
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}
	}

	// 点击事件处理类
	private class ClickActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 如果设置对话框显示则发生抖动事件
			if (isSetDialogVisible) {
				sd.shake();
			} else if (isSetMenuVisible && e.getSource() != set) {
				setMenu.setVisible(false);
				isSetMenuVisible = false;
			} else {
				// 设置按钮事件
				if (e.getSource() == set) {
					if (isSetMenuVisible == false) {
						setMenu.setLocation(MainUI.getX() + 200,
								MainUI.getY() + 25);
						setMenu.setVisible(true);
						isSetMenuVisible = true;
					} else {
						setMenu.setVisible(false);
						isSetMenuVisible = false;
					}
				}
				// 最小化事件
				else if (e.getSource() == minSize) {
					MainUI.setExtendedState(JFrame.ICONIFIED);
				}
				// 关闭按钮事件
				else if (e.getSource() == close) {
					if (CloseOption == 1) {
						SaveData();// 保存所有记录后再退出
						System.exit(0);
					} else
						MainUI.setExtendedState(JFrame.ICONIFIED);
				}
				// 点击连接其他客户端事件
				else if (e.getSource() == BConnect) {
					// 添加链接之后的处理操作
					if (BConnect.getText().equals("连接客户端")) {
						String s = (String) JOptionPane
								.showInputDialog(MainUI, "输入对方IP地址", "",
										JOptionPane.INFORMATION_MESSAGE);
						IP = s;
						new Thread() {
							public void run() {
								try {
									if (isIPAddress(IP)) {
										ConnectState
												.setText("<html>连接中.......");
										talkingSocket = new Socket(IP, 9999);
										output = new PrintWriter(
												talkingSocket.getOutputStream(),
												true);
										output.println("[TALK]");
										ConnectState.setText("<html>连接到" + IP);

										input = new BufferedReader(
												new InputStreamReader(
														talkingSocket
																.getInputStream()));
										// 连接成功
										isConnected = true;
										WatchDogThread wdt = new WatchDogThread();
										wdt.start();
										BConnect.setText("断开连接");
										if (ContactsMap.containsKey(IP)) {// 如果此IP在联系人列表中
											pMessage.LReceiver.setText("  "
													+ ContactsMap.get(IP));
											ConnectState.setText("<html>连接到"
													+ ContactsMap.get(IP));
										} else {
											pMessage.LReceiver.setText(IP);
											ConnectState.setText("<html>连接到"
													+ IP);

											// 询问是否添加陌生IP地址到联系人列表
											int iSAddToContacts = JOptionPane
													.showConfirmDialog(MainUI,
															"是否添加到联系人列表！");
											if (iSAddToContacts == JOptionPane.OK_OPTION) {
												String nickName = JOptionPane
														.showInputDialog(
																MainUI,
																"输入对方的昵称");
												if (!nickName.trim().equals("")) {
													pContacts.addContacts(
															nickName, IP);
													ContactsMap.put(nickName,
															IP);
													pMessage.LReceiver
															.setText(nickName);
													ConnectState
															.setText("<html>连接到"
																	+ nickName);
												}
											}
										}
										// 启动上次为完成的任务
										StartLastUncomplishedTask();
									} else {// 输入了不正确的ip地址
										JOptionPane.showMessageDialog(pCenter,
												"请先输入对方IP", "不是正确的IP地址",
												JOptionPane.ERROR_MESSAGE);
									}
								} catch (IOException ioe) {
									System.err.println(ioe.toString());
									ConnectState.setText("<html>连接失败");
								}
							}
						}.start();
					} else if (BConnect.getText().equals("断开连接")) {
						try {
							if (slt.fileReceiver != null)
								slt.fileReceiver.close();
							if (slt.listener != null)
								slt.listener.close();
							if (slt.WatchDog != null)
								slt.WatchDog.close();
							SocketListenerThread.SocketListenerFinished = true;
							
							BConnect.setText("连接客户端");
							ConnectState.setText("传输文件");
							pMessage.LReceiver.setText("未连接");
							IP = null;
							isConnected = false;
							SaveData();
							for(int i=0;i<pUploadPanel.table.getRowCount();i++)
							{
								pUploadPanel.table.setValueAt("中断", i, 3);
							}
							for(int i=0;i<pDownloadPanel.table.getRowCount();i++)
							{
								pDownloadPanel.table.setValueAt("中断", i, 3);
							}
							SocketListenerThread.SocketListenerFinished = false;
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}// end of connect to others
					// 点击消息按钮
				else if (e.getSource() == mb) {
					card.show(pCenter, "pMessage");
				} else if (e.getSource() == hb) {
					card.show(pCenter, "pMain");
				} else if (e.getSource() == cb) {
					card.show(pCenter, "pContacts");
				} else if (e.getSource() == tb) {
					card.show(pCenter, "pTask");
				}
				// 添加联系人按钮事件
				else if (e.getSource() == pContacts.addContact
						|| e.getSource() == ContactsPopMenu.addItem) {
					acd = new AddContactsDialog(MainUI.getX() + 20,
							MainUI.getY() + 100);
					acd.ensure.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (acd.nickname.getText().trim().length() > 0
									&& acd.IPAddress.getText().trim().length() > 0) {
								pContacts.addContacts(acd.nickname.getText(),
										acd.IPAddress.getText());
								ContactsMap.put(acd.IPAddress.getText(),
										acd.nickname.getText());
								acd.dispose();
							}
						}
					});
				}// end of add contacts
					// 删除联系人事件
				else if (e.getSource() == pContacts.deleteContact) {
					int n = pContacts.table.getSelectedRow();
					if (n >= 0) {
						pContacts.model.removeRow(n);
						ContactsMap.remove(pContacts.table.getValueAt(n, 1));
					}
				}// end of delete contacts
					// 发送消息事件处理
				else if (e.getSource() == pMessage.MessageSendButton
						|| e.getSource() == pMessage.MessageSendField) {
					// 添加发送消息
					String str = pMessage.MessageSendField.getText().trim();
					if (str.equals("")) {
						// 发送的消息为空不进行操作，此处不写任何代码
					} else if (isConnected == false) {
						JOptionPane.showMessageDialog(pMessage, "请先连接您的好友",
								"没有连接到任何好友哦!", JOptionPane.ERROR_MESSAGE);
					} else {
						output.println(str);// 发送到对方
						// 在消息框中添加自己发送的消息
						pMessage.ta.append("\n" + MyNickname + "\n  "
								+ pMessage.MessageSendField.getText() + "\n");
						pMessage.MessageSendField.setText(null);// 消息发送框清空
						pMessage.ta.setCaretPosition(pMessage.ta.getText()
								.length());
						if (str.contains("\\抖动"))
							shake();
					}
				}// end of send message
					// 删除联系人
				else if (e.getSource() == ContactsPopMenu.deleteItem) {
					int n = pContacts.table.getSelectedRow();
					if (n >= 0) {
						pContacts.deleteConatacts(n);
						ContactsMap.remove(pContacts.table.getValueAt(n, 1));
					}
				}
				// 打开下载文件位置
				else if (e.getSource() == downloadTaskPopMenu.openFile) {
					try {
						Runtime.getRuntime().exec(
								"cmd /c start " + DefaultFileSavePath);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				// 打开已完成文件位置
				else if (e.getSource() == complishedTaskPopMenu.openFile) {
					try {
						Runtime.getRuntime().exec(
								"cmd /c start " + DefaultFileSavePath);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				// 下载暂停事件
				else if (e.getSource() == downloadTaskPopMenu.waitTask) {
					int n = pDownloadPanel.table.getSelectedRow();
					isFileTransferWaitMap.remove(pDownloadPanel.table
							.getValueAt(n, 0));
					isFileTransferWaitMap.put(
							(String) pDownloadPanel.table.getValueAt(n, 0), 1);
				}
				// 下载继续事件
				else if (e.getSource() == downloadTaskPopMenu.continueTask) {
					int n = pDownloadPanel.table.getSelectedRow();
					isFileTransferWaitMap.remove(pDownloadPanel.table
							.getValueAt(n, 0));
					isFileTransferWaitMap.put(
							(String) pDownloadPanel.table.getValueAt(n, 0), 0);
				}
				// 删除下载任务
				else if (e.getSource() == downloadTaskPopMenu.deleteTask) {
					int n = pComplishedPanel.table.getSelectedRow();
					if (n >= 0) {

					}
				}
				// 删除已下载完的任务
				else if (e.getSource() == complishedTaskPopMenu.deleteTask) {
					int n = pComplishedPanel.table.getSelectedRow();
					if (n >= 0)
						pComplishedPanel.deleteRow(n);// 执行删除行的操作
				}
			}// end of else isSeDialogVisible
		}// end of actionPerformed
	}// end of ClickActionListener
		// 联系人列表事件，双击连接到联系人主机

	private class ContactsMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2) {
				// 在此添加连接到对应的主机的操作
				isConnected = false;
				pMessage.LReceiver.setText(null);
				int n = pContacts.table.getSelectedRow();
				String s = (String) pContacts.table.getValueAt(n, 1);
				IP = s;
				new Thread() {
					public void run() {
						try {
							if (isIPAddress(IP)) {
								ConnectState.setText("<html>连接中.......");
								talkingSocket = new Socket(IP, 9999);
								output = new PrintWriter(
										talkingSocket.getOutputStream(), true);
								output.println("[TALK]");

								input = new BufferedReader(
										new InputStreamReader(
												talkingSocket.getInputStream()));
								// 连接成功
								isConnected = true;
								WatchDogThread wdt = new WatchDogThread();
								wdt.start();
								BConnect.setText("断开连接");
								StartLastUncomplishedTask();
								if (ContactsMap.containsKey(IP)) {// 如果此IP在联系人列表中
									pMessage.LReceiver.setText("  "
											+ ContactsMap.get(IP));
									ConnectState.setText("<html>连接到"
											+ ContactsMap.get(IP));
								} else {
									pMessage.LReceiver.setText(IP);
									ConnectState.setText("<html>连接到" + IP);
								}
								card.show(pCenter, "pMain");
							} else {// 输入了不正确的ip地址
								JOptionPane.showMessageDialog(pCenter,
										"请先输入对方IP", "不是正确的IP地址",
										JOptionPane.ERROR_MESSAGE);
							}
						} catch (IOException ioe) {
							System.err.println(ioe.toString());
							ConnectState.setText("<html>连接失败");
						}
					}
				}.start();
			} else if (e.isMetaDown()) {
				ContactsPopMenu.show(pContacts, e.getX(), e.getY());
			}
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}
	}

	// 下载列表事件：双击暂停任务
	private class DownloadMouserListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				// 双击暂停下载
				// int n = pDownloadPanel.table.getSelectedRow();
				// pDownloadPanel.table.getCellRenderer(n, 2);
			} else if (e.isMetaDown()
					&& pDownloadPanel.table.getSelectedRow() >= 0) {
				// 添加右键弹出式菜单
				downloadTaskPopMenu.show(pDownloadPanel, e.getX(), e.getY());
			}
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}
	}

	// 已完成任务列表双击事件：打开下载文件位置
	private class ComplishedMouserListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2) {
				// 已完成任务双击直接打开位置
				try {
					Runtime.getRuntime().exec(
							"cmd /c start " + DefaultFileSavePath);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (e.isMetaDown()
					&& pComplishedPanel.table.getSelectedRow() >= 0) {
				// 添加右键弹出式菜单
				complishedTaskPopMenu
						.show(pComplishedPanel, e.getX(), e.getY());
			}
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}

	/*
	 * 软件启动时加载文件进行初始化： 1.读取软件的设置：退出按钮选项、默认保存路径 2.读取联系人列表；
	 * 3.读取软件执行后的发送、接收、完成任务记录， 发现有任务未完成则启动一条线程继续次任务
	 */
	public void Init() {
		FileInputStream fis;
		File InitFileFolder = new File(this.DefaultFileSavePath
				+ "\\P2PFileTransferUsers");
		// 以此文件来判别是否是第一次启动软件
		if (!InitFileFolder.exists()) {
			InitFileFolder.mkdir();
			this.pContacts.addContacts("LocalHost", "127.0.0.1");
			this.pContacts.addContacts("雄哥", "222.20.35.82");
			this.pContacts.addContacts("傻帽蕾哥", "222.20.10.177");
			this.pContacts.addContacts("老宋", "222.20.36.178");
			this.ContactsMap.put("LocalHost", "127.0.0.1");
			this.ContactsMap.put("雄哥", "222.20.35.82");
			this.ContactsMap.put("傻帽蕾哥", "222.20.10.177");
			this.ContactsMap.put("老宋", "222.20.36.178");
		} else {
			// 读取软件设置信息
			File SettingSave = new File(InitFileFolder, "setting.dat");
			// 如果设置文件不存在则按默认的设置软件
			if (!SettingSave.exists()) {
				try {
					SettingSave.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					fis = new FileInputStream(SettingSave);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(fis));
					this.CloseOption = in.read();
					this.DefaultFileSavePath = in.readLine();
					in.close();
					fis.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 读取联系人
			File ContactsSave = new File(InitFileFolder, "ContactsSave.dat");
			if (!ContactsSave.exists()) {
				try {
					ContactsSave.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					fis = new FileInputStream(ContactsSave);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(fis));
					String s, IPaddr;
					while ((s = in.readLine()) != null) {
						in.readLine();// 由于空行的存在词句去掉空行
						IPaddr = in.readLine();
						this.pContacts.addContacts(s, IPaddr);
						ContactsMap.put(IPaddr, s);// 向联系人哈希表中添加一对联系人-IP地址映射
						in.readLine();
					}
					in.close();
					fis.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 读取已完成任务
			File ComplishedTaskSave = new File(InitFileFolder,
					"ComplishedTaskSave.dat");
			if (!ComplishedTaskSave.exists()) {
				try {
					ComplishedTaskSave.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					fis = new FileInputStream(ComplishedTaskSave);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(fis));
					String s, s2;
					while ((s = in.readLine()) != null && s.length() > 1) {
						in.readLine();// 由于空行的存在词句去掉空行
						s2 = in.readLine();
						this.pComplishedPanel.addRow(s, s2, 100, 0);
						in.readLine();
					}
					in.close();
					fis.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * 软件退出之前保存所有的设置： 1.保存软件的设置：退出按钮选项、默认保存路径 2.保存联系人列表； 3.软件执行后的发送、接收、完成任务记录
	 */
	public void SaveData() {
		FileOutputStream fos;
		File InitFileFolder = new File(this.DefaultFileSavePath
				+ "P2PFileTransferUsers");
		InitFileFolder.mkdir();
		File SettingSave = new File(InitFileFolder, "setting.dat");
		if (!SettingSave.exists())
			SettingSave = new File(InitFileFolder, "setting.dat");
		try {
			fos = new FileOutputStream(SettingSave);
			PrintWriter out = new PrintWriter(fos);
			out.write(CloseOption);
			out.write(DefaultFileSavePath);
			out.flush();
			out.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 保存联系人
		File ContactsSave = new File(InitFileFolder, "ContactsSave.dat");
		if (!ContactsSave.exists())
			ContactsSave = new File(InitFileFolder, "ContactsSave.dat");
		try {
			fos = new FileOutputStream(ContactsSave);
			PrintWriter out = new PrintWriter(fos);
			for (int i = 0; i < this.pContacts.table.getRowCount(); i++) {
				out.write((String) this.pContacts.table.getValueAt(i, 0));
				out.write("\n\r");
				out.write((String) this.pContacts.table.getValueAt(i, 1));
				out.write("\n\r");
			}
			out.flush();
			out.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 发送任务列表保存
		File SendTaskSave = new File(InitFileFolder, "SendTaskSave.dat");
		if (!SendTaskSave.exists())
			SendTaskSave = new File(InitFileFolder, "SendTaskSave.dat");
		try {
			fos = new FileOutputStream(SendTaskSave);
			PrintWriter out = new PrintWriter(fos);
			for (int i = 0; i < this.pUploadPanel.table.getRowCount(); i++) {
				out.write((String) this.pUploadPanel.table.getValueAt(i, 0));
				out.write("\n\r");
				out.write((String) this.pUploadPanel.table.getValueAt(i, 1));
				out.write("\n\r");
				out.write(this.pUploadPanel.table.getValueAt(i, 2).toString());
				out.write("\n\r");
				out.flush();
			}
			out.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 接收任务列表保存
		File ReceiveTaskSave = new File(InitFileFolder, "ReceiveTaskSave.dat");
		if (!ReceiveTaskSave.exists())
			ReceiveTaskSave = new File(InitFileFolder, "ReceiveTaskSave.dat");
		try {
			fos = new FileOutputStream(ReceiveTaskSave);
			PrintWriter out = new PrintWriter(fos);
			for (int i = 0; i < this.pDownloadPanel.table.getRowCount(); i++) {
				out.write((String) this.pDownloadPanel.table.getValueAt(i, 0));
				out.write("\n\r");
				out.write((String) this.pDownloadPanel.table.getValueAt(i, 1));
				out.write("\n\r");
				out.write((int) this.pDownloadPanel.table.getValueAt(i, 2));
				out.write("\n\r");
				out.flush();
			}
			out.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 已完成任务列表保存
		File ComplishedTaskSave = new File(InitFileFolder,
				"ComplishedTaskSave.dat");
		if (!ComplishedTaskSave.exists())
			ComplishedTaskSave = new File(InitFileFolder,
					"ComplishedTaskSave.dat");
		try {
			fos = new FileOutputStream(ComplishedTaskSave);
			PrintWriter out = new PrintWriter(fos);
			for (int i = 0; i < this.pComplishedPanel.table.getRowCount(); i++) {
				out.write((String) this.pComplishedPanel.table.getValueAt(i, 0));
				out.write("\n\r");
				out.write((String) this.pComplishedPanel.table.getValueAt(i, 1));
				out.write("\n\r");
			}
			out.flush();
			out.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 为所有的上次软件关闭前没有完成的所有任务开启线程 并且是所有的线程处于运行态，暂停在断点出，等待客 户端点击继续后接着传输。
	 * 连接其他客户端后才调用，否则没有任何意义 *
	 */
	public void StartLastUncomplishedTask() {
		//移除之前的任务
		this.pDownloadPanel.removeAll();
		this.pUploadPanel.removeAll();
		RandomAccessFile tempFile;
		String IPaddr;
		String fileName;
		@SuppressWarnings("unused")
		long startLength;
		File InitFileFolder = new File(this.DefaultFileSavePath
				+ "P2PFileTransferUsers");
		if (!InitFileFolder.exists())
			InitFileFolder.mkdir();// 如果不存在则创建一个文件夹
		// 读取上传任务，在默认路径下搜索是否有后缀为.send.cfg问价
		//有则读取并判断是否创建任务
		File file = new File(DefaultFileSavePath);
		File[] files = file.listFiles();//读取下载目录中的所有文件
		
			for (int i = 0; i < files.length; i++) {
				if((!files[i].isDirectory()) && (files[i].getName().endsWith(".send.cfg"))){  //文件
					try {
						tempFile = new RandomAccessFile(files[i],"rw");						
						IPaddr = tempFile.readLine();// 读取发送者的IP地址
						tempFile.readLine();
						fileName = tempFile.readUTF();// 读取发送的文件
						tempFile.readLine();
						startLength = tempFile.readLong();// 读取已发送的长度
						if (IPaddr.equals(IP))// 当次次连接的IP地址与描述文件一致时创建新线程
						{
							File sendFile = new File(fileName);
							SendFileThread sft = new SendFileThread(this, sendFile);// 启动一个发送任务的线程处于等待放状态
							sft.start();
							while(sft.cur>0){;}
						}	
						tempFile.close();
					} catch (FileNotFoundException e) {
				} catch (IOException e) {}
			} 
	}
		
//		File SendTaskSave = new File(InitFileFolder, "SendTaskSave.dat");
//		if (!SendTaskSave.exists()) {
//			try {
//				SendTaskSave.createNewFile();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//		}
//		try {
//			fis = new FileInputStream(SendTaskSave);
//			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
//			@SuppressWarnings("unused")
//			String s, s2;
//			while ((s = in.readLine()) != null) {
//				in.readLine();// 由于空行的存在词句去掉空行
//				s2 = in.readLine();
//				in.readLine();
//				// 启动相应线程
//				tempFile = new RandomAccessFile(DefaultFileSavePath + "\\" + s
//						+ ".send.cfg", "rw");
//				IPaddr = tempFile.readLine();// 读取发送者的IP地址
//				tempFile.readLine();
//				fileName = tempFile.readUTF();// 读取发送的文件
//				tempFile.readLine();
//				startLength = tempFile.readLong();// 读取已发送的长度
//				if (IPaddr.equals(IP))// 当次次连接的IP地址与描述文件一致时创建新线程
//				{
//					File sendFile = new File(fileName);
//					SendFileThread sft = new SendFileThread(this, sendFile);// 启动一个发送任务的线程处于等待放状态
//					sft.start();
//					while(sft.cur>0){;}
//				}
//				in.readLine();
//			}
//			in.close();
//			fis.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	class WatchDogThread extends Thread {
		int timeout = 5;// 默认超时时间为5秒
		Socket s;
		boolean finished = false;

		public WatchDogThread(int timeout) {
			this.timeout = timeout;
		}

		// 缺省构造方法
		public WatchDogThread() {
		}

		public void run() {
			try {
				s = new Socket(IP, 9999);
				PrintWriter out = new PrintWriter(s.getOutputStream(), true);
				out.println("[WatchDog]");// 发送文件名
				BufferedOutputStream wout = new BufferedOutputStream(
						s.getOutputStream());
				while (!finished) {
					wout.write(0xff);
					Thread.sleep(timeout * 1000);
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (java.net.SocketException e) {
				finished = true;
				System.out.println("连接超时！");
			} catch (IOException e) {// 连接中断
				finished = true;
				SocketListenerThread.SocketListenerFinished = true;
				System.out.println("socke输出流异常");
				IP = null;
				isConnected = false;
				BConnect.setText("连接客户端");
				ConnectState.setText("传输文件");
				pMessage.LReceiver.setText("未连接");
				JOptionPane.showMessageDialog(MainUI, "连接中断！");
				SaveData();
				slt.start();
				SocketListenerThread.SocketListenerFinished = false;
				StartLastUncomplishedTask();
			} catch (InterruptedException e) {
				e.printStackTrace(); 
			}
		}
	}

}

class UploadMouseListener implements MouseListener {
	MainPage mp;
	File SendFile; // 发送文件类

	public UploadMouseListener(MainPage mp) {
		this.mp = mp;
	}

	public void mouseClicked(MouseEvent e) {
		if (mp.isSetDialogVisible) {
			mp.sd.shake();
		} else {
			if (mp.isSetMenuVisible) {
			}
			mp.setMenu.setVisible(false);
			mp.isSetMenuVisible = false;
			if (mp.isConnected) {
				JFileChooser jfc = new JFileChooser();
				jfc.showOpenDialog(jfc);
				jfc.setFont(new Font("楷体", Font.PLAIN, 10));
				jfc.setDialogTitle("选择要发送的文件");
				SendFile = jfc.getSelectedFile();
				if (SendFile == null || SendFile.getName().trim().equals(""))// 发送文件选择错误
				{
					JOptionPane.showMessageDialog(mp.MainUI,
							"Invalid File Name", "Invalid File Name",
							JOptionPane.ERROR_MESSAGE);
				} else {
					File file = new File(SendFile.getAbsolutePath());
					SendFileThread sft = new SendFileThread(mp, file);
					sft.start();
				}
			} else {
				JOptionPane.showMessageDialog(mp.MainUI, "请先连接您的好友",
						"没有连接到任何好友哦!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		mp.upload.setForeground(new Color(1.0f, 0.0f, 0.0f));
	}

	public void mouseExited(MouseEvent e) {
		mp.upload.setForeground(Color.black);
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}