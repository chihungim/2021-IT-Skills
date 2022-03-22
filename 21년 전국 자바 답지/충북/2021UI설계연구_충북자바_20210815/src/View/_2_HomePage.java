package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;

public class _2_HomePage extends MyCommonPage {
	MyPanel panel1, panel2, panel3, panel4;
	MyPanel subpanel;

	String kind[], info1[][], info2[];

	public _2_HomePage() {
		// B���� ��ġ
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] { 0, 2, 1015, 57 });
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] { 0, 61, 1015, 173 });
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] { 0, 235, 1015, 39 });
		bpanel.addPanel(null, Color.DARK_GRAY, new int[] { 1, 276, 1016, 400 });

		data();
		ui();
	}

	private void data() {
		// panel2�� ������
		kind = "�̹��� ��Ʈ ��, �ֱ� �߸ŵ� �ٹ�, ����Ư������ �α� ����".split(",");
		info1 = new String[][] { { "1.jpg", "Answer: Love Myself", "Love Yourself: Answer" },
				{ "2.jpg", "Intro: Persona", "MAP OF THE SOUL: 7" }, { "3.jpg", "Seattle Alone", "�������! �ɱ�� - EP" } };

		// panel4�� ������
		info2 = "��,��ũ,��,��,�Ͽ콺,��Ʈ��,����,R&B,����,Ŭ����,�߶��,����ƽ,K-POP".split(",");
	}

	private void ui() {
		// panel1 ����
		panel1 = new MyPanel(bpanel.panels.get(0), new FlowLayout(FlowLayout.RIGHT));
		panel1.addButton("�α׾ƿ�", Color.WHITE, new int[] { 0, 0, 100, 40 });

		// panel2 ����
		panel2 = new MyPanel(bpanel.panels.get(1), new FlowLayout(FlowLayout.LEFT));
		for (int i = 0; i < 3; i++) {
			panel2.addPanel(null, Color.BLUE, new int[] { 0, 0, 995 / 3, 160 });

			subpanel = new MyPanel(panel2.panels.get(i), new FlowLayout(FlowLayout.LEFT, 5, 10));
			subpanel.addLabel(kind[i], Color.WHITE, "", JLabel.LEFT, Font.BOLD, 15, new int[] { 0, 0, 200, 20 });
			subpanel.addInfoBoxLabel(Color.WHITE, "./�����ڷ�/images/album/" + info1[i][0],
					"<html>" + info1[i][1] + "<br>" + info1[i][2], Font.PLAIN, 12, new int[] { 100, 100 });
			subpanel.alignLabelimgtxt(subpanel.lbls.get(1), JLabel.RIGHT, JLabel.CENTER);
		}

		// panel3 ����
		panel3 = new MyPanel(bpanel.panels.get(2), new FlowLayout(FlowLayout.LEFT));
		panel3.addLabel("ī�װ� �ѷ�����", Color.WHITE, "", JLabel.LEFT, Font.BOLD, 15, new int[] { 0, 0, 200, 20 });

		// panel4 ����
		panel4 = new MyPanel(bpanel.panels.get(3), new FlowLayout(FlowLayout.LEFT));
		for (int i = 0; i < 13; i++) {
			panel4.addLabel(info2[i], Color.WHITE, "./�����ڷ�/images/category/" + info2[i] + ".jpg", JLabel.LEFT,
					Font.BOLD, 15, new int[] { 0, 0, 995 / 3, 120 });
			panel4.alignLabelimgtxt(panel4.lbls.get(i), JLabel.CENTER, JLabel.CENTER);
		}

		// ��ũ���� �� �ֵ��� B���� ���̸� �����Ѵ�.
		panel4.me.setBounds(1, 276, 1016, (120 + 5) * 5 + 30);
		bpanel.me.setPreferredSize(new Dimension(1016, panel4.me.getY() + (120 + 5) * 5 + 30));

		// Ư�� ó��: ����
		panel4.me.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(1, -1, -1, -1),
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

		new _2_HomePage();
	}
}
