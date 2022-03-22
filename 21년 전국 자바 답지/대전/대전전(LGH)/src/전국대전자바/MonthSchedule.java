package 전국대전자바;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class MonthSchedule extends BaseFrame {
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel n = new JPanel(new BorderLayout()), c = new JPanel(new GridLayout(0, 7, 30, 10)),
			s = new JPanel(new GridLayout(0, 7, 0, 0));
	JPanel n_n = new JPanel(new FlowLayout(0)), n_s = new JPanel(new FlowLayout(2));
	JPanel n_c;
	JLabel left = lbl("◀", 0, 30), right = lbl("▶", 0, 30), lbl = lbl("", 0, 30);
	JLabel idxlbl[] = new JLabel[7];
	{
		for (int i = 0; i < idxlbl.length; i++) {
			idxlbl[i] = lbl("일,월,화,수,목,금,토".split(",")[i], JLabel.CENTER, 13);
		}
	}
	DateIndexPanel days[] = new DateIndexPanel[42];
	String iconText[] = "M,O,C".split(",");
	Color col[] = { Color.PINK, Color.BLUE.darker(), Color.YELLOW };
	LocalDate today = LocalDate.now();
	int year, month, sday;
	IconPanel[] ipanels = new IconPanel[3];
	String titles[] = "뮤지컬,오페라,콘서트".split(",");
	ArrayList obj[] = new ArrayList[32];

	public MonthSchedule() {
		super("월별 일정", 800, 750);
		ui();
		data();
		event();
		setVisible(true);
	}

	private void ui() {
		add(mainp);
		mainp.add(n, "North");
		mainp.add(c, "Center");
		mainp.add(s, "South");
		n.add(n_n, "North");
		n.add(n_c = new JPanel(new FlowLayout(FlowLayout.RIGHT)));
		n.add(n_s, "South");

		n_n.add(left);
		n_n.add(lbl);
		n_n.add(right);

		n_c.add(ipanels[0] = new IconPanel(titles[0], "M"));
		n_c.add(ipanels[1] = new IconPanel(titles[1], "O"));
		n_c.add(ipanels[2] = new IconPanel(titles[2], "C"));
		n_c.add(btn("전체", a -> {

		}));

		for (int i = 0; i < idxlbl.length; i++) {
			if (i % 7 == 6)
				idxlbl[i].setForeground(Color.BLUE);
			if (i % 7 == 0)
				idxlbl[i].setForeground(Color.RED);
			c.add(idxlbl[i]);
		}

		for (int i = 0; i < days.length; i++) {
			s.add(days[i] = new DateIndexPanel(i));
			if (i % 7 == 6)
				days[i].setForeGroundColor(Color.BLUE);
			if (i % 7 == 0)
				days[i].setForeGroundColor(Color.RED);

		}

		setCal();

	}

	private void data() {

		try {
			ResultSet rs = stmt
					.executeQuery("SELECT *, day(p_date) d FROM 2021전국.perform where month(p_date) = '" + month + "'");
			while (rs.next()) {
				int idx = rs.getInt(8);
				if (obj[idx] == null)
					obj[idx] = new ArrayList<Object[]>();
				obj[idx].add(new Object[] { rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7) });
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < 42; i++) {
			days[i].data();
		}
	}

	void setCal() {
		year = today.getYear();
		month = today.getMonthValue();

		lbl.setText(month + "월");

		LocalDate sdate = LocalDate.of(year, month, 1);
		sday = sdate.getDayOfWeek().getValue() % 7;

		for (int i = 0; i < days.length; i++) {
			LocalDate tmp = sdate.plusDays(i - sday);
			if (tmp.getMonth() != today.getMonth())
				continue;
			days[i].setText(tmp.getDayOfMonth() + "");
		}
	}

	private void event() {

	}

	class DateIndexPanel extends JPanel {
		int idx;
		JLabel lbl;
		JLabel lbl2[][] = new JLabel[3][2];
		int w1 = 20, w2 = 75, h1 = w1, h2 = w1;
		int loc[][] = { { 0, 0, 90, 20 }, { 0, w1, w1, h1 }, { w1, w1, w2, h2 }, { 0, w1 * 2 + 2, w1, h1 },
				{ w1, w1 * 2 + 2, w2, h2 }, { 0, w1 * 3 + 4, w1, h1 }, { w1, w1 * 3 + 4, w2, h2 } };

		public DateIndexPanel(int idx) {
			this.idx = idx;
			this.setLayout(null);
			this.setBorder(new LineBorder(Color.black));
			this.setPreferredSize(new Dimension(90, 90));
			lbl = BaseFrame.lbl("", JLabel.RIGHT, 15);
			lbl.setBounds(loc[0][0], loc[0][1], loc[0][2], loc[0][3]);
			this.add(lbl);

			for (int i = 0; i < 3; i++) {
				lbl2[i][0] = new IconLabel();
				lbl2[i][0].setBounds(loc[i * 2 + 1][0], loc[i * 2 + 1][1], loc[i * 2 + 1][2], loc[i * 2 + 1][3]);
				this.add(lbl2[i][0]);

				lbl2[i][1] = new JLabel();
				lbl2[i][1].setBounds(loc[i * 2 + 2][0], loc[i * 2 + 2][1], loc[i * 2 + 2][2], loc[i * 2 + 2][3]);
				this.add(lbl2[i][1]);

				// 이벤트
				lbl2[i][0].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2) {

						}
					}
				});
			}

		}

		void data() {
			if (lbl.getText() != "") {
				if (obj[idx - sday + 1] != null) {
					ArrayList<Object> o = obj[idx - sday + 1];
					// 출연자와 장르 아이콘
					for (int i = 0; i < o.size(); i++) {
						try {
							Object b[] = ((Object[]) o.get(i));
							((IconLabel) lbl2[i][0]).data(b[0].toString().substring(0, 1));
							lbl2[i][1].setText(b[1] + "");
						} catch (Exception e) {
						}
					}
				}
			}
			this.repaint();
			this.revalidate();
		}

		public void setText(String text) {
			lbl.setText(text);
			revalidate();
			repaint();
		}

		void setForeGroundColor(Color col) {
			lbl.setForeground(col);
			revalidate();
			repaint();
		}
	}

	class IconPanel extends JPanel {
		IconLabel il;

		public IconPanel(String title, String mode) {
			super(new BorderLayout());
			il = new IconLabel();
			il.data(mode);

			add(BaseFrame.size(il, 20, 20), "West");
			add(lbl(title, JLabel.LEFT));
		}

	}

	class IconLabel extends JLabel {
		String iconText = "MOC";
		Color[] color = { Color.MAGENTA.brighter(), Color.BLUE.darker(), Color.orange };
		int mode = 0;

		public void data(String mode) {
			this.mode = iconText.indexOf(mode);

			this.setText(mode);
			this.setLayout(new FlowLayout(0));
			this.setOpaque(true);
			this.setBackground(color[this.mode]);
			this.setForeground(Color.WHITE);
			this.setFont(new Font("맑은 고딕", Font.BOLD, 10));
			this.setBorder(new LineBorder(Color.BLACK));
		}
	}

	public static void main(String[] args) {
		new MonthSchedule().setVisible(true);
	}
}
