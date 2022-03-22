import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ȭ��Ŭ��_��ǥ����_��Ұ��� extends JFrame {
	static ArrayList<String> list = new ArrayList<String>();

	public ȭ��Ŭ��_��ǥ����_��Ұ���() {
		setSize(1600, 1000);
		setLocationRelativeTo(null);

		// ���1
		ImageIcon img = new ImageIcon(new ImageIcon("./metro.png").getImage());

		JPanel jp = new JPanel();
		JLabel jl = new JLabel(img) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(3));
				g2.setColor(Color.BLACK);

				// ��ϵ� �ڽ��� ��� ǥ��
				for (int i = 0; i < list.size(); i++) {
					String p[] = list.get(i).substring(list.get(i).indexOf("{") + 1, list.get(i).indexOf("}"))
							.split(",");
					g.drawRect(Integer.parseInt(p[0]) - 10, Integer.parseInt(p[1]) - 10, 20, 20);
				}
			}
		};
		jp.add(jl);

		JScrollPane jsp = new JScrollPane(jp);
		jsp.setAutoscrolls(true);

		add(jsp);
		setVisible(true);

		// ȭ����ǥ �̺�Ʈ
		jl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				list.add("{" + e.getX() + "," + e.getY() + "}");
				jp.repaint();
				System.out.println("��ǥ���:" + String.join(",", list));
			}
		});

		// ��� ���
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == 'Z' || e.getKeyChar() == 'z') {
					// ������ �ڽ� ��ǥ ����, ��ü �ڽ� ��ǥ ���
					list.remove(list.size() - 1);
					System.out.println("��ǥ���:" + String.join(",", list));

					jp.repaint();
					jp.revalidate();
				}
			}
		});
	}

	public static void main(String[] args) {
		new ȭ��Ŭ��_��ǥ����_��Ұ���();
	}
}
