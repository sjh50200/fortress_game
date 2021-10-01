package Frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import BottomUI.ChatPanel;
import BottomUI.MainPanel;

public class GameFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket socket;
	private String UserName;
	private Vector UserVec;
	private Boolean myTurn = false;
	JPanel temp = new JPanel();
	MainPanel mainPanel;
	ChatPanel cp;
	int movecount = 0;
	boolean isFired = false;
	boolean isCharging = false;
	private String missileSort = "Normal";

	public GameFrame(String username, String ip_addr, String port_no) {
		this.setTitle("Fortress");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1296, 839);
		this.setVisible(true);
		this.setResizable(false);

		mainPanel = new MainPanel(this);
		mainPanel.setLayout(null);
		mainPanel.addKeyListener(new MyKeyListener());
		mainPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				mainPanel.requestFocus();
			}
		});
		this.add(mainPanel);
		cp = new ChatPanel(this);
		mainPanel.add(cp);
		cp.setOpaque(false);
		cp.setBounds(0, 550, 1280, 250);
		cp.setLayout(null);
		cp.txtInput.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				cp.txtInput.requestFocus();
			}
		});
		TextSendAction action = new TextSendAction();
		cp.btnSend.addActionListener(action);
		cp.txtInput.addActionListener(action);

		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			UserName = username;
			System.out.println("User " + username + " connecting " + ip_addr + " " + port_no);
			// SendMessage("/login " + UserName);

			ChatMsg obcm = new ChatMsg(UserName, "100", "Hello");
			SendObject(obcm);

			ListenNetwork net = new ListenNetwork();
			net.start();

			obcm = new ChatMsg(UserName, "103", "Ready");
			SendObject(obcm);

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
			System.exit(1);
		}
	}

	public Boolean isTurn() {
		if (myTurn == true)
			return true;
		else
			return false;
	}

	private void doHitted(String missileSort) {
		cp.doHitted(missileSort);
	}

	public void hit() {
		ChatMsg cm = new ChatMsg(UserName, "450", "Hit");
		cm.missileSort = missileSort;
		SendObject(cm);
		cm = new ChatMsg(UserName, "451", "Next");
		SendObject(cm);
	}

	public void miss() {
		ChatMsg cm = new ChatMsg(UserName, "449", "Miss");
		SendObject(cm);
		cm = new ChatMsg(UserName, "451", "Next");
		SendObject(cm);
	}

	public void sendLogout() {
		ChatMsg cm = new ChatMsg(UserName, "500", "EXIT");
		SendObject(cm);
	}

	public void SendKeyEvent(KeyEvent e) {
		if (myTurn == true) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
				ChatMsg cm = new ChatMsg(UserName, "300", "KEY");
				cm.keycode = e.getKeyCode();
				SendObject(cm);
				movecount++;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				ChatMsg cm = new ChatMsg(UserName, "400", "FIRE");
				cm.gaugeCount = mainPanel.getGauge();
				cm.missileSort = missileSort;
				cm.missileAngleX = mainPanel.getAngleX();
				cm.missileAngleY = mainPanel.getAngleY();
				cp.angleInit();
				SendObject(cm);
			}
		} else
			return;
	}

	public void GameStart(Vector<String> userList, int[] startX, int[] startY) {
		mainPanel.setCharPosition(userList, UserName, startX, startY);
		mainPanel.drawMissle();
		if (userList.elementAt(0).equals(UserName))
			cp.setTeam("red");
		else if (userList.elementAt(1).equals(UserName))
			cp.setTeam("blue");
		cp.makeUI(userList, UserName);
	}

	class MyKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (myTurn == true) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if (movecount <= 20) {
						mainPanel.moveMyCharacter(e.getKeyCode());
						cp.moveGaugeUp();
						SendKeyEvent(e);
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (movecount <= 20) {
						mainPanel.moveMyCharacter(e.getKeyCode());
						cp.moveGaugeUp();
						SendKeyEvent(e);
					}
					break;
				case KeyEvent.VK_SPACE:
					if (!isFired) {
						cp.fillGauge();
						mainPanel.fillGauge();
					}
					break;
				case KeyEvent.VK_UP:
					mainPanel.angleUp();
					cp.lineUp();
					mainPanel.missileInitWithoutAngle();
					break;
				case KeyEvent.VK_DOWN:
					mainPanel.angleDown();
					cp.lineDown();
					mainPanel.missileInitWithoutAngle();
					break;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (myTurn == true) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_SPACE:
					if (!isFired) {
						mainPanel.fire(missileSort);
						movecount = 0;
						cp.resetGaugeAndFire();
						SendKeyEvent(e);
					}
					break;
				}
			}
		}
	}

	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button or Enter key
			if (e.getSource() == cp.btnSend || e.getSource() == cp.txtInput) {
				String msg = null;
				msg = cp.txtInput.getText();
				cp.txtInput.setText("");
				cp.txtInput.requestFocus();
				AppendText("[" + UserName + "] " + msg);
				SendMessage(msg);
			}
		}
	}

	public void AppendText(String msg) {
		msg = msg.trim();
		int len = cp.textArea.getDocument().getLength(); // 끝으로 이동

		StyledDocument doc = cp.textArea.getStyledDocument();
		SimpleAttributeSet left = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(left, Color.BLACK);
		doc.setParagraphAttributes(doc.getLength(), 1, left, false);
		try {
			doc.insertString(doc.getLength(), msg + "\n", left);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void SendObject(Object ob) {
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			AppendText("SendObject Error");
		}
	}

	public void SendMessage(String msg) {
		try {
			ChatMsg obcm = new ChatMsg(UserName, "200", msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			AppendText("oos.writeObject() error");
			try {
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}

	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						if (cm.UserName.equals("SERVER")) {
							msg = String.format("%s", cm.data);
						} else {
							msg = String.format("[%s] %s", cm.UserName, cm.data);
						}
					} else
						continue;
					switch (cm.code) {
					case "200": // chat
						AppendText(msg);
						break;
					case "104":
						GameStart(cm.UserList, cm.startX, cm.startY);
						mainPanel.repaint();
						break;
					case "300":
						if (!cm.UserName.equals(UserName))
							mainPanel.moveEnemyCharacter(cm.keycode);
						break;
					case "400":
						if (!cm.UserName.equals(UserName)) {
							mainPanel.missileInit();
							mainPanel.fireEnemy(cm.gaugeCount, cm.missileAngleX, cm.missileAngleY, cm.missileSort);
						}
						break;
					case "449":
						doNothing();
						break;
					case "450":
						if (!cm.UserName.equals(UserName))
							doHitted(cm.missileSort);
						break;
					case "451":
						if (cm.UserName.equals(UserName)) {
							AppendText("Your turn!");
							missileSort = "Normal";
							myTurn = true;
							mainPanel.setTurnTrue();
							// mainPanel.missileInit();
						} else {
							myTurn = false;
							mainPanel.setTurnFalse();
							mainPanel.setHittedFalse();
							cp.moveInit();
						}
						break;
					case "452":
						if (!cm.UserName.equals(UserName))
							mainPanel.win();
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
						System.exit(1);
						ois.close();
						oos.close();
						socket.close();
					} catch (Exception ee) {
						break;
					}
				}
			}
		}

		private void doNothing() {
			// TODO Auto-generated method stub

		}
	}

	public void gameOver() {
		mainPanel.gameOver();
	}

	public void SetMissileSize(String missileSort) {
		if (myTurn == true)
			this.missileSort = missileSort;
	}
}