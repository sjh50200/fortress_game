package Frame;


import java.awt.Canvas;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Loading extends JFrame implements Runnable {
	long startTime = System.currentTimeMillis();
	long endTime = startTime;
	//long endTime = startTime + 5000;  //이거 로딩 살려야됨
	JPanel bt;
	JPanel can;
	Image buffer;
	Image[] duke;
	Toolkit tk;
	boolean repeat = true;
	
	public Loading(String name) {
		tk = Toolkit.getDefaultToolkit();
		initDuke();
		setTitle(name);
		this.add(can = new JPanel() {

			@Override
			public void paint(Graphics g) {
				g.clearRect(0, 0, 800, 500);
				g.drawImage(buffer, 0, 0, 800, 500, this);
			}

			@Override
			public void update(Graphics g) {
				paint(g);
			}

		});

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		setBounds(0, 0, 800, 500);
		setResizable(false);
		// setVisible(true);

		makeThread();
	}

	public void makeThread() {
		new Thread(this).start();
	}
	
	// 듀크 초기화
	public void initDuke() {
		duke = new Image[5];

		for (int i = 0; i < duke.length; i++) {
			duke[i] = tk.getImage(String.format("src/img/main%d.jpg", i + 1));
		}
	}

	public void bufferPaint(int n) { //로딩화면 더블 버퍼링
		buffer = createImage(800, 500);
		Graphics buffer_g = buffer.getGraphics();

		buffer_g.drawImage(duke[n], 0, 0, 800, 500, this);

		can.repaint();
	}
	
	public void bufferPaint2() { //로그인 화면
		remove(can);
		LoginBoard loginBoard = new LoginBoard(this);
		this.add(loginBoard);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Loading first = new Loading("Fortress");
					first.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void run() {
		long time;
		while (true) {
			time = System.currentTimeMillis();
			if (time < endTime) {
				for (int i = 0; i < 4; i++) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					bufferPaint(i);
				}
			} else {
				bufferPaint2();
				break;
			}
		}
	}
}
