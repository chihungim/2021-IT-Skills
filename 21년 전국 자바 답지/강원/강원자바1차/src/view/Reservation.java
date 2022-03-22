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
	DefaultTableModel m = new DefaultTableModel(null, new String[] { "no",  "출발지", "도착지", "출발시간", "도착시간", "", "sno", "sdate", "elpased" });
//	DefaultTableModel m = new DefaultTableModel(null, "no,출발지,도착지,출발시간,도착시간, ,sno,date,time".split(","));
	JTable t = new JTable(m);
	JScrollPane scr = new JScrollPane(t);
	Tbtn tbtn = new Tbtn();
	
	public Reservation(String sql) {
		super("예매조회", 1080, 600);
		
		this.add(lbl("예매", 2, 25), "North");
		this.add(scr);
		
		try {
			var rs = DBManager.rs(sql);
			int i = 1;
			while (rs.next()) {
				Object row[] = { i++, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), new JButton("예매"), rs.getString(5), rs.getString(6), rs.getString(7) };
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
			btn = new JButton("예매");
			
			btn.addActionListener(a->{
				int yn = JOptionPane.showConfirmDialog(null,
						"[이동시간 가격 안내]\n1시간 이하 100Point,\n2시간 이하 300Point,\n3시간 이하 500Point,\n이외 700Point 차감\n\n예매를 진행하시겠습니까?","안내",JOptionPane.YES_NO_OPTION);
				
				if(yn == JOptionPane.YES_OPTION) {
					if(LocalDate.parse(t.getValueAt(t.getSelectedRow(), 7).toString()).toEpochDay() < LocalDate.now().toEpochDay()) {
						new eMsg("예매할 수 없는 일정입니다.");
						return;
					}
					
					try {
						ResultSet rs = DBManager.rs("select * from reservation where schedule_no = '"+t.getValueAt(t.getSelectedRow(), 6)+"'");
						rs.last();
						if(rs.getRow() == 25) {
							new eMsg("해당 일정에 좌석이 모두 매진되었습니다.");
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
						new eMsg("포인트가 부족합니다.");
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
		new Reservation("SELECT a.name, b.name, right(date,8), right(date_add(date, interval elapsed_time HOUR_SECOND),8), s.no, date, elapsed_time FROM loc a, loc b, schedule s where s.departure_location2_no=a.no and s.arrival_location2_no=b.no and a.name = '서울 송파구' and b.name='경상북도 봉화군' and s.date = '2021-09-08'");
	}
	
}
