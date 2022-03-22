package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TimeTable extends BaseFrame {

	JScrollPane scr, scr2;
	JComboBox<String> metroCombo = new JComboBox<String>(), timeCombo = new JComboBox<String>();
	ArrayList<String> metroList = new ArrayList<String>(), timeList = new ArrayList<String>();
	DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
	JLabel jl;
	JPanel e_scr_p;

	public TimeTable() {
		super("열차 시간표", 1000, 700);

		this.add(w = new JPanel(new FlowLayout(0)), "West");
		this.add(scr = new JScrollPane(c = new JPanel(new GridLayout(0, 1, 5, 5))));
		this.add(e = new JPanel(new BorderLayout()), "East");

		scr.setBorder(BorderFactory.createEmptyBorder());
		dataInit();
		westUI();

		centerUI();

		eastUI();

		sz(w, 250, 150);
		sz(e, 350, 1);
		setEmpty(c, 10, 10, 10, 10);

		this.setVisible(true);
	}

	void westUI() {

		w.add(sz(metroCombo, 230, 25));
		w.add(sz(timeCombo, 230, 25));
		w.add(jl = sz(lbl("<html><b>1호선 - 경원선</b><br>소요 시간 : 00:35:45", 2, 12, Color.black, Font.PLAIN), 230, 50));

		try {
			var rs = stmt.executeQuery("select name from metro");
			while (rs.next()) {
				metroCombo.addItem(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		metroCombo.addItemListener(a -> {
			jl.setText("<html><b>" + metroCombo.getSelectedItem() + "</b><br>소요 시간 : 00:35:45");
			centerUI();
			e.removeAll();
		});
		metroCombo.setSelectedIndex(0);
		jl.setText("<html><b>" + metroCombo.getSelectedItem() + "</b><br>소요 시간 : 00:35:45");

		LocalTime start, end, interval;
		start = LocalTime
				.parse(getOne("select start from metro where serial =" + (metroCombo.getSelectedIndex() + 1) + ""));
		end = LocalTime
				.parse(getOne("select end from metro where serial =" + (metroCombo.getSelectedIndex() + 1) + ""));
		interval = LocalTime.parse(
				getOne("select `interval` from metro where serial =" + (metroCombo.getSelectedIndex() + 1) + ""));

		LocalTime time = start;
		while (end.isAfter(time)) {
			timeCombo.addItem(timeFormat.format(time));
			time = time.plusMinutes(interval.getMinute());
		}
	}

	void centerUI() {
		ArrayList<ItemPanel> panels = new ArrayList<ItemPanel>();
		c.removeAll();
		try {
			var rs = stmt.executeQuery(
					"SELECT r.serial, r.station, s.name, r.metro  FROM route r, station s where r.station=s.serial and metro = "
							+ (metroCombo.getSelectedIndex() + 1));
			int i = 1;
			LocalTime time = LocalTime.parse(timeCombo.getSelectedItem() + "");
			LocalTime arriveTime = LocalTime.of(0, 0, 0);
			while (rs.next()) {
				ItemPanel p = new ItemPanel(rs.getInt(2), rs.getString(3));
				c.add(sz(p, 1, 50));
				p.add(lbl(i + ". " + rs.getString(3), 0, 13), "West");
				if (i == 1) {
					p.add(lbl(timeCombo.getSelectedItem() + "도착", 0, 13), "East");
				} else {
					time = time.plusSeconds(cost[rs.getInt(1) - 2][2] * 5);
					arriveTime = arriveTime.plusSeconds(cost[rs.getInt(1) - 2][2] * 5);
					p.add(lbl(timeFormat.format(time) + "도착", 0, 13), "East");
				}
				i++;
				panels.add(p);

			}
			jl.setText("<html><b>" + metroCombo.getSelectedItem() + "</b><br>소요 시간 : " + timeFormat.format(arriveTime));
			for (int j = 0; j < panels.size(); j++) {
				panels.get(j).addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent a) {
						for (int j2 = 0; j2 < panels.size(); j2++) {
							panels.get(j2).onClicked(false);
						}
						ItemPanel p = ((ItemPanel) a.getSource());
						p.onClicked(true);
						try {
							e.removeAll();
							e.add(scr2 = new JScrollPane(e_scr_p = new JPanel(new GridLayout(0, 1, 5, 5))), "Center");
							scr2.setBorder(BorderFactory.createEmptyBorder());
							setEmpty(e, 10, 10, 10, 10);
							var rs = stmt.executeQuery(
									"SELECT r.metro, r.station, s.name, m.name from route r, station s, metro m where s.serial = r.station and m.serial = r.metro and s.name like '%"
											+ map[metroCombo.getSelectedIndex() + 1].get(p.serial)
											+ "%' and r.metro <> " + (metroCombo.getSelectedIndex() + 1));
							while (rs.next()) {
								System.out.println(rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + ","
										+ rs.getString(4) + ",");
								e.add(lbl("<html>" + p.name + "에서<br>환승 가능한 노선", 2, 15), "North");
								ItemPanel pp = new ItemPanel(rs.getInt(1), rs.getString(4));
								pp.add(lbl(rs.getString(4), 2, 13));
								e_scr_p.add(sz(pp, 1, 50));

							}
							if (e_scr_p.getComponentCount() < 10) {
								for (int k = e_scr_p.getComponentCount(); k < 10; k++) {
									e_scr_p.add(sz(new JPanel(), 1, 50));
								}
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						repaint();
						revalidate();
					}
				});
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repaint();
		revalidate();

	}

	void eastUI() {

	}

	class ItemPanel extends JPanel {

		boolean chk;
		int serial;
		String name;

		public ItemPanel(int serial, String name) {
			this.serial = serial;
			this.name = name;
			this.setLayout(new BorderLayout());
		}

		void onClicked(boolean chk) {
			this.chk = chk;

			repaint();
			revalidate();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(chk ? Color.orange : Color.white);
			RoundRectangle2D rec = new RoundRectangle2D.Float(1.5f, 1.5f, getWidth() - 3, getHeight() - 3, 15, 15);
			g2.fill(rec);
		}
	}

	public static void main(String[] args) {
		new TimeTable();
	}

}
