package ����;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class chart extends Baseframe {
	BufferedImage img, img2;
	JLabel imgLbl[] = new JLabel[10];
	String names[] = "������,��⵵,��󳲵�,���ϵ�,���󳲵�,����ϵ�,���ֵ�,��û����,��û�ϵ�".split(",");
	int point[][] = { { 161, 0 }, { 45, 49 }, { 230, 435 }, { 263, 227 }, { 0, 506 }, { 79, 396 }, { 88, 768 },
			{ 37, 243 }, { 189, 218 } };
	Color col[] = { Color.red, Color.yellow, Color.green, Color.blue, Color.cyan, Color.magenta, Color.black,
			Color.orange };
	JPanel n, c, w;

	public chart() {
		super("������ ������Ȳ", 1250, 1000);
		setLayout(new BorderLayout());

		add(n = new JPanel(new GridLayout(0, 1)), "North");
		add(w = new JPanel(null), "West");
		add(c = new JPanel(new BorderLayout()));

		n.add(label("������ ���� ��Ȳ", 0, 30));
		n.add(label("C H A R T", 0, 20));
		size(n, 1, 70);
		size(w, 500, 500);

		try {
			for (int i = 0; i < names.length; i++) {
				img = ImageIO.read(new File("./Datafiles/����/" + names[i] + ".png"));
				imgLbl[i] = new JLabel(
						new ImageIcon(img.getScaledInstance(img.getWidth(), img.getHeight(), Image.SCALE_DEFAULT)));
				imgLbl[i].setName(names[i]);
				imgLbl[i].setBorder(new LineBorder(Color.BLACK));
				w.add(imgLbl[i]);
				imgLbl[i].setBounds(point[i][0], point[i][1], img.getWidth(), img.getHeight());
				
				int j = i;
				
				imgLbl[i].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						colorChange(j);
						
						String cad = ((JLabel) e.getSource()).getName();
						switch (cad) {
						case "������":
							cad = "����";
							break;
						case "��⵵":
							cad = "���";
							break;
						case "��󳲵�":
							cad = "�泲";
							break;
						case "���ϵ�":
							cad = "���";
							break;
						case "���󳲵�":
							cad = "����";
							break;
						case "����ϵ�":
							cad = "����";
							break;
						case "���ֵ�":
							cad = "����";
							break;
						case "��û����":
							cad = "�泲";
							break;
						case "��û�ϵ�":
							cad = "���";
							break;
						}
						c.removeAll();
						cp chart = new cp();
						try {
							ResultSet rs = stmt.executeQuery(
									"select substring_index(c.c_name, ' ', 1) as name, count(*) from cafe c, reservation r where c.c_no = r.c_no and c.c_address like '%"
											+ cad + "%' group by name order by name");
							while (rs.next()) {
								chart.hash.put(rs.getString(1), rs.getInt(2));
								chart.loc.add(rs.getString(1));
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						c.add(chart);
						chart.drawChart();
						repaint();
						revalidate();
					}

				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		setVisible(true);
	}

	private void colorChange(int i) {

		for (int j = 0; j < names.length; j++) {
			try {
				img = ImageIO.read(new File("./Datafiles/����/" + names[j] + ".png"));
				img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (int x = 0; x < img.getWidth(); x++) {
				for (int y = 0; y < img.getHeight(); y++) {
					Color color = new Color(img.getRGB(x, y));
					int r = color.getRed();
					int g = color.getGreen();
					int b = color.getBlue();
					if (color.getRGB() != -16777216) {
						if (j == i)
							img2.setRGB(x, y, new Color(r, 0, 0).getRGB());
						else
							img2.setRGB(x, y, new Color(r, g, b).getRGB());
					}
				}
			}

			imgLbl[j].setIcon(
					new ImageIcon(img2.getScaledInstance(img2.getWidth(), img2.getHeight(), Image.SCALE_DEFAULT)));
		}

		repaint();
		revalidate();
	}

	class cp extends JPanel {
		HashMap<String, Integer> hash = new HashMap<String, Integer>();
		ArrayList<Integer> list;
		ArrayList<Color> col;
		ArrayList<String> loc = new ArrayList<String>();
		Random r = new Random();

		void drawChart() {
			list = new ArrayList<Integer>();
			col = new ArrayList<Color>();
			int sum = 0;

			for (var h : hash.keySet()) {
				sum += hash.get(h);
			}
			for (var h : hash.keySet()) {
				list.add((int) Math.round((double) hash.get(h) / (double) sum * 360));
			}

			for (int i = 0; i < hash.size(); i++) {
				col.add(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			}

			repaint();
			revalidate();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			int sArc = 0, k = 0;

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			for (var h : hash.keySet()) {
				g2.setColor(col.get(k));
				g2.setColor(Color.BLACK);
				k++;
			}

			for (int i = 0; i < list.size(); i++) {
				g2.setColor(col.get(i));
				g2.fillArc(100, 100, 500, 500, sArc, list.get(i));
				sArc += list.get(i);

				g2.fillRect(620, 150 + (i * 30), 10, 10);
				g2.setColor(Color.BLACK);
				g2.drawString(loc.get(i), 640, 160 + (i * 30));
			}
		}
	}

	public static void main(String[] args) {
		new chart();
	}

}
