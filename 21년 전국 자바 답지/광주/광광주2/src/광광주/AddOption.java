package ������;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class AddOption extends BaseFrame {

	DefaultTableModel m;
	JTable t;

	String holders[] = "�ɼ� �̸�,�ɼ� ����,�ɼ� �׷� �̸� �Է��ϱ�".split(",");

	PlaceHolderTextField txt[] = { new PlaceHolderTextField(1), new PlaceHolderTextField(1) };
	PlaceHolderTextField groupName = new PlaceHolderTextField(10);

	MenuEdit me;
	int lastref = 0;

	public AddOption(MenuEdit me) {
		super("�ɼ��߰�", 300, 400);

		this.me = me;
		lastref = getLastRef("select no from options");
		dtcr.setHorizontalAlignment(SwingConstants.LEFT);

		m = model("��ȣ,�ɼǸ�,����".split(","));
		t = table(m);

		setLayout(new BorderLayout(5, 5));

		add(new JScrollPane(t));

		var s = new JPanel(new GridLayout(0, 1, 5, 5));
		var s_s = new JPanel(new BorderLayout(5, 5));

		add(s, "South");

		for (int i = 0; i < txt.length; i++) {
			txt[i].setPlaceHolder(holders[i]);
			s.add(txt[i]);
		}
		groupName.setPlaceHolder(holders[2]);

		s.add(btn("�ɼ� ���", a -> {
			for (int i = 0; i < txt.length; i++) {
				if (txt[i].getText().isEmpty()) {
					eMsg("�� ĭ ���� ��� �Է��ؾ� �մϴ�.");
					return;
				}
			}

			if (txt[1].getText().matches(".*[^0-9].*")) {
				eMsg("�ɼ� ������ ���ڷθ� �Է��ؾ� �մϴ�.");
				return;
			}

			Object row[] = new Object[3];
			row[0] = lastref;
			row[1] = txt[0].getText();
			row[2] = txt[1].getText();
			m.addRow(row);

			lastref++;
		}));

		s.add(s_s);

		s_s.add(groupName);
		s_s.add(btn("�ɼ� ����", a -> {
			if (groupName.getText().isEmpty()) {
				eMsg("�ɼ� �׷� �̸��� �Է��ؾ��մϴ�.");
				return;
			}

			if (t.getRowCount() == 0) {
				eMsg("1�� �̻��� �ɼ��� ����ؾ� �մϴ�.");
				return;
			}

			for (int i = 0; i < me.type.getItemCount(); i++) {
				if(me.type.getItemAt(i).equals(groupName.getText())){
					
					Object[] row = {"", "" , ""};
					
				}
			}

		}), "East");

		((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));
		setVisible(true);
	}

}
