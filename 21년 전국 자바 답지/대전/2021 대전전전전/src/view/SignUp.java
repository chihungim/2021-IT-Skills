package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import db.DBManager;

public class SignUp extends BaseFrame {
	
	JLabel img;
	JTextField txt[] = new JTextField[3];
	String cap[] = "�̸�,ID,PW".split(",");
	String fpath;
	
	public SignUp() {
		super("ȸ������", 400, 250);
		
		this.add(w = new JPanel(new BorderLayout(20, 20)), "West");
		this.add(c = new JPanel(new GridLayout(0, 1)));
		
		w.add(img = sz(new JLabel(), 150, 150));
		setLine(img, Color.black);
		w.add(btn("���� ���", a->{
			JFileChooser jfc = new JFileChooser("./Datafiles/ȸ������");
			jfc.setMultiSelectionEnabled(false);
			if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				fpath = jfc.getSelectedFile().getPath();
				img.setIcon(img(fpath, 130, 130));
			}
		}), "South");
		sz(w, 130, 1);
		
		for (int i = 0; i < txt.length; i++) {
			var tmp = new JPanel();
			tmp.add(sz(new JLabel(cap[i]+" : ", 2), 60, 25));
			tmp.add(sz(txt[i] = new JTextField(), 150, 25));
			c.add(tmp);
		}
		
		var cs = new JPanel(new FlowLayout(2));
		c.add(cs);
		cs.add(btn("ȸ������", a->{
			String name = txt[0].getText(), id = txt[1].getText(), pw = txt[2].getText();
			if (name.isEmpty() || id.isEmpty() || pw.isEmpty()) {
				eMsg("��ĭ�� �ֽ��ϴ�.");
				return;
			}
			if (!DBManager.getOne("select * from user where u_id='"+id+"'").isEmpty()) {
				eMsg("�̹� �����ϴ� ���̵��Դϴ�.");
				txt[1].setText("");
				txt[1].requestFocus();
				return;
			}
			
			if (!(pw.matches(".*[0-9].*") && pw.matches(".*[a-zA-Z].*") && pw.matches(".*[!@#$%^&?*].*") && pw.length() < 4)) {
				eMsg("��й�ȣ�� Ȯ�����ּ���.");
				return;
			}
			
			if (img.getIcon()==null) {
				eMsg("������ ������ּ���.");
				return;
			}
			
			try (var pst = DBManager.con.prepareStatement("insert into user values(0, ?, ?, ?, ?)")) {
				for (int i = 0; i < txt.length; i++) 
					pst.setObject(i+1, txt[i].getText());
				pst.setObject(4, new FileInputStream(new File(fpath)));
				pst.execute();
				ImageIO.write(ImageIO.read(new File(fpath)), "jpg", new File("./Datafiles/ȸ������/"+DBManager.getOne("select max(u_no)+1 from user")+".jpg"));
				iMsg("ȸ�������� �Ϸ�Ǿ����ϴ�.");
				this.dispose();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}));
		cs.add(btn("���", a->{
			
		}));
		
		setEmpty((JPanel)getContentPane(), 10, 10, 10, 10);
		setEmpty(w, 10, 0, 10, 0);
		setEmpty(c, 0, 10, 0, 0);
		
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new SignUp();
	}
	
}
