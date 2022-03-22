package view;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class Main extends BaseFrame {
	static String cap[] = "로그인,회원가입,위치정보,예매,매직패스(0),Mypage,놀이기구 인기순위 Top5,놀이기구 등록/수정,월별 분석,종료".split(",");
	static JLabel lbl[] = new JLabel[cap.length];
	static final int LOGOUT = 0, USER = 1, ADMIN = 2;
	static String cnt = "";
	JLabel img1, img2;

	int lorry = 1, lotty = 1;

	public Main() {
		super("메인", 700, 300);
		ui();
		events();
		enableLabel(LOGOUT);
		setVisible(true);
	}

	void ui() {
		var w = new JPanel();
		var c = new JPanel(new GridLayout());

		add(w, "West");
		add(c);

		for (int i = 0; i < cap.length; i++) {
			w.add(Box.createVerticalStrut(5));
			w.add(lbl[i] = new JLabel(cap[i], 0));
			lbl[i].setAlignmentX(CENTER_ALIGNMENT);
		}

		c.add(img1 = imglbl("./datafiles/캐릭터/로티" + lotty + ".jpg", 200, 250));
		c.add(img2 = imglbl("./datafiles/캐릭터/로리" + lorry + ".jpg", 200, 250));

		w.setLayout(new BoxLayout(w, BoxLayout.Y_AXIS));
		w.setOpaque(false);
		c.setOpaque(false);
		((JPanel) (getContentPane())).setBorder(new EmptyBorder(5, 5, 5, 5));
	}

	void events() {

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
				super.windowClosing(e);
			}
		});

		for (int i = 0; i < lbl.length; i++) {
			lbl[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					var l = (JLabel) e.getSource();
					if (!l.isEnabled()) {
						return;
					}

					if (e.getSource().equals(lbl[0])) {
						if (lbl[0].getText().contentEquals("로그인")) {
							new Login().addWindowListener(new Before(Main.this));
						} else {
							uno = "";
							uname = "";
							uheight = 0;
							uold = 0;
							udisable = 0;
							lbl[0].setText("로그인");
							enableLabel(LOGOUT);
						}
					} else if (e.getSource().equals(lbl[1])) {
						new Register().addWindowListener(new Before(Main.this));
					} else if (e.getSource().equals(lbl[2])) {
						new Location_Info().addWindowListener(new Before(Main.this));
					} else if (e.getSource().equals(lbl[3])) {
						new Reserve().addWindowListener(new Before(Main.this));
					} else if (e.getSource().equals(lbl[4])) {
						new MagicPass().addWindowListener(new Before(Main.this));
					} else if (e.getSource().equals(lbl[5])) {
						new MyPage().addWindowListener(new Before(Main.this));
					} else if (e.getSource().equals(lbl[6])) {
						new Chart().addWindowListener(new Before(Main.this));
					} else if (e.getSource().equals(lbl[7])) {
						new Ride().addWindowListener(new Before(Main.this));
					} else if (e.getSource().equals(lbl[8])) {
						new Cal().addWindowListener(new Before(Main.this));
					} else if (e.getSource().equals(lbl[9])) {
						Main.this.dispose();
						System.exit(0);
					}
					super.mousePressed(e);
				}
			});
		}

		new Timer(500, a -> {
			if (lorry == 3) {
				lorry = 1;
				lotty = 1;
			} else {
				lorry++;
				lotty++;
			}
			img1.setIcon(img("./datafiles/캐릭터/로티" + lotty + ".jpg", 200, 250));
			img2.setIcon(img("./datafiles/캐릭터/로리" + lorry + ".jpg", 200, 250));
			repaint();
			revalidate();
		}).start();

	}

	static void enableLabel(int type) {
		for (int i = 0; i < lbl.length; i++) {
			lbl[i].setEnabled(false);
		}

		int nums[];

		if (type == LOGOUT) {
			nums = new int[] { 0, 1, 6 };
		} else if (type == USER) {
			if (val(lbl[4].getText()) != 0)
				nums = new int[] { 0, 2, 3, 4, 5, 6 };
			else
				nums = new int[] { 0, 2, 3, 5, 6 };
//			lbl[4].setText("매직패스(1)");

		} else {
			nums = new int[] { 0, 6, 7, 8 };
		}

		for (int i = 0; i < nums.length; i++) {
			lbl[nums[i]].setEnabled(true);
		}
		lbl[9].setEnabled(true);
	}

	public static void main(String[] args) {
		new Main();
	}
}
