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
 * @author ����
 * @version 1.0
 * @file mainPage.java
 * @date 2014-12-5 to 2015-1-
 * */
public class MainPage {
	// �������
	public final int mainPageWidth = 300;
	public final int mainPageHeight = 516;
	private int xx, yy;
	private boolean isDraging = false;// �Ƿ������϶���ʶ
	boolean isSetMenuVisible = false;// ���öԻ����Ƿ���ʾ��ʶ
	public boolean isConnected = false; // �Ƿ������ӵ������ͻ���
	private int CloseOption = 1; // ���ùرհ�ťѡ��
	boolean isSetDialogVisible = false;// ���öԻ����Ƿ���ʾ��׼
	public String DefaultFileSavePath = "E:\\";
	public String IP; // ���ӶԷ���IP��ַ
	public String MyNickname = "me"; // �Լ����ǳ�
	Socket talkingSocket; // �����׽���
	PrintWriter output = null; // �����
	BufferedReader input = null; // ������
	public HashMap<String, Integer> isFileTransferWaitMap = new HashMap<String, Integer>();
	// ��ϵ��<IP��ַ,�ǳ�>��ϣ��
	public HashMap<String, String> ContactsMap = new HashMap<String, String>();

	public JFrame MainUI; // ������
	/* ������� */
	private JButton drag; // ������קͼ�ν���
	private JLabel softName; // �����
	private JButton BLogo; // ���ͼ��
	private SetButton set; // ��������ð�ť
	private MinSizeButton minSize;// �����С����ť
	private CloseButton close; // ����رհ�ť
	private JPanel pConnect; // ��ʾ���Ӱ�ťʵ���������ͻ�������
	private JPanel pOperation; // �ļ��������ز������
	private JPanel pMain; // Home��������
	public JPanel pCenter; // �м���ʾ�����
	public JPanel pHome; // �ײ���Ҫ�������
	public ChatPanel pMessage; // ��Ϣ���
	JPanel pSysSet;
	public ContactsPanel pContacts;// ��ϵ�����
	public JTabbedPane pTask; // �������
	public TaskPanel pDownloadPanel; // �����б����
	public TaskPanel pUploadPanel; // �ϴ��б����
	public TaskPanel pComplishedPanel; // ������������
	public CardLayout card;
	JButton upload; // �����ļ���ť
	JButton download; // �����ļ���ť
	JButton suggest; // ��������
	public JButton BConnect; // ���������ͻ��˰�ť
	public JLabel ConnectState; // ��ʾ����״̬��ǩ
	public SetMenu setMenu; // ϵͳ���ò˵���
	public SetDialog sd;
	public MessageButton mb; // ��Ϣ��ť
	public HomeButton hb; // ��ҳ��ť
	public ContactsButton cb; // ��ϵ�˰�ť
	private PopMenu ContactsPopMenu;
	private TaskPopMenu downloadTaskPopMenu;// �����б����˵�
	private TaskPopMenu complishedTaskPopMenu;// ������б����˵�
	public TaskButton tb;
	InputStream in;
	AddContactsDialog acd;
	// �̼߳�����
	public SocketListenerThread slt;
	Image image;

	public MainPage() {
		MainUI = new JFrame();
		MainUI.setSize(mainPageWidth, mainPageHeight);
		MainUI.setResizable(false);
		/* ���ý������ */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		MainUI.setLocation((screenSize.width - mainPageWidth) / 2,
				(screenSize.height - mainPageHeight) / 2);
		MainUI.setUndecorated(true);/* �����ޱ߿� */
		MainUI.setOpacity(0.93f);
		MainUI.setIconImage(new ImageIcon("Image\\SystemLogo.png").getImage());
		//MainUI.setIconImage(new ImageIcon(getClass().getResource("/Image\\SystemLogo.png")).getImage());
		// �������ƶ��Ĵ���
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

		// ���ϵͳ�������
		// �������ð�ť��
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
		softName = new JLabel("TCP/IP�ϵ�����");
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
		// ������ͼ�꣬��������հ��������ã���С�����رտؼ�
		pSysSet.add(BLogo);
		pSysSet.add(softName);
		pSysSet.add(drag);
		pSysSet.add(set);
		pSysSet.add(minSize);
		pSysSet.add(close);
		// �м�������
		pMain = new JPanel();

		pConnect = new JPanel() {
			private static final long serialVersionUID = 1L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, -30, mainPageWidth, 300, this);
			}
		};
		ConnectState = new JLabel("�����ļ�");
		ConnectState.setFont(new Font("����", 14, 14));
		ConnectState.setForeground(Color.WHITE);

		pConnect.setPreferredSize(new Dimension(mainPageWidth, 267));
		pConnect.setBorder(null);
		BConnect = new JButton("���ӿͻ���");
		BConnect.setContentAreaFilled(false);
		BConnect.setPreferredSize(new Dimension(110, 20));
		BConnect.setBorderPainted(false);
		BConnect.setFocusPainted(false);
		BConnect.setFont(new Font("����", Font.PLAIN, 14));
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

		upload = new JButton("�����ļ�");
		upload.setFont(new Font("����", 12, 20));
		upload.setBorderPainted(false);
		upload.setPreferredSize(new Dimension(mainPageWidth, 52));
		upload.setContentAreaFilled(false);
		upload.setFocusPainted(false);

		upload.addMouseListener(new UploadMouseListener(this));
		download = new JButton("�����ļ�");
		download.setBorderPainted(false);
		download.setFont(new Font("����", 12, 20));
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

		suggest = new JButton("�°汾���飬ȫ��Ϊ������");
		suggest.setBorderPainted(false);
		suggest.setFont(new Font("����", 12, 20));
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

		// �ײ��������
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
		 * �м���������Ϊ��Ƭ���֣��ֱ�װ����Ϣ��壬������壬 ��ϵ����壬���������壬�����ͬ�İ�ť���л�Ϊ��Ӧ ����ʾ���*
		 */
		// ��Ϣ���
		pMessage = new ChatPanel();
		pMessage.MessageSendButton.addActionListener(new ClickActionListener());
		pMessage.MessageSendField.addActionListener(new ClickActionListener());
		
		// ��ϵ�����
		pContacts = new ContactsPanel();
		pContacts.addContact.addActionListener(new ClickActionListener());
		pContacts.deleteContact.addActionListener(new ClickActionListener());
		pContacts.table.addMouseListener(new ContactsMouseListener());
		pContacts.setBackground(Color.blue);
		ContactsPopMenu = new PopMenu();
		pContacts.add(ContactsPopMenu);
		ContactsPopMenu.addItem.addActionListener(new ClickActionListener());
		ContactsPopMenu.deleteItem.addActionListener(new ClickActionListener());

		// �������
		pTask = new JTabbedPane();
		pTask.setBorder(null);
		pTask.setFont(new Font("����", Font.PLAIN, 16));
		pTask.setBackground(new Color(189, 252, 201));
		pDownloadPanel = new TaskPanel();
		pUploadPanel = new TaskPanel();
		pComplishedPanel = new TaskPanel();
		pTask.addTab("  ����  ", pUploadPanel);
		pTask.addTab("  ����  ", pDownloadPanel);
		pTask.addTab("  ���  ", pComplishedPanel);
		// ����������������¼�
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
		// �����������ӵ���¼�
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

		// ���弯��
		MainUI.setLayout(new BorderLayout());
		MainUI.add(pSysSet, BorderLayout.NORTH);
		MainUI.add(pCenter, BorderLayout.CENTER);
		MainUI.add(pHome, BorderLayout.SOUTH);

		MainUI.setVisible(true);
		slt = new SocketListenerThread(this);
		slt.start();
	}

	// MainPage����
	/** ���ڶ��� */
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

	// ���ñ���ͼƬ
	public void setBackImage(Image im) {
		image = im;
	}

	// �����������Ĭ�ϱ���ͼƬ
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

	// �ж������IP��ַ�Ƿ���ϱ�׼
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

	/** �¼���Ӧ�ڲ��� */
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

	// ����ϵͳ�¼�����
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

	// ������������¼�����
	private class UpdateMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent arg0) {
			setMenu.setVisible(false);
			isSetMenuVisible = false;
			JOptionPane.showMessageDialog(MainUI, "�Ѿ������°汾!", "�������",
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

	// ���÷�����Ϣ�¼�����
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

	// ���ù����¼�����
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

	// ����¼�������
	private class ClickActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// ������öԻ�����ʾ���������¼�
			if (isSetDialogVisible) {
				sd.shake();
			} else if (isSetMenuVisible && e.getSource() != set) {
				setMenu.setVisible(false);
				isSetMenuVisible = false;
			} else {
				// ���ð�ť�¼�
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
				// ��С���¼�
				else if (e.getSource() == minSize) {
					MainUI.setExtendedState(JFrame.ICONIFIED);
				}
				// �رհ�ť�¼�
				else if (e.getSource() == close) {
					if (CloseOption == 1) {
						SaveData();// �������м�¼�����˳�
						System.exit(0);
					} else
						MainUI.setExtendedState(JFrame.ICONIFIED);
				}
				// ������������ͻ����¼�
				else if (e.getSource() == BConnect) {
					// �������֮��Ĵ������
					if (BConnect.getText().equals("���ӿͻ���")) {
						String s = (String) JOptionPane
								.showInputDialog(MainUI, "����Է�IP��ַ", "",
										JOptionPane.INFORMATION_MESSAGE);
						IP = s;
						new Thread() {
							public void run() {
								try {
									if (isIPAddress(IP)) {
										ConnectState
												.setText("<html>������.......");
										talkingSocket = new Socket(IP, 9999);
										output = new PrintWriter(
												talkingSocket.getOutputStream(),
												true);
										output.println("[TALK]");
										ConnectState.setText("<html>���ӵ�" + IP);

										input = new BufferedReader(
												new InputStreamReader(
														talkingSocket
																.getInputStream()));
										// ���ӳɹ�
										isConnected = true;
										WatchDogThread wdt = new WatchDogThread();
										wdt.start();
										BConnect.setText("�Ͽ�����");
										if (ContactsMap.containsKey(IP)) {// �����IP����ϵ���б���
											pMessage.LReceiver.setText("  "
													+ ContactsMap.get(IP));
											ConnectState.setText("<html>���ӵ�"
													+ ContactsMap.get(IP));
										} else {
											pMessage.LReceiver.setText(IP);
											ConnectState.setText("<html>���ӵ�"
													+ IP);

											// ѯ���Ƿ����İ��IP��ַ����ϵ���б�
											int iSAddToContacts = JOptionPane
													.showConfirmDialog(MainUI,
															"�Ƿ���ӵ���ϵ���б�");
											if (iSAddToContacts == JOptionPane.OK_OPTION) {
												String nickName = JOptionPane
														.showInputDialog(
																MainUI,
																"����Է����ǳ�");
												if (!nickName.trim().equals("")) {
													pContacts.addContacts(
															nickName, IP);
													ContactsMap.put(nickName,
															IP);
													pMessage.LReceiver
															.setText(nickName);
													ConnectState
															.setText("<html>���ӵ�"
																	+ nickName);
												}
											}
										}
										// �����ϴ�Ϊ��ɵ�����
										StartLastUncomplishedTask();
									} else {// �����˲���ȷ��ip��ַ
										JOptionPane.showMessageDialog(pCenter,
												"��������Է�IP", "������ȷ��IP��ַ",
												JOptionPane.ERROR_MESSAGE);
									}
								} catch (IOException ioe) {
									System.err.println(ioe.toString());
									ConnectState.setText("<html>����ʧ��");
								}
							}
						}.start();
					} else if (BConnect.getText().equals("�Ͽ�����")) {
						try {
							if (slt.fileReceiver != null)
								slt.fileReceiver.close();
							if (slt.listener != null)
								slt.listener.close();
							if (slt.WatchDog != null)
								slt.WatchDog.close();
							SocketListenerThread.SocketListenerFinished = true;
							
							BConnect.setText("���ӿͻ���");
							ConnectState.setText("�����ļ�");
							pMessage.LReceiver.setText("δ����");
							IP = null;
							isConnected = false;
							SaveData();
							for(int i=0;i<pUploadPanel.table.getRowCount();i++)
							{
								pUploadPanel.table.setValueAt("�ж�", i, 3);
							}
							for(int i=0;i<pDownloadPanel.table.getRowCount();i++)
							{
								pDownloadPanel.table.setValueAt("�ж�", i, 3);
							}
							SocketListenerThread.SocketListenerFinished = false;
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}// end of connect to others
					// �����Ϣ��ť
				else if (e.getSource() == mb) {
					card.show(pCenter, "pMessage");
				} else if (e.getSource() == hb) {
					card.show(pCenter, "pMain");
				} else if (e.getSource() == cb) {
					card.show(pCenter, "pContacts");
				} else if (e.getSource() == tb) {
					card.show(pCenter, "pTask");
				}
				// �����ϵ�˰�ť�¼�
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
					// ɾ����ϵ���¼�
				else if (e.getSource() == pContacts.deleteContact) {
					int n = pContacts.table.getSelectedRow();
					if (n >= 0) {
						pContacts.model.removeRow(n);
						ContactsMap.remove(pContacts.table.getValueAt(n, 1));
					}
				}// end of delete contacts
					// ������Ϣ�¼�����
				else if (e.getSource() == pMessage.MessageSendButton
						|| e.getSource() == pMessage.MessageSendField) {
					// ��ӷ�����Ϣ
					String str = pMessage.MessageSendField.getText().trim();
					if (str.equals("")) {
						// ���͵���ϢΪ�ղ����в������˴���д�κδ���
					} else if (isConnected == false) {
						JOptionPane.showMessageDialog(pMessage, "�����������ĺ���",
								"û�����ӵ��κκ���Ŷ!", JOptionPane.ERROR_MESSAGE);
					} else {
						output.println(str);// ���͵��Է�
						// ����Ϣ��������Լ����͵���Ϣ
						pMessage.ta.append("\n" + MyNickname + "\n  "
								+ pMessage.MessageSendField.getText() + "\n");
						pMessage.MessageSendField.setText(null);// ��Ϣ���Ϳ����
						pMessage.ta.setCaretPosition(pMessage.ta.getText()
								.length());
						if (str.contains("\\����"))
							shake();
					}
				}// end of send message
					// ɾ����ϵ��
				else if (e.getSource() == ContactsPopMenu.deleteItem) {
					int n = pContacts.table.getSelectedRow();
					if (n >= 0) {
						pContacts.deleteConatacts(n);
						ContactsMap.remove(pContacts.table.getValueAt(n, 1));
					}
				}
				// �������ļ�λ��
				else if (e.getSource() == downloadTaskPopMenu.openFile) {
					try {
						Runtime.getRuntime().exec(
								"cmd /c start " + DefaultFileSavePath);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				// ��������ļ�λ��
				else if (e.getSource() == complishedTaskPopMenu.openFile) {
					try {
						Runtime.getRuntime().exec(
								"cmd /c start " + DefaultFileSavePath);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				// ������ͣ�¼�
				else if (e.getSource() == downloadTaskPopMenu.waitTask) {
					int n = pDownloadPanel.table.getSelectedRow();
					isFileTransferWaitMap.remove(pDownloadPanel.table
							.getValueAt(n, 0));
					isFileTransferWaitMap.put(
							(String) pDownloadPanel.table.getValueAt(n, 0), 1);
				}
				// ���ؼ����¼�
				else if (e.getSource() == downloadTaskPopMenu.continueTask) {
					int n = pDownloadPanel.table.getSelectedRow();
					isFileTransferWaitMap.remove(pDownloadPanel.table
							.getValueAt(n, 0));
					isFileTransferWaitMap.put(
							(String) pDownloadPanel.table.getValueAt(n, 0), 0);
				}
				// ɾ����������
				else if (e.getSource() == downloadTaskPopMenu.deleteTask) {
					int n = pComplishedPanel.table.getSelectedRow();
					if (n >= 0) {

					}
				}
				// ɾ���������������
				else if (e.getSource() == complishedTaskPopMenu.deleteTask) {
					int n = pComplishedPanel.table.getSelectedRow();
					if (n >= 0)
						pComplishedPanel.deleteRow(n);// ִ��ɾ���еĲ���
				}
			}// end of else isSeDialogVisible
		}// end of actionPerformed
	}// end of ClickActionListener
		// ��ϵ���б��¼���˫�����ӵ���ϵ������

	private class ContactsMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2) {
				// �ڴ�������ӵ���Ӧ�������Ĳ���
				isConnected = false;
				pMessage.LReceiver.setText(null);
				int n = pContacts.table.getSelectedRow();
				String s = (String) pContacts.table.getValueAt(n, 1);
				IP = s;
				new Thread() {
					public void run() {
						try {
							if (isIPAddress(IP)) {
								ConnectState.setText("<html>������.......");
								talkingSocket = new Socket(IP, 9999);
								output = new PrintWriter(
										talkingSocket.getOutputStream(), true);
								output.println("[TALK]");

								input = new BufferedReader(
										new InputStreamReader(
												talkingSocket.getInputStream()));
								// ���ӳɹ�
								isConnected = true;
								WatchDogThread wdt = new WatchDogThread();
								wdt.start();
								BConnect.setText("�Ͽ�����");
								StartLastUncomplishedTask();
								if (ContactsMap.containsKey(IP)) {// �����IP����ϵ���б���
									pMessage.LReceiver.setText("  "
											+ ContactsMap.get(IP));
									ConnectState.setText("<html>���ӵ�"
											+ ContactsMap.get(IP));
								} else {
									pMessage.LReceiver.setText(IP);
									ConnectState.setText("<html>���ӵ�" + IP);
								}
								card.show(pCenter, "pMain");
							} else {// �����˲���ȷ��ip��ַ
								JOptionPane.showMessageDialog(pCenter,
										"��������Է�IP", "������ȷ��IP��ַ",
										JOptionPane.ERROR_MESSAGE);
							}
						} catch (IOException ioe) {
							System.err.println(ioe.toString());
							ConnectState.setText("<html>����ʧ��");
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

	// �����б��¼���˫����ͣ����
	private class DownloadMouserListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				// ˫����ͣ����
				// int n = pDownloadPanel.table.getSelectedRow();
				// pDownloadPanel.table.getCellRenderer(n, 2);
			} else if (e.isMetaDown()
					&& pDownloadPanel.table.getSelectedRow() >= 0) {
				// ����Ҽ�����ʽ�˵�
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

	// ����������б�˫���¼����������ļ�λ��
	private class ComplishedMouserListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2) {
				// ���������˫��ֱ�Ӵ�λ��
				try {
					Runtime.getRuntime().exec(
							"cmd /c start " + DefaultFileSavePath);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (e.isMetaDown()
					&& pComplishedPanel.table.getSelectedRow() >= 0) {
				// ����Ҽ�����ʽ�˵�
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
	 * �������ʱ�����ļ����г�ʼ���� 1.��ȡ��������ã��˳���ťѡ�Ĭ�ϱ���·�� 2.��ȡ��ϵ���б�
	 * 3.��ȡ���ִ�к�ķ��͡����ա���������¼�� ����������δ���������һ���̼߳���������
	 */
	public void Init() {
		FileInputStream fis;
		File InitFileFolder = new File(this.DefaultFileSavePath
				+ "\\P2PFileTransferUsers");
		// �Դ��ļ����б��Ƿ��ǵ�һ���������
		if (!InitFileFolder.exists()) {
			InitFileFolder.mkdir();
			this.pContacts.addContacts("LocalHost", "127.0.0.1");
			this.pContacts.addContacts("�۸�", "222.20.35.82");
			this.pContacts.addContacts("ɵñ�ٸ�", "222.20.10.177");
			this.pContacts.addContacts("����", "222.20.36.178");
			this.ContactsMap.put("LocalHost", "127.0.0.1");
			this.ContactsMap.put("�۸�", "222.20.35.82");
			this.ContactsMap.put("ɵñ�ٸ�", "222.20.10.177");
			this.ContactsMap.put("����", "222.20.36.178");
		} else {
			// ��ȡ���������Ϣ
			File SettingSave = new File(InitFileFolder, "setting.dat");
			// ��������ļ���������Ĭ�ϵ��������
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
			// ��ȡ��ϵ��
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
						in.readLine();// ���ڿ��еĴ��ڴʾ�ȥ������
						IPaddr = in.readLine();
						this.pContacts.addContacts(s, IPaddr);
						ContactsMap.put(IPaddr, s);// ����ϵ�˹�ϣ�������һ����ϵ��-IP��ַӳ��
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
			// ��ȡ���������
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
						in.readLine();// ���ڿ��еĴ��ڴʾ�ȥ������
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
	 * ����˳�֮ǰ�������е����ã� 1.������������ã��˳���ťѡ�Ĭ�ϱ���·�� 2.������ϵ���б� 3.���ִ�к�ķ��͡����ա���������¼
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
		// ������ϵ��
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
		// ���������б���
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
		// ���������б���
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
		// ����������б���
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
	 * Ϊ���е��ϴ�����ر�ǰû����ɵ������������߳� ���������е��̴߳�������̬����ͣ�ڶϵ�����ȴ��� ���˵����������Ŵ��䡣
	 * ���������ͻ��˺�ŵ��ã�����û���κ����� *
	 */
	public void StartLastUncomplishedTask() {
		//�Ƴ�֮ǰ������
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
			InitFileFolder.mkdir();// ����������򴴽�һ���ļ���
		// ��ȡ�ϴ�������Ĭ��·���������Ƿ��к�׺Ϊ.send.cfg�ʼ�
		//�����ȡ���ж��Ƿ񴴽�����
		File file = new File(DefaultFileSavePath);
		File[] files = file.listFiles();//��ȡ����Ŀ¼�е������ļ�
		
			for (int i = 0; i < files.length; i++) {
				if((!files[i].isDirectory()) && (files[i].getName().endsWith(".send.cfg"))){  //�ļ�
					try {
						tempFile = new RandomAccessFile(files[i],"rw");						
						IPaddr = tempFile.readLine();// ��ȡ�����ߵ�IP��ַ
						tempFile.readLine();
						fileName = tempFile.readUTF();// ��ȡ���͵��ļ�
						tempFile.readLine();
						startLength = tempFile.readLong();// ��ȡ�ѷ��͵ĳ���
						if (IPaddr.equals(IP))// ���δ����ӵ�IP��ַ�������ļ�һ��ʱ�������߳�
						{
							File sendFile = new File(fileName);
							SendFileThread sft = new SendFileThread(this, sendFile);// ����һ������������̴߳��ڵȴ���״̬
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
//				in.readLine();// ���ڿ��еĴ��ڴʾ�ȥ������
//				s2 = in.readLine();
//				in.readLine();
//				// ������Ӧ�߳�
//				tempFile = new RandomAccessFile(DefaultFileSavePath + "\\" + s
//						+ ".send.cfg", "rw");
//				IPaddr = tempFile.readLine();// ��ȡ�����ߵ�IP��ַ
//				tempFile.readLine();
//				fileName = tempFile.readUTF();// ��ȡ���͵��ļ�
//				tempFile.readLine();
//				startLength = tempFile.readLong();// ��ȡ�ѷ��͵ĳ���
//				if (IPaddr.equals(IP))// ���δ����ӵ�IP��ַ�������ļ�һ��ʱ�������߳�
//				{
//					File sendFile = new File(fileName);
//					SendFileThread sft = new SendFileThread(this, sendFile);// ����һ������������̴߳��ڵȴ���״̬
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
		int timeout = 5;// Ĭ�ϳ�ʱʱ��Ϊ5��
		Socket s;
		boolean finished = false;

		public WatchDogThread(int timeout) {
			this.timeout = timeout;
		}

		// ȱʡ���췽��
		public WatchDogThread() {
		}

		public void run() {
			try {
				s = new Socket(IP, 9999);
				PrintWriter out = new PrintWriter(s.getOutputStream(), true);
				out.println("[WatchDog]");// �����ļ���
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
				System.out.println("���ӳ�ʱ��");
			} catch (IOException e) {// �����ж�
				finished = true;
				SocketListenerThread.SocketListenerFinished = true;
				System.out.println("socke������쳣");
				IP = null;
				isConnected = false;
				BConnect.setText("���ӿͻ���");
				ConnectState.setText("�����ļ�");
				pMessage.LReceiver.setText("δ����");
				JOptionPane.showMessageDialog(MainUI, "�����жϣ�");
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
	File SendFile; // �����ļ���

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
				jfc.setFont(new Font("����", Font.PLAIN, 10));
				jfc.setDialogTitle("ѡ��Ҫ���͵��ļ�");
				SendFile = jfc.getSelectedFile();
				if (SendFile == null || SendFile.getName().trim().equals(""))// �����ļ�ѡ�����
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
				JOptionPane.showMessageDialog(mp.MainUI, "�����������ĺ���",
						"û�����ӵ��κκ���Ŷ!", JOptionPane.ERROR_MESSAGE);
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