package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import db.DBManager;

public class Recruitment extends BaseFrame implements ActionListener {

	String cap[] = "����,����".split(",");
	JPanel search;
	JTextField txt = new JTextField();
	JLabel loc[] = new JLabel[18];
	String locate;
	JCheckBox[][] chk = {
			{ new JCheckBox("1��"), new JCheckBox("1�����̳�"), new JCheckBox("1����~3����"), new JCheckBox("3����~6����"),
					new JCheckBox("6����~1��"), new JCheckBox("1���̻�") },
			{ new JCheckBox("��~��"), new JCheckBox("��~��"), new JCheckBox("�ָ�"), new JCheckBox("��������") },
			{ new JCheckBox("����"), new JCheckBox("����"), new JCheckBox("����"), new JCheckBox("�ð�����") }, };
	int widths[] = { 200, 330, 130, 130, 130 };
	String period, week, time;

	public Recruitment() {
		super("ä������", 1000, 650);

		this.add(n = new JPanel(new BorderLayout()), "North");
		this.add(c = new JPanel(new GridLayout(0, 1)));
		this.add(s = new JPanel(new BorderLayout()), "South");

		var n_e = new JPanel();

		n.add(lbl("ä�� ����", 0, 25));
		n.add(n_e, "East");
		n_e.add(sz(btn("�˻��ϱ�", a -> {

		}), 120, 25));

		JPanel bord[] = new JPanel[2];
		for (int i = 0; i < bord.length; i++) {
			c.add(bord[i] = new JPanel(new BorderLayout()));
			setEmpty(bord[i], 10, 0, 10, 0);
		}

		bord[0].add(sz(new JLabel("����", 0), 50, 25), "West");
		var c_c = new JPanel(new GridLayout(0, 9, 0, 0));
		bord[0].add(c_c);
		String addr[] = "����,���,��õ,�λ�,�뱸,����,�泲,����,�泲,����,���,���,����,���,����,����,����,����".split(",");
		for (int j = 0; j < addr.length; j++) {
			c_c.add(loc[j] = new JLabel(addr[j], 0));
			loc[j].setBackground(Color.white);
			loc[j].setOpaque(true);
			loc[j].setBorder(new LineBorder(Color.gray));
			loc[j].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					for (int k = 0; k < loc.length; k++) {
						loc[k].setBackground(Color.white);
					}
					locate = ((JLabel) e.getSource()).getText();
					((JLabel) e.getSource()).setBackground(Color.yellow);
				}
			});
		}
		locate = "����";
		loc[loc.length - 1].setBackground(Color.yellow);

		bord[1].add(sz(new JLabel("����", 0), 50, 25), "West");
		var c_bord = new JPanel(new BorderLayout(10, 10));
		bord[1].add(c_bord);
		c_bord.add(txt);

		var c_grid = new JPanel(new GridLayout(0, 3, 0, 0));
		c_bord.add(c_grid, "South");

		JPanel chkP[] = new JPanel[3];
		String kind[] = "�ٹ��Ⱓ,�ٹ�����,�ٹ��ð�".split(",");
		for (int l = 0; l < chkP.length; l++) {
			c_grid.add(chkP[l] = new JPanel(new BorderLayout()));
			var tmp = new JPanel();
			tmp.setBackground(Color.white);
			tmp.add(new JLabel(kind[l], 0));
			setLine(tmp);
			chkP[l].add(tmp, "North");

			var grid = new JPanel(new GridLayout(0, 2));
			chkP[l].add(grid);
			setLine(grid);

			ButtonGroup bg = new ButtonGroup();
			for (int i = 0; i < chk[l].length; i++) {
				bg.add(chk[l][i]);
				grid.add(chk[l][i]);
				chk[l][i].addActionListener(this);
			}

		}

		String skind[] = "����̸�,����,�޿�,�޿�����,�����".split(",");
		var s_n = new JPanel(new FlowLayout(0, 0, 0));
		s.add(s_n, "North");

		for (int i = 0; i < skind.length; i++) {
			JLabel jl;
			s_n.add(jl = sz(new JLabel(skind[i], 0), widths[i], 30));
			jl.setOpaque(true);
			setLine(jl);
			jl.setBackground(Color.white);
		}
		JScrollPane scr;
		s.add(scr = new JScrollPane(search = new JPanel(new GridLayout(0, 1))));

		search();

		sz(s, 1, 250);
		setEmpty(s, 0, 30, 0, 30);
		setEmpty(c, 0, 100, 0, 100);
		setEmpty(n, 0, 100, 0, 100);
		this.setVisible(true);
	}

	void search() {
		search.removeAll();
		System.out.println(period+", "+week+", "+time);
		try {
			int cnt = 0;
			String sql = "SELECT c.c_no, c.name, r.title, r.time, r.standard, r.date FROM recruitment r, company c where r.c_no=c.c_no order by c.c_no";
			var rs = DBManager.rs(sql);
			while (rs.next()) {
				cnt++;
				search.add(new SearchPanel(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
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

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
