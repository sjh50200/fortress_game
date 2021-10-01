package BottomUI;


import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class UIRedTeam extends JPanel {
	ImageIcon i = new ImageIcon("../Fortress/src/Character/char1.png");
	Image im = i.getImage();

	public UIRedTeam() {
		this.setOpaque(false);
		this.setBounds(0, 0, 310, 250);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(im, 10, 50, 180, 150, this);
	}
}
