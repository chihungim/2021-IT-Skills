package view;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class Main extends BaseFrame {
	static String cap[] = "로그인,회원가입,위치정보,예매,매직패스(0),MyPage,놀이기구 인기순위 Top5,놀이기구 등록/수정,월별 분석,종료".split(",");
	static JLabel lbl[] = new JLabel[cap.length];
	static final int LOGOUT = 0, USER = 1, ADMIN = 2;
	static String cnt = "";
	JLabel img1, img2;

	int lorry = 1, lotty = 1;

	public Main() {
		super("메인", 700, 300);
		ui();
		events();
		enbLabel(LOGOUT);
		setVisible(true);
	}

	private void ui() {
		var w = new JPanel();
		var c = new JPanel(new GridLayout());

		add(w, "West");
		add(c);

		for (int i = 0; i < cap.length; i++) {
			w.add(Box.createVerticalStrut(5));
			w.add(lbl[i] = new JLabel(cap[i], 0));
			lbl[i].setAlignmentX(CENTER_ALIGNMENT);
		}

		c.add(img1 = imglbl("./datafile/캐릭터/로티" + lotty + ".jpg", 200, 250));
		c.add(img2 = imglbl("./datafile/캐릭터/로리" + lorry + ".jpg", 200, 250));
		w.setLayout(new BoxLayout(w, BoxLayout.Y_AXIS));
		w.setOpaque(false);
		c.setOpaque(false);

		((JPanel) (getContentPane())).setBorder(new EmptyBorder(5, 5, 5, 5));
	}

	private void events() {

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
					super.mousePressed(e);

					var l = (JLabel) e.getSource();
					int no = Arrays.asList(lbl).indexOf(l);
					if (!l.isEnabled())
						return;
					switch (no) {
					case 0:
						if (l.getText().equals("로그인")) {
							new Login().addWindowListener(new Before(Main.this));
						} else {
							uno = "";
							uname = "";
							uheight = 0;
							uold = 0;
							udisable = 0;
							lbl[0].setText("로그인");
							iMsg("로그아웃이 완료되었습니다.");
							enbLabel(LOGOUT);
						}
						break;
					case 1:
						new Register().addWindowListener(new Before(Main.this));
						break;
					case 2:
						new LocationInfo().addWindowListener(new Before(Main.this));
						break;
					case 3:
						new Reserve().addWindowListener(new Before(Main.this));
						break;
					case 4:
						new MagicPass().addWindowListener(new Before(Main.this));
						break;
					case 5:
						new MyPage().addWindowListener(new Before(Main.this));
						break;
					case 6:
						new Chart().addWindowListener(new Before(Main.this));
						break;
					case 7:
						new Ride().addWindowListener(new Before(Main.this));
						break;
					case 8:
						new Cal().addWindowListener(new Before(Main.this));
						break;
					default:
						System.exit(0);
					}
				}
			});
		}

		new Timer(500, a -> {
			if (lorry == 3) {
				lotty = 1;
				lorry = 1;
			} else {
				lotty++;
				lorry++;
			}

			img1.setIcon(img("./datafiles/캐릭터/로티" + lotty + ".jpg", 200, 250));
			img2.setIcon(img("./datafiles/캐릭터/로리" + lorry + ".jpg", 200, 250));
			revalidate();
			repaint();
		}).start();
	}

	static void enbLabel(int mode) {
		for (int i = 0; i < lbl.length; i++) {
			lbl[i].setEnabled(false);
		}

		int nums[];

		if (mode == LOGOUT) {
			nums = new int[] { 0, 1, 6 };
		} else if (mode == USER) {
			lbl[0].setText("로그아웃");
			if (val(lbl[4].getText()) != 0)
				nums = new int[] { 0, 2, 3, 4, 5, 6 };
			else
				nums = new int[] { 0, 2, 3, 5, 6 };
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
