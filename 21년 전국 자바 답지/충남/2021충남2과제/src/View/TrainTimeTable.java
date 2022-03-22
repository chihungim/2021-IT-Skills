package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class TrainTimeTable extends BaseFrame {

	JComboBox box[] = new JComboBox[2];
	int interval, realIndex = 1;
	JLabel location, totalTime;
	LocalTime s_time, e_time;

	JScrollPane pane;
	JPanel c, w, e;

	public TrainTimeTable() {
		super("열차 시간표", 1100, 800);
		w = new JPanel(new BorderLayout());
		var w_n = new JPanel(new GridLayout(0, 1));
		e = new JPanel(new BorderLayout());

		add(w, "West");
		add(e, "East");
		add(pane = new JScrollPane(c = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0))));
		w.add(w_n, "North");

		for (int i = 0; i < box.length; i++) {
			var tmp = new JPanel(new FlowLayout(FlowLayout.LEFT));
			box[i] = size(new JComboBox<>(), 300, 30);
			if (i == 0) {
				try {
					var rs = stmt.executeQuery("select * from metro");
					while (rs.next()) {
						box[0].addItem(rs.getString(2));
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				box[0].addItemListener(a -> setTime());
			}

			tmp.add(box[i]);
			w_n.add(tmp);
		}

		w_n.add(location = lbl(box[0].getSelectedItem().toString(), JLabel.LEFT, 15));
		w_n.add(totalTime = lbl("", JLabel.LEFT, 10));
		pane.setOpaque(false);
		pane.setBorder(BorderFactory.createEmptyBorder());
		box[1].addItemListener(i -> setTimeLine());
		setTime();
		init();
		setVisible(true);
	}

	void setTimeLine() {
		if (box[1].getSelectedItem() == null)
			return;
		try {
			var rs = stmt.executeQuery(
					"SELECT r.metro, r.station, s.name, m.name from route r, station s, metro m where s.serial = r.station and m.serial = r.metro and r.metro="
							+ (box[0].getSelectedIndex() + 1));
			c.removeAll();
			c.setLayout(new GridLayout(0, 1));
			int index = 1, metro = 0;
			s_time = LocalTime.parse(box[1].getSelectedItem().toString());
			LocalTime t = s_time;
			final ArrayList<ItemIndex> times = new ArrayList<>();

			if (rs.next()) {
				var tmp = new JPanel(new FlowLayout(FlowLayout.LEFT));
				metro = rs.getInt(2);
				times.add(new ItemIndex(index, rs.getString(3), t, metro));
				tmp.add(times.get(index - 1));
				c.add(tmp);
				index++;
			}

			while (rs.next()) {
				var tmp = new JPanel(new FlowLayout(FlowLayout.LEFT));
				t = t.plusSeconds(cost[metro][rs.getInt(2)] * 5);
				times.add(new ItemIndex(index, rs.getString(3), t, rs.getInt(2)));
				tmp.add(times.get(index - 1));
				times.get(index - 1).addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						times.forEach(a -> a.onClicked(false));
						((ItemIndex) e.getSource()).onClicked(true);
						revalidate();
						repaint();

					}
				});
				c.add(tmp);
				c.setBorder(new EmptyBorder(0, 0, 1000 % index, 0));

				System.out.println(metro + "," + rs.getInt(2));
				metro = rs.getInt(2);
				index++;
			}

			revalidate();
			repaint();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	void setTime() {
		box[1].removeAllItems();
		try {
			var rs = stmt.executeQuery("select * from metro where serial=" + (box[0].getSelectedIndex() + 1));
			if (rs.next()) {
				s_time = LocalTime.parse(rs.getString(3));
				e_time = LocalTime.parse(rs.getString(4));

				interval = rs.getTime(5).toLocalTime().getMinute();
				while (true) {
					box[1].addItem(s_time.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
					s_time = s_time.plusMinutes(interval);
					if (s_time.toSecondOfDay() >= e_time.toSecondOfDay())
						break;
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	class ItemIndex extends JPanel {

		Color color = Color.WHITE;
		int station;

		public ItemIndex(int idx, String name, LocalTime time, int station) {
			super(new BorderLayout());
			this.station = station;
			this.setPreferredSize(new Dimension(350, 40));
			add(lbl(" " + idx + "." + name, JLabel.RIGHT), "West");
			add(lbl(time.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "도착", JLabel.LEFT), "East");

		}

		public ItemIndex(String name) {
			super(new BorderLayout());
			this.station = station;
			this.setPreferredSize(new Dimension(400, 40));
			add(lbl(name, JLabel.RIGHT), "West");

		}

		void onClicked(boolean clicked) {
			color = clicked ? color.ORANGE : color.WHITE;
			e.removeAll();
			e.setPreferredSize(new Dimension(400, 20));
			e.setLayout(new BorderLayout());
			e.add(lbl("<html><font face=\"맑은 고딕\">교대에서<br>환승 가능한 노선", JLabel.LEFT, 20), "North");

			var e_c = new JPanel(new FlowLayout());
			e.add(e_c);
			try {
				var rs = stmt.executeQuery(
						"SELECT r.metro, r.station, s.name, m.name from route r, station s, metro m where s.serial = r.station and m.serial = r.metro and s.name like '%"
								+ map[box[0].getSelectedIndex() + 1].get(station) + "%' and r.metro <> "
								+ (box[0].getSelectedIndex() + 1));

				while (rs.next()) {
					var tmp = new JPanel(new FlowLayout());
					tmp.add(new ItemIndex(rs.getString(4)));
					e_c.add(tmp);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(color);
			RoundRectangle2D rec = new RoundRectangle2D.Float(1.5f, 1.5f, getWidth() - 3, getHeight() - 3, 15, 15);
			g2.fill(rec);
		}

	}

	public static void main(String[] args) {
		new TrainTimeTable();
	}
}
