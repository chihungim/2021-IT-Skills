package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class Recruitment extends BaseFrame {

	JPanel local = new JPanel(new GridLayout(2, 9));
	String l[] = "����,���,��õ,�λ�,�뱸,����,�泲,����,�泲,����,���,���,����,���,����,����,����,����".split(",");
	JLabel lo[] = new JLabel[l.length];
	{
		for (int i = 0; i < lo.length; i++) {
			lo[i] = lbl(l[i], JLabel.CENTER, 15);
			lo[i].setOpaque(true);
			lo[i].setBackground(Color.WHITE);
			lo[i].setBorder(new LineBorder(Color.BLACK));
			lo[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					var lbl = ((JLabel) e.getSource());

					if (lbl.getBackground().equals(Color.WHITE)) {
						for (int i = 0; i < lo.length; i++) {
							lo[i].setBackground(Color.WHITE);
						}
						lbl.setBackground(Color.YELLOW);
						localtion = lbl.getText();
						if (localtion.equals("����")) {
							localtion = "";
						}
					}

					super.mousePressed(e);
				}
			});
			local.add(lo[i]);
		}

		lo[lo.length - 1].setBackground(Color.YELLOW);
	}

	String localtion = "";
	String term = "";
	String week = "";
	String time = "";
	JTextField txt;
	JCheckBox[][] chk = {
			{ new JCheckBox("1��"), new JCheckBox("1�����̳�"), new JCheckBox("1����~3����"), new JCheckBox("3����~6����"),
					new JCheckBox("6����~1��"), new JCheckBox("1���̻�") },
			{ new JCheckBox("��~��"), new JCheckBox("��~��"), new JCheckBox("�ָ�"), new JCheckBox("��������") },
			{ new JCheckBox("����"), new JCheckBox("����"), new JCheckBox("����"), new JCheckBox("�ð�����") } };
	ButtonGroup bg[] = new ButtonGroup[3];
	JPanel s = new JPanel(new GridLayout(1, 0));

	JPanel[] sel = new JPanel[3];
	{
		String kind[] = "�ٹ��Ⱓ,�ٹ�����,�ٹ��ð�".split(",");
		for (int i = 0; i < sel.length; i++) {
			bg[i] = new ButtonGroup();
			s.add(sel[i] = new JPanel(new BorderLayout()));
			JLabel lbl = lbl(kind[i], 0, 15);
			lbl.setOpaque(true);
			lbl.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
			lbl.setBackground(Color.WHITE);
			sel[i].add(lbl, "North");
			sel[i].setBorder(new LineBorder(Color.BLACK));
			var c = new JPanel(new GridLayout(0, 2));

			sel[i].add(c);
			for (int j = 0; j < chk[i].length; j++) {
				c.add(chk[i][j]);
				bg[i].add(chk[i][j]);
				chk[i][j].setName(kind[i]);
				chk[i][j].addActionListener(a -> {
					var box = ((JCheckBox) a.getSource());

					if (box.isSelected()) {
						if (box.getName().equals("�ٹ��Ⱓ")) {
							term = box.getText();
						} else if (box.getName().equals("�ٹ�����")) {
							week = box.getText();
						} else {
							time = box.getText();
						}
					}
				});
			}
		}
	}
	JButton btn = btn("�˻��ϱ�", a -> search());

	JTextField sfield;

	JPanel sIpanel = new JPanel();

	JPanel sPanel = new JPanel(new BorderLayout());
	{

		var n = new JPanel();
		n.setLayout(new BoxLayout(n, BoxLayout.X_AXIS));
		sIpanel.setLayout(new BoxLayout(sIpanel, BoxLayout.Y_AXIS));
		int height = 50;
		String skind[] = "����̸�,����,�޿�,�޿�����,�����".split(",");
		int w[] = { 150, 380, 200, 130, 130, 130 };

		for (int i = 0; i < skind.length; i++) {
			JLabel lbl = lbl(skind[i], JLabel.CENTER, 15);
			lbl.setOpaque(true);
			lbl.setBackground(Color.WHITE);
			lbl.setBorder(new LineBorder(Color.BLACK));
			lbl.setMaximumSize(new Dimension(w[i], height));
			n.add(lbl);
		}

		sPanel.add(n, "North");
		sPanel.add(sIpanel);
	}

	JComponent jc[] = { lbl("ä�� ����", 0, 20), btn, lbl("����", JLabel.CENTER), local, lbl("����", JLabel.CENTER),
			sfield = new JTextField(), s, new JScrollPane(sPanel) };

	{
		sfield.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				new Choice(Recruitment.this, sfield);
				super.mousePressed(e);
			}
		});
	}

	int xywh[][] = { { 405, 11, 146, 39 }, { 766, 27, 90, 30 }, { 27, 88, 75, 27 }, { 141, 84, 708, 74 },
			{ 27, 171, 78, 28 }, { 142, 169, 709, 30 }, { 143, 211, 708, 139 }, { 19, 365, 950, 239 } };

	public Recruitment() {
		super("ä������", 1000, 650);
		setUI();
		setVisible(true);
	}

	private void setUI() {
		setLayout(null);
		for (int i = 0; i < jc.length; i++) {
			add(jc[i]);
			jc[i].setBounds(xywh[i][0], xywh[i][1], xywh[i][2], xywh[i][3]);
		}
		search();

	}

	void search() {
		sIpanel.removeAll();
		try {
			int cnt = 0;

			String lq = localtion.equals("") ? "" : " and left(c.address,2) = '" + localtion + "' ";

			String pq = "";
			String wq = "";
			String tq = "";
			String dq = "";

			if (!sfield.getText().isEmpty()) {
				dq = " and d.name like '%" + sfield.getText() + "%'";
			}

			if (!term.equals(""))
				pq = term.equals("1��") ? " and r.period = '1��'"
						: term.equals("1���� �̳�") ? " and r.period regexp '^[1234567]��$'"
								: term.equals("1����~3����") ? " and r.period regexp '^[123]����$'"
										: term.equals("3����~6����") ? " and r.period regexp '^[3456]����$'"
												: term.equals("6����~1��")
														? " and r.period regexp '.*((6|7|8|9|10|11|12)����|1��).*'"
														: " and r.period regexp '.*��.*'";
			if (!week.equals(""))
				wq = week.equals("��~��") ? " and r.week regexp '.*[^��������].*' and not (r.week regexp '.*[��|��].*'"
						: week.equals("��~��")
								? " and not (r.week regexp '.*��������.*') and r.week regexp '.*[��ȭ�����].*' and r.week regexp '.*[����].*'"
								: week.equals("�ָ�") ? "select * from recruitment r where r.week = '��, ��'"
										: " and r.week = '��������'";
			if (!time.equals(""))
				tq = time.equals("����") ? " and hour(right(r.time, 5)) < 12"
						: time.equals("����") ? " and hour(left(r.time, 5)) >= 12"
								: time.equals("����") ? " and hour(left(r.time, 5)) < 12 and hour(right(r.time, 5)) >= 12"
										: " and r.time='�ð�����'";

			String sql = "SELECT c.c_no, c.name, r.title, r.time, r.standard, r.date FROM recruitment r, company c, details d where r.c_no=c.c_no and c.d_no = d.d_no"
					+ tq + pq + wq + lq + dq + " order by c.c_no";

			System.out.println(sql);
			var rs = stmt.executeQuery(sql);
			while (rs.next()) {

				cnt++;
				sIpanel.add(new SearchPanel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6)));
			}
			if (cnt == 0) {
				eMsg("������ �����ϴ�.");
			} else {
				iMsg(cnt + "���� ������ �ֽ��ϴ�.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		repaint();
		revalidate();

	}

	class SearchPanel extends JPanel {

		JLabel lblKind;
		int widths[] = { 50, 100, 380, 130, 130, 130 };

		public SearchPanel(String cno, String name, String title, String time, String standard, String date) {
			String path = isPath(name);

			this.setLayout(new FlowLayout(0));
			this.setPreferredSize(new Dimension(1, 50));

			this.add(sz(new JLabel(img(path, 50, 40)), 50, 40));
			this.add(sz(new JLabel("<html><center>" + name, 0), 150, 25));

			this.add(sz(new JLabel(title), 310, 25));
			this.add(sz(new JLabel(time, 0), 130, 25));

			JLabel lbl = new JLabel(standard, 0);
			Color col = (standard.contentEquals("�ñ�")) ? Color.green
					: (standard.contentEquals("�ϱ�")) ? Color.orange
							: (standard.contentEquals("����")) ? Color.cyan : Color.pink;
			lbl.setBackground(col);
			lbl.setOpaque(true);
			new Thread(() -> {
				while (true) {
					lbl.setForeground(Color.white);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					lbl.setForeground(Color.black);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					repaint();
					revalidate();
				}

			}).start();
			this.add(sz(lbl, 100, 25));

			this.add(sz(new JLabel(date, 0), 130, 25));

			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (LocalDate.now().isAfter(LocalDate.parse(date))) {
						eMsg("�������� ���� ä������Դϴ�.");
						return;
					} else {
						new Inform(cno);
					}
				}
			});
			this.setBorder(new CompoundBorder(new LineBorder(Color.black), new EmptyBorder(0, 0, 10, 0)));

		}

	}

	public static void main(String[] args) {
		new Recruitment();
	}
}
