import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MonthSchedule extends BaseFrame {

	JLabel monthlbl, prev, next;
	LocalDate today = LocalDate.now();

	int month = 0, year = 0;

	Type types[] = { new Type("M", "뮤지컬"), new Type("O", "오페라"), new Type("C", "콘서트") };
	DayBox box[] = new DayBox[42];

	ArrayList<Type> music = new ArrayList<>();
	ArrayList<Type> opera = new ArrayList<>();
	ArrayList<Type> concert = new ArrayList<>();
	boolean all = true, musicFlag, operaFlag, concertFlag;

	// 야가다 밖에 없는 거;;

	public MonthSchedule() {
		super("월별 일정", 1000, 800);
		var n = new JPanel(new BorderLayout());
		var n_c = new JPanel(new FlowLayout(FlowLayout.LEFT));
		var n_s = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		var c = new JPanel(new GridLayout(0, 7));

		add(n, "North");
		add(c);

		n.add(n_c);
		n.add(n_s, "South");

		n_c.add(prev = lbl("◀", JLabel.LEFT, 20));
		n_c.add(monthlbl = lbl(today.getMonthValue() + "월", JLabel.LEFT, 20), "North");
		n_c.add(next = lbl("▶", JLabel.LEFT, 20));

		for (int i = 0; i < types.length; i++) {
			n_s.add(types[i]);
		}

		n_s.add(btn("전체", a -> {
			all = true;
			music.forEach(m -> m.setVisible(true));
			opera.forEach(o -> o.setVisible(true));
			concert.forEach(cc -> cc.setVisible(true));
		}));

		for (int i = 0; i < types.length; i++) {
			types[i].addMouseListener(new MouseAdapter() {

				int cnt = 0;

				@Override
				public void mouseClicked(MouseEvent e) {
					cnt++;
					if (cnt == 2) {
						if (((Type) e.getSource()).typeLabel.getText().equals("M")) {
							musicFlag = true;
							operaFlag = false;
							concertFlag = false;

							music.forEach(a -> a.setVisible(true));
							opera.forEach(a -> a.setVisible(false));
							concert.forEach(a -> a.setVisible(false));
						} else if (((Type) e.getSource()).typeLabel.getText().equals("O")) {
							musicFlag = false;
							operaFlag = true;
							concertFlag = false;

							opera.forEach(a -> a.setVisible(true));
							music.forEach(a -> a.setVisible(false));
							concert.forEach(a -> a.setVisible(false));
						} else {
							musicFlag = false;
							operaFlag = false;
							concertFlag = true;

							concert.forEach(a -> a.setVisible(true));
							music.forEach(a -> a.setVisible(false));
							opera.forEach(a -> a.setVisible(false));
						}
						all = false;
						cnt = 0;
					}
				}
			});
		}

		prev.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (today.plusMonths(-1).getYear() != today.getYear())
					return;
				today = today.plusMonths(-1);
				monthlbl.setText(today.getMonthValue() + "월");
				setDay();
			}
		});
		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (today.plusMonths(1).getYear() != today.getYear())
					return;

				today = today.plusMonths(1);
				monthlbl.setText(today.getMonthValue() + "월");
				setDay();
			}
		});

		for (int i = 0; i < box.length; i++) {
			c.add(box[i] = new DayBox(0));
			if (i % 7 == 0)
				box[i].setDaylblForegroundColor(Color.RED);

			if (i % 7 == 6)
				box[i].setDaylblForegroundColor(Color.BLUE);

		}
		setDay();
		setVisible(true);
	}

	void setDay() {
		music.clear();
		opera.clear();
		concert.clear();

		year = today.getYear();
		month = today.getMonthValue();
		LocalDate sdate = LocalDate.of(year, month, 1);
		int sday = sdate.getDayOfWeek().getValue() % 7;
		for (int i = 0; i < box.length; i++) {
			LocalDate tmp = sdate.plusDays(i - sday);

			box[i].c.removeAll();

			box[i].daylbl.setText(tmp.getDayOfMonth() + "");

			if (tmp.getMonthValue() != month) {
				box[i].c.setVisible(false);
				box[i].daylbl.setVisible(false);
			} else {
				try {
					var rs = stmt.executeQuery("select * from perform where p_date='" + tmp + "' limit 0, 3");
					while (rs.next()) {
						Type typePanel;
						String type = rs.getString(2).substring(0, 1);
						box[i].c.add(BaseFrame.size(typePanel = new Type(type, rs.getString(3)), 100, 20));
						if (type.equals("M")) {
							music.add(typePanel);
						} else if (type.equals("O")) {
							opera.add(typePanel);
						} else {
							concert.add(typePanel);
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				box[i].c.setVisible(true);
				box[i].daylbl.setVisible(true);
				repaint();
				revalidate();
			}
		}

		if (all) {
			music.forEach(m -> m.setVisible(true));
			opera.forEach(o -> o.setVisible(true));
			concert.forEach(cc -> cc.setVisible(true));
		} else if (musicFlag) {
			music.forEach(a -> a.setVisible(true));
			opera.forEach(a -> a.setVisible(false));
			concert.forEach(a -> a.setVisible(false));
		} else if (operaFlag) {
			music.forEach(a -> a.setVisible(false));
			opera.forEach(a -> a.setVisible(true));
			concert.forEach(a -> a.setVisible(false));
		} else {
			music.forEach(a -> a.setVisible(false));
			opera.forEach(a -> a.setVisible(false));
			concert.forEach(a -> a.setVisible(true));
		}

	}

	class Type extends JPanel {

		JLabel typeLabel;

		public Type(String type, String name) {
			super(new BorderLayout());

			add(typeLabel = lbl(type, 0), "West");
			add(lbl(name, JLabel.LEFT));

			typeLabel.setOpaque(true);

			if (type.equals("M")) {
				typeLabel.setBackground(Color.MAGENTA.darker());
			} else if (type.equals("O")) {
				typeLabel.setBackground(Color.BLUE);
			} else if (type.equals("C")) {
				typeLabel.setBackground(Color.orange);
			}
			typeLabel.setBorder(new LineBorder(Color.BLACK));
			typeLabel.setForeground(Color.WHITE);
		}
	}

	class DayBox extends JPanel {

		JLabel daylbl;
		JPanel c;

		public DayBox(int day) {
			super(new BorderLayout());
			add(daylbl = lbl(day + "", JLabel.RIGHT), "North");
			add(c = new JPanel(new FlowLayout(FlowLayout.LEFT)));
			c.setBorder(new EmptyBorder(5, 5, 5, 5));
			setBorder(new LineBorder(Color.BLACK));

		}

		void setDaylblForegroundColor(Color col) {
			daylbl.setForeground(col);
		}

	}

	public static void main(String[] args) {
		new MonthSchedule();
	}
}
