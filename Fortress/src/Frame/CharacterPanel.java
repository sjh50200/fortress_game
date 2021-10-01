package Frame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class CharacterPanel extends JPanel{
	Toolkit tk = Toolkit.getDefaultToolkit();
	int n;
	ImageIcon imi = new ImageIcon("../Fortress/src/Character/char1.png");
	Image im = imi.getImage();
	
	public CharacterPanel(int n) {
		this.n = n;
		this.setVisible(true);
	}
	
}
