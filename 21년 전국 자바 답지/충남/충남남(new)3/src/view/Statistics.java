package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Statistics extends BaseFrame {

	JComboBox box;
	Color color[] = new Color[7];
	{
		for (int i = 6, pos = 1; i >= 0; i--) {
			if (i == 6) {
				color[i] = Color.BLUE.darker();
				continue;
			}
			int red = color[i + 1].getRed(), blue = color[i + 1].getBlue(), green = color[i + 1].getGreen();
			color[i] = pos == 0 ? new Color(red + 17, green + 17, blue) : new Color(red + 34, green + 34, blue);
			pos = pos == 0 ? 1 : 0;
		}
	}

	public Statistics() {
		super("통계", 800, 750);
		setLayout(new GridLayout(1, 0));
		add(new TimeLine());
		add(new Chart());
		((JPanel) (getContentPane())).setBorder(new EmptyBorder(20, 20, 20, 20));
		setVisible(true);
	}

	class TimeLine extends JPanel {

		JPanel c, c_c, s, s_c, s_s;

		JLabel lbl[][] = new JLabel[24][7];
		String cap[] = "12, ,2, ,4, ,6, ,8, ,10, ,".split(",");
		String week[] = "일,월,화,수,목,금,토".split(",");
		String list[] = "최근 7일,최근 30일,최근 90일".split(",");

		public TimeLine() {
			super(new BorderLayout());
			add(lbl("자주 이용하는 시간대", JLabel.LEFT, 20), "North");
			add(BaseFrame.sz(c = new JPanel(new FlowLayout()), 400, getHeight()));

			c.add(BaseFrame.sz(lbl("시간 별 이용자 수", JLabel.LEFT), 355, getHeight()), "North");
			c.add(c_c = new JPanel(new GridLayout(0, 8, 2, 2)));
			c.add(BaseFrame.sz(s = new JPanel(new BorderLayout(5, 10)), 350, 60), "South");

			for (int i = 0, j = 0, pos = 0; i < lbl.length; i++, j++) {
				for (int k = 0; k < lbl[0].length; k++) {
					lbl[i][k] = new JLabel("", JLabel.CENTER);
					lbl[i][k].setOpaque(true);
					lbl[i][k].setBackground(Color.LIGHT_GRAY);
					lbl[i][k].setForeground(Color.RED);
					c_c.add(BaseFrame.sz(lbl[i][k], 400 / 10, 20));
				}
				if (j == cap.length) {
					j = 0;
					pos = 1;
				}
				c_c.add(lbl(cap[j] + " " + (!cap[j].equals(" ") ? pos == 0 ? "오전" : "오후" : ""), JLabel.LEFT));
			}

			for (int i = 0; i < week.length; i++) {
				c_c.add(lbl(week[i], 0));
			}

			s.add(BaseFrame.sz(s_c = new JPanel(new GridLayout(1, 0, 5, 5)), 1, 30));

			for (int i = 0; i < color.length; i++) {
				JLabel lbl = new JLabel("+" + (i * 5) + "", JLabel.LEFT);
				s_c.add(lbl);
				lbl.setForeground(Color.RED);
				lbl.setOpaque(true);
				lbl.setBackground(color[i]);
			}

			s.add(s_s = new JPanel(new BorderLayout()), "South");
			s_s.add(box = new JComboBox<>());
			s.setOpaque(false);
			s_c.setOpaque(false);
			s_s.setOpaque(false);

//			s_s.setBorder(new LineBorder(Color.BLACK));
//			c.setBorder(new LineBorder(Color.BLACK));

			c.setBackground(Color.WHITE);
			c_c.setBackground(Color.WHITE);

			for (String li : list) {
				box.addItem(li);
			}
			event();

			box.setSelectedIndex(0);
			box.addItemListener(i -> event());

			// c.setBorder(new EmptyBorder(30, 30, 30, 30));
		}

		void event() {
			// ;
			Arrays.stream(lbl).flatMap(a -> Arrays.stream(a)).forEach(a -> a.setText(""));
			Arrays.stream(lbl).flatMap(a -> Arrays.stream(a)).forEach(a -> a.setBackground(Color.LIGHT_GRAY));

			try {
				var rs = stmt.executeQuery(
						"select hour(`time`), sum(if(weekday(`date`) = 6 , 1, 0)) as '일', sum(if(weekday(`date`) = 0 , 1, 0)) as '월', sum(if(weekday(`date`) = 1 , 1, 0)) as '화' , sum(if(weekday(`date`) = 2 , 1, 0)) as '수' , sum(if(weekday(`date`) = 3 , 1, 0)) as '목', sum(if(weekday(`date`) = 4 , 1, 0)) as '금' , sum(if(weekday(`date`) = 4 , 1, 0)) as '토' from purchase where `date` >= now() and `date` <= DATE_ADD(now(), INTERVAL "
								+ box.getSelectedItem().toString().replaceAll("[^0-9]", "")
								+ " DAY) group by `time` order by `time` desc");

				while (rs.next()) {
					for (int x = 0; x < 7; x++) {
						if (rs.getInt(x + 2) == 0)
							continue;
						lbl[rs.getInt(1)][x]
								.setText(BaseFrame.rei(lbl[rs.getInt(1)][x].getText()) + rs.getInt(x + 2) + "");
						lbl[rs.getInt(1)][x].setBackground(color[BaseFrame.rei(lbl[rs.getInt(1)][x].getText()) / 5]);
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		new Statistics();
	}

	@SuppressWarnings("serial")
	class Chart extends JPanel {
		JLabel chartJLabel;
		JPanel c, userJPanel;
		private double total = 0, curV = 0;
		int sarc = 0, arc = 0;
		Color col[] = { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PINK, // 뭔색인지 몰라서 그냥
																									// WHITE박음 ㅋㅋ
				Color.BLACK };

		ArrayList<Integer> list = new ArrayList<>();
		HashMap<Integer, String> map = new HashMap<>();

		public Chart() {
			setLayout(new BorderLayout());
			add(lbl("자주 이용하는 회원", JLabel.LEFT, 20), "North");
			add(c = new JPanel(new GridLayout(0, 1)));

			try {
				var rs = stmt.executeQuery(
						"select u.serial,u.name, count(*) from user u, purchase p where  p.`user` = u.serial group by u.`name` order by count(*) desc limit 0,8");
				while (rs.next()) {
					list.add(rs.getInt(3));
					map.put(rs.getInt(1), rs.getString(2));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setChart();

			c.add(userJPanel = new JPanel(new GridLayout(0, 1)));
//			Collections.reverse(list);
			for (Integer n : map.keySet()) {
				var tmp = new JPanel(new FlowLayout(FlowLayout.CENTER));
				JPanel rect = new JPanel(null);
				rect.setBackground(col[n - 1]);
				tmp.add(BaseFrame.sz(rect, 20, 20));
				tmp.add(BaseFrame.lbl(n + "등 " + map.get(n) + " (" + list.get(7 - n) + "회)", JLabel.LEFT));
				userJPanel.add(tmp);
			}
		}

		void setChart() {

			for (Integer v : list) {
				total += v;
			}

			Collections.reverse(list);

			c.add(chartJLabel = new JLabel() {
				@Override
				public void paint(Graphics g) {

					Graphics2D g2 = (Graphics2D) g;

					for (int i = 0; i < list.size(); i++) {
						sarc = (int) (curV * 360 / total) + 80;
						int arc = (int) (list.get(i) * 360 / total) + 1;

						g2.setColor(col[6 - i]);
						g2.fillArc(60, 20, 250, 250, sarc, arc);
						curV += list.get(i);
					}
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					super.paint(g2);
				}
			});
		}

	}
}
