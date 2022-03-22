package 충남;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Route extends BaseFrame {
	JComboBox<String> combo = new JComboBox<String>();
	ArrayList<String> list;

	r r;

	public Route() {
		super("노선도", 850, 650);
		try {
			data();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < metroNames.size() - 1; i++) {
			combo.addItem(metroNames.get(i + 1));
		}

		add(combo, "North");
		add(c = new JPanel(new BorderLayout()));
		setData();

		c.add(new JScrollPane(r = new r()));

		combo.addItemListener(a -> {
			setData();
			r.removeAll();
			r.setXY();
			r.draw();
		});

		setVisible(true);
	}

	void setData() {
		list = new ArrayList<String>();
		try {
			ResultSet rs = stmt.executeQuery(
					"select r.metro, r.station, s.name, m.name from route r, station s, metro m where s.serial = r.station and m.serial = r.metro and r.metro = "
							+ (combo.getSelectedIndex() + 1));
			while (rs.next()) {
				list.add(rs.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	class r extends JPanel {
		int x[], y[], px[], py[];
		HashSet<Integer> hash = new HashSet<Integer>();

		public r() {
			setLayout(null);

			setXY();
			draw();
		}

		void setXY() {
			int size = list.size();
			x = new int[size];
			y = new int[size];
			px = new int[size + (size / 6) + 1];
			py = new int[size + (size / 6) + 1];

			int sx = 130, ox = 100, sy = 70, oy = 100, idx = 0;
			x[0] = sx;
			y[0] = sy;
			px[idx] = x[0] - ox / 2;
			py[idx] = y[0];
			idx++;
			px[idx] = x[0] - ox / 2;
			py[idx] = y[0];

			for (int i = 1; i < size; i++) {
				idx++;

				if (i % 6 == 0) {
					px[idx] = px[idx - 1];
					py[idx] = py[idx - 1] + oy;
					idx++;
				}
				py[idx] = y[i] = (i / 6) * oy + sy;

				if ((i / 6) % 2 == 0) {
					x[i] = (i % 6) * ox + sx;
					px[idx] = x[i] + ox / 2;
				} else {
					x[i] = 5 * ox - (i % 6) * 100 + sx;
					px[idx] = x[i] - ox / 2;
				}
			}
			repaint();
			revalidate();
		}

		void draw() {
			for (int i = 0; i < x.length; i++) {
				JLabel jl = label(list.get(i), 0, 13);
				jl.setName(i + "");
				jl.setBounds(x[i] - 45, y[i] + 5, 100, 25);
				add(jl);

				jl.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						JLabel j = ((JLabel) e.getSource());
						int serial = stNames.indexOf(jl.getText());

						hash.clear();
						try {
							ResultSet rs = stmt.executeQuery("select * from path where departure = " + serial
									+ " or arrive = " + serial + " group by cost");
							while (rs.next()) {
								hash.add(rs.getInt(2));
								hash.add(rs.getInt(3));
							}
							hash.remove(serial);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						String msg = j.getText() + "에서 갈 수 있는 역";
						for (var h : hash) {
							msg += stNames.get(h) + ",";
						}
						msg = msg.substring(0, msg.length() - 1);
						msg += "\n" + j.getText() + "를 지나가는 노선\n";
						for (int k = 1; k < line.length; k++) {
							if (line[serial][k] != 0) {
								msg += metroNames.get(line[serial][k])+"\n";
							}
						}
						msg(msg);
					}
				});
			}
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setStroke(new BasicStroke(3));
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("맑은 고딕", Font.BOLD, 20));
			g2.drawString(combo.getSelectedItem() + "", 80, 40);

			g2.setColor(Color.blue);
			if (((ArrayList) metroStinfo[combo.getSelectedIndex() + 1][0]).size() % 6 == 0) {
				g2.drawPolyline(px, py, px.length - 1);
			} else {
				g2.drawPolyline(px, py, px.length);
			}
			g2.setColor(new Color(255, 50, 100));
			for (int i = 0; i < x.length; i++) {
				g2.fillOval(x[i], y[i] - 5, 10, 10);
			}
		}
	}

	public static void main(String[] args) {
		new Route();
	}

}
