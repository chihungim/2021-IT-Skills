import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MonthSchedule extends BaseFrame {
	String filter = "";
	JLabel plbl, mlbl, nlbl;
	Day d[] = new Day[42];
	Type t[] = { new Type("M", "뮤지컬"), new Type("O", "오페라"), new Type("C", "콘서트") };
	int month, year;
	LocalDate today = LocalDate.now();

	ArrayList<Type> all = new ArrayList<>();

	public MonthSchedule() {
		super("월별 일정", 1000, 800);
		ui();
		events();
		setDay();
		setVisible(true);
	}

	public static void main(String[] args) {
		new MonthSchedule();
	}

	void ui() {
		var n = new JPanel(new BorderLayout());
		var n_c = new JPanel(new FlowLayout(FlowLayout.LEFT));
		var n_s = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		var c = new JPanel(new GridLayout(0, 7, 0, 0));

		add(n, "North");
		add(c);

		n.add(n_c);
		n.add(n_s, "South");

		n_c.add(plbl = lbl("◀", 0, 20));
		n_c.add(mlbl = lbl(today.getMonthValue() + "월", 0, 20));
		n_c.add(nlbl = lbl("▶", 0, 20));

		for (int i = 0; i < t.length; i++) {
			n_s.add(t[i]);
		}

		n_s.add(btn("전체", a -> {
			filter = "";
			all.forEach(e -> e.setVisible(true));
		}));

		for (var cap : "일,월,화,수,목,금,토".split(",")) {
			var lbl = lbl(cap, 0);
			c.add(lbl);

			if (cap.equals("일"))
				lbl.setForeground(Color.RED);
			else if (cap.equals("토"))
				lbl.setForeground(Color.BLUE);
		}

		for (int i = 0; i < 42; i++) {
			c.add(d[i] = new Day(i + ""));
			if (i % 7 == 0)
				d[i].daylbl.setForeground(Color.RED);
			else if (i % 7 == 6)
				d[i].daylbl.setForeground(Color.BLUE);
		}

	}

	void setDay() {
		month = today.getMonthValue();
		year = today.getYear();
		mlbl.setText(month + "월");

		LocalDate sdate = LocalDate.of(year, month, 1);
		int sday = sdate.getDayOfWeek().getValue() % 7;
		all.clear();
		for (int i = 0; i < 42; i++) {
			LocalDate tmp = sdate.plusDays(i - sday);
			d[i].daylbl.setText(tmp.getDayOfMonth() + "");
			if (tmp.getMonthValue() != month) {
				d[i].daylbl.setVisible(false);
				d[i].center.setVisible(false);
			} else {
				d[i].daylbl.setVisible(true);
				d[i].center.setVisible(true);
			}
			d[i].center.removeAll();

			try {
				var rs = stmt.executeQuery(
						"SELECT left(pf_no,1), p_name FROM 2021전국.perform where p_date = '" + tmp + "' limit 0, 3");
				while (rs.next()) {
					Type t = new Type(rs.getString(1), rs.getString(2));
					d[i].center.add(t);
					d[i].center.add(Box.createVerticalStrut(5));
					all.add(t);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			d[i].center.revalidate();
			d[i].center.repaint();
		}

		if (!filter.isEmpty())
			all.stream().filter(a -> !a.typelbl.getText().equals(filter)).forEach(a -> a.setVisible(false));
	}

	void events() {
		plbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (today.plusMonths(-1).getYear() != year)
					return;
				today = today.plusMonths(-1);
				setDay();
				super.mousePressed(e);
			}
		});

		nlbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (today.plusMonths(1).getYear() != year)
					return;
				today = today.plusMonths(1);
				setDay();
				super.mousePressed(e);
			}
		});

		for (int i = 0; i < t.length; i++) {
			t[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {

						var me = (Type) e.getSource();

						all.forEach(a -> a.setVisible(true));

						if (e.getClickCount() == 2) {
							filter = me.typelbl.getText();
							all.stream().filter(a -> !a.typelbl.getText().equals(filter))
									.forEach(el -> el.setVisible(false));
						}
					}
					super.mouseClicked(e);
				}
			});
		}
	}

	class Type extends JPanel {

		JLabel typelbl;

		public Type(String type, String name) {
			setLayout(new BorderLayout());
			add(BaseFrame.size(typelbl = lbl(type, JLabel.CENTER), 20, 20), "West");
			typelbl.setOpaque(true);
			switch (type) {
			case "M":
				typelbl.setBackground(Color.MAGENTA);
				break;
			case "O":
				typelbl.setBackground(Color.BLUE);
				break;
			default:
				typelbl.setBackground(Color.YELLOW);
				break;
			}
			typelbl.setForeground(Color.WHITE);
			typelbl.setBorder(new LineBorder(Color.BLACK));
			add(lbl(name, JLabel.CENTER));
			setMaximumSize(new Dimension(100, 20));
		}
	}

	class Day extends JPanel {

		JPanel center;
		JLabel daylbl;

		public Day(String day) {
			setLayout(new BorderLayout());
			add(daylbl = lbl(day, JLabel.LEFT), "North");
			add(center = new JPanel());
			center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
			setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(5, 5, 5, 5)));
		}
	}

//	String filter = "";
//	JLabel monthlbl, prev, next;
//	int month = 0, year = 0;
//	ArrayList<Type> all = new ArrayList<>();
//	LocalDate today = LocalDate.now();
//	Type types[] = { new Type("M", "뮤지컬"), new Type("O", "오페라"), new Type("C", "콘서트") };
//	DayBox box[] = new DayBox[42];
//
//	public MonthSchedule() {
//		super("월별 일정", 1000, 800);
//
//		ui();
//		events();
//		setDay();
//
//		setVisible(true);
//	}
//
//	void ui() {
//		var n = new JPanel(new BorderLayout());
//		var n_c = new JPanel(new FlowLayout(FlowLayout.LEFT));
//		var n_s = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
//		var c = new JPanel(new GridLayout(0, 7));
//
//		add(n, "North");
//		add(c);
//
//		n.add(n_c);
//		n.add(n_s, "South");
//
//		n_c.add(prev = lbl("◀", JLabel.LEFT, 20));
//		n_c.add(monthlbl = lbl(today.getMonthValue() + "월", JLabel.LEFT, 20));
//		n_c.add(next = lbl("▶", JLabel.LEFT, 20));
//
//		Arrays.stream(types).forEach(n_s::add);
//
//		n_s.add(btn("전체", a -> {
//			all.stream().forEach(e -> e.setVisible(true));
//			filter = "";
//		}));
//
//		for (var cap : "일,월,화,수,목,금,토".split(",")) {
//			var lbl = lbl(cap, 0);
//			c.add(lbl);
//			if (cap.equals("일"))
//				lbl.setForeground(Color.RED);
//			else if (cap.equals("토"))
//				lbl.setForeground(Color.BLUE);
//		}
//
//		for (int i = 0; i < box.length; i++) {
//			c.add(box[i] = new DayBox(i));
//		}
//
//	}
//
//	void events() {
//		prev.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent e) {
//				if (today.plusMonths(-1).getYear() != today.getYear())
//					return;
//				today = today.plusMonths(-1);
//				setDay();
//				super.mousePressed(e);
//			}
//		});
//
//		next.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent e) {
//				if (today.plusMonths(1).getYear() != today.getYear())
//					return;
//				today = today.plusMonths(1);
//				setDay();
//				super.mousePressed(e);
//			}
//		});
//
//		for (int i = 0; i < types.length; i++) {
//			types[i].addMouseListener(new MouseAdapter() {
//				@Override
//				public void mousePressed(MouseEvent e) {
//					if (e.getClickCount() == 2) {
//						filter = ((Type) e.getSource()).typeLabel.getText();
//						all.forEach(a -> a.setVisible(true));
//						all.stream().filter(a -> !a.typeLabel.getText().equals(filter))
//								.forEach(x -> x.setVisible(false));
//					}
//					super.mousePressed(e);
//				}
//			});
//		}
//	}
//
//	public static void main(String[] args) {
//		new MonthSchedule();
//	}
//
//	void setDay() {
//		all.clear();
//		year = today.getYear();
//		month = today.getMonthValue();
//		monthlbl.setText(today.getMonthValue() + "월");
//		LocalDate sdate = LocalDate.of(year, month, 1);
//		int sday = sdate.getDayOfWeek().getValue() % 7;
//
//		for (int i = 0; i < box.length; i++) {
//
//			LocalDate tmp = sdate.plusDays(i - sday);
//
//			box[i].c.removeAll();
//			box[i].daylbl.setText(tmp.getDayOfMonth() + "");
//
//			if (tmp.getMonthValue() != month) {
//				box[i].c.setVisible(false);
//				box[i].daylbl.setVisible(false);
//			} else {
//				try {
//					var rs = stmt.executeQuery("select * from perform where p_date='" + tmp + "' limit 0,3");
//					while (rs.next()) {
//						String type = rs.getString(2).substring(0, 1);
//						var typePanel = new Type(type, rs.getString(3));
//						box[i].c.add(size(typePanel, 100, 20));
//						all.add(typePanel);
//					}
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				box[i].c.setVisible(true);
//				box[i].daylbl.setVisible(true);
//				repaint();
//				revalidate();
//			}
//		}
//
//		if (!filter.isEmpty())
//			all.stream().filter(e -> !e.typeLabel.getText().equals(filter)).forEach(e1 -> e1.setVisible(false));
//	}
//
//	class Type extends JPanel {
//		JLabel typeLabel;
//
//		public Type(String type, String name) {
//			setLayout(new BorderLayout());
//			add(BaseFrame.size(typeLabel = lbl(type, 0), 20, 20), "West");
//			add(lbl(name, JLabel.LEFT));
//			typeLabel.setOpaque(true);
//
//			switch (type) {
//			case "M":
//				typeLabel.setBackground(Color.MAGENTA.darker());
//				break;
//			case "O":
//				typeLabel.setBackground(Color.BLUE);
//				break;
//			default:
//				typeLabel.setBackground(Color.ORANGE);
//				break;
//			}
//
//			typeLabel.setBorder(new LineBorder(Color.BLACK));
//			typeLabel.setForeground(Color.WHITE);
//		}
//	}
//
//	class DayBox extends JPanel {
//		JLabel daylbl;
//		JPanel c;
//
//		public DayBox(int day) {
//			super(new BorderLayout());
//			add(daylbl = lbl(day + "", JLabel.RIGHT), "North");
//
//			add(c = new JPanel(new FlowLayout(FlowLayout.LEFT)));
//			c.setBorder(new EmptyBorder(5, 5, 5, 5));
//			setBorder(new LineBorder(Color.BLACK));
//
//			if (day % 7 == 0)
//				daylbl.setForeground(Color.RED);
//
//			if (day % 7 == 6)
//				daylbl.setForeground(Color.BLUE);
//		}
//	}
}
