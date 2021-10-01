package BottomUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Frame.GameFrame;

public class GaugePanel extends JPanel {
	int misStartX = 60;
	int misStartY = 410;
	double gauge = Math.PI;
	final double gaugeDIF = Math.PI / 60;
	final double gaugeBarRadius = 30;
	double missilePower = gauge * 1.6;
	double cannonAngleDIF = Math.PI / 60;
	double cannonAngle = Math.PI / 4;
	double missileDx = missilePower * Math.cos(cannonAngle);
	double missileDy = missilePower * Math.sin(cannonAngle);
	final double GRAVITY = 0.098;
	private Vector<Integer> missileX = new Vector<Integer>();
	private Vector<Integer> missileY = new Vector<Integer>();
	int gaugePower = 0;

	private ImageIcon i1 = new ImageIcon("../Fortress/src/img/missileRed.png");
	private ImageIcon i2 = new ImageIcon("../Fortress/src/img/portion.png");

	private JButton item1; // 더블 미사일
	private JButton item2; // HP 회복 아이템
	private String missilesort = "Normal";
	int missileCount = 1;

	GameFrame gf;
	GaugeBar gb;
	HpBar hb;
	MoveBar mb;
	int cc = 0;
	double rad = 0;
	ImageIcon mis = new ImageIcon("../Fortress/src/img/miss.png");
	Image ms = mis.getImage();

	GaugePanel(GameFrame gf) {
		this.gf = gf;
		Font font = new Font("Aharoni", Font.BOLD, 17);
		this.setLayout(null);

		JPanel hp = new JPanel();
		hp.setBackground(Color.BLACK);
		hp.setBounds(140, 110, 80, 30);
		JLabel energy = new JLabel("H       P");
		energy.setFont(font);
		energy.setForeground(Color.GREEN);
		hp.add(energy, BorderLayout.CENTER);
		this.add(hp);

		JPanel power = new JPanel();
		power.setBackground(Color.BLACK);
		power.setBounds(140, 150, 80, 30);
		JLabel p = new JLabel("POWER");
		p.setFont(font);
		p.setForeground(Color.RED);
		power.add(p);
		this.add(power);

		JPanel move = new JPanel();
		move.setBackground(Color.BLACK);
		move.setBounds(140, 190, 80, 30);
		JLabel m = new JLabel("M O V E");
		m.setFont(font);
		m.setForeground(Color.YELLOW);
		move.add(m);
		this.add(move);

		gb = new GaugeBar();
		gb.setBounds(225, 150, 510, 30);
		this.add(gb);

		hb = new HpBar();
		hb.setBounds(225, 110, 510, 30);
		this.add(hb);

		mb = new MoveBar();
		mb.setBounds(225, 190, 510, 30);
		this.add(mb);

		item1 = new JButton(i1); // 큰 미사일
		item1.setBounds(180, 20, 60, 60);
		item1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				missilesort = "Big";
				if (missileCount != 0)
					missileCount--;
				else return;
				gf.SetMissileSize(missilesort);
			}
		});
		this.add(item1);

		item2 = new JButton(i2); // 이게 체력회복
		item2.setBounds(245, 20, 60, 60);
		item2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				hb.recovery();
			}
		});
		this.add(item2);
		this.repaint();
	}

	public void fillGauge() {
		gb.fillGaugeBar();
	}

	public void resetFire() {
		gb.reset();
	}

	public void moveGaugeUp() {
		mb.moveOnce();
	}

	public void doHitted(String missileSort) {
		hb.doHitted(missileSort);
	}

	public void moveInit() {
		mb.moveInit();
	}

	class GaugeBar extends JPanel {
		private static final long serialVersionUID = 1L;

		GaugeBar() {
		}

		@Override
		public void paint(Graphics g) {
			g.setColor(Color.BLACK);
			g.fillRect(gaugePower, 0, 510 - gaugePower, 30);
			g.setColor(Color.RED);
			g.fillRect(0, 0, gaugePower, 30);
		}

		public void reset() {
			gaugePower = 0;
			repaint();
		}

		public void fillGaugeBar() {
			if (gaugePower == 510)
				gaugePower = 510;
			else if (gaugePower < 500)
				gaugePower += 6;
			repaint();
		}
	}

	class HpBar extends JPanel {
		private static final long serialVersionUID = 1L;
		int hp = 510;
		int recoveryCount = 1;

		HpBar() {
		}

		public void recovery() {
			if (recoveryCount == 1) {
				if (hp == 510) {
					return;
				} else {
					hp += 255;
					repaint();
					recoveryCount--;
				}
			}
		}

		@Override
		public void paint(Graphics g) {
			g.setColor(Color.BLACK);
			g.fillRect(hp, 0, 510 - hp, 30);
			g.setColor(Color.GREEN);
			g.fillRect(0, 0, hp, 30);
		}

		// 맞았을 때 코드 작성
		public void doHitted(String missileSort) {
			if (missileSort.equals("Normal")) {
				hp -= 255;
			} else if (missileSort.equals("Big")) {
				hp -= 510;
			}
			if (hp <= 0) {
				gf.gameOver();
			}
			repaint();
		}

	}

	class MoveBar extends JPanel {
		private static final long serialVersionUID = 1L;
		int move = 0;
		int moveCount = 0;

		MoveBar() {
		}

		public void moveInit() {
			move = 0;
			moveCount = 0;
			repaint();
		}

		public void moveOnce() {
			if (move < 510) {
				if (moveCount % 2 == 1)
					move += 26;
				else
					move += 25;
				moveCount++;
			}
		}

		@Override
		public void paint(Graphics g) {
			g.setColor(Color.BLACK);
			g.fillRect(move, 0, 510 - move, 30);
			g.setColor(Color.YELLOW);
			g.fillRect(0, 0, move, 30);
		}

		// 움직일때
		public void moveGaugeUp() {
			if (move > 510)
				move = 510;
			else
				move += 26;
		}
	}

}
