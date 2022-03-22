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

public class Sign extends Basedialog {
	String str1[] = "��ȭ��ȣ,�̸���,��й�ȣ,�̸�,�ּ�,��� ������".split(",");
	String str2[] = "�� ��ȣ�� ������ �̹� �ֽ��ϴ�.,�� �̸����� ������ �̹� �ֽ��ϴ�.,,,,".split(",");
	String str3[] = {"^[0-9]{3}-[0-9]{4}-[0-9]{4}$", "^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z]{3,}@(outlook|daum|naver|gmail).(net|com|kr)$", "^(?=.*[0-9])(?=.*[~`!@#$%^&*()-])(?=.*[a-zA-Z]).{8,12}$", "", "", "^[0-9]+$"};
	
	int level;
	
	public Sign() {
		super("ȸ������", 450, 300);
		
		phText = "000-0000-0000;example@example.net;��ҹ���, ����, Ư����ȣ�� ������ 8�� �̻�;ȫ�浿;Ŭ���Ͽ� �ּҸ� �Է��ϱ�;000".split(";");
		
		np.setLayout(new FlowLayout(FlowLayout.LEFT));
		size(np, getWidth()-20, 50);
		np.add(jl1 = label("<html><font size=6>�����ϱ�.</font>", 2, 30, getWidth()-20, 50));
		
		cp.setLayout(new GridLayout(3,1));
		cp.add(jl2 = label(str1[0], 2, 13, 0, 0));
		cp.add(jtf1 = textPH(0, 15, 0, 0));
		cp.add(jl3 = label("", 2, 13, 0, 0));
		jl3.setForeground(Color.RED);
		
		sp.setLayout(new GridLayout(2, 1));
		sp.add(jb1=new JButton("����"));
	
		sp.add(jp1 = new JPanel());
		jp1.add(label("�̹� ��ɹ�� ȸ���̽ʴϱ�?", JLabel.RIGHT, 10, 150, 20));
		jp1.add(jl4=labelB("�α���", JLabel.CENTER, 12));
		
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
			if(level<6) {
				//�� ĭ �Ǵ� �÷��̽� Ȧ�� �⺻�� �˻�
				if (jtf1.getText().equals("") || jtf1.getText().equals(phText[level])) {
					jl3.setText(str1[level] + "�� �Է����ּ���.");
					return;
				}
				
				//���Խ� �˻�
				if ((level<3 || level==5) && !jtf1.getText().matches(str3[level])) {
					System.out.println();
					jl3.setText(str1[level] + " ������ �ùٸ��� �ʽ��ϴ�.");
					return;
				}
				
				//��ȭ��ȣ, �̸��� ���� ���� �˻�
				if(level<2) {
					try {
						ResultSet rs = stmt.executeQuery("Select * from (SELECT email, pw, phone, '������' kind FROM `user` Union SELECT email, pw, phone, '�Ǹ���' kind FROM seller  Union SELECT email, pw, phone, '���̴�' kind FROM rider) as sub where sub.email='" + jtf1.getText() + "' or phone='" + jtf1.getText() + "'");
						if(rs.next()) {
							jl3.setText(str2[level]);
							return;
						}
					} catch (SQLException e1) {
					}
				}
				
				level++;
			}
			
			//���� �ܰ� �Ǵ� �α��� ������ �̵�
			if(level<6) {
				jl2.setText(str1[level]);
				jtf1.setText(phText[level]);
				jtf1.setName(level+"");
				jtf1.setForeground(Color.LIGHT_GRAY);
				
				jl3.setText("");
			} else {
				new Login();
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		new Login();
	}

	public static void main(String[] args) {
		new Sign();
	}
}
