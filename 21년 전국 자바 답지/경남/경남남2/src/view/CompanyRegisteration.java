package view;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class CompanyRegisteration extends BaseFrame {
	int xywh[][] = { { 321, 21, 140, 134 }, { 13, 23, 90, 30 }, { 108, 22, 181, 31 }, { 14, 62, 83, 27 },
			{ 109, 61, 180, 28 }, { 15, 97, 83, 30 }, { 110, 97, 180, 31 }, { 15, 136, 83, 29 }, { 109, 136, 180, 30 },
			{ 17, 172, 81, 31 }, { 109, 173, 180, 28 }, { 15, 212, 84, 29 }, { 106, 211, 185, 30 }, { 17, 249, 81, 24 },
			{ 106, 249, 184, 24 }, { 16, 284, 84, 23 }, { 110, 285, 91, 21 }, { 210, 283, 202, 22 },
			{ 360, 326, 109, 24 }, { 6, 8, 471, 304 } };

	JLabel img = imglbl("./�����ڷ�/�̹�������.png", 140, 134);
	JTextField txt[] = { new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(),
			new JTextField(), new JTextField(), new JTextField() };
	String fpath;
	JButton btn = new JButton("����");

	JComboBox local = new JComboBox<>();
	{
		for (String str : "����,���,��õ,�λ�,�뱸,����,�泲,����,�泲,����,���,���,����,���,����,����,����,����".split(",")) {
			local.addItem(str);
		}
	}

	JPanel borderP = new JPanel();
	{
		borderP.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "��� ���"));
	}

	JComponent[] jc = { img, lbl("����ڵ�Ϲ�ȣ", JLabel.LEFT), txt[0], lbl("�����", JLabel.LEFT), txt[1],
			lbl("��ǥ��", JLabel.LEFT), txt[2], lbl("����", JLabel.LEFT), txt[3], lbl("�������", JLabel.LEFT), txt[4],
			lbl("�������", JLabel.LEFT), txt[5], lbl("����ó", JLabel.LEFT), txt[6], lbl("�����ġ", JLabel.LEFT), local, txt[7],
			btn, borderP };

	public CompanyRegisteration() {
		super("���", 500, 400);

		setUI();
		setVisible(true);
	}

	void setUI() {
		setLayout(null);
		for (int i = 0; i < xywh.length; i++) {
			add(jc[i]);
			jc[i].setBounds(xywh[i][0], xywh[i][1], xywh[i][2], xywh[i][3]);
		}

		txt[3].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Choice(CompanyRegisteration.this, txt[3]);
			}
		});

		img.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FileDialog fd = new FileDialog(CompanyRegisteration.this);
				fd.setVisible(true);
				fpath = fd.getDirectory() + fd.getFile();
				if (new File(fpath).exists())
					img.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(fpath).getScaledInstance(140, 134,
							Image.SCALE_SMOOTH)));
				super.mouseClicked(e);
			}
		});

		img.setBorder(new LineBorder(Color.BLACK));

		txt[0].addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!isNumeric(e.getKeyChar())) {
					e.consume();
				}
			}
		});

		txt[6].addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!isNumeric(e.getKeyChar())) {
					e.consume();
				}
			}
		});

		btn.addActionListener(a -> {
			for (int i = 0; i < txt.length; i++) {
				if (txt[i].getText().isEmpty()) {
					eMsg("��ĭ");
					return;
				}
			}

			if (txt[0].getText().length() != 10) {
				eMsg("����ڵ�Ϲ�ȣ�� 10�ڸ����� �մϴ�.");
				return;
			}

			if (checkIsExists("select * from company where number ='" + txt[0].getText() + "'")) {
				eMsg("�̹� �ִ� ����� ��Ϲ�ȣ�Դϴ�.");
				return;
			}
			// ????

			if (txt[6].getText().length() != 11) {
				eMsg("����ó�� 11�ڸ��� �Է����ּ���.");
				return;
			}

			new ManagerSign(this).addWindowListener(new Before(this));
		});
	}

	public static void main(String[] args) {
		new CompanyRegisteration();
	}
}
