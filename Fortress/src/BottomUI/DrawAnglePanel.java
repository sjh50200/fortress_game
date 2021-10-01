package BottomUI;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class DrawAnglePanel extends AnglePanel{

	ImageIcon i = new ImageIcon("../Fortress/src/img/Angle.png");
	Image im = i.getImage();

	DrawAnglePanel(String team) {
		super(team);
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(im, 0, 0, 250, 250, this);
		Graphics2D tg = (Graphics2D) g;
		tg.setColor(Color.white);
		tg.rotate(super.lineAngle, 125,125);
		tg.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,0));
		tg.draw(new Line2D.Double(125, 125, 242, 125)); //¹ÝÁö¸§ 121*/
	}
	

}
