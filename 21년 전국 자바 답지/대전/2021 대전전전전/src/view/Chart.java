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

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import db.DBManager;

public class Chart extends BaseFrame {
	
	DefaultMutableTreeNode node = new DefaultMutableTreeNode("Orange Ticket");

	DefaultMutableTreeNode cate = new DefaultMutableTreeNode("분류");
	DefaultMutableTreeNode cates[] = { new DefaultMutableTreeNode("뮤지컬"), new DefaultMutableTreeNode("오페라"), new DefaultMutableTreeNode("콘서트") };
	
	DefaultMutableTreeNode design = new DefaultMutableTreeNode("차트디자인");
	DefaultMutableTreeNode designs[] = { new DefaultMutableTreeNode("막대 그래프"), new DefaultMutableTreeNode("꺾은선 그래프") };
	
	DefaultMutableTreeNode col = new DefaultMutableTreeNode("차트색깔");
	DefaultMutableTreeNode cols[] = { new DefaultMutableTreeNode("Red"), new DefaultMutableTreeNode("Orange"), new DefaultMutableTreeNode("Blue") };
	
	JTree tree = new JTree(node);
	
	String type = "M";
	int mod = 1;
	int x[] = new int[5], y[] = new int[5];
	boolean oc, dic, dec, cc;
	Color color = Color.red;
	JPanel chart;
	
	public Chart() {
		super("차트", 650, 700);
		
		this.add(n = new JPanel(new FlowLayout()), "North");
		this.add(c = new JPanel(new BorderLayout()));
		
		TreeTitle tit = new TreeTitle();
		n.add(tit);
		n.setBackground(Color.white);
		tit.setBackground(Color.white);
		
		tree.expandPath(new TreePath(node.getPath()));
		
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
				String tmp = tp.getLastPathComponent().toString();
				
				if (e.getClickCount()==2) {
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
						mod = 1;
					} else if (tmp.equals("꺾은선 그래프")) {
						mod = 2;
					}
				}
				drawChart();
			}
		});
		
		drawChart();
		
		this.setVisible(true);
	}
	
	void drawChart() {
		c.removeAll();
		chart = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				
				try {
					var rs = DBManager.rs("SELECT p.p_no, p.p_name, p.pf_no, count(*) FROM perform p, ticket t where p.p_no=t.p_no and pf_no like '"+type+"%' group by p.p_name order by count(*) desc limit 0, 5");
					int max = 0, i = 0, height = 0, width = 50, base = 350;
					if (mod == 1) {
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
							x[i] = 95 + (110 * i);
							y[i] = base - height;
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

						g2.drawPolyline(x, y, x.length);

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
		c.add(chart);
		repaint();
		revalidate();
	}
	
	class TreeTitle extends JPanel {
		public TreeTitle() {
			super(new FlowLayout(0));
			n.setBorder(new TitledBorder(new LineBorder(Color.black), "TOP 5", TitledBorder.LEFT, TitledBorder.TOP, new Font("", Font.BOLD, 25)));
			n.add(tree);
			
			node.add(cate);
			node.add(design);
			node.add(col);
			
			for (int i = 0; i < cates.length; i++) {
				cate.add(cates[i]);
				col.add(cols[i]);
			}
			for (int i = 0; i < designs.length; i++) {
				design.add(designs[i]);
			}
		}
	}
	
	public static void main(String[] args) {
		new Chart();
	}
	
}
