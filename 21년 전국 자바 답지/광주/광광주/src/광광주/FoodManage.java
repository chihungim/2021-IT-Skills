package ������;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class FoodManage extends BaseFrame {

	String bcap[] = "�߰�,����".split(",");

	DefaultTableModel m = model("��ȣ,�̸�,����,����,�����ð�".split(","));
	JTable t = table(m);

	PlaceHolderTextField txt;

	public FoodManage() {
		super("�޴� ����", 700, 400);

		setLayout(new BorderLayout(5, 5));

		var n = new JPanel(new BorderLayout(5, 5));
		var s = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		add(n, "North");

		n.add(txt = new PlaceHolderTextField(20));
		n.add(btn("�˻�", a -> {
			setData();
		}), "East");

		add(new JScrollPane(t));
		add(s, "South");

		for (String bc : bcap) {
			s.add(btn(bc, a -> {
				if (a.getActionCommand().equals("�߰�")) {
					new MenuEdit(this, false);
				} else {
					if (t.getSelectedRow() == -1) {
						eMsg("������ �޴��� �����ؾ� �մϴ�.");
						return;
					}
					new MenuEdit(this, true);
				}
			}));
		}

		((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));
		setData();
		setVisible(true);
	}

	void setData() {
		addrow("SELECT no, name, DESCRIPTION, format(price, '#,##0'), cooktime FROM eats.menu where seller = " + sno
				+ (txt.getText().isEmpty() ? "" : " and name like '%" + txt.getText() + "%'"), m);
	}
}
