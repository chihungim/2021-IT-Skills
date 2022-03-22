package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class WriteResume extends BaseFrame {

	int point[][] = { { 16, 13, 150, 180 }, { 216, 36, 250, 136 }, { 16, 200, 546, 93 }, { 139, 350, 80, 30 },
			{ 18, 380, 543, 130 }, { 53, 520, 152, 32 }, { 449, 560, 112, 40 } };
	JPanel p[] = new JPanel[point.length];
	JLabel lblImg;
	String cap[] = "�ּ�,�޴���,�̸���".split(",");
	String info[] = new String[6];
	JRadioButton rbtn[] = { new JRadioButton("����"), new JRadioButton("�����") };
	JLabel type[] = { new JLabel("����", 0), new JLabel("���", 0) };
	JTextField txt[] = new JTextField[4];
	JComboBox[] combo = new JComboBox[3];
	JCheckBox chk[] = { new JCheckBox("�Ƹ�����Ʈ"), new JCheckBox("�����"), new JCheckBox("�İ���"), new JCheckBox("������"),
			new JCheckBox("������") };
	String path;

	public WriteResume() {
		super("�̷¼� �ۼ�", 600, 650);

//		this.add(new MyPanel());
		this.add(c = new JPanel(null));

		for (int i = 0; i < p.length; i++) {
			c.add(p[i] = new JPanel(new FlowLayout(0)));
			p[i].setBounds(point[i][0], point[i][1], point[i][2], point[i][3]);
		}

		p[0].setLayout(new BorderLayout());
		p[0].add(lblImg = new JLabel(img("./�����ڷ�/�̹�������.png", 150, 150)));
		setLine(lblImg);
		p[0].add(btn("�������", a -> {
			JFileChooser jfc = new JFileChooser();
			jfc.setMultiSelectionEnabled(false);
			if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				path = jfc.getSelectedFile().toString();
				lblImg.setIcon(img(path, 200, 200));
			}
		}), "South");

		p[1].setLayout(new GridLayout(0, 1));
		try {
			var rs = stmt.executeQuery(
					"SELECT name, concat(gender, '��'), concat(SUBSTRING_INDEX(birth, '-', 1), '���'), SUBSTRING_INDEX(address, ' ', 1), phone, email FROM user where u_no="
							+ uno);
			if (rs.next()) {
				var flow = new JPanel(new FlowLayout(0));
				for (int i = 0; i < info.length; i++) {
					info[i] = rs.getString(i + 1);
				}
				p[1].add(flow);
				for (int i = 0; i < 3; i++) {
					flow.add(sz(new JLabel(info[i]), 70, 25));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 3; i < info.length; i++) {
			var tmp = new JPanel(new FlowLayout(0));
			p[1].add(tmp);
			tmp.add(sz(new JLabel(cap[i - 3]), 70, 25));
			tmp.add(sz(new JLabel(info[i]), 150, 25));
		}

		p[2].setLayout(new GridLayout(0, 1));
		String p_2[] = "�̷¼� ����,�����з�,��±���".split(",");
		for (int i = 0; i < p_2.length; i++) {
			var tmp = new JPanel(new FlowLayout(0));
			tmp.add(sz(new JLabel(p_2[i]), 80, 25));
			if (i == 0) {
				tmp.add(sz(txt[0] = new JTextField(), 400, 25));
			} else if (i == 1) {
				tmp.add(sz(combo[0] = new JComboBox(), 220, 25));
			} else {
				for (int j = 0; j < type.length; j++) {
					tmp.add(sz(type[j], 110, 25));
					setLine(type[j]);
					type[j].setBackground(Color.white);
					type[j].setOpaque(true);
					type[j].addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							for (int k = 0; k < type.length; k++) {
								type[k].setForeground(Color.black);
								type[k].setBorder(new LineBorder(Color.black));
							}
							((JLabel) e.getSource()).setForeground(Color.blue);
							((JLabel) e.getSource()).setBorder(new LineBorder(Color.blue));
						}
					});
					type[0].setForeground(Color.blue);
					type[0].setBorder(new LineBorder(Color.blue));
				}
			}
			p[2].add(tmp);
		}

		p[3].add(new JLabel("����ٹ�����"));

		p[4].add(sz(new JLabel("����"), 80, 25));
		p[4].add(sz(txt[1] = new JTextField(), 340, 25));
		p[4].add(sz(btn("�ʱ�ȭ", a -> {
			txt[1].setText("");
		}), 100, 25));
		p[4].add(sz(new JLabel("�ٹ�����"), 80, 25));
		for (int i = 0; i < chk.length; i++) {
			p[4].add(sz(chk[i], 85, 25));
		}
		p[4].add(sz(new JLabel("�ٹ��Ⱓ"), 80, 25));
		p[4].add(sz(txt[2] = new JTextField(), 110, 25));
		p[4].add(sz(combo[1] = new JComboBox(), 110, 25));
		p[4].add(sz(new JLabel(), 150, 25));
		p[4].add(sz(new JLabel("�޿�"), 80, 25));
		p[4].add(sz(combo[2] = new JComboBox(), 110, 25));
		p[4].add(sz(txt[3] = new JTextField(), 110, 25));
		p[4].add(new JLabel("��"));
		p[4].add(sz(btn("�޿�����", a -> {
//			new Calc();
		}), 120, 25));

		p[5].add(rbtn[0]);
		p[5].add(rbtn[1]);

		p[6].setLayout(new FlowLayout(2));
		p[6].add(btn("�ۼ��Ϸ�", a -> {
			if (!(txt[0].getText().isEmpty() || txt[1].getText().isEmpty() || txt[2].getText().isEmpty()
					|| txt[3].getText().isEmpty())) {
				eMsg("��ĭ�� �ֽ��ϴ�.");
				return;
			}
			if (!(combo[0].getSelectedIndex() == -1 || combo[1].getSelectedIndex() == -1
					|| combo[2].getSelectedIndex() == -1)) {
				eMsg("��ĭ�� �ֽ��ϴ�.");
				return;
			}
			if (!(chk[0].isSelected() || chk[1].isSelected() || chk[2].isSelected() || chk[3].isSelected()
					|| chk[4].isSelected())) {
				eMsg("��ĭ�� �ֽ��ϴ�.");
				return;
			}
			if (!(rbtn[0].isSelected() || rbtn[1].isSelected())) {
				eMsg("��ĭ�� �ֽ��ϴ�.");
				return;
			}

			iMsg("�ۼ��� �Ϸ�Ǿ����ϴ�.");
			try {
				if (!path.isEmpty()) {
					ImageIO.write(ImageIO.read(new File(path)), "jpg", new File("./�����ڷ�/�̷¼�/" + uno + ".jpg"));
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String dno = getOne("select name from details where name ='" + txt[1].getText() + "'");
			String eno = "";
			for (int i = 0; i < chk.length; i++) {
				if (chk[i].isSelected()) {
					eno = i + 1 + "";
					break;
				}
			}
			String period = txt[2].getText() + combo[1].getSelectedItem();
			int anonymous = (rbtn[0].isSelected()) ? 1 : 0;
			execute("insert into information values(0, '" + uno + "','" + txt[0].getText() + "','"
					+ combo[0].getSelectedItem() + "','" + dno + "','" + eno + "','" + period + "','" + anonymous
					+ "')");

		}));

		String level[] = "�ʵ��б� ����,���б� ����,����б� ����,����б� ����,���б� ����,���б� ����,���п� ����,���п� ����".split(",");
		for (int i = 0; i < level.length; i++) {
			combo[0].addItem(level[i]);
		}

		for (int i = 0; i < combo.length; i++) {
			combo[i].setSelectedIndex(-1);
		}

		txt[1].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Choice(WriteResume.this, txt[1]);
			}
		});

		String combo_1[] = "��,����,��,��,�Ⱓ����".split(",");
		for (int i = 0; i < combo_1.length; i++)
			combo[1].addItem(combo_1[i]);

		combo[1].addItemListener(a -> {
			txt[1].setText("");
			txt[1].setEnabled(combo[1].getSelectedIndex() != 4);
		});

		String combo_2[] = "�ñ�,�ϱ�,�ֱ�,����,����".split(",");
		for (int i = 0; i < combo_2.length; i++) {
			combo[2].addItem(combo_2[i]);
		}

		this.setVisible(true);
	}

	public static void main(String[] args) {
		uno = 1;
		new WriteResume();
	}

}
