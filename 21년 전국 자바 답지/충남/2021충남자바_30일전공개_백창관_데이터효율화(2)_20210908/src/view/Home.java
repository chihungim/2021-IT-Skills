package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.time.LocalTime;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import db.stmt;

public class Home extends BaseFrame {
	
	JLabel back;
	JLabel logo;
	JPanel jp[] = new JPanel[3];
	int marg = 40;
	double dis = 0;
	JLabel time;
	
	public Home() {
		super("홈", 1000, 430);
		
		back = new JLabel(img("./background.png", getWidth(), getHeight()));
		this.add(back);
		back.setLayout(new BorderLayout());
		
		back.add(n = new JPanel(new BorderLayout()), "North");
		back.add(c = new JPanel(new GridLayout(1, 0, 10, 10)));
		n.setOpaque(false);
		c.setOpaque(false);
		
		n.add(new JLabel("<html><font size = \"6\"; color = \"blue\"; face = \"맑은 고딕\">안전한 도시철도, 편리한 교통 서비스<br><font size = \"4\"; color = \"white\"; face = \"맑은 고딕\">서울교통공사가 도시교통의 미래를 만들어 갑니다.", 0), "West");
		n.add(logo = new JLabel(img("./logo.png", 180, 30)), "East");
		logo.setOpaque(false);
		
		for (int i = 0; i < jp.length; i++) {
			c.add(jp[i] = new JPanel(new GridLayout(0, 1, 10, 10)));
			jp[i].setBackground(new Color(0, 0, 0, 128));
			setEmpty(jp[i], 10, 10, 10, 10);
		}
		
		this.setBtn();
		
		Timer re = new Timer(1, a->{
			tnow = LocalTime.now();
			time.setText(tformat(tnow, "HH:mm:ss"));
			repaint();
			revalidate();
		});
		re.start();
		
		setEmpty(back, marg, marg, marg, marg);
		setEmpty(n, 0, 0, 10, 0);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				re.stop();
			}
		});
		
		
	}
	
	void setBtn() {
		jp[0].add(btn("경로 검색", a->{
			new Map().addWindowListener(new Before(h));
		}));
		
		JPanel grid = new JPanel(new GridLayout(1, 0, 10, 10));
		grid.add(btn("노선도", a->{
			new Route().addWindowListener(new Before(h));
		}));
		grid.add(btn("열차 시간표", a->{
			new TimeTable().addWindowListener(new Before(h));
		}));
		grid.setOpaque(false);
		jp[0].add(grid);
		jp[1].add(time = new JLabel(tformat(tnow, "HH:mm:ss"), 0));
		time.setFont(new Font("", Font.PLAIN, 50));
		time.setForeground(Color.white);
		time.setOpaque(false);
		
		jp[1].add(btn("마이페이지", a->{
			if (uno.isEmpty()) {
				eMsg("로그인 후 이용 가능합니다.");
				return;
			}
			new MyPage().addWindowListener(new Before(h));
		}));
		
		jp[2].add(btn("로그인", a->{
			h.setVisible(false);
			new Login();
			
		}));
		jp[2].add(btn("회원가입", a->{
			new SignUp().addWindowListener(new Before(h));
		}));
	}
	
	void setLog(boolean chk) {
		jp[2].removeAll();
		if (chk) {
			dis = 0;
			try {
				var rs= stmt.rs("select * from purchase where user ="+uno);
				while (rs.next()) {
					dis += rs.getInt(8);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dis /= 10.0;
			jp[2].add(lbl("<html><font face =\"맑은 고딕\"; color = \"white\"; size=\"5\"><b>안녕하세요? " + uname+"님</b><br><font face = \"함초롬 돋움\"; color = \"white\"; size = \"4\">서울메트로와 총 "+dis+"km를 함께했습니다.</html>", 0));
			jp[2].add(btn("로그아웃", a->{
				uno = "";
				uname = "";
				setLog(false);
			}));
		} else {
			jp[2].add(btn("로그인", a->{
				h.setVisible(false);
				new Login();
				
			}));
			jp[2].add(btn("회원가입", a->{
				new SignUp().addWindowListener(new Before(h));
			}));
		}
		
		repaint();
		revalidate();
	}
	
	public static void main(String[] args) {
		h.setVisible(true);
	}
	
}
