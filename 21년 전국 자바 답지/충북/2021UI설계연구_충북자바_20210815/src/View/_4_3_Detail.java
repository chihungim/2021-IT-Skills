package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class _4_3_Detail extends MyCommonPage {
	MyPanel panel1, panel2;
	String info1[][];

	public _4_3_Detail() {
		// B구역 배치
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] { 0, 2, 1016, 30 });
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] { 0, 35, 1016, 173 + 39 + 400 });

		data();
		ui();
	}

	private void data() {
		info1 = new String[][] { { "./지급자료/images/album/1.jpg", "Breathing" },
				{ "./지급자료/images/album/2.jpg", "Breathing" }, { "./지급자료/images/album/3.jpg", "Breathing" },
				{ "./지급자료/images/album/4.jpg", "Breathing" }, { "./지급자료/images/album/5.jpg", "Breathing" },
				{ "./지급자료/images/album/6.jpg", "Breathing" }, { "./지급자료/images/album/7.jpg", "Breathing" },
				{ "./지급자료/images/album/8.jpg", "Breathing" }, { "./지급자료/images/album/9.jpg", "Breathing" },
				{ "./지급자료/images/album/10.jpg", "Breathing" }, { "./지급자료/images/album/11.jpg", "Breathing" },
				{ "./지급자료/images/album/12.jpg", "Breathing" }, { "./지급자료/images/album/13.jpg", "Breathing" },
				{ "./지급자료/images/album/14.jpg", "Breathing" }, { "./지급자료/images/album/15.jpg", "Breathing" },
				{ "./지급자료/images/album/16.jpg", "Breathing" }, { "./지급자료/images/album/17.jpg", "Breathing" },
				{ "./지급자료/images/album/18.jpg", "Breathing" }, { "./지급자료/images/album/19.jpg", "Breathing" } };
	}

	private void ui() {
		// panel1 생성
		panel1 = new MyPanel(bpanel.panels.get(0), new FlowLayout(FlowLayout.RIGHT));
		panel1.addLabel("음악 19건", Color.WHITE, "", JLabel.LEFT, Font.PLAIN, 12, new int[] { 0, 0, 1000, 20 });

		// panel2 생성
		panel2 = new MyPanel(bpanel.panels.get(1), new FlowLayout(FlowLayout.LEFT, 10, 10));
		for (int i = 0; i < 19; i++) {
			panel2.addInfoBoxLabel(Color.WHITE, info1[i][0], info1[i][1], Font.PLAIN, 12, new int[] { 170, 170 });
			panel2.alignLabelimgtxt(panel2.lbls.get(i), JLabel.CENTER, JLabel.BOTTOM);
		}

		// 여백, 스크롤 높이, 점선 테두리
		panel2.me.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel2.me.setBounds(0, 35, 1016, (200 + 5) * 4 + 30);
		bpanel.me.setPreferredSize(new Dimension(1016, panel2.me.getY() + (200 + 5) * 4 + 30));

		panel2.me.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(1, -1, -1, -1),
				BorderFactory.createDashedBorder(Color.WHITE)));

		Main.mf.repaint();
		Main.mf.revalidate();
	}

	public static void main(String[] args) {
		// 테스트
		if (Main.mf == null)
			Main.mf = new MyFrame();
		else {
			Main.mf.panels.clear();
		}

		new _4_3_Detail();
	}
}
