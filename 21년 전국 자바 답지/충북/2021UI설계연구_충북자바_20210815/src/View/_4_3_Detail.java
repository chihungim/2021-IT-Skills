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
		// B���� ��ġ
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] { 0, 2, 1016, 30 });
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] { 0, 35, 1016, 173 + 39 + 400 });

		data();
		ui();
	}

	private void data() {
		info1 = new String[][] { { "./�����ڷ�/images/album/1.jpg", "Breathing" },
				{ "./�����ڷ�/images/album/2.jpg", "Breathing" }, { "./�����ڷ�/images/album/3.jpg", "Breathing" },
				{ "./�����ڷ�/images/album/4.jpg", "Breathing" }, { "./�����ڷ�/images/album/5.jpg", "Breathing" },
				{ "./�����ڷ�/images/album/6.jpg", "Breathing" }, { "./�����ڷ�/images/album/7.jpg", "Breathing" },
				{ "./�����ڷ�/images/album/8.jpg", "Breathing" }, { "./�����ڷ�/images/album/9.jpg", "Breathing" },
				{ "./�����ڷ�/images/album/10.jpg", "Breathing" }, { "./�����ڷ�/images/album/11.jpg", "Breathing" },
				{ "./�����ڷ�/images/album/12.jpg", "Breathing" }, { "./�����ڷ�/images/album/13.jpg", "Breathing" },
				{ "./�����ڷ�/images/album/14.jpg", "Breathing" }, { "./�����ڷ�/images/album/15.jpg", "Breathing" },
				{ "./�����ڷ�/images/album/16.jpg", "Breathing" }, { "./�����ڷ�/images/album/17.jpg", "Breathing" },
				{ "./�����ڷ�/images/album/18.jpg", "Breathing" }, { "./�����ڷ�/images/album/19.jpg", "Breathing" } };
	}

	private void ui() {
		// panel1 ����
		panel1 = new MyPanel(bpanel.panels.get(0), new FlowLayout(FlowLayout.RIGHT));
		panel1.addLabel("���� 19��", Color.WHITE, "", JLabel.LEFT, Font.PLAIN, 12, new int[] { 0, 0, 1000, 20 });

		// panel2 ����
		panel2 = new MyPanel(bpanel.panels.get(1), new FlowLayout(FlowLayout.LEFT, 10, 10));
		for (int i = 0; i < 19; i++) {
			panel2.addInfoBoxLabel(Color.WHITE, info1[i][0], info1[i][1], Font.PLAIN, 12, new int[] { 170, 170 });
			panel2.alignLabelimgtxt(panel2.lbls.get(i), JLabel.CENTER, JLabel.BOTTOM);
		}

		// ����, ��ũ�� ����, ���� �׵θ�
		panel2.me.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel2.me.setBounds(0, 35, 1016, (200 + 5) * 4 + 30);
		bpanel.me.setPreferredSize(new Dimension(1016, panel2.me.getY() + (200 + 5) * 4 + 30));

		panel2.me.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(1, -1, -1, -1),
				BorderFactory.createDashedBorder(Color.WHITE)));

		Main.mf.repaint();
		Main.mf.revalidate();
	}

	public static void main(String[] args) {
		// �׽�Ʈ
		if (Main.mf == null)
			Main.mf = new MyFrame();
		else {
			Main.mf.panels.clear();
		}

		new _4_3_Detail();
	}
}
