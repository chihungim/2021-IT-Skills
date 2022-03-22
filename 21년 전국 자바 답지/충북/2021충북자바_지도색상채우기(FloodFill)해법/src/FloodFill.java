import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FloodFill extends JFrame {
	BufferedImage img;
	JLabel imgLbl;
	int[][] visit;
	Queue<Point> que = new LinkedList<Point>();
	Point cPos = new Point();

	public FloodFill() {
		super("Flood Fill");
		this.setSize(1200, 900);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(2);

		// 이미지
		repaint();
		try {
			img = ImageIO.read(new File("map.png"));

			visit = new int[img.getHeight()][img.getWidth()];

			imgLbl = new JLabel();

			new Thread(() -> {
				floodFill();
			}).start();

			imgLbl.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println(e.getX() - img.getWidth() + "," + (e.getY() - img.getHeight()));
					super.mouseClicked(e);
				}
			});
		} catch (IOException e1) {
		}

		add(imgLbl);
		this.setVisible(true);
	}

	private void floodFill() { // BFS
		int sx = 600, sy = 720;
		que.add(new Point(sx, sy));
		visit[sy][sx] = 1;

		while (!que.isEmpty()) {
			cPos = que.poll();

			int x = cPos.x, y = cPos.y;
			img.setRGB(x, y, Color.RED.getRGB());
			// 우하좌상 연결 좌표 추가
			for (int i = 1; i <= 5; i++) {
				if (visit[y][x + i] != 1 && img.getRGB(x + i, y) == -8) {
					que.add(new Point(x + i, y));
					visit[y][x + i] = 1;
				}
				if (visit[y + i][x] != 1 && img.getRGB(x, y + i) == -8) {
					que.add(new Point(x, y + i));
					visit[y + i][x] = 1;
				}
				if (visit[y][x - i] != 1 && img.getRGB(x - i, y) == -8) {
					que.add(new Point(x - i, y));
					visit[y][x - i] = 1;
				}
				if (visit[y - i][x] != 1 && img.getRGB(x, y - i) == -8) {
					que.add(new Point(x, y - i));
					visit[y - i][x] = 1;
				}
			}

			imgLbl.setIcon(new ImageIcon(img.getScaledInstance(600, 700, Image.SCALE_DEFAULT)));

		}
	}

	public static void main(String[] args) {
		new FloodFill();
	}
}
