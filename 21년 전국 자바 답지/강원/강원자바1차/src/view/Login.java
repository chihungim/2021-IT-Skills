package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import db.DB;
import db.DBManager;

public class Login extends BaseFrame {

	JPanel p;
	JTextField txt[] = { new HolderField(15, "id"), new HolderPWField(15, "Password") };
	
	public Login() {
		super("버스예매", 1200, 600);
		
		this.add(w = pnl(new BorderLayout()), "West");
		this.add(p = pnl(new GridBagLayout()));
		p.add(c = pnl(new BorderLayout(5,5)));
		
		w.add(new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급파일/images/login.jpg").getScaledInstance(750, 600, Image.SCALE_SMOOTH))));
		
		var cc = pnl(new BorderLayout(5,5));
		var ccc = pnl(new GridLayout(0, 1));
		var ccs = pnl(new BorderLayout());
		
		c.add(cc);
		cc.add(ccc);
		cc.add(ccs, "South");
		
		cc.add(lbl("로그인", 2, 20), "North");
		for (int i = 0; i < txt.length; i++) {
			var tmp = pnl(new FlowLayout());
			tmp.add(sz(txt[i], 150, 30));
			ccc.add(tmp);
		}
		
		cc.add(sz(btn("다음", a->{
			String id = txt[0].getText(), pw = txt[1].getText();
			
			if (id.isEmpty()) {
				new eMsg("아이디를 입력해주세요.");
				return;
			}
//			
			if (pw.isEmpty()) {
				new eMsg("비밀번호를 입력해주세요.");
				return;
			}
			
			if (id.equals("admin") && pw.equals("1234")) {
				new iMsg("관리자로 로그인합니다.");
				new Admin();
				return;
			}
			
			if (DBManager.getOne("select * from user where id='"+id+"' and pwd= '"+pw+"'").isEmpty()) return;
			
			System.out.println("select * from user where id='"+id+"' and pwd= '"+pw+"'");
			uno = DBManager.getOne("select * from user where id='"+id+"' and pwd= '"+pw+"'");
			new Main().addWindowListener(new Before(l));
			
		}), 80, 80), "East");
		
		ccs.add(lbl("아이디/비밀번호 찾기", 2, new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Find();
			}
		}), "West");
		
		ccs.add(sz(theme(), 80, 25), "East");
		
		c.add(s = pnl(new FlowLayout(0)), "South");
		s.add(lbl("새로운 계정 만들기 →", 2, new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new SignUp();
			}
		}));
		
		
		setEmpty(s, 50, 0, 0, 0);
		
	}
	
	public static void main(String[] args) {
		l.setVisible(true);
	}
	
}
