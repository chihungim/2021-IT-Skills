import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MonthSchedule extends BaseFrame {

	String filter = "";
	JLabel monthlbl, prev, next;
	int month = 0, year = 0;
	ArrayList<Type> all = new ArrayList<>();
	LocalDate today = LocalDate.now();
	Type types[] = { new Type("M", "뮤지컬"), new Type("O", "오페라"), new Type("C", "콘서트") };

	DayBox box[] = new DayBox[42];

	public MonthSchedule() {
		super("월별 일정", 1000, 800);
		ui();
		setDay();
		addEvents();
		setVisible(true);
	}

	void ui() {
		var n = new JPanel(new BorderLayout());
		var n_c = new JPanel(new FlowLayout(FlowLayout.LEFT));
		var n_s = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		var c = new JPanel(new GridLayout(0, 7));

		add(n, "North");
		add(c);

		n.add(n_c);
		n.add(n_s, "South");

		n_c.add(prev = lbl("◀", JLabel.LEFT, 20));
		n_c.add(monthlbl = lbl(today.getMonthValue() + "월", JLabel.LEFT, 20));
		n_c.add(next = lbl("▶", JLabel.LEFT, 20));

		Arrays.stream(types).forEach(n_s::add);

		n_s.add(btn("전체", a -> {
			all.stream().forEach(e -> e.setVisible(true));
			filter = "";
		}));

		for (var cap : "일,월,화,수,목,금,토".split(",")) {
			var lbl = lbl(cap, 0);
			c.add(lbl);
			if (cap.equals("일")) {
				lbl.setForeground(Color.red);
			} else if (cap.equals("토")) {
				lbl.setForeground(Color.BLUE);
			}
		}

		for (int i = 0; i < box.length; i++) {
			c.add(box[i] = new DayBox(0));
			if (i % 7 == 0)
				box[i].setDaylblForegroundColor(Color.RED);

			if (i % 7 == 6)
				box[i].setDaylblForegroundColor(Color.BLUE);
		}

	}

	void addEvents() {
		prev.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (today.plusMonths(-1).getYear() != today.getYear())
					return;
				today = today.plusMonths(-1);
				setDay();
			}
		});

		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (today.plusMonths(1).getYear() != today.getYear())
					return;
				today = today.plusMonths(1);
				setDay();
			}
		});

		for (int i = 0; i < types.length; i++) {
			types[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						filter = ((Type) e.getSource()).typeLabel.getText();
						all.forEach(a -> a.setVisible(true));
						all.stream().filter(a -> !a.typeLabel.getText().equals(filter))
								.forEach(x -> x.setVisible(false));
					}
				}
			});
		}
	}

	void setDay() {
		all.clear();
		year = today.getYear();
		month = today.getMonthValue();
		monthlbl.setText(today.getMonthValue() + "월");
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
					var rs = stmt.executeQuery("select * from perform where p_date ='" + tmp + "' limit 0 ,3");
					while (rs.next()) {
						Type typePanel;
						System.out.println("ㅇㅇ");
						String type = rs.getString(2).substring(0, 1);
						box[i].c.add(BaseFrame.size(typePanel = new Type(type, rs.getString(3)), 100, 20));
						all.add(typePanel);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				box[i].c.setVisible(true);
				box[i].daylbl.setVisible(true);
				repaint();
				revalidate();
			}
		}

		if (!filter.isEmpty())
			all.stream().filter(e -> !e.typeLabel.getText().equals(filter)).forEach(el -> el.setVisible(false));
	}

	class Type extends JPanel {

		JLabel typeLabel;

		public Type(String type, String name) {
			super(new BorderLayout());

			add(BaseFrame.size(typeLabel = lbl(type, 0), 20, 20), "West");
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
