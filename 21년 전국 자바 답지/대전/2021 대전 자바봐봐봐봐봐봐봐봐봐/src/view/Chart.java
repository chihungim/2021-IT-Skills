package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sound.sampled.Control;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Line.Info;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class Chart extends BaseFrame {

	DefaultMutableTreeNode node = new DefaultMutableTreeNode("Orange Ticket");
	DefaultMutableTreeNode cate = new DefaultMutableTreeNode("분류");
	DefaultMutableTreeNode cates[] = { new DefaultMutableTreeNode("뮤지컬"), new DefaultMutableTreeNode("오페라"),
			new DefaultMutableTreeNode("콘서트") };

	DefaultMutableTreeNode design = new DefaultMutableTreeNode("차트디자인");
	DefaultMutableTreeNode designs[] = { new DefaultMutableTreeNode("막대 그래프"), new DefaultMutableTreeNode("꺾은선 그래프") };

	DefaultMutableTreeNode col = new DefaultMutableTreeNode("차트색깔");
	DefaultMutableTreeNode cols[] = { new DefaultMutableTreeNode("Red"), new DefaultMutableTreeNode("Orange"),
			new DefaultMutableTreeNode("Blue") };
	JTree tree;
	String type = "M";
	Color color = Color.red;
	JPanel c = new JPanel(new BorderLayout());
	JPanel chart;
	int mode = 1;

	public Chart() {
		super("차트", 650, 700);
		var n = new JPanel(new FlowLayout(FlowLayout.LEFT));

		add(n, "North");
		add(c);

		n.add(tree = new JTree(node));
		node.add(cate);
		node.add(col);
		node.add(design);

		for (int i = 0; i < cates.length; i++) {
			cate.add(cates[i]);
			col.add(cols[i]);
		}

		Arrays.stream(designs).forEach(design::add);
		var title = new TitledBorder(new LineBorder(Color.BLACK), "TOP5");
		title.setTitleFont(new Font("맑은 고딕", Font.BOLD, 20));
		n.setBorder(title);
		n.setOpaque(true);
		n.setBackground(Color.WHITE);
		tree.expandPath(new TreePath(node.getPath()));
		drawChart();

		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
				String tmp = tp.getLastPathComponent().toString();
				if (e.getClickCount() == 2) {
					if (tmp.equals("뮤지컬")) {
						type = "M";
					} else if (tmp.equals("오페라")) {
						type = "O";
					} else if (tmp.equals("콘서트")) {
						type = "C";
					} else if (tmp.equals("Red")) {
						color = Color.red;
					} else if (tmp.equals("Orange")) {
						color = Color.orange;
					} else if (tmp.equals("Blue")) {
						color = Color.blue;
					} else if (tmp.equals("막대 그래프")) {
						mode = 1;
					} else if (tmp.equals("꺾은선 그래프")) {
						mode = 2;
					}
				}
				drawChart();
			}
		});
		setVisible(true);
	}

	void drawChart() {
		c.removeAll();

		chart = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				try {
					var rs = stmt.executeQuery(
							"SELECT p.p_no, p.p_name, p.pf_no, count(*) FROM perform p, ticket t where p.p_no=t.p_no and pf_no like '"
									+ type + "%' group by p.p_name order by count(*) desc limit 0, 5");
					int max = 0, idx = 0, w = 50, base = 350;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					if (mode == 1) {
						while (rs.next()) {
							max = idx == 0 ? rs.getInt(4) : max;
							var pr = rs.getDouble(4) / max;
							var h = (int) (pr * 300);
							var y = (int) (base - (pr * 300));
							var x = 100 + (idx * 100);
							g2.setColor(color);
							g2.fillRect(x, y, w, h);
							g2.setColor(Color.BLACK);
							g2.drawRect(x, y, w, h);
							g2.setFont(new Font("", Font.BOLD, 13));
							FontMetrics metrics = g2.getFontMetrics(g2.getFont());
							g2.drawString(rs.getString(2), x, base + 20);
							var fw = metrics.stringWidth(rs.getString(4));
							g2.drawString(rs.getString(4), fw + x, y - 10);
							idx++;
						}
					} else {
						int xx[] = new int[5];
						int yy[] = new int[5];

						while (rs.next()) {
							max = idx == 0 ? rs.getInt(4) : max;
							var pr = rs.getDouble(4) / max;
							var h = (int) (pr * 300);
							var y = (int) (base - (pr * 300));
							var x = 100 + (idx * 100);
							xx[idx] = x;
							yy[idx] = y;
							g2.setFont(new Font("", Font.BOLD, 13));
							FontMetrics metrics = g2.getFontMetrics(g2.getFont());
							var fw = metrics.stringWidth(rs.getString(4));
							g2.drawString(rs.getString(4), (x - (fw / 2)), y - 20);
							fw = metrics.stringWidth(rs.getString(2));
							g2.drawString(rs.getString(2), x - (fw / 2), base + 20);
							idx++;
						}
						g2.setColor(color);
						g2.setStroke(new BasicStroke(3));
						g2.drawPolyline(xx, yy, xx.length);
						for (int i = 0; i < xx.length; i++) {
							g2.setColor(Color.BLACK);
							g2.fillOval(xx[i] - 10, yy[i] - 10, 20, 20);
						}
					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};

		c.add(sz(chart, 650, 500));
		pack();
		repaint();
		revalidate();
	}


	public static void main(String[] args) {
		new Chart();
	}
}
