package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class Main extends BaseFrame {

	JLabel back, time;
	static JPanel panels[] = new JPanel[3];
	static JButton btn1, btn2;

	public Main() {
		super("홈", 1000, 400);
		add(back = new JLabel(getIcon(IMAGE + "background.png")));
		back.setLayout(new BorderLayout());
		back.add(n = new JPanel(new BorderLayout()), "North");
		back.add(c = new JPanel(new GridLayout(1, 0, 5, 5)));
		n.add(new JLabel(getIcon(IMAGE + "logo.png")), "East");
		n.add(lbl(
				"<html><font size = '6', color = 'blue', face = '맑은 고딕'>안전한 도시철도, 편리한 교통서비스<br><font size = '4', color = 'white'>서울교통공사가 도시 교통의 미래를 만들어 갑니다.",
				JLabel.LEFT));

		for (int i = 0; i < panels.length; i++) {
			panels[i] = new JPanel(new GridLayout(0, 1, 5, 5));
			panels[i].setBackground(new Color(0, 0, 0, 123));
			c.add(panels[i]);
			panels[i].setBorder(new EmptyBorder(5, 5, 5, 5));
		}

		panels[0].add(btn("경로검색", a -> {
			new Map().addWindowListener(new before(this));
		}));
		var grid0 = new JPanel(new GridLayout(1, 0, 5, 5));
		panels[0].add(grid0);
		grid0.add(btn("노선도", a -> {
			
		}));
		grid0.add(btn("열차 시간표", a -> {
		
		}));

		panels[1].add(time = lbl(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), JLabel.CENTER, 30));
		time.setForeground(Color.WHITE);

		Timer t = new Timer(1, a -> {
			time.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
			repaint();
			revalidate();
		});
		t.start();

		panels[1].add(btn("마이페이지", a -> {
			new MyPage().addWindowListener(new before(this));
		}));

		panels[2].add(btn1 = btn("로그인", a -> {
			new Login().addWindowListener(new before(this));
		}));
		panels[2].add(btn2 = btn("회원가입", a -> {
			new Sign().addWindowListener(new before(this));
		}));

		back.setBorder(new EmptyBorder(50, 50, 50, 50));
		grid0.setOpaque(false);
		n.setOpaque(false);
		c.setOpaque(false);
		setVisible(true);
	}

	static void setLogin(boolean enb) {

		panels[2].removeAll();

		if (enb) {
			try {
				var rs = stmt.executeQuery("select sum(distance)*0.1 from purchase where user = " + no);
				if (rs.next()) {
					panels[2].add(lbl("<html><font color = 'white'><b>안녕하세요? " + name + "님</b><br>서울메트로와 총 "
							+ rs.getDouble(1) + "km를 함께했습니다.", JLabel.LEFT));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			panels[2].add(btn("로그아웃", a -> {
				setLogin(false);
			}));
		} else {
			panels[2].add(btn1);
			panels[2].add(btn2);
		}
	}

	public static void main(String[] args) {
		new Main();
	}
}
