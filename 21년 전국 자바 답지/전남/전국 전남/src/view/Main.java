package view;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class Main extends BaseFrame {

	JPanel c;

	ArrayList<JLabel> imageList = new ArrayList<>();
	ArrayList<JLabel> titleList = new ArrayList<>();
	String cap[] = "검색,게시판,지역별 지점 찾기,지역별 예약현황 차트,퀴즈".split(",");

	public Main() {
		super("메인", 600, 400);
		
		setUI();
		playAnimation();
		setVisible(true);
	}

	void setUI() {
		setLayout(new BorderLayout(5, 5));
		var n = new JPanel(new BorderLayout());
		var n_c = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(n, "North");
		n.add(n_c);

		for (var bcap : "로그아웃,마이페이지".split(",")) {
			n_c.add(btn(bcap, a -> {
				if (a.getActionCommand().equals("로그아웃")) {
					dispose();
				} else {
					new MyPage().addWindowListener(new Before(Main.this));
				}
			}));
		}

		add(c = new JPanel(null));

		for (int i = 1; i <= 5; i++) {
			var imglbl = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					Graphics2D g2 = (Graphics2D) g;
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					super.paintComponent(g);
				}

				@Override
				protected void paintChildren(Graphics g) {
					Graphics2D g2 = (Graphics2D) g;
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
					super.paintChildren(g);
				}
			};
			var textlbl = lbl(cap[i - 1], 0, 40);

			imglbl.setIcon(img("./Datafiles/표지/" + i + ".jpg", 580, 320));
			imglbl.setLayout(new BorderLayout());
			imglbl.add(textlbl);

			imglbl.setBorder(new LineBorder(Color.BLACK));

			textlbl.setForeground(Color.BLACK);
			textlbl.setVerticalTextPosition(SwingConstants.CENTER);
			textlbl.setHorizontalTextPosition(SwingConstants.CENTER);
			imageList.add(imglbl);
			titleList.add(textlbl);

			textlbl.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					titleList.forEach(a -> a.setForeground(Color.BLACK));
					var lbl = (JLabel) e.getSource();
					lbl.setForeground(Color.red);

					switch (lbl.getText()) {
					case "검색":
						new Search().addWindowListener(new Before(Main.this));
						break;
					case "게시판":
						new Notice().addWindowListener(new Before(Main.this));
						break;
					case "지역별 지점 찾기":
						new Find().addWindowListener(new Before(Main.this));
						break;
					case "지역별 예약현황 차트":
						new Chart().addWindowListener(new Before(Main.this));
						break;
					default:
						break;
					}

					super.mousePressed(e);
				}
			});

			c.add(imglbl);
			imglbl.setBounds((i - 1) * 580, 0, 580, 320);
		}

		n.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
		n_c.setBorder(new EmptyBorder(5, 5, 5, 5));
		((JPanel) (getContentPane())).setBorder(new EmptyBorder(5, 5, 5, 5));
	}

	void playAnimation() {
		new Thread(() -> {
			int x = 3;
			while (true) {
				imageList.forEach(img -> {
					if (img.getX() - x < -580) {
						img.setLocation(580 * 4, 0);
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						img.setLocation(img.getX() - x, 0);
					}
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				revalidate();
				repaint();
			}
		}).start();
	}

	@Override
	public JLabel lbl(String cap, int alig, int size) {
		var a = super.lbl(cap, alig, size);
		a.setFont(new Font("HY헤드라인M", Font.BOLD, size));
		return a;
	}

	public static void main(String[] args) {
		new Main();
	}
}
