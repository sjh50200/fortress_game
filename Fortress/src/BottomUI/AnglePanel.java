package BottomUI;

import javax.swing.JPanel;

public class AnglePanel extends JPanel {
	protected double lineAngle;
	protected double lineAngleDIF = Math.PI / 60;
	String team;

	AnglePanel(String team) {
		this.team = team;
		this.setOpaque(false);

		if (team.equals("red"))
			lineAngle = Math.PI * 1.75;
		else if (team.equals("blue"))
			lineAngle = Math.PI * 1.25;
	}

	public void lineUp() {
		switch (team) {
		case "red":
			if (lineAngle >= Math.PI * 1.5)
				lineAngle -= lineAngleDIF;
			repaint();
			break;
		case "blue":
			if (lineAngle <= Math.PI * 1.5)
				lineAngle += lineAngleDIF;
			repaint();
			break;
		}
	}

	public void lineDown() {
		switch (team) {
		case "red":
			if (lineAngle <= Math.PI * 2)
				lineAngle += lineAngleDIF;
			repaint();
			break;
		case "blue":
			if (lineAngle >= Math.PI)
				lineAngle -= lineAngleDIF;
			repaint();
			break;
		}
	}

	public void angleInit() {
		switch (team) {
		case "red":
			lineAngle = Math.PI * 1.75;
			break;
		case "blue":
			lineAngle = Math.PI * 1.25;
			break;
		}
	}
}
