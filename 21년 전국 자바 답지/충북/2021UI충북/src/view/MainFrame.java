package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class MainFrame {
	public static MyFrame bf = new MyFrame();
	public MyPanel aPanel, bPanel, cPanel;
	public static JScrollPane scr;

	// a구역 b구역 c구역
	// {1,3,278,600},{1,605,1280,154},{281,2,1000,601}
	public MainFrame() {
		bf.setLayout(null);
		bf.setVisible(true);
		// A
		aPanel = new MyPanel(bf.addPanel(new EmptyBorder(10, 20, 10, 20), Color.DARK_GRAY, "./지급자료/images/side.png",
				new int[] { 1, 3, 278, 600 }), new GridLayout(20, 1, 0, 5));

		String titles[] = "MENU,홈,검색하기,,LIBRARY,좋아요,재생기록,,PLAYLIST,재생목록 추가,방탄소년단 모음,K팝,내가 듣고 싶은거,플레이리스트 1".split(",");
		int styles[] = { Font.BOLD, Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.BOLD, Font.PLAIN, Font.PLAIN, Font.PLAIN,
				Font.BOLD, Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.PLAIN, Font.PLAIN };
		int sizes[] = { 15, 12, 12, 12, 15, 12, 12, 12, 15, 12, 12, 12, 12, 12 };

		for (int i = 0; i < titles.length; i++) {
			aPanel.addLabel(titles[i], Color.WHITE, "", JLabel.LEFT, styles[i], sizes[i], new int[] { 0, 0, 0, 0 });
		}

		// B
		bPanel = new MyPanel(new JPanel(null), null);
		bPanel.me.setBackground(Color.black);
		scr = bf.addScrollPane(bPanel.me, new int[] { 281, 2, 1000, 601 });
		SwingUtilities.invokeLater(() -> {
			scr.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
				protected JButton createDecreaseButton(int orientation) {
					super.decrButton = EmptyButton();
					return super.decrButton;
				}

				@Override
				protected JButton createIncreaseButton(int orientation) {
					super.incrButton = EmptyButton();
					return super.incrButton;
				}

				private JButton EmptyButton() {
					JButton btn = new JButton();
					btn.setPreferredSize(new Dimension(0, 0));
					btn.setMinimumSize(new Dimension(0, 0));
					btn.setMaximumSize(new Dimension(0, 0));
					return btn;
				}

			});
		});

//		scr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		// {10,36,108,105},{225,91,50,44},{593,82,53,47},{952,80,48,46},{1187,31,86,100},{552,40,141,26}
		// img << p >> [] t
		// C
		cPanel = new MyPanel(bf.addPanel(new EmptyBorder(0, 0, 0, 0), Color.BLACK, "", new int[] { 1, 605, 1280, 154 }),
				null);

		var img = cPanel.addLabel("", Color.WHITE, "", 0, 0, 12, new int[] { 10, 14, 108, 105 });
		cPanel.addLabel("", Color.RED, "./지급자료/images/prev.png", 0, 0, 12, new int[] { 225, 91, 50, 44 });
		cPanel.addLabel("", null, "./지급자료/images/play.png", 0, 0, 0, new int[] { 593, 82, 53, 49 });
		cPanel.addLabel("", null, "./지급자료/images/next.png", 0, 0, 0, new int[] { 952, 80, 48, 46 });
		cPanel.addLabel("", null, "./지급자료/images/queue.png", 0, 0, 0, new int[] { 1187, 31, 86, 100 });
		cPanel.addLabel("재생중이 아님", Color.WHITE, "", JLabel.CENTER, 0, 12, new int[] { 552, 40, 141, 26 });

		img.setBorder(new LineBorder(Color.WHITE));

		bf.revalidate();

		bf.repaint();
	}

	public static void main(String[] args) {
//		new MainFrame();
	}

}
