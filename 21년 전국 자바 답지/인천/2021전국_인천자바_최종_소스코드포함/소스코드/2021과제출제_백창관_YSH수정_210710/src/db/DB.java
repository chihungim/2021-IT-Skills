package db;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class DB extends JFrame {
	static Connection con;
	static Statement stmt;
	static Icon eIcon = UIManager.getIcon("OptionPane.errorIcon");
	static Icon iIcon = UIManager.getIcon("OptionPane.informationIcon");
	DefaultTableModel m = new DefaultTableModel(null, "惑怕,捞抚".split(",")) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
		public java.lang.Class<?> getColumnClass(int columnIndex) {
			return getValueAt(0, columnIndex).getClass();
		};
	};
	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer() {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			if (value instanceof JLabel) {
				return (JLabel)value;
			} else return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	};
	JPanel n, c, s;
	JTable t = new JTable(m);
	int w;
	JScrollPane scr = new JScrollPane(t);
	int cnt;
	String cascade = "on delete cascade on update cascade";
	
	static {
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost?serverTimezone=UTC&allowLoadLocalInfile=true&allowPublicKeyRetrieval=true",
					"root", "1234");
			stmt = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void execute(String cap, String sql) {
		try {
			stmt.execute(sql);
			iLog(cap, true);
		} catch (SQLException e) {
			iLog(cap, false);
			e.printStackTrace();
		}
	}

	void createTable(String cap, String t, String c) throws SQLException {
		try {
			execute(cap, "create table "+t+"("+c+")");
			stmt.execute("load data local infile './datafile/"+t+".txt' into table "+t+" lines terminated by '\r\n' ignore 1 lines");
			iLog(t+" Table 檬扁拳", true);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	void iLog(String cap, boolean bool) {
		JLabel lbl = new JLabel(bool?"己傍":"角菩", bool?iIcon:eIcon, 0);
		JLabel clbl = new JLabel(cap);
		lbl.setFont(new Font("奔覆", Font.PLAIN, 11));
		clbl.setFont(new Font("奔覆", Font.PLAIN, 11));
		lbl.setForeground(bool?Color.blue:Color.red);
		clbl.setForeground(bool?Color.blue:Color.red);
		m.addRow(new Object[] { lbl, clbl });
		try {
			Thread.sleep(100);
			scr.getVerticalScrollBar().setValue(scr.getVerticalScrollBar().getMaximum());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DB() {
		super("db 檬扁拳");
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(2);
		this.add(c = new JPanel(new BorderLayout()));
		this.add(s = new JPanel(), "South");
		c.add(scr);
		c.setBorder(new TitledBorder("Log"));
		 
		t.setRowHeight(40);
		t.setGridColor(Color.white);
		t.setShowGrid(false);
		t.setSelectionMode(2);
		t.setOpaque(false);
		

		int widths[] = { 30, 250 };
		for (int i = 0; i < widths.length; i++) {
			t.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
		}
		t.setDefaultRenderer(JLabel.class, dtcr);
		this.setVisible(true);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		init();
		
		
	}
	
	void init() {
		String r = null;
		boolean state = false;
		try {
			execute("DB 昏力", "drop database if exists delivery");
			execute("DB 积己", "create database delivery");
			stmt.execute("set global local_infile=1");
			stmt.execute("use delivery");
			execute("user 昏力", "drop user if exists user@localhost");
			execute("user 积己", "create user user@localhost identified by '1234'");
			execute("user 鼻茄 汲沥", "grant select,insert,update,delete on delivery.* to user@localhost");
			
			createTable("point Table 积己", "point", "po_No int primary key not null auto_increment, po_X int not null, po_Y int not null");
			createTable("connect Table 积己", "connect", "c_Node1 int not null, c_Node2 int not null, foreign key(c_Node1) references point(po_No), foreign key(c_Node2) references point(po_No) "+cascade);
			createTable("user Table 积己", "user", "u_No int primary key not null auto_increment, u_Id varchar(20) not null, u_Pattern varchar(10) not null, u_Name varchar(20) not null, u_Addr int not null, u_Rating varchar(20) not null, foreign key(u_Addr) references point(po_No) "+cascade);
			createTable("seller Table 积己", "seller", "s_No int primary key not null auto_increment, s_Id varchar(20) not null, s_Pattern varchar(10) not null, s_Name varchar(20) not null, s_Addr int not null, foreign key(s_Addr) references point(po_No) "+cascade);
			createTable("category Table 积己", "category", "c_No int primary key not null auto_increment, c_Name varchar(20) not null");
			createTable("product Table 积己", "product", "p_No int primary key not null auto_increment, c_No int not null, p_Name varchar(50) not null, p_price int not null, foreign key(c_No) references category(c_No) "+cascade);
			createTable("stock Table 积己", "stock", "st_No int primary key not null auto_increment, s_No int not null, p_No int not null, st_Count int not null, foreign key(s_No) references seller(s_No) "+cascade+", foreign key(p_No) references product(p_No) "+cascade);
			createTable("event Table 积己", "event", "e_No int primary key not null auto_increment, p_No int not null, e_Month int not null, foreign key(p_No) references product(p_No) "+cascade);
			createTable("purchase Table 积己", "purchase", "pu_No int primary key not null auto_increment, s_No int not null, p_No int not null, u_No int not null, pu_Count int not null, pu_Price int not null, pu_Date date not null, foreign key(s_No) references seller(s_No) "+cascade+", foreign key(p_No) references product(p_No) "+cascade+", foreign key(u_No) references user(u_No) "+cascade);
			createTable("receive Table 积己", "receive", "r_No int primary key not null auto_increment, pu_No int not null, r_Time datetime not null, foreign key(pu_No) references purchase(pu_No) "+cascade);
			
			stmt.execute("update receive set r_Time = null where r_Time = '0000-00-00 00:00:00'");
			stmt.execute("create view v1 as SELECT pu.pu_No, pu.pu_Count, pu.pu_Price, pu.pu_Date, s.s_No, s.s_Addr, u.u_No, u.u_Addr, p.p_No, c.c_No, c.c_Name, p.p_Name FROM purchase pu inner join receive r on r.pu_No=pu.pu_No inner join seller s on s.s_No=pu.s_No inner join user u on u.u_No=pu.u_No inner join product p on p.p_No=pu.p_No inner join category c on c.c_No=p.c_No");
			
			r= "<html><b>DB 备己 己傍";
			state = true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			r = "<html><b>DB 备己 角菩";
			state = false;
			e.printStackTrace();
		} finally {
			for (int i = 0; i < 5; i++) {
				iLog(r + ", "+(5-i)+"檬 饶 辆丰邓聪促.", state);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			this.dispose();
		}
	}
	
	<T extends JComponent> T sz(T c, int w,int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}

	public static void main(String[] args) {
		new DB();
	}

}
