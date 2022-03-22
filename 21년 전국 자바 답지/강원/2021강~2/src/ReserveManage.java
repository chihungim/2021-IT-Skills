import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ReserveManage extends JPanel {
	JPanel n, c, s;
	JPanel n_s;
	JPanel chart;

	DefaultTableModel m = BaseFrame.model("순번,예매자,출발지,도착지,출발날짜,도착시간".split(","));
	JTable t = BaseFrame.table(m);
	JScrollPane jsc = new JScrollPane(t);

	JComboBox<String> com = new JComboBox<String>();

	JPanel p1 = BaseFrame.gPanel(0, 1), p2 = BaseFrame.gPanel(0, 1), menuP = BaseFrame.gPanel(1, 0);
	JPopupMenu pop = new JPopupMenu();
	JScrollPane jsc1 = new JScrollPane(p1);
	JScrollPane jsc2 = new JScrollPane(p2);
	JButton btn1[] = new JButton[16], btn2[];

	
	int cnt = 0;
	boolean isEdited;

	public ReserveManage() {
		ui();
		data();
		event();
	}

	void ui() {
		setLayout(null);

		pop.add(menuP);
		menuP.add(jsc1);
		menuP.add(jsc2);

		BaseFrame.size(pop, 240, 280);

		add(BaseFrame.lbl("예매 관리", 2, 20)).setBounds(10, 20, 100, 30);
		add(com).setBounds(110, 25, 100, 25);
		add(BaseFrame.lbl("<가장 예매가 많은 일정 TOP 6>", 0, 13)).setBounds(370, 20, 200, 30);

		com.addItem("2차원 영역형");
		com.addItem("방사형");

		add(jsc).setBounds(10, 420, 940, 180);
		add(BaseFrame.btn("저장", e -> save())).setBounds(720, 600, 110, 30);
		add(BaseFrame.btn("삭제", e -> delete())).setBounds(840, 600, 110, 30);
	}

	void save() {
		if (isEdited) {
			BaseFrame.iMsg = new BaseFrame.Imsg("수정내용을 저장 완료하였습니다.");
			for (int i = 0; i < t.getRowCount(); i++) {
				int depart = BaseFrame
						.toInt(BaseFrame.getOne("select no from a where name = '" + t.getValueAt(i, 1) + "'"));
				int arrive = BaseFrame
						.toInt(BaseFrame.getOne("select no from a where name = '" + t.getValueAt(i, 2) + "'"));
				BaseFrame.execute("update schedule set departure_location2_no = " + depart + ", arrival_location2_no = "
						+ arrive + " where no = " + BaseFrame.toInt(t.getValueAt(i, 0)));
			}
		}
	}

	void delete() {
		BaseFrame.iMsg = new BaseFrame.Imsg("삭제를 완료하였습니다.");
		BaseFrame.execute("delete from schedule where no = " + BaseFrame.toInt(t.getValueAt(t.getSelectedRow(), 0)));
	}

	void rowData() {
		m.setRowCount(0);

		try {
			var rs = BaseFrame.stmt.executeQuery(
					"select u.name, a.name, b.name, date, date_add(date, interval elapsed_time hour_second) from reservation r, user u, schedule s, a a, a b where r.user_no = u.no and r.schedule_no = s.no and s.departure_location2_no = a.no and s.arrival_location2_no = b.no order by r.no asc");
			while (rs.next()) {
				Object row[] = { rs.getRow(), rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5).split(" ")[1] };
				m.addRow(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void data() {
		try {
			var rs = BaseFrame.stmt.executeQuery("select * from location");
			int i = 0;
			while (rs.next()) {
				p1.add(btn1[i] = new JButton(rs.getString("name")));
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void event() {
		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 3 && (t.getSelectedColumn() == 2 || t.getSelectedColumn() == 3)) {
					cnt = t.getSelectedColumn();
					pop.show(t, e.getX(), e.getY());
				}
			}
		});

		for (int i = 0; i < btn1.length; i++) {
			int idx = i;
			btn1[i].addActionListener(e -> {
				p2.removeAll();

				try {
					var rs = BaseFrame.stmt.executeQuery("select * from location2 where location_no = " + (idx + 1));
					rs.last();
					btn2 = new JButton[rs.getRow()];
					rs.beforeFirst();

					int k = 0;
					while (rs.next()) {
						p2.add(btn2[k] = new JButton(rs.getString(2)));
						btn2[k].addActionListener(e2 -> {
							JButton tmp = (JButton) e2.getSource();
							String text = btn1[idx].getText() + " " + tmp.getText();
							t.setValueAt(text, t.getSelectedRow(), cnt);
							isEdited = true;
						});
						k++;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				repaint();
				revalidate();
			});

			for (Component c : getComponents()) {
				if (c instanceof JPanel)
					remove(c);
			}

			areaChart();

			com.addItemListener(e -> {
				if (com.getSelectedIndex() == 0) {
					for (Component c : getComponents()) {
						if (c instanceof JPanel)
							remove(c);
					}

					areaChart();
					repaint();
					revalidate();
				} else {
					for (Component c : getComponents()) {
						if (c instanceof JPanel)
							remove(c);
					}

					radialChart();
					repaint();
					revalidate();
				}
			});
		}
		rowData();
	}

	void areaChart() {
		chart = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;

//				this.setBackground(BaseFrame.whiteColor);
//				this.setForeground(BaseFrame.blackColor);

				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g2d.drawLine(50, 20, 50, 260);
				for (int i = 0; i < 5; i++) {
					g2d.drawLine(50, 20 + (i * 60), 600, 20 + (i * 60));
				}

				Polygon pg = new Polygon();

				try {
					var rs = BaseFrame.stmt.executeQuery(
							"select *, count(schedule_no) as cnt from reservation group by schedule_no order by cnt desc, schedule_no asc limit 6");
					int i = 0, max = 0;
					while (rs.next()) {
						if (i == 0)
							max = rs.getInt(4);

						if (i < 5)
							g2d.drawString(max - (i * 2) + "", 30, 25 + (i * 60));

						g2d.drawString(rs.getString(3), 50 + (i * 105), 290);
						pg.addPoint(50 + (i * 110), 20 + ((max - rs.getInt(4)) * 30));
						i++;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				pg.addPoint(600, 260);
				pg.addPoint(50, 260);
//				g2d.setColor(BaseFrame.btnColor);
				g2d.fillPolygon(pg);
			}
		};

		add(chart).setBounds(15, 100, 900, 300);
		chart.setOpaque(true);
		repaint();
		revalidate();
	}

	void radialChart() {
		chart = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;

//				this.setBackground(BaseFrame.whiteColor);

				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//				g2d.setColor(BaseFrame.blackColor);

				int po[][] = new int[6][2];
				Polygon pg[] = { new Polygon(), new Polygon(), new Polygon(), new Polygon(), new Polygon(),
						new Polygon() };

				for (int i = 0; i < 5; i++) {
					for (int j = 0; j < pg.length; j++) {
						pg[i].addPoint((int) (380 + (100 - i * 20) * Math.cos(j * Math.PI / 3 + (Math.PI / 6))),
								(int) (150 + (100 - i * 20) * Math.sin(j * Math.PI / 3 + (Math.PI / 6))));
					}
					g2d.drawPolygon(pg[i]);
				}

				for (int i = 0; i < pg.length; i++) {
					po[i][0] = (int) (375 + 110 * Math.cos(i * Math.PI / 3 - (Math.PI / 2)));
					po[i][1] = (int) (150 + 110 * Math.sin(i * Math.PI / 3 - (Math.PI / 2)));
				}

				try {
					var rs = BaseFrame.stmt.executeQuery(
							"select *, count(schedule_no) as cnt from reservation group by schedule_no order by cnt desc, schedule_no asc limit 6");
					int i = 0, max = 0;
					while (rs.next()) {
						if (i == 0)
							max = rs.getInt(4);

//						g2d.setColor(BaseFrame.blackColor);

						if (i < 5) {
							g2d.drawString(max - 2 * i + "", 370, 55 + (i * 20));
						}
						g2d.drawString(rs.getString(3), po[i][0], po[i][1]);
						num[i] = rs.getInt(4);
						i++;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//				g2d.setColor(BaseFrame.btnColor);
				Polygon p = new Polygon();

				for (int i = 0; i < num.length; i++) {
					p.addPoint(
							(int) (380
									+ (100 - (num[0] - num[i]) / 2.0 * 20) * Math.cos(i * Math.PI / 3 - (Math.PI / 2))),
							(int) (150 + (100 - (num[0] - num[i]) / 2.0 * 20)
									* Math.sin(i * Math.PI / 3 - (Math.PI / 2))));
				}

				g2d.drawPolygon(p);
			}
		};

		chart.setOpaque(true);
		add(chart).setBounds(100, 100, 500, 300);
		repaint();
		revalidate();
	}

}
