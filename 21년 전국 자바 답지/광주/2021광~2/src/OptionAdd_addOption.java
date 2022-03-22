

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class OptionAdd_addOption extends Basedialog {
	DefaultTableModel dtm = model("��ȣ,�ɼǸ�,����".split(","));
	JTable table = new JTable(dtm);
	JScrollPane scr= new JScrollPane(table);
	String str[] = "�ɼ� �̸�,�ɼ� ����,�ɼ� �׷� �̸� �Է��ϱ�".split(",");
	PlaceH tt[] = new PlaceH[3];
	JButton jb[] = new JButton[2];
	JPanel s = new JPanel(new GridLayout(0, 1, 0, 10)), ss = new JPanel(new BorderLayout());
	int last;
	
	public OptionAdd_addOption(Modify m) {
		super("�ɼ� �߰�", 350, 500);
		setModal(true);
		
		add(scr);
		add(s,"South");
		
		try {
			ResultSet rs = stmt.executeQuery("select no from options");
			rs.last();
			last = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		s.add(tt[0] = new PlaceH(15));
		s.add(tt[1] = new PlaceH(15));
		s.add(jb[0] = btn("�ɼ� ���", a->{
			if(tt[0].getText().isEmpty() || tt[1].getText().isEmpty()) {
				errmsg("�� ĭ ���� ��� �Է��ؾ� �մϴ�.");
				return;
			}
			if(tt[1].getText().matches(".*\\D.*")) {
				errmsg("�ɼ� ������ ���ڷθ� �Է��ؾ� �մϴ�.");
				return;
			}
			
			Object row[] = new Object[3];
			row[0] = ++last;
			row[1] = tt[0].getText();
			row[2] = format.format(toint(tt[1].getText()));
			dtm.addRow(row);
		}));
		s.add(ss);
		ss.add(tt[2] = new PlaceH(15));
		ss.add(jb[1] = btn("�ɼ� ����", a->{
			if(tt[2].getText().isEmpty()) {
				errmsg("�ɼ� �׷� �̸��� �Է��ؾ� �մϴ�.");
				return;
			}
			if(table.getRowCount() == 0) {
				errmsg("1�� �̻��� �ɼ��� ����ؾ� �մϴ�.");
				return;
			}
			
			String aa = "";
			for (int i = 0; i < table.getRowCount(); i++) {
				aa += "," + table.getValueAt(i, 1);
			}
			for (int i = 0; i < m.table.getRowCount(); i++) {
				if(m.table.getValueAt(i, 1).equals(tt[2].getText())) {
					m.table.setValueAt(m.table.getValueAt(i, 2) + aa, i, 2);
					dispose();
					return;
				}
			}
			Object row[] = {last-table.getRowCount() + 1, tt[2].getText(), aa.substring(1, aa.length())};
			System.out.println(m.combo.getSelectedIndex());
			m.models.get(m.combo.getSelectedIndex()).addRow(row);
			dispose();
		}),"East");
		
		for (int i = 0; i < tt.length; i++) {
			tt[i].setPlace(str[i]);
		}
		
		emp(s, 10, 0, 0, 0);
		
		emp((JPanel)getContentPane(), 10, 10, 10, 10);
		
		s.setOpaque(false);
		
		setVisible(true);
	}

}
