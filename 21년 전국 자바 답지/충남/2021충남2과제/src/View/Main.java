package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Main extends BaseFrame {

	JPanel n, c;
	JLabel backGround;
	HalfTransParentPanel HTPP[] = new HalfTransParentPanel[3];

	String bcap[] = "로그인,회원가입".split(",");

	public Main() {
		super("홈", 1000, 430);
		backGround = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급자료/images/background.png")
				.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH)));
		add(backGround);
		backGround.setLayout(new BorderLayout());
		backGround.add(n = new JPanel(new BorderLayout()), "North");
		n.setOpaque(false);
		n.add(lbl(
				"<html><font size = \"5\"; color=\"blue\"; face=\"맑은 고딕\" >안전한 도시철도, 편리한 교통 서비스<br><font size = \"4\"; color=\"white\" ;face=\"맑은 고딕\">서울교통공사가 도시교통의 미래를 만들어 갑니다.",
				JLabel.LEFT), "West");
		n.add(new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급자료/images/logo.png"))), "East");
		backGround.add(c = new JPanel(new GridLayout(1, 0, 10, 10)));
		c.setOpaque(false);
		for (int i = 0; i < HTPP.length; i++) {
			c.add(HTPP[i] = new HalfTransParentPanel());
			if (i == 0) {
				HTPP[i].InsertGridBag(size(btn("경로 검색", a -> {

				}), 220, 100), 0, 0, 2, 1);
				HTPP[i].InsertGridBag(size(btn("노선도", a -> {

				}), 100, 100), 0, 1, 1, 1);
				HTPP[i].InsertGridBag(btn("열차 시간표", a -> {

				}), 1, 1, 1, 1);
			} else if (i == 1) {
				HTPP[i].InsertGridBag(size(new Timer(), 200, 100), 0, 0, 2, 1);
				HTPP[i].InsertGridBag(size(btn("마이페이지", a -> {

				}), 200, 100), 0, 1, 2, 1);
			} else if (i == 2) {
				for (int j = 0; j < bcap.length; j++) {
					HTPP[i].InsertGridBag(size(btn(bcap[j], a -> {

					}), 200, 100), 0, j, 2, 1);
				}
			}
		}

		backGround.setBorder(new EmptyBorder(50, 50, 50, 50));
		setVisible(true);
	}

	class HalfTransParentPanel extends JPanel {
		GridBagLayout gBag;

		public HalfTransParentPanel() {
			gBag = new GridBagLayout();
			setLayout(gBag);
			setBackground(new Color(0, 0, 0, 123));
		}

		public void InsertGridBag(Component c, int x, int y, int w, int h) {
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			gbc.insets = new Insets(2, 2, 3, 3);
			gbc.gridx = x;
			gbc.gridy = y;
			gbc.gridwidth = w;
			gbc.gridheight = h;
			gBag.setConstraints(c, gbc);
			add(c);
		}
	}

	class Timer extends JPanel {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
		JLabel timer;
		//timer 왜 background 변하지
		public Timer() {
			add(timer = new JLabel(LocalTime.now().format(formatter)));
			javax.swing.Timer t = new javax.swing.Timer(1000, a -> {
				timer.setText(LocalTime.now().format(formatter));
				timer.setFont(new Font("", Font.PLAIN, 25));
				timer.setOpaque(false);
				timer.repaint();
				timer.revalidate();
			});

			setOpaque(false);
			t.start();
		}

	}

	public static void main(String[] args) {
		new Main();
	}
}
