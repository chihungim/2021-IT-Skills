package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class Search extends BaseFrame {
	JMenuBar bar;

	JMenu menu;
	JTextField txt;

	DefaultTableModel m = model("p_no,������¥,������,��������".split(","));
	JTable t = table(m);
	HashMap<Integer, ArrayList<String>> ImgMap = new HashMap<>();
	String type = "��ü";
	JPanel imgPanel;
	int pageIdx = 1;

	public Search() {
		super("�˻�", 700, 600);
		setJMenuBar(bar = new JMenuBar());
		bar.add(menu = new JMenu("�з�"));

		for (var item : "��ü,������,�����,�ܼ�Ʈ".split(",")) {
			var it = new JMenuItem(item);
			it.addActionListener(a -> {
				type = a.getActionCommand();
				search(a.getActionCommand());
			});
			menu.add(it);
		}

		add(n = new JPanel(new FlowLayout(FlowLayout.RIGHT)), "North");
		add(c = new JPanel(new BorderLayout()));

		n.add(lbl("������ : ", JLabel.LEFT));
		n.add(txt = new JTextField(15));
		n.add(btn("�˻�", a -> search(type)));
		var c_c = new JPanel(new GridLayout(1, 0));
		c.add(lbl("���� ���� ���� ����", JLabel.RIGHT, 15), "North");
		c.add(c_c);
		c_c.add(imgPanel = new JPanel(new BorderLayout()));
		c_c.add(new JScrollPane(t));
		t.getColumnModel().getColumn(0).setMinWidth(0);
		t.getColumnModel().getColumn(0).setMaxWidth(0);
		t.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (t.getSelectedRow() == -1)
					return;
				new Reserve(t.getValueAt(t.getSelectedRow(), 0).toString()).addWindowListener(new before(Search.this));
			};
		});
		search(type);
		setVisible(true);
	}

	void setImgPanel() {
		imgPanel.removeAll();
		CardLayout page;
		JLabel prev, next, last, first;
		imgPanel.add(prev = lbl("��", JLabel.CENTER, 20), "West");
		imgPanel.add(next = lbl("��", JLabel.CENTER, 20), "East");
		JLabel dots[] = new JLabel[ImgMap.keySet().size()];
		var c = new JPanel(page = new CardLayout());
		var s = new JPanel(new BorderLayout());
		var s_c = new JPanel(new FlowLayout());
		var s_s = new JPanel(new FlowLayout());
		imgPanel.add(new JLabel("�з� : " + type, JLabel.RIGHT), "North");
		imgPanel.add(c);
		imgPanel.add(s, "South");
		s.add(s_c);
		s.add(s_s, "South");
		s_s.add(first = lbl("��ó������", JLabel.RIGHT, 15));
		s_s.add(last = lbl("���������Ρ�", JLabel.LEFT, 15));
		JLabel evntlbls[] = { first, last, prev, next };
		c.setBorder(new EmptyBorder(50, 50, 50, 50));
		for (var k : ImgMap.keySet()) {
			var tmp = new JPanel(new GridLayout(0, 2, 5, 5));

			for (var l : ImgMap.get(k)) {
				JLabel img = null;
				if (l.split(",").length > 1) {
					img = new JLabel(getIcon("./DataFiles/��������/" + l.split(",")[1] + ".jpg", 100, 100));
					img.setToolTipText(l.split(",")[3] + "/" + l.split(",")[2]);
					img.setName(l.split(",")[0]);
				} else {
					img = new JLabel();
				}

				img.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						var cap = ((JLabel) e.getSource()).getName();
						if (cap == null) {
							eMsg("���� ������ �����ϴ�.");
						} else {
							new Reserve(cap).addWindowListener(new before(Search.this));
						}
						super.mousePressed(e);
					}
				});
				img.setBorder(new LineBorder(Color.BLACK));

				tmp.add(img);
			}
			c.add(k + "", tmp);
		}

		for (int i = 0; i < ImgMap.keySet().size(); i++) {
			dots[i] = new JLabel("��");
			s_c.add(dots[i]);
		}

		for (int i = 0; i < evntlbls.length; i++) {
			evntlbls[i].addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {
					var cap = ((JLabel) e.getSource()).getText();
					switch (cap) {
					case "��":
						if (pageIdx == 1)
							return;
						page.previous(c);
						pageIdx--;
						break;
					case "��":
						if (pageIdx == ImgMap.keySet().size())
							return;
						page.next(c);
						pageIdx++;
						break;
					case "��ó������":
						pageIdx = 1;
						page.first(c);
						break;
					default:
						pageIdx = ImgMap.keySet().size();
						page.last(c);
						break;
					}

					for (int i = 0; i < dots.length; i++) {
						dots[i].setForeground(Color.BLACK);
					}

					dots[pageIdx - 1].setForeground(Color.RED);
				}
			});
		}
		dots[0].setForeground(Color.RED);
		revalidate();
		repaint();
	}

	void search(String t) {
		ImgMap.clear();
		var type = t.equals("��ü") ? "" : t.equals("������") ? "M" : t.equals("�����") ? "O" : t.equals("�ܼ�Ʈ") ? "C" : "";
		addRow("SELECT p_no, p_date, p_name, format(p_price, '#,##0') from perform where p_date >= curdate() and pf_no like '%"
				+ type + "%' and p_name like '%" + txt.getText() + "%' order by p_date, p_price desc", m);

		try {
			var rs = stmt.executeQuery(
					"SELECT p_no, pf_no, p_date, p_actor from perform where p_date >= curdate() and pf_no like '%"
							+ type + "%' and p_name like '%" + txt.getText()
							+ "%' group by p_name order by p_date, p_price desc");

			ArrayList<String> list = new ArrayList<>();
			int page = 1;
			while (rs.next()) {
				list.add(rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + "," + rs.getString(4));
				if (list.size() == 4) {
					ImgMap.put(page, list);
					list = new ArrayList<>();
					page++;
				}
			}
			var size = list.size();
			// �˻� ����� �������� �̻��ε� list�� 4�� �ƴҶ�

			if (list.size() > 0) {
				ImgMap.put(page, list);
				for (int i = 0; i < 4 - size; i++) {
					list.add("");
				}
			}

			// �˻� ����� ������
			if (ImgMap.keySet().size() == 0) {
				ImgMap.put(page, list);
				for (int i = 0; i < 4; i++) {
					list.add("");
				}
			}
			// �˻� ����� ��������
			if (ImgMap.keySet().size() == 0) {
				if (list.size() > 0) {
					ImgMap.put(page, list);
					for (int i = 0; i < 4 - size; i++) {
						list.add("");
					}
				}
			}

			if (m.getRowCount() == 0)
				eMsg("�˻������ �����ϴ�.");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setImgPanel();
	}
}
