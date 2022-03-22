package view;

import java.awt.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import db.DBManager;

public class Reservation extends BaseDialog {
	
	int sno;
	DefaultTableModel m = new DefaultTableModel(null, new String[] { "no",  "�����", "������", "��߽ð�", "�����ð�", "", "sno", "sdate", "elpased" });
//	DefaultTableModel m = new DefaultTableModel(null, "no,�����,������,��߽ð�,�����ð�, ,sno,date,time".split(","));
	JTable t = new JTable(m);
	JScrollPane scr = new JScrollPane(t);
	Tbtn tbtn = new Tbtn();
	
	public Reservation(String sql) {
		super("������ȸ", 1080, 600);
		
		this.add(lbl("����", 2, 25), "North");
		this.add(scr);
		
		try {
			var rs = DBManager.rs(sql);
			int i = 1;
			while (rs.next()) {
				Object row[] = { i++, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), new JButton("����"), rs.getString(5), rs.getString(6), rs.getString(7) };
				m.addRow(row);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < t.getColumnCount(); i++) {
			t.getColumnModel().getColumn(i).setCellRenderer(BaseFrame.dtcr);
		}
		
		t.getColumnModel().getColumn(5).setCellRenderer(tbtn);
		t.getColumnModel().getColumn(5).setCellEditor(tbtn);
		
		
			
		setEmpty((JPanel)getContentPane(), 15, 15, 15, 15);
		this.setVisible(true);
	}
	
	class Tbtn extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

		JButton btn;
		
		public Tbtn() {
			btn = new JButton("����");
			
			btn.addActionListener(a->{
				int yn = JOptionPane.showConfirmDialog(null,
						"[�̵��ð� ���� �ȳ�]\n1�ð� ���� 100Point,\n2�ð� ���� 300Point,\n3�ð� ���� 500Point,\n�̿� 700Point ����\n\n���Ÿ� �����Ͻðڽ��ϱ�?","�ȳ�",JOptionPane.YES_NO_OPTION);
				
				if(yn == JOptionPane.YES_OPTION) {
					if(LocalDate.parse(t.getValueAt(t.getSelectedRow(), 7).toString()).toEpochDay() < LocalDate.now().toEpochDay()) {
						new eMsg("������ �� ���� �����Դϴ�.");
						return;
					}
					
					try {
						ResultSet rs = DBManager.rs("select * from reservation where schedule_no = '"+t.getValueAt(t.getSelectedRow(), 6)+"'");
						rs.last();
						if(rs.getRow() == 25) {
							new eMsg("�ش� ������ �¼��� ��� �����Ǿ����ϴ�.");
							return;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					int point = 0;
					LocalTime l = LocalTime.parse(t.getValueAt(t.getSelectedRow(), 8).toString());
					
					if(l.getHour() <= 1) {
						point = 100;
					} else if(l.getHour() <= 2) {
						point = 300;
					} else if(l.getHour() <= 3) {
						point = 500;
					} else {
						point = 700;
					}
					
					int upoint = rei(DBManager.getOne("select point from user where no = "+BaseFrame.uno));
					
					if(point > upoint) {
						new eMsg("����Ʈ�� �����մϴ�.");
						return;
					}
					DBManager.execute("update user set point = point - "+point+" where no = "+BaseFrame.uno);
					DBManager.execute("insert into reservation values(0,'"+BaseFrame.uno+"','"+t.getValueAt(t.getSelectedRow(), 6)+"')");
					
				}
			});
		}
		
		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			// TODO Auto-generated method stub
			return btn;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			// TODO Auto-generated method stub
			return btn;
		}
		
	}
	
	public static void main(String[] args) {
		new Reservation("SELECT a.name, b.name, right(date,8), right(date_add(date, interval elapsed_time HOUR_SECOND),8), s.no, date, elapsed_time FROM loc a, loc b, schedule s where s.departure_location2_no=a.no and s.arrival_location2_no=b.no and a.name = '���� ���ı�' and b.name='���ϵ� ��ȭ��' and s.date = '2021-09-08'");
	}
	
}
