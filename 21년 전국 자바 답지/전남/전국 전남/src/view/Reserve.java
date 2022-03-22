package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.border.LineBorder;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Reserve extends BaseFrame {

	JTable loc, cafe, theme, time;
	DefaultTableModel mloc, mCafe, mTheme, mTime;
	JScrollPane pane1, pane2, pane3, pane4;
	Cal cal;

	LocalDate date = LocalDate.now();
	LocalDate cDate;

	public Reserve() {
		super("예약", 1110, 550);
		this.dtcr = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				var r = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (isSelected) {

					r.setBackground(Color.ORANGE);

				} else {
					r.setBackground(Color.lightGray);
				}
				return r;
			}
		};

		setUI();
		setData();
		addEvents();
		setVisible(true);

	}

	public Reserve(String lname, String cname, String tname) {
		this();
		for (int i = 0; i < loc.getRowCount(); i++) {
			if (loc.getValueAt(i, 0).toString().equals(lname)) {
				loc.setRowSelectionInterval(i, i);
			}
		}

		addRow(mCafe, "select c_name from cafe where c_address like '%"
				+ (loc.getValueAt(loc.getSelectedRow(), 0) == "전국" ? "" : loc.getValueAt(loc.getSelectedRow(), 0))
				+ "%'");
		for (int i = 0; i < cafe.getRowCount(); i++) {
			if (cafe.getValueAt(i, 0).toString().equals(cname)) {
				cafe.setRowSelectionInterval(i, i);
			}
		}

		var division = "";

		try {
			var rs = stmt.executeQuery(
					"select * from cafe where c_name like '%" + cafe.getValueAt(cafe.getSelectedRow(), 0) + "%'");
			if (rs.next()) {
				division = rs.getString(3);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		addRow(mTheme, "select t_name from theme where t_no in (" + division + ")");

		for (int i = 0; i < theme.getRowCount(); i++) {
			if (theme.getValueAt(i, 0).toString().equals(tname)) {
				theme.setRowSelectionInterval(i, i);
			}
		}

		checkisAfter();
	}

	public Reserve(String lname, String cname) {
		this();
		for (int i = 0; i < loc.getRowCount(); i++) {
			if (loc.getValueAt(i, 0).toString().equals(lname)) {
				loc.setRowSelectionInterval(i, i);
			}
		}

		addRow(mCafe, "select c_name from cafe where c_address like '%"
				+ (loc.getValueAt(loc.getSelectedRow(), 0) == "전국" ? "" : loc.getValueAt(loc.getSelectedRow(), 0))
				+ "%'");
		for (int i = 0; i < cafe.getRowCount(); i++) {
			if (cafe.getValueAt(i, 0).toString().equals(cname)) {
				cafe.setRowSelectionInterval(i, i);
			}
		}

		var division = "";

		try {
			var rs = stmt.executeQuery(
					"select * from cafe where c_name like '%" + cafe.getValueAt(cafe.getSelectedRow(), 0) + "%'");
			if (rs.next()) {
				division = rs.getString(3);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		addRow(mTheme, "select t_name from theme where t_no in (" + division + ")");

	}

	void setUI() {
		var n = new JPanel(new BorderLayout());
		var c = new JPanel();
		add(n, "North");

		n.add(lbl("방탈출 예약", 0, 30));
		n.add(lbl("Room Escape Reservation", 0, 20), "South");
		add(c);
		c.setLayout(new BoxLayout(c, BoxLayout.X_AXIS));

		loc = table(mloc = model("지역"));
		cafe = table(mCafe = model("매장"));
		theme = table(mTheme = model("테마"));
		time = table(mTime = model("시간,여부".split(",")));

		c.add(Box.createHorizontalStrut(5));
		c.add(cal = new Cal());
		c.add(Box.createHorizontalStrut(5));
		c.add(pane1 = new JScrollPane(loc));
		c.add(Box.createHorizontalStrut(5));
		c.add(pane2 = new JScrollPane(cafe));
		c.add(Box.createHorizontalStrut(5));
		c.add(pane3 = new JScrollPane(theme));
		c.add(Box.createHorizontalStrut(5));
		c.add(pane4 = new JScrollPane(time));
		c.add(Box.createHorizontalStrut(5));

		loc.setRowHeight(22);
		time.setRowHeight(26);

		time.getColumnModel().getColumn(1).setMinWidth(0);
		time.getColumnModel().getColumn(1).setMaxWidth(0);

		cal.setPreferredSize(new Dimension(600, 1));
		pane1.setPreferredSize(new Dimension(120, 1));
		pane4.setPreferredSize(new Dimension(120, 1));
		pane1.getViewport().setBackground(Color.LIGHT_GRAY);
		pane2.getViewport().setBackground(Color.LIGHT_GRAY);
		pane3.getViewport().setBackground(Color.LIGHT_GRAY);
		pane4.getViewport().setBackground(Color.LIGHT_GRAY);
		pane1.setBorder(BorderFactory.createEmptyBorder());
		pane2.setBorder(BorderFactory.createEmptyBorder());
		pane3.setBorder(BorderFactory.createEmptyBorder());
		pane4.setBorder(BorderFactory.createEmptyBorder());
	}

	void addEvents() {
		loc.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (loc.getSelectedRow() == -1)
					return;
				mCafe.setRowCount(0);
				mTheme.setRowCount(0);
				mTime.setRowCount(0);

				addRow(mCafe,
						"select c_name from cafe where c_address like '%"
								+ (loc.getValueAt(loc.getSelectedRow(), 0) == "전국" ? ""
										: loc.getValueAt(loc.getSelectedRow(), 0))
								+ "%'");
				super.mouseClicked(e);
			}
		});

		cafe.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (cafe.getSelectedRow() == -1)
					return;

				var division = "";

				try {
					var rs = stmt.executeQuery("select * from cafe where c_name like '%"
							+ cafe.getValueAt(cafe.getSelectedRow(), 0) + "%'");
					if (rs.next()) {
						division = rs.getString(3);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mTheme.setRowCount(0);
				mTime.setRowCount(0);

				addRow(mTheme, "select t_name from theme where t_no in (" + division + ")");
				super.mouseClicked(e);
			}
		});

		theme.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (theme.getSelectedRow() == -1)
					return;
				time.clearSelection();
				mTime.setRowCount(0);
				checkisAfter();
				super.mouseClicked(e);
			}
		});

		time.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (time.getSelectedRow() == -1 || time.getValueAt(time.getSelectedRow(), 1).equals(false)) {
					time.clearSelection();
					return;
				}

				cno = getValue("select * from cafe where c_name = '" + cafe.getValueAt(cafe.getSelectedRow(), 0) + "'");
				cname = getValue(
						"select * from cafe where c_name = '" + cafe.getValueAt(cafe.getSelectedRow(), 0) + "'",
						"c_name");
				cprice = getValue(
						"select * from cafe where c_name = '" + cafe.getValueAt(cafe.getSelectedRow(), 0) + "'",
						"c_price");

				tno = toInt(getValue(
						"select * from theme where t_name = '" + theme.getValueAt(theme.getSelectedRow(), 0) + "'",
						"t_no"));
				tname = getValue(
						"select * from theme where t_name = '" + theme.getValueAt(theme.getSelectedRow(), 0) + "'",
						"t_name");
				tpersonnel = getValue(
						"select * from theme where t_name = '" + theme.getValueAt(theme.getSelectedRow(), 0) + "'",
						"t_personnel");
				new Pay(cDate.toString(), time.getValueAt(time.getSelectedRow(), 0).toString())
						.addWindowListener(new Before(Reserve.this));
				super.mouseClicked(e);
			}
		});

	}

	void checkisAfter() {
		mTime.setRowCount(0);

		for (var o : new String[] { "10:30", "11:30", "12:30", "13:30", "14:30", "15:30", "16:30", "17:30", "18:30",
				"19:30", "20:30", "21:30", "22:30", "23:30", "24:30" }) {
			var t = o.split(":");
			var d = LocalDateTime.of(cDate, LocalTime.of(toInt(t[0]) == 24 ? 1 : toInt(t[0]), toInt(t[1])));
			boolean chk = d.isAfter(LocalDateTime.now());

			try {
				var rs = stmt.executeQuery(
						"select r_no, c.c_name, t.t_name, r.r_date ,r.r_time from reservation r, theme t, cafe c where r.c_no = c.c_no and t.t_no = r.t_no and r_date = '"
								+ d.toLocalDate() + "' and r_time = '" + d.toLocalTime() + "' and t_name = '"
								+ theme.getValueAt(theme.getSelectedRow(), 0) + "' and c_name = '"
								+ cafe.getValueAt(cafe.getSelectedRow(), 0) + "'");
				chk |= rs.next();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mTime.addRow(new Object[] { o, chk });

		}

	}

	void setData() {
		mloc.addRow(new Object[] { "전국" });
		try {
			var rs = stmt.executeQuery("select distinct left(c_address,2) from cafe ");
			while (rs.next()) {
				mloc.addRow(new Object[] { rs.getString(1) });
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		loc.setRowHeight(loc.getRowCount() - 1, 15);

	}

	class Cal extends JPanel {

		String week[] = "SUN,MON,TUE,WED,THU,FRI,SAT".split(",");
		int month, year;
		JLabel datelbl, prev, next;

		JToggleButton btn[] = new JToggleButton[42];
		ButtonGroup bg = new ButtonGroup();

		public Cal() {

			setLayout(new BorderLayout());

			var n = new JPanel(new BorderLayout());
			var c = new JPanel(new GridLayout(0, 7, 5, 5));

			for (var str : week) {
				c.add(lbl(str, 0, 20));
			}

			add(n, "North");
			add(c);

			n.add(datelbl = lbl("", JLabel.CENTER));
			n.add(prev = lbl("◀", JLabel.CENTER, 20), "West");
			n.add(next = lbl("▶", JLabel.CENTER, 20), "East");

			prev.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					date = date.plusMonths(-1);
					setDate();
					bg.clearSelection();
					super.mousePressed(e);
				}
			});

			next.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					date = date.plusMonths(1);
					setDate();
					bg.clearSelection();
					super.mousePressed(e);
				}
			});

			n.setPreferredSize(new Dimension(1, 100));

			for (int i = 0; i < 42; i++) {
				c.add(btn[i] = new JToggleButton());
				bg.add(btn[i]);
				btn[i].setBorderPainted(false);
				btn[i].setFocusPainted(false);
				btn[i].setBackground(Color.LIGHT_GRAY);
				btn[i].setUI(new MetalToggleButtonUI() {
					@Override
					protected Color getSelectColor() {
						return Color.ORANGE;
					}
				});

				btn[i].addActionListener(a -> {
					var btn = (JToggleButton) a.getSource();
					if (btn.isSelected()) {
						if (cafe.getSelectedRow() == -1 || theme.getSelectedRow() == -1)
							return;
						cDate = LocalDate.of(year, month, toInt(a.getActionCommand()));
						checkisAfter();
					}
				});
			}

			n.setBorder(new LineBorder(Color.BLACK));
			n.setOpaque(true);
			n.setBackground(Color.BLACK);
			prev.setForeground(Color.white);
			next.setForeground(Color.white);
			datelbl.setForeground(Color.white);
			c.setOpaque(false);
			this.setOpaque(true);
			this.setBackground(Color.lightGray);
			setDate();
		}

		void setDate() {
			month = date.getMonthValue();
			year = date.getYear();
			datelbl.setText(date.getMonth() + " " + year);

			LocalDate sday = LocalDate.of(year, month, 1);
			int sWeek = sday.getDayOfWeek().getValue() % 7;

			for (int i = 0; i < 42; i++) {
				LocalDate tmp = sday.plusDays(i - sWeek);
				btn[i].setText(tmp.getDayOfMonth() + "");
				if (tmp.toEpochDay() < LocalDate.now().toEpochDay()) {
					btn[i].setEnabled(false);
				} else
					btn[i].setEnabled(true);

				if (tmp.getMonth() != date.getMonth()) {
					btn[i].setVisible(false);
				} else {
					btn[i].setVisible(true);
				}

				if (tmp.equals(LocalDate.now())) {
					btn[i].setSelected(true);
					cDate = LocalDate.of(year, month, toInt(btn[i].getText()));
				}
			}
		}
	}

	@Override
	JTable table(DefaultTableModel m) {
		var t = super.table(m);
		t.getTableHeader().setPreferredSize(new Dimension(50, 50));
		t.getTableHeader().setBackground(Color.BLACK);
		t.getTableHeader().setForeground(Color.WHITE);
		t.setGridColor(Color.LIGHT_GRAY);
		t.setBorder(BorderFactory.createEmptyBorder());
		return t;
	}

}
