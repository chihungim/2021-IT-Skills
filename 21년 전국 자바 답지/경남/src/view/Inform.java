package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Inform extends BaseFrame {

	JTabbedPane tab = new JTabbedPane();
	String benefit[] = "���ο���,��뺸��,���纸��,�ǰ�����,���غ���,��������,���п���,���⺸�ʽ�,����������,���ټ�������,�μ�Ƽ����,�ް�,�����ٹ���,�ð�������,�����ٹ���,��������������,��ٹ��� ����,�߰����������,�����,��������,��ħ�Ļ� ����,���ɽĻ� ����,����Ļ� ����,��������,�ǰ�����,���������,�ݿ�����"
			.split(",");
	int xywh1[][] = { { 13, 30, 91, 30 }, { 100, 30, 107, 31 }, { 350, 30, 79, 25 }, { 450, 30, 97, 24 },
			{ 550, 20, 100, 40 }, { 14, 80, 89, 29 }, { 100, 80, 197, 32 }, { 350, 80, 85, 29 }, { 450, 80, 145, 31 },
			{ 13, 120, 91, 34 }, { 100, 120, 198, 33 }, { 350, 120, 93, 28 }, { 450, 120, 163, 29 },
			{ 13, 190, 91, 35 }, { 100, 190, 202, 35 }, { 350, 190, 90, 36 }, { 450, 190, 166, 35 },
			{ 13, 240, 91, 31 }, { 100, 240, 205, 30 }, { 350, 240, 100, 29 }, { 450, 240, 171, 31 } };
	int xywh2[][] = { { 16, 26, 87, 24 }, { 16, 61, 113, 31 }, { 142, 61, 176, 32 }, { 16, 107, 113, 30 },
			{ 143, 106, 174, 32 }, { 16, 151, 113, 28 }, { 142, 150, 175, 29 }, { 15, 192, 111, 30 },
			{ 142, 191, 175, 31 }, { 423, 21, 204, 135 } };
	JPanel chart;
	JLabel lbl1[] = new JLabel[10];
	JLabel lbl2[] = new JLabel[4];
	String cno;
	JLabel img;
	String info[] = new String[10];
	JComponent jc1[] = { lbl("��ȸ��", JLabel.LEFT), lbl1[0] = lbl("", JLabel.LEFT), lbl("�޿�", JLabel.LEFT),
			lbl1[1] = lbl("", JLabel.LEFT), btn("�޿� ����", a -> {
			}), lbl("����", JLabel.LEFT), lbl1[2] = lbl("", JLabel.LEFT), lbl("�ٹ��Ⱓ", JLabel.LEFT),
			lbl1[3] = lbl("", JLabel.LEFT), lbl("����", JLabel.LEFT), lbl1[4] = lbl("", JLabel.LEFT),
			lbl("�ٹ�����", JLabel.LEFT), lbl1[5] = lbl("", JLabel.LEFT), lbl("��������", JLabel.LEFT),
			lbl1[6] = lbl("", JLabel.LEFT), lbl("�ٹ��ð�", JLabel.LEFT), lbl1[7] = lbl("", JLabel.LEFT),
			lbl("������", JLabel.LEFT), lbl1[8] = lbl("", JLabel.LEFT), lbl("�����Ļ�", JLabel.LEFT),
			lbl1[9] = lbl("", JLabel.LEFT) };

	JComponent jc2[] = { lbl("ä���� ����", JLabel.LEFT), lbl("����̸�", JLabel.LEFT), lbl2[0] = lbl("", JLabel.LEFT),
			lbl("��ǥ��", JLabel.LEFT), lbl2[1] = lbl("", JLabel.LEFT), lbl("ȸ���ּ�", JLabel.LEFT),
			lbl2[2] = lbl("", JLabel.LEFT), lbl("�������", JLabel.LEFT), lbl2[3] = lbl("", JLabel.LEFT),
			img = imglbl("./�����ڷ�/�̹�������.png", 204, 135) };
	{
		img.setBorder(new LineBorder(Color.BLACK));
	}
	JPanel tab1, tab2;

	public Inform(String cno) {
		super("������", 700, 500);
		this.cno = cno;

		setUI();
		setData();

		setVisible(true);
	}

	void setUI() {
		add(n = new JPanel(new BorderLayout()), "North");
		n.add(lbl(getOne("select title from recruitment where c_no=" + cno), JLabel.CENTER, 25));
		n.add(btn("�����ϱ�", a -> {

		}), "East");
		add(tab);

		tab.addTab("����, �ٹ�����", tab1 = new JPanel(null));
		tab.addTab("�������", new JScrollPane(tab2 = new JPanel(null)));

		for (int i = 0; i < jc1.length; i++) {
			tab1.add(jc1[i]);
			jc1[i].setBounds(xywh1[i][0], xywh1[i][1], xywh1[i][2], xywh1[i][3]);
		}

		for (int i = 0; i < jc2.length; i++) {
			tab2.add(jc2[i]);
			jc2[i].setBounds(xywh2[i][0], xywh2[i][1], xywh2[i][2], xywh2[i][3]);
		}

		sz(tab2, 1, 800);

		((JPanel) (getContentPane())).setBorder(new EmptyBorder(5, 5, 5, 5));
	}

	void setData() {
		try {
			var rs = stmt.executeQuery(
					"SELECT r.hits, concat(r.standard, ' ', r.salary, '��'), r.title, r.period, d.name, r.week, e.name, r.time, r.deadline, r.benefit FROM recruitment r, details d, employment e where r.d_no=d.d_no and r.e_no=e.e_no and c_no="
							+ cno);
			if (rs.next()) {
				for (int i = 0; i < info.length; i++) {
					info[i] = rs.getString(i + 1);
				}
				String eno[] = info[9].split(",");
				info[9] = "";
				for (int i = 0; i < eno.length; i++) {
					eno[i] = benefit[rei(eno[i]) - 1];
				}
				info[9] = String.join(",", eno);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			var rs = stmt
					.executeQuery("select c_no, name, entrepreneur, address, detail from company where c_no=" + cno);
			while (rs.next()) {
				for (int i = 0; i < 4; i++) {
					lbl2[i].setText(rs.getString(i + 2));
				}

				System.out.println(rs.getString(2));

				if (new File("./�����ڷ�/�귣��/" + rs.getString(2) + ".jpg").exists()) {

					img.setIcon(
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("./�����ڷ�/�귣��/" + rs.getString(2) + ".jpg")
									.getScaledInstance(200, 135, Image.SCALE_SMOOTH)));
				}
				rs.getString(2);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < info.length; i++) {
			lbl1[i].setText(info[i]);
		}

	}

	void setChart() {

	}

}
