package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;

public class Main extends BaseFrame {

	Image img = new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급자료/images/background.png")).getImage();
	JLabel bg;
	JLabel logo;
	JPanel panels[] = new JPanel[3];
	int marz = 40;
	double distance = 0;
	JLabel time;

	public Main() {
		super("홈", 1000, 430);
		bg = new JLabel(img("./지급자료/images/background.png", getWidth(), getHeight()));
		add(bg);
		bg.setLayout(new BorderLayout());
		dataInit();

		bg.add(n = new JPanel(new BorderLayout(10, 10)), "North");
		bg.add(c = new JPanel(new GridLayout(1, 0, 10, 10)));
		n.setOpaque(false);
		c.setOpaque(false);

		n.add(new JLabel(
				"<html><font size = \"5\"; color=\"blue\"; face=\"맑은 고딕\" >안전한 도시철도, 편리한 교통 서비스<br><font size = \"4\"; color=\"white\" ;face=\"맑은 고딕\">서울교통공사가 도시교통의 미래를 만들어 갑니다."),
				"West");

		n.add(logo = new JLabel(img("./지급자료/images/logo.png", 180, 30)), "East");
		logo.setOpaque(false);

		for (int i = 0; i < panels.length; i++) {
			c.add(panels[i] = new JPanel(new GridLayout(0, 1, 10, 10)));
			panels[i].setBackground(new Color(0, 0, 0, 128));
			setEmpty(panels[i], 10, 10, 10, 10);
		}

		makeBtnUI();

		Timer repaint = new Timer(1, a -> {
			Main.this.startTime = LocalTime.now();
			Main.this.time.setText(startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
			repaint();
			revalidate();
		});

		repaint.start();

		setEmpty(bg, marz, marz, marz, marz);
		setEmpty(n, 0, 0, 10, 0);
		this.setVisible(true);
	}

	private void makeBtnUI() {
		panels[0].add(btn("경로 검색", a -> {
			new Map().addWindowListener(new Before(this));
		}));

		var grid_f = new JPanel(new GridLayout(1, 0, 10, 10));
		grid_f.add(btn("노선도", a -> {
			new Route().addWindowListener(new Before(this));
		}));
		grid_f.add(btn("열차 시간표", a -> {
			new TimeTable().addWindowListener(new Before(this));
		}));
		grid_f.setOpaque(false);
		panels[0].add(grid_f);

		panels[1].add(time = new JLabel(DateTimeFormatter.ofPattern("hh:mm:ss").format(startTime), 0));
		time.setFont(new Font("", Font.PLAIN, 50));
		time.setForeground(Color.white);
		time.setOpaque(false);

		panels[1].add(btn("마이페이지", a -> {
			if (uno == 0) {
				eMsg("로그인 후 이용 가능합니다.");
				return;
			}
			new MyPage().addWindowListener(new Before(this));
		}));

		panels[2].add(btn("로그인", a -> {
			new Login();
			dispose();
		}));
		panels[2].add(btn("회원가입", a -> {
			new SignUp().addWindowListener(new Before(this));
		}));
	}

	void setLogin(boolean chk) {
		panels[2].removeAll();
		if (chk) {
			distance = 0;
			try {
				var rs = stmt.executeQuery("select * from purchase where user=" + uno);
				while (rs.next()) {
					distance += rs.getInt(8);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			distance = distance / 10.0;

			panels[2].add(lbl("<html><font face = \"맑은 고딕\"; color =\"WHITE\"; size = \"6\"><b>안녕하세요? " + uname
					+ "</b><br><font face = \"함초롬돋움\"; color =\"WHITE\"; size = \"5\">서울메트로와 총 " + distance
					+ "km를 함께했습니다.", JLabel.CENTER));

			panels[2].add(btn("로그아웃", a -> {
				uno = 0;
				uname = null;
				this.setLogin(false);
			}));
		} else {
			panels[2].add(btn("로그인", a -> {
				new Login().addWindowListener(new Before(this));
			}));
			panels[2].add(btn("회원가입", a -> {
				new SignUp().addWindowListener(new Before(this));
			}));
		}
		repaint();
		revalidate();
		c.revalidate();
		c.repaint();
		panels[2].repaint();
		panels[2].revalidate();
	}

	public static void main(String[] args) {
		new Main();
	}

}
