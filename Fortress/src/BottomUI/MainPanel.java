package BottomUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import Frame.ChatMsg;
import Frame.GameFrame;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	ImageIcon char1Im = new ImageIcon("../Fortress/src/Character/char1(1).png");
	ImageIcon char1N = new ImageIcon("../Fortress/src/Character/RedTeam.png");

	ImageIcon char2Im = new ImageIcon("../Fortress/src/Character/char2(1).png");
	ImageIcon char2N = new ImageIcon("../Fortress/src/Character/BlueTeam.png");

	ImageIcon exit = new ImageIcon("../Fortress/src/img/exitButton.png");
	ImageIcon exitClick = new ImageIcon("../Fortress/src/img/exitButtonclick.png");

	JLabel me = null;
	JLabel Enemy = null;
	private int mystartX;
	private int mystartY;
	String UserName = null;
	private Vector<String> UserList = new Vector<String>();
	private int[] startX;
	private int[] startY;
	private int missleStartX;
	private int missleStartY;
	private int EnemyX;
	private int EnemyY;
	private int EmissleStartX;
	private int EmissleStartY;
	private int hitIndex;
	private boolean isHitted = false;
	private boolean myTurn = false;
	private JButton jb = new JButton(exit);
	private JLabel characterUI = new JLabel();
	private JLabel characterUI2 = new JLabel();
	GameFrame gf;

	String team;

	FirePanel fire;

	public MainPanel(GameFrame gf) {
		this.gf = gf;

		jb.setPressedIcon(exitClick);
		jb.setBorderPainted(false);
		jb.setContentAreaFilled(false);
		jb.setContentAreaFilled(false);
		jb.setOpaque(false);
		jb.setBounds(590, 0, 100, 35);
		jb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				gf.sendLogout();
				System.exit(0);
			}
		});
		this.add(jb);
		this.setComponentZOrder(jb, 0);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		ImageIcon img = new ImageIcon("../Fortress/src/Maps/map1.jpg");
		Image im = img.getImage();
		g.drawImage(im, 0, 0, 1280, 800, this);
	}

	public void setCharPosition(Vector<String> UserList, String UserName, int[] startX, int[] startY) {
		this.startX = new int[startX.length];
		this.startY = new int[startY.length];
		this.startX = startX;
		this.startY = startY;
		this.UserList = UserList;
		this.UserName = UserName;
		MakeCharacters();
	}

	public void MakeCharacters() {
		characterUI.setBounds(startX[0], startY[0], 80, 90);
		characterUI.setLayout(null);
		this.add(characterUI);

		JLabel tank = new JLabel(char1Im);
		tank.setBounds(0, 0, 80, 60);
		characterUI.add(tank);

		JLabel name = new JLabel(char1N);
		name.setBounds(0, 60, 80, 30);
		name.setLayout(new BorderLayout());
		characterUI.add(name);

		JLabel text = new JLabel(UserList.elementAt(0), JLabel.CENTER);
		text.setName(UserList.elementAt(0));
		text.setForeground(Color.WHITE);
		name.add(text, BorderLayout.CENTER);
		name.repaint();

		characterUI2.setBounds(startX[1], startY[1], 80, 90);
		characterUI2.setLayout(null);
		this.add(characterUI2);

		JLabel tank2 = new JLabel(char2Im);
		tank2.setBounds(0, 0, 80, 60);
		characterUI2.add(tank2);

		JLabel name2 = new JLabel(char2N);
		name2.setBounds(0, 60, 80, 30);
		name2.setLayout(new BorderLayout());
		characterUI2.add(name2);

		JLabel text2 = new JLabel(UserList.elementAt(1), JLabel.CENTER);
		text2.setName(UserList.elementAt(1));
		text2.setForeground(Color.WHITE);
		name2.add(text2, BorderLayout.CENTER);
		name2.repaint();

		if (UserName.equals(text.getName())) {
			me = characterUI;
			team = "red";
			Enemy = characterUI2;
			mystartX = me.getX();
			mystartY = me.getY();
			EnemyX = Enemy.getX();
			EnemyY = Enemy.getY();
			missleStartX = mystartX + 40;
			missleStartY = mystartY + 20;
			EmissleStartX = EnemyX + 40;
			EmissleStartY = EnemyY + 20;
		}
		if (UserName.equals(text2.getName())) {
			me = characterUI2;
			team = "blue";
			Enemy = characterUI;
			mystartX = me.getX();
			mystartY = me.getY();
			EnemyX = Enemy.getX();
			EnemyY = Enemy.getY();
			missleStartX = mystartX + 40;
			missleStartY = mystartY + 20;
			EmissleStartX = EnemyX + 40;
			EmissleStartY = EnemyY + 20;
		}
	}

	public void gameOver() {
		ImageIcon gameover = new ImageIcon("../Fortress/src/img/gameover.png");
		Image over = gameover.getImage();

		JPanel losePanel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(over, 0, 0, 640, 519, this);
			}
		};
		losePanel.setBounds(320, 140, 640, 519);
		this.add(losePanel);
		this.setComponentZOrder(losePanel, 0);
		this.remove(characterUI);
		this.remove(characterUI2);
		this.repaint();

		ChatMsg cm = new ChatMsg(UserName, "452", "Game Over");
		gf.SendObject(cm);
	}

	public void win() {
		ImageIcon winIcon = new ImageIcon("../Fortress/src/img/win.png");
		Image win = winIcon.getImage();

		JPanel winPanel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				g.drawImage(win, 0, 0, 640, 519, this);
			}
		};
		winPanel.setBounds(320, 140, 640, 519);
		this.add(winPanel);
		this.setComponentZOrder(winPanel, 0);
		this.remove(characterUI);
		this.remove(characterUI2);
		this.repaint();
	}

	public void moveMyCharacter(int keycode) {
		switch (keycode) {
		case KeyEvent.VK_LEFT:
			me.setLocation(mystartX - 1, mystartY);
			mystartX -= 1;
			if (team.equals("red"))
				missleStartX -= 1;
			else if (team.equals("blue"))
				missleStartX -= 1;
			this.repaint();
			break;
		case KeyEvent.VK_RIGHT:
			me.setLocation(mystartX + 1, mystartY);
			mystartX += 1;
			if (team.equals("red"))
				missleStartX += 1;
			else if (team.equals("blue"))
				missleStartX += 1;
			this.repaint();
			break;
		}
	}

	public void moveEnemyCharacter(int keycode) {
		switch (keycode) {
		case KeyEvent.VK_LEFT:
			Enemy.setLocation(EnemyX - 1, EnemyY);
			EnemyX -= 1;
			if (team.equals("red"))
				EmissleStartX -= 1;
			else if (team.equals("blue"))
				EmissleStartX -= 1;
			this.repaint();
			break;
		case KeyEvent.VK_RIGHT:
			Enemy.setLocation(EnemyX + 1, EnemyY);
			EnemyX += 1;
			if (team.equals("red"))
				EmissleStartX += 1;
			else if (team.equals("blue"))
				EmissleStartX += 1;
			this.repaint();
			break;
		}
	}

	public Boolean isHitted() {
		Vector<Integer> vx = fire.getVectorX();
		Vector<Integer> vy = fire.getVectorY();
		for (int i = 0; i < vx.size(); i++) {
			if ((EnemyX + 80 > vx.elementAt(i) && EnemyX < vx.elementAt(i))
					&& (EnemyY < vy.elementAt(i) && EnemyY + 60 > vy.elementAt(i))) {
				// hit 된 인덱스
				isHitted = true;
				hitIndex = i;
				break;
			}
		} // 상대가 맞았나?

		if (isHitted == true)
			return true;
		else
			return false;
	}

	public int hitIndex() {
		return hitIndex;
	}

	public Boolean isTurn() {
		if (myTurn == true)
			return true;
		else
			return false;
	}

	public void setTurnFalse() {
		myTurn = false;
	}

	public void setTurnTrue() {
		myTurn = true;
	}

	public void setHittedFalse() {
		isHitted = false;
	}

	public void angleUp() {
		fire.angleUp();
	}

	public void missileInit() {
		fire.init();
	}

	public int getGauge() {
		return fire.getGauge();
	}

	public void fillGauge() {
		fire.fillGaugeCount();
	}

	public void fireEnemy(int gaugeCount, double missileAngleX, double missileAngleY, String missileSort) {
		fire.drawEnemyPower(gaugeCount, missileAngleX, missileAngleY, EmissleStartX, EmissleStartY, missileSort);
	}

	public void fire(String missileSort) {
		fire.drawPower(missleStartX, missleStartY, missileSort);
	}

	public void angleDown() {
		fire.angleDown();
	}

	public void missileInitWithoutAngle() {
		fire.initWithoutAngle();
	}

	public double getAngleX() {

		return fire.getAngleX();
	}

	public double getAngleY() {

		return fire.getAngleY();
	}

	public void drawMissle() {
		this.add(fire = new FirePanel(team, this));
		this.setComponentZOrder(fire, 0);
	}

	public int[] getEnemyPosition() {
		int xy[] = { mystartX, mystartY };
		return xy;
	}

	public void sendHitMessage() {
		// TODO Auto-generated method stub
		if (gf.isTurn() == true) {
			if (isHitted == true)
				gf.hit();
			else
				gf.miss();
		}
	}

}
