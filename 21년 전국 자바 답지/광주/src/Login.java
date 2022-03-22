import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

public class Login extends Basedialog {
	String str1[] = "�̸��� �ּ� �Ǵ� �޴�����ȣ�� �α����ϼ���.,��й�ȣ�� �Է��ϰ� �α����ϼ���.".split(",");
	String str2[] = "�� �̸��� �Ǵ� �޴����� ã�� �� �����ϴ�.".split(",");
	
	int level;
	
	public Login() {
		super("�α���", 450, 300);
		
		phText = "�̸��� �ּ� �Ǵ� �޴�����ȣ;��ҹ���, ����, Ư�����ڸ� ������ 8�� �̻�".split(";");
		
		np.setLayout(new FlowLayout(FlowLayout.LEFT));
		size(np, getWidth()-20, 50);
		np.add(jl1 = label("<html><font size=6>���ƿ��Ű��� ȯ���մϴ�.</font>", 2, 30, getWidth()-20, 50));
		
		cp.setLayout(new GridLayout(3,1));
		cp.add(jl2 = label(str1[0], 2, 13, 0, 0));
		cp.add(jtf1 = textPH(0, 15, 0, 0));
		cp.add(jl3 = label("", 2, 13, 0, 0));
		jl3.setForeground(Color.RED);
		
		sp.setLayout(new GridLayout(2, 1));
		sp.add(jb1=new JButton("�α���"));
	
		sp.add(jp1 = new JPanel());
		jp1.add(label("��ɹ���� ó���̽ʴϱ�?", JLabel.RIGHT, 10, 150, 20));
		jp1.add(jl4=labelB("���� �����", JLabel.CENTER, 12));
		
		jl4.setForeground(Color.GREEN.darker().darker());
		jl4.setBorder(new MatteBorder(0, 0, 1, 0, green));
		
		emp((JPanel)getContentPane(), 10, 10, 10, 10);
		emp(cp, 10, 0, 0, 0);
		emp(sp, 30, 0, 0, 0);
		
		//�̺�Ʈ
		jb1.addActionListener(this);
		jl4.addMouseListener(this);
		
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(jb1)) {
			if(level<1) {
				try {
					ResultSet rs = stmt.executeQuery("Select * from (SELECT email, pw, phone, '������' kind FROM `user` Union SELECT email, pw, phone, '�Ǹ���' kind FROM seller  Union SELECT email, pw, phone, '���̴�' kind FROM rider) as sub where sub.email='" + jtf1.getText() + "' or phone='" + jtf1.getText() + "'");
					if(rs.next()) {
						jl2.setText(str1[1]);
						jtf1.setText(phText[1]);
						jtf1.setName(1+"");
						jtf1.setForeground(Color.LIGHT_GRAY);
					}
					else {
						jl3.setText(str2[level]);
					}
				} catch (SQLException e1) {
				}
			} else {
				try {
					ResultSet rs = stmt.executeQuery("Select * from (SELECT email, pw, phone, '������' kind FROM `user` Union SELECT email, pw, phone, '�Ǹ���' kind FROM seller Union SELECT email, pw, phone, '���̴�' kind FROM rider) as sub where sub.pw='" + jtf1.getText() + "'");
					if(rs.next()) {
						if(rs.getString(3).equals("������")) {
							new Mainf().addWindowListener(new be(Login.this));
						}else if(rs.getString(3).equals("�Ǹ���")) {
							new Seller().addWindowListener(new be(Login.this));
						}else {
							new Delivery().addWindowListener(new be(Login.this));
						}
					}else {
						errmsg("�Է��� ��й�ȣ�� �ùٸ��� �ʽ��ϴ�.");
						return;
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		new Sign().addWindowListener(new be(Login.this));
	}

	public static void main(String[] args) {
		new Login();
	}
}
