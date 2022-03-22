

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
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.Renderer;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class Chart extends BaseFrame {
	int cfw, cfh;
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel n = new JPanel(new FlowLayout(0)), c = new JPanel(new BorderLayout());
	JPanel Chart;
	TitledBorder border = new TitledBorder(new LineBorder(Color.black), "TOP 5");
	DefaultMutableTreeNode node = new DefaultMutableTreeNode("Orange Ticket");
	DefaultMutableTreeNode div = new DefaultMutableTreeNode("분류");
	DefaultMutableTreeNode design = new DefaultMutableTreeNode("차트디자인");
	DefaultMutableTreeNode color = new DefaultMutableTreeNode("차트색깔");
	DefaultMutableTreeNode dividx[] = { new DefaultMutableTreeNode("뮤지컬"), new DefaultMutableTreeNode("오페라"),
			new DefaultMutableTreeNode("콘서트") };
	DefaultMutableTreeNode designidx[] = { new DefaultMutableTreeNode("막대 그래프"),
			new DefaultMutableTreeNode("꺽은선 그래프") };
	DefaultMutableTreeNode coloridx[] = { new DefaultMutableTreeNode("Red"), new DefaultMutableTreeNode("Orange"),
			new DefaultMutableTreeNode("Blue") };
	JTree tree = new JTree(node);
	String name = "M";
	int height = 500, h = 50, model = 1;
	int xx[] = new int[5], yy[] = new int[5];
	boolean orangeClicked, divClicked, designClicked, colorClicked;
	Color col = Color.red;

	public Chart() {
		super("차트", 650, 700);
		ui();
		event();
		setChart(name, col, model);
		setVisible(true);
	}

	void event() {
		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
				String t = tp.getLastPathComponent().toString();
				if (t.equals("뮤지컬")) {
					name = "M";
				} else if (t.equals("오페라")) {
					name = "O";
				} else if (t.equals("콘서트")) {
					name = "C";
				} else if (t.equals("Red")) {
					col = Color.red;
				} else if (t.equals("Orange")) {
					col = Color.orange;
				} else if (t.equals("Blue")) {
					col = Color.blue;
				} else if (t.equals("막대 그래프")) {
					model = 1;
				} else if (t.equals("꺽은선 그래프")) {
					model = 2;
				}

				if (e.getClickCount() == 2) {
				}
				setChart(name, col, model);
			}

		});

	}

	void ui() {
		this.add(mainp);
		mainp.add(n, "North");
		mainp.add(c, "Center");

		TreeTitle title = new TreeTitle();
		n.add(title);
		n.setBackground(Color.white);
		title.setBackground(Color.white);
	}

	void setChart(String kind, Color color, int model) {
		c.removeAll();
		Chart = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				try {
					ResultSet rs = stmt.executeQuery(
							"select p.p_no, p.p_name, p.pf_no, count(*) c from perform p, ticket t where t.p_no = p.p_no and left(p.pf_no,1) = '"
									+ kind + "' group by p.p_name order by c desc limit 0, 5");
					int max = 0, i = 0, height = 0, width = 50, base = 350;
					if (model == 1) {
						while (rs.next()) {
							if (i == 0)
								max = rs.getInt(4);
							height = (int) (rs.getInt(4) / (double) max * 300);
							g2.setColor(color);
							g2.fillRect(100 + (100 * i), base - height, 50, height);
							g2.setColor(Color.BLACK);
							g2.drawRect(100 + (100 * i), base - height, 50, height);
							g2.setFont(new Font("굴림", Font.BOLD, 13));
							FontMetrics metrics = g2.getFontMetrics(g2.getFont());
							width = metrics.stringWidth(rs.getString(2));
							g2.drawString(rs.getString(2), 100 + (100 * i), base + 20);
							width = metrics.stringWidth(rs.getString(4));
							g2.drawString(rs.getString(4), 120 + (100 * i), base - height - 15);
							i++;
						}
					} else {
						int array[][] = new int[5][2];
						while (rs.next()) {
							if (i == 0)
								max = rs.getInt(4);
							height = (int) (rs.getInt(4) / (double) max * 300);
							xx[i] = 95 + (110 * i);
							yy[i] = base - height;
							array[i][0] = 95 + (110 * i);
							array[i][1] = base - height;
							g2.setFont(new Font("", Font.BOLD, 13));
							FontMetrics metrics = g2.getFontMetrics(g2.getFont());
							width = metrics.stringWidth(rs.getString(2));
							g2.drawString(rs.getString(2), 100 + (100 * i), base + 20);
							width = metrics.stringWidth(rs.getString(4));
							g2.drawString(rs.getString(4), 120 + (100 * i), base - height - 15);
							i++;
						}

						g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						g2.setStroke(new BasicStroke(3));
						g2.setColor(color);

						g2.drawPolyline(xx, yy, xx.length);

						for (int k = 0; k < array.length; k++) {
							g2.setColor(Color.black);
							g2.fillOval(array[k][0], array[k][1], 10, 10);
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		c.add(Chart);
		repaint();
		revalidate();
	}

	public static void main(String[] args) {
		new Chart().setVisible(true);
	}

	class TreeTitle extends JPanel {

		public TreeTitle() {
			n.setBorder(border);
			n.add(tree);

			node.add(div);
			node.add(design);
			node.add(color);

			for (int i = 0; i < 3; i++) {
				div.add(dividx[i]);
				color.add(coloridx[i]);
			}
			for (int i = 0; i < 2; i++) {
				design.add(designidx[i]);
			}

			border.setTitleFont(new Font("굴림", Font.BOLD, 25));
		}

	}
}
