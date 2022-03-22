package view;

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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class Main extends BaseFrame {

	String titles[] = "인기공연(뮤지컬),인기공연(오페라),인기공연(콘서트)".split(","), type[] = "M,C,O".split(",");
	JLabel lbls[] = lbls("TICKETING,MONTH SCHEDULE,CHART,LOGIN,MYPAGE".split(","), JLabel.LEFT, 15);
	JPanel[] pop = new JPanel[3];
	static JLabel uimg, loginlbl;
	CardLayout page;
	JPanel viewP;
	int pageIdx;

	public Main() {
		super("메인", 620, 300);
		ui();
		events();
		setVisible(true);
	}

	void ui() {
		setLayout(new BorderLayout(0, 20));
		var n = new JPanel(new BorderLayout());
		var n_w = new JPanel(new FlowLayout(0, 10, 0));
		var n_e = new JPanel(new FlowLayout(2, 10, 0));
		viewP = new JPanel(page = new CardLayout());

		add(n, "North");
		n.add(n_w, "West");
		n.add(n_e, "East");
		add(viewP);

		for (int i = 0; i < lbls.length; i++) {
			if (i < 3)
				n_w.add(lbls[i]);
			else {
				if (i == 3) {
					n_e.add(uimg = new JLabel());
					loginlbl = lbls[i];
				}
				n_e.add(lbls[i]);
			}
		}

		for (int i = 0; i < pop.length; i++) {
			viewP.add(pop[i] = new JPanel(new BorderLayout(0, 0)), i + "");
			pop[i].setBorder(new TitledBorder(new LineBorder(Color.BLACK), titles[i]));
			var c = new JPanel(new GridLayout(1, 0));
			var s = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			pop[i].add(c);
			pop[i].add(s, "South");
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

			for (int j = 0; j < "1,2,3".split(",").length; j++) {
				JLabel l = lbl("1,2,3".split(",")[j], 10);
				if (i == j)
					l.setForeground(Color.RED);
				s.add(l);
			}
		}

		setBorder((JComponent) getContentPane(), new EmptyBorder(5, 5, 5, 5));
	}

	void events() {
		for (int i = 0; i < lbls.length; i++) {
			lbls[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					String name = ((JLabel) e.getSource()).getText();

					switch (name) {
					case "TICKETING":
						new Search().addWindowListener(new Before(Main.this));
						break;
					case "MONTH SCHEDULE":
						new MonthSchedule().addWindowListener(new Before(Main.this));
						break;
					case "CHART":
						new Chart().addWindowListener(new Before(Main.this));
						break;
					case "LOGIN":
						new Login().addWindowListener(new Before(Main.this));
						break;
					case "LOGOUT":
						eMsg("로그아웃 되었습니다.");
						uimg.setIcon(null);
						uimg.setBorder(null);
						loginlbl.setText("LOGIN");
						break;
					case "MYPAGE":
						if (loginlbl.getText().equals("LOGIN")) {
							eMsg("로그인을 하세요.");
							return;
						}

						new MyPage().addWindowListener(new Before(Main.this));
						break;
					default:
						break;
					}
					super.mousePressed(e);
				}
			});
		}

		Timer animeT = new Timer(2000, a -> {
			pageIdx = pageIdx == 3 ? 0 : pageIdx;
			page.show(viewP, pageIdx + "");
			pageIdx++;
		});
		animeT.start();
	}

	public static void main(String[] args) {
		new Main().setVisible(true);
	}

}
