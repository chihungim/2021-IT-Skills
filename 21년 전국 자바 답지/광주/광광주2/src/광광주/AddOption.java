package 광광주;

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

	String holders[] = "옵션 이름,옵션 가격,옵션 그룹 이름 입력하기".split(",");

	PlaceHolderTextField txt[] = { new PlaceHolderTextField(1), new PlaceHolderTextField(1) };
	PlaceHolderTextField groupName = new PlaceHolderTextField(10);

	MenuEdit me;
	int lastref = 0;

	public AddOption(MenuEdit me) {
		super("옵션추가", 300, 400);

		this.me = me;
		lastref = getLastRef("select no from options");
		dtcr.setHorizontalAlignment(SwingConstants.LEFT);

		m = model("번호,옵션명,가격".split(","));
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

		s.add(btn("옵션 등록", a -> {
			for (int i = 0; i < txt.length; i++) {
				if (txt[i].getText().isEmpty()) {
					eMsg("빈 칸 없이 모두 입력해야 합니다.");
					return;
				}
			}

			if (txt[1].getText().matches(".*[^0-9].*")) {
				eMsg("옵션 가격은 숫자로만 입력해야 합니다.");
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
		s_s.add(btn("옵션 저장", a -> {
			if (groupName.getText().isEmpty()) {
				eMsg("옵션 그룹 이름을 입력해야합니다.");
				return;
			}

			if (t.getRowCount() == 0) {
				eMsg("1개 이상의 옵션을 등록해야 합니다.");
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
