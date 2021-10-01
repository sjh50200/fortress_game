package BottomUI;

import java.awt.Color;
import java.awt.Font;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

import Frame.GameFrame;

public class ChatPanel extends JPanel {
	public JButton btnSend;
	public JTextPane textArea;
	public JTextField txtInput;
	private Vector<String> UserList = new Vector<String>();
	private String UserName;
	private String team;
	private AnglePanel ap;
	private GaugePanel gp;
	GameFrame gf;

	public ChatPanel(GameFrame gf) {
		this.gf = gf;
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(950, 0, 330, 210);
		scrollPane.getViewport().setOpaque(false);
		this.add(scrollPane);

		textArea = new JTextPane();
		textArea.setEditable(false);
		textArea.setFont(new Font("±¼¸²Ã¼", Font.PLAIN, 14));
		textArea.setOpaque(false);
		scrollPane.setViewportView(textArea);

		txtInput = new JTextField();
		txtInput.setBounds(950, 220, 260, 30);// 561
		txtInput.setBorder(new RoundBorder(20));
		this.add(txtInput);
		txtInput.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.setFont(new Font("±¼¸²", Font.PLAIN, 14));
		btnSend.setBounds(1211, 220, 60, 30);
		btnSend.setBorder(new RoundBorder(20));
		this.add(btnSend);

		
	}

	public void setTeam(String team) {
		if (team.equals("red")) {
			ap = new DrawAnglePanel(team);
			ap.setBounds(0, 0, 250, 250);
			this.add(ap);
		}
		else {
			ap = new DrawAnglePanel(team);
			ap.setBounds(0, 0, 250, 250);
			this.add(ap);
		}
		makeGaugePanel();
	}

	private void makeGaugePanel() {
		gp = new GaugePanel(gf);
		gp.setBounds(125, 0, 824, 250);
		gp.setBackground(new Color(0x9966CC));
		gp.setBorder(new LineBorder(new Color(0xCC99FF), 10));
		this.add(gp);
	}

	public void fillGauge() {
		gp.fillGauge();
	}

	public void resetGaugeAndFire() {
		gp.resetFire();
	}

	public void makeUI(Vector<String> userList, String userName) {
		this.UserName = userName;
		this.UserList = userList;

		String team = null;
		for (int i = 0; i < 2; i++) {
			if (UserList.elementAt(i).equals(UserName)) {
				if (i == 0)
					team = "red";
				else
					team = "blue";
			}
		}
		JPanel UIPanel = null;
		switch (team) {
		case "red":
			UIPanel = new UIRedTeam();
			break;
		case "blue":
			UIPanel = new UIBlueTeam();
			break;
		default:
			System.out.println(team + UIPanel.toString());
			break;
		}
		this.add(UIPanel);
	}

	public void lineUp() {
		ap.lineUp();
	}

	public void lineDown() {
		ap.lineDown();
	}

	public void angleInit() {
		ap.angleInit();

	}

	public void doHitted(String missileSort) {
		gp.doHitted(missileSort);		
	}

	public void moveGaugeUp() {
		gp.moveGaugeUp();		
	}

	public void moveInit() {
		gp.moveInit();	
	}
}
