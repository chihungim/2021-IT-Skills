package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.Format;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;

public class Search extends BaseFrame {
	String sql;

	JPanel c_c;
	JTextField sField;
	String selLocal = "����";
	DefaultTableModel m = model("����");
	JTable t = table(m);
	JScrollPane pane;
	ButtonGroup group = new ButtonGroup();

	public Search() {
		super("�˻�", 750, 450);
		setUI();
		setData();
		search();
		setVisible(true);
	}

	void setUI() {
		add(lbl("��Ż�� ī�� �˻�", JLabel.LEFT, 40), "North");

		var c = new JPanel(new BorderLayout());
		var c_n = new JPanel(new BorderLayout());
		var c_n_w = new JPanel(new FlowLayout(FlowLayout.LEFT));
		var c_w = new JPanel(new BorderLayout());
		var c_n_e = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		add(c);
		c.add(c_n, "North");
		c.add(c_w, "West");
		c_n.add(c_n_w, "West");
		c_n.add(c_n_e, "East");

		for (var bcap : "���� ���� ��,���� ���� ��,1�� ����,���̵� 5".split(",")) {
			JToggleButton btn = new JToggleButton(bcap);
			if (bcap.equals("���� ���� ��"))
				btn.setSelected(true);
			group.add(btn);
			c_n_w.add(btn);
			btn.addActionListener(a -> {
				var b = (JToggleButton) a.getSource();
				if (b.isSelected()) {
					switch (a.getActionCommand()) {
					case "���� ���� ��":
						sql = "select c.c_no, c_name, c_address from cafe c left outer join evaluation e on c.c_no = e.c_no where c.c_name like %s and c_address like %s group by c_name order by e.e_score desc";
						break;
					case "���� ���� ��":
						sql = "select c.c_no, c_name ,c_address, count(e.c_no) cnt from cafe c left outer join evaluation e on c.c_no = e.c_no and c.c_name like %s and c_address like %s group by c.c_name order by cnt desc";
						break;
					case "1�� ����":
						sql = "select c.c_no,c_name,c_address from cafe c, theme t where t.t_no in (c.c_division) and t.t_personnel = 1 and  c.c_name like %s and c_address like %s";
						break;
					default:
						sql = "select c.c_no,c_name ,c_address from cafe c, theme t where t.t_no in (c.c_division) and t.t_difficulty = 5 and c.c_name like %s and c_address like %s group by c_name ";
						break;
					}
					search();
				}
			});
		}

		sql = "select c.c_no,c_name,c_address from cafe c left outer join evaluation e on c.c_no = e.c_no where c.c_name like %s and c.c_address like %s group by c_name order by e.e_score desc";

		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (t.getSelectedRow() == -1)
					return;
				selLocal = t.getValueAt(t.getSelectedRow(), 0).toString();
				location = t.getValueAt(t.getSelectedRow(), 0).toString();
				search();
				super.mousePressed(e);
			}
		});

		c_n_e.add(sField = new JTextField(15));
		c_n_e.add(btn("�˻�", a -> {
			search();
		}));

		c_w.add(sz(new JScrollPane(t), 100, 2));
		c.add(pane = new JScrollPane(c_c = new JPanel(new GridLayout(0, 3))));
		location = "����";
		pane.setBorder(BorderFactory.createEmptyBorder());
	}

	void setData() {
		m.addRow(new Object[] { "����" });
		try {
			var rs = stmt.executeQuery(
					"SELECT distinct left(c_address,2) FROM roomescape.cafe where left(c_address,2) != '����'");
			while (rs.next()) {
				Object row[] = new Object[m.getColumnCount()];
				for (int i = 0; i < row.length; i++) {
					row[i] = rs.getString(i + 1);
				}
				m.addRow(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void search() {
		c_c.removeAll();
		selLocal = selLocal.equals("����") ? "" : selLocal;
		var s = String.format(sql, "'%" + sField.getText() + "%'", "'%" + selLocal + "%'");
		try {
			var rs = stmt.executeQuery(s);
			while (rs.next()) {
				String name = rs.getString(2);
				String no = rs.getString(1);
				JPanel tmp = new JPanel(new BorderLayout());
				tmp.add(new JLabel(img("./Datafiles/����/" + name.split(" ")[0] + ".jpg", 180, 180)));
				tmp.add(lbl(name, 0), "South");
				tmp.setName(no);
				tmp.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2) {
							cno = ((JPanel) e.getSource()).getName();
							cname = ((JLabel) ((JPanel) e.getSource()).getComponents()[1]).getText();
							new Intro().addWindowListener(new Before(Search.this));
						}
						super.mouseClicked(e);
					}
				});

				c_c.add(tmp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		revalidate();
		repaint();
	}

	@Override
	public JLabel lbl(String cap, int alig, int size) {
		var a = super.lbl(cap, alig, size);
		a.setFont(new Font("HY������M", Font.BOLD, size));
		return a;
	}

	public static void main(String[] args) {
		new Search();
	}
}
