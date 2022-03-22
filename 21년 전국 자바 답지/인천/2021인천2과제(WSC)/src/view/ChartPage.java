package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import additional.Util;
import db.Tools;

public class ChartPage extends BasePage {

	JLabel lblDate;
	JButton prev, next;
	JPanel days[] = new JPanel[42];
	DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy년 MM월");
	LocalDate date = LocalDate.now();
	Color color[] = { Color.red, Color.orange, Color.yellow, Color.green, Color.blue };
	String category[] = "음료,생활용품,아이스크림,식품,과자류".split(",");
	int max;

	public ChartPage() {
		super();

		this.formInit();

		this.setCal();
	}

	void formInit() {
		max = Util.rei(Tools.getOneRs(
				"select max(sub.s) from (select c_No, pu_Date, sum(pu.pu_Count) as s from purchase pu, product p where pu.p_No=p.p_No and pu.s_No="
						+ BasePage.sno + " group by pu_Date, p.c_No) as sub;"));
		this.add(n = new JPanel(new FlowLayout(1, 20, 5)), "North");
		this.add(c = new JPanel(new GridLayout(0, 7)));
		date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);

		n.add(prev = Util.btn("◀", a -> {
			this.setMonth(-1);
		}));
		n.add(lblDate = Util.lbl(format.format(date), 0, 20));
		n.add(next = Util.btn("▶", a -> {
			this.setMonth(1);
		}));

		Util.sz(prev, 35, 35);
		Util.sz(next, 35, 35);
		prev.setMargin(new Insets(0, 0, 0, 0));
		next.setMargin(new Insets(0, 0, 0, 0));

		n.setOpaque(false);
		c.setOpaque(false);

	}

	void setMonth(int months) {
		date = date.plusMonths(months);
		lblDate.setText(format.format(date));
		c.removeAll();
		this.setCal();
	}

	void setCal() {
		int sday = date.getDayOfWeek().getValue(), eday = date.lengthOfMonth();
		for (int i = 0; i < days.length; i++) {
			LocalDate l = LocalDate.ofEpochDay(date.toEpochDay() - sday + i);
			String d = l.getDayOfMonth() + "";
			Color col = (i % 7 == 0 ? Color.red : (i % 7 == 6 ? Color.blue : Color.black));
			if (l.toEpochDay() < date.toEpochDay() || l.toEpochDay() > date.plusMonths(1).toEpochDay() - 1) {
				col = new Color(col.getRed(), col.getGreen(), col.getBlue(), 127);
			}
			try {
				var rs = Tools.rs(
						"select c.c_No, c.c_Name, sum(pu.pu_Count) from product p inner join category c on c.c_No=p.c_No inner join purchase pu on pu.p_No=p.p_No where pu.s_No="
								+ BasePage.sno + " and pu.pu_Date='" + l.toString()
								+ "' group by c.c_No order by c.c_No");
				HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
				for (int j = 1; j <= 5; j++) {
					hash.put(j, 0);
				}
				while (rs.next()) {
					hash.put(rs.getInt(1), hash.get(rs.getInt(1)) + rs.getInt(3));
				}
				c.add(days[i] = new ChartPanel(d, col, hash.values().toArray(Integer[]::new)));
				System.out.println(
						"select c.c_No, c.c_Name, sum(pu.pu_Count) from product p inner join category c on c.c_No=p.c_No inner join purchase pu on pu.p_No=p.p_No where pu.s_No="
								+ BasePage.sno + " and pu.pu_Date='" + l.toString()
								+ "' group by c.c_No order by c.c_No");
				int k = i;
				days[i].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						ChartPanel p = (ChartPanel) e.getSource();
						JPanel s = new JPanel();
						s.setOpaque(false);
						JDialog dialog = new JDialog();
						dialog.add(Util.lbl(l.toString() + " 매출 실적", 0, 15), "North");

						p.removeMouseListener(this);
						p.isVisible = true;
						p.width = p.width * 2;
						dialog.add(p);
						dialog.add(s, "South");
						s.add(Util.sz(Util.btn("OK", a -> {
							dialog.dispose();
							setMonth(0);
							repaint();
							revalidate();
						}), 60, 25), "South");

						JLabel jl;
						dialog.add(jl = new JLabel() {
							@Override
							protected void paintComponent(Graphics g) {
								super.paintComponent(g);
								Graphics2D g2 = (Graphics2D) g;
								g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
								for (int j = 0; j < color.length; j++) {
									g2.setColor(color[j]);
									g2.fillRect(10, 60 * j + 10, 10, 10);
									g2.setColor(Color.black);
									g2.drawString(category[j], 30, 60 * j + 20);
								}

							}
						}, "East");
						Util.sz(jl, 100, 100);

						dialog.getContentPane().setBackground(Color.white);
						dialog.setModal(true);
						dialog.setUndecorated(true);
						((JPanel) dialog.getContentPane()).setBorder(
								new CompoundBorder(new LineBorder(Color.black, 2), new EmptyBorder(10, 10, 10, 10)));
						dialog.setSize(500, 350);
						dialog.setLocationRelativeTo(null);
						dialog.setVisible(true);
					}
				});
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			days[i].setOpaque(false);
		}
	}

	class ChartPanel extends JPanel {
		String day;
		JLabel chart;
		int[] data;
		int width;
		boolean isVisible;
		HashMap<String, Integer> hash = new HashMap<String, Integer>();

		public ChartPanel(String day, Color foreColor) {
			this.day = day;
			this.width = 20;

			this.setLayout(new BorderLayout());
			this.setBorder(new LineBorder(Color.black));

			var lbl = Util.lbl(day, JLabel.LEFT, 15);
			this.add(lbl, "North");
			lbl.setForeground(foreColor);

		}

		public ChartPanel(String day, Color foreColor, Integer[] integers) {
			this(day, foreColor);
			this.add(new Data(Arrays.stream(integers).mapToInt(i -> i).toArray()));
//			Arrays.stream(integers).forEach(s->System.out.println(s));
		}

		class Data extends JPanel {
			int data[];

			public Data(int data[]) {
				this.data = data;
				this.setOpaque(false);
			}

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				int hgap = this.getWidth() / 5, margin = 15, height = this.getHeight();
				for (int i = 0; i < 5; i++) {
					double p = (double) data[i] / max;
					int barHeight = (int) (p * height), barStart = (int) ((1 - p) * height);

					if (max > 0) {
						g2.setColor(color[i]);
						g2.fillRect(hgap * i + (hgap - 20) / 2, barStart, width, barHeight);
					}

					if (isVisible) {
						var lbl = String.format("%d개", data[i]);
						System.out.println(data[i]);
						var b1 = g2.getFontMetrics().getStringBounds(lbl, g2);
						g2.setColor(Color.black);
						g2.drawString(lbl, (int) (hgap * i + (hgap - b1.getWidth()) / 2) + 10, barStart);
					}

				}
			}
		}
	}

}
