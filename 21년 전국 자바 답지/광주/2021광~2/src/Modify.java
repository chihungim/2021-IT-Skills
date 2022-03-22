

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Modify extends Basedialog {
	JLabel img;
	PlaceH tt[] = new PlaceH[4];
	JButton jb[] = new JButton[2];
	String str[] = "�޴���,����,�ǸŰ�,�����ð�".split(","), aa[] = "�ɼ� �߰�,�ɼ� ����".split(",");
	JComboBox<String> combo = new JComboBox<String>();
	DefaultTableModel dtm = model("��ȣ,�̸�,�ɼǵ�".split(","));
	JTable table = new JTable(dtm);
	JScrollPane scr = new JScrollPane(table);
	JPanel n = new JPanel(new BorderLayout()), ne = new JPanel(new GridLayout(0, 1, 0, 10)), c = new JPanel(new BorderLayout()), sn = new JPanel(new FlowLayout(0)), s = new JPanel(new BorderLayout());
	JPopupMenu pop = new JPopupMenu();
	JMenuItem item = new JMenuItem("���� �׷� �߰�");
	ArrayList<String > type = new ArrayList<String>();
	ArrayList<DefaultTableModel> models = new ArrayList<DefaultTableModel>();
	ArrayList<String> blacklist = new ArrayList<String>();
	
	public Modify() {
		super("�޴�����", 500, 500);
		
		setUI();
		
		s.add(btn("���", a->{
			for (int i = 0; i < tt.length; i++) {
				if(tt[i].getText().isEmpty()) {
					errmsg("�� ĭ ���� ��� �Է��ؾ� �մϴ�.");
					return;
				}
			}
			if(tt[2].getText().matches(".*\\D.*")) {
				errmsg("�Ǹ� ������ ���ڷ� �Է��ؾ� �մϴ�.");
				return;
			}
			if(!tt[3].getText().matches("[0-9]{2}:[0-9]{2}:[0-9]{2}")) {
				errmsg("�����ð��� hh:mm:ss �������� �Է��ؾ� �մϴ�.");
			}
			if(LocalTime.parse(tt[3].getText()).toSecondOfDay() < LocalTime.of(0, 0, 1).toSecondOfDay()) {
				errmsg("�����ð��� 1�� �̻��̾�� �մϴ�.");
				return;
			}
			if(!getone("select * from menu where seller = "+NO+" and name = "+tt[0].getText()).equals("")) {
				errmsg("�ߺ��Ǵ� �޴����Դϴ�.");
				return;
			}
			msg("�޴��� ����߽��ϴ�.");
			for (int i = 0; i < type.size(); i++) {
				execute("insert into type values(0,'"+type.get(i)+"',"+NO+")");
			}
			String t = getone("select no from type where name = '"+combo.getSelectedItem()+"' and seller = "+NO);
			execute("insert into menu values(0,'"+tt[0].getText()+"','"+tt[1].getText()+"','"+tt[2].getText()+"','"+tt[3].getText()+"','"+NO+"','"+t+"')");
		}));
		
		c.add(scr);
		
		setVisible(true);
	}
	
	public Modify(int no) {
		super("�޴�����", 500, 500);
		
		setUI();
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM menu m, type t where m.type = t.no and m.seller = "+NO+" and m.no = "+no);
			if(rs.next()) {
				for (int i = 0; i < tt.length; i++) {
					tt[i].setText(rs.getString(i+2));
				}
				combo.setSelectedItem(rs.getString("t.name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		s.add(btn("���", a->{
			for (int i = 0; i < tt.length; i++) {
				if(tt[i].getText().isEmpty()) {
					errmsg("�� ĭ ���� ��� �Է��ؾ� �մϴ�.");
					return;
				}
			}
			if(tt[2].getText().matches(".*\\D.*")) {
				errmsg("�Ǹ� ������ ���ڷ� �Է��ؾ� �մϴ�.");
				return;
			}
			if(!tt[3].getText().matches("[0-9]{2}:[0-9]{2}:[0-9]{2}")) {
				errmsg("�����ð��� hh:mm:ss �������� �Է��ؾ� �մϴ�.");
			}
			if(LocalTime.parse(tt[3].getText()).toSecondOfDay() < LocalTime.of(0, 0, 1).toSecondOfDay()) {
				errmsg("�����ð��� 1�� �̻��̾�� �մϴ�.");
				return;
			}
			if(!getone("select * from menu where seller = "+NO+" and name = '"+tt[0].getText()+"' and no <>"+no).equals("")) {
				errmsg("�ߺ��Ǵ� �޴����Դϴ�.");
				return;
			}
			for (int i = 0; i < type.size(); i++) {
				execute("insert into type values(0,'"+type.get(i)+"',"+NO+")");
			}
			
			for (int i = 0; i < blacklist.size(); i++) {
				execute("delete from options where title = '"+blacklist.get(i)+"' and menu = "+no);
			}
			
			for (int i = 0; i < table.getRowCount(); i++) {
				if(!getone("select * from options where title = '"+table.getValueAt(i, 1)).equals("")) {
					String aaa[] = table.getValueAt(i, 2).toString().split(",");
					for (int j = 0; j < aaa.length; j++) {
//					execute("update options set );
					}
				}else {
					
				}
			}
			
			msg("�޴��� �����߽��ϴ�.");
			String t = getone("select no from type where name = '"+combo.getSelectedItem()+"' and seller = "+NO);
			execute("update menu set name = '"+tt[0].getText()+"', description = '"+tt[1].getText()+"', price = '"+tt[2].getText()+"', cooktime = '"+tt[3].getText()+"', type = '"+t+"' where no = "+no);
		}));
		
		img.setIcon(img("�޴�/"+no+".png",165,170));
		
		for (int i = 0; i < combo.getItemCount(); i++) {
			try {
				ResultSet rs = stmt.executeQuery("select m.no, o.title, o.name from menu m, type t, options o where m.type = t.no and o.menu = m.no and t.name like '%"+combo.getItemAt(i)+"%' and m.no = "+no);
				DefaultTableModel m = model("��ȣ,�̸�,�ɼǵ�".split(","));
				Object row[] = {"","",""};
				if(!rs.next()) {
					models.add(m);
					continue;
				}
				rs.previous();
				
				while(rs.next()) {
					row[0] = rs.getInt(1);
					row[1] = rs.getString(2);
					row[2] += "," + rs.getString(3);
				}
				row[2] = row[2].toString().substring(1, row[2].toString().length() -1);
				m.addRow(row);
				models.add(m);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		combo.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				c.removeAll();
				table = new JTable(models.get(combo.getSelectedIndex()));
				c.add(scr = new JScrollPane(table));
				repaint();
				revalidate();
			}
		});
		
		c.add(scr = new JScrollPane(table = new JTable(models.get(combo.getSelectedIndex()))));
		
		setVisible(true);
	}

	private void setUI() {
		
		add(n,"North");
		add(c);
		add(s,"South");
		
		n.add(img = new JLabel());
		n.add(ne,"East");
		
		size(n, 0, 180);
		
//		c.add(scr);
		
		s.add(sn,"North");
		
		for (int i = 0; i < aa.length; i++) {
			sn.add(jb[i] = btn(aa[i], a->{
				if(a.getSource().equals(jb[0])) {
					new OptionAdd_addOption(Modify.this).addWindowListener(new be(this));
				}
				if(a.getSource().equals(jb[1])) {
					if(table.getSelectedRow() == -1) {
						errmsg("������ �ɼ��� �����ؾ� �մϴ�.");
						return;
					}
					if(!getone("select * from options where no = '"+table.getValueAt(table.getSelectedRow(), 0)+"'").equals("")) {
						blacklist.add(table.getValueAt(table.getSelectedRow(), 1).toString());
					}
					models.get(combo.getSelectedIndex()).removeRow(table.getSelectedRow());
				}
			}));
		}
		
		for (int i = 0; i < tt.length; i++) {
			ne.add(tt[i] = new PlaceH(15));
			tt[i].setPlace(str[i]);
		}
		
		try {
			ResultSet rs = stmt.executeQuery("select * from type where seller = "+NO);
			while(rs.next()) {
				combo.addItem(rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ne.add(combo);
		
		pop.add(item);
		
		combo.setComponentPopupMenu(pop);
		
		item.addActionListener(a->{
			String str = JOptionPane.showInputDialog(null, "���� �߰��� ���� �׷� �̸��� �Է��ϼ���.","�޽���",JOptionPane.INFORMATION_MESSAGE);
			
			if(str == null) {
				errmsg("�̸��� �Է��ؾ� �մϴ�.");
				return;
			}
			if(!getone("select * from type where seller = "+NO+" and name = '"+str+"'").equals("")) {
				errmsg("�ߺ��Ǵ� ���� �׷��Դϴ�.");
				return;
			}
			type.add(str);
			combo.addItem(str);
		});
		
		n.setOpaque(false);
		ne.setOpaque(false);
		c.setOpaque(false);
		sn.setOpaque(false);
		s.setOpaque(false);
		
		size(ne, 300, 0);
		
		emp((JPanel)getContentPane(), 10, 10, 10, 10);
		emp(n, 0, 0, 10, 0);
		emp(ne, 0, 20, 0, 0);
		
		line(img);
		
		img.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FileDialog file = new FileDialog(Modify.this, "", FileDialog.LOAD);
				file.setDirectory(System.getProperty("user.home")+File.separator+"Documents");
				file.setFile("*.png");
				file.setVisible(true);
				File f = new File(file.getDirectory()+file.getFile());
				img.setIcon(imgP(f.getPath(), 165, 170));
			}
		});
		
	}
	
	public static void main(String[] args) {
		NO = 1;
		new Modify(4);
	}
}
