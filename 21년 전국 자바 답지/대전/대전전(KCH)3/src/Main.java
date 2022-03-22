import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class Main extends BaseFrame {

	String titles[] = "인기공연(뮤지컬),인기공연(오페라),인기공연(콘서트)".split(","),
			cap[] = "TICKETING,MONTH SCHEDULE,CHART,LOGIN,MYPAGE".split(","), type[] = "M,C,O".split(",");
	JLabel uImg, loginlbl;
	JPanel p;
	JPanel[] popular = new JPanel[3];
	CardLayout card;
	int cardIdx = 0;

	public Main() {
		super("메인", 620, 300);
		setLayout(new BorderLayout(0, 20));
		var n = new JPanel(new BorderLayout());
		var n_w = new JPanel(new FlowLayout(0, 10, 0));
		var n_e = new JPanel(new FlowLayout(2, 10, 0));

		p = new JPanel(card = new CardLayout());

		add(n, "North");
		n.add(n_w, "West");
		n.add(n_e, "East");
		add(p);

		for (int i = 0; i < cap.length; i++) {
			JLabel l = lbl(cap[i], 0);
			l.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					String name = ((JLabel) e.getSource()).getText();
					switch (name) {
					case "TICKETING":
						new Search().addWindowListener(new before(Main.this));
						break;
					case "MONTH SCHEDULE":
						new MonthSchedule().addWindowListener(new before(Main.this));
						break;
					case "CHART":
						new Chart().addWindowListener(new before(Main.this));
						break;
					case "LOGIN":
						new Login(Main.this).addWindowListener(new before(Main.this));
						break;
					case "LOGOUT":
						eMsg("로그아웃 되었습니다.");
						uImg.setIcon(null);
						uImg.setBorder(null);
						loginlbl.setText("LOGIN");
						break;
					case "MYPAGE":
						if (!isLogined) {
							eMsg("로그인을 하세요.");
							return;
						}

						new MyPage().addWindowListener(new before(Main.this));
						break;
					default:
						break;
					}
				}
			});
			if (i < 3) {
				n_w.add(l);
			} else {
				if (i == 3)
					n_e.add(uImg = new JLabel());
				if (i == 3)
					loginlbl = l;
				n_e.add(l);
			}
		}

		for (int i = 0; i < popular.length; i++) {
			p.add(popular[i] = new JPanel(new BorderLayout(0, 0)), i + "");
			popular[i].setBorder(new TitledBorder(new LineBorder(Color.BLACK), titles[i]));
			var c = new JPanel(new GridLayout(1, 0));
			var s = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			popular[i].add(c);
			popular[i].add(s, "South");

			try {
				var rs = stmt.executeQuery(
						"select  p_name, pf_no, count(*) from perform p, ticket t where p.p_no = t.p_no and left(p.pf_no,1) = '"
								+ type[i] + "' group by p.p_name order by count(*) desc limit 0, 5");
				int rank = 1;
				while (rs.next()) {
					var item = new JPanel(new BorderLayout());
					item.add(lbl(rank + "위", JLabel.LEFT, 15), "North");
					item.add(new JLabel(new ImageIcon(
							Toolkit.getDefaultToolkit().getImage("./Datafiles/공연사진/" + rs.getString(2) + ".jpg")
									.getScaledInstance(100, 100, Image.SCALE_SMOOTH))));
					item.add(lbl(rs.getString(1), 0), "South");
					c.add(item);
					rank++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String[] ott = "1,2,3".split(",");

			for (int j = 0; j < ott.length; j++) {
				JLabel l = lbl(ott[j], 10);
				if (i == j)
					l.setForeground(Color.RED);
				s.add(l);
			}
		}

		new Thread(() -> {
			while (true) {
				cardIdx = cardIdx == 3 ? 0 : cardIdx;
				card.show(p, cardIdx + "");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cardIdx++;
			}
		}).start();

		setVisible(true);
	}

	public static void main(String[] args) {
		new Main();
	}
}
