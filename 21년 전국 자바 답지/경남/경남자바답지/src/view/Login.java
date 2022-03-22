package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import db.DBManager;

public class Login extends BaseFrame {

	JRadioButton rbtn[] = { new JRadioButton("����ȸ��"), new JRadioButton("���ȸ��") };
	ButtonGroup bg = new ButtonGroup();
	String cap[] = "���̵�,��й�ȣ".split(",");
	JTextField txt[] = { new JTextField(), new JPasswordField() };
	String bcap[] = "�α���,ȸ������".split(",");

	public Login() {
		super("�α���", 400, 250);

		this.add(c = new JPanel(new BorderLayout()));

		var c_c = new JPanel(new GridLayout(0, 1));
		c.add(n = new JPanel(new GridLayout()), "North");
		c.add(c_c);
		c.add(s = new JPanel(new GridLayout(1, 0, 10, 10)), "South");

		n.add(rbtn[0]);
		n.add(rbtn[1]);
		bg.add(rbtn[0]);
		bg.add(rbtn[1]);

		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new FlowLayout(0));
			tmp.add(sz(new JLabel(cap[i], 2), 60, 25));
			tmp.add(sz(txt[i], 150, 25));
			if (i == 1) {
				JLabel jl;
				tmp.add(jl = new JLabel("����"));
				jl.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (jl.getText().contentEquals("����")) {
							jl.setText("����");
							((JPasswordField) txt[1]).setEchoChar((char) 0);
						} else {
							jl.setText("����");
							((JPasswordField) txt[1]).setEchoChar('��');
						}

					}
				});
			}
			c_c.add(tmp);
		}

		((JPasswordField) txt[1]).setEchoChar('��');
		for (int i = 0; i < bcap.length; i++) {
			s.add(sz(btn(bcap[i], a -> {
				if (a.getActionCommand().contentEquals(bcap[0])) {
					String id = txt[0].getText(), pw = txt[1].getText();
					if (id.isEmpty()) {
						eMsg("���̵� �Է����ּ���.");
						return;
					}
					if (pw.isEmpty()) {
						eMsg("��й�ȣ�� �Է����ּ���.");
						return;
					}
					
					// �α���
					type = (rbtn[0].isSelected())?"user":"manager";
					try {
						var rs = DBManager.rs("select * from "+type+" where id='"+id+"' and pw ='"+pw+"'");
						if (rs.next()) {
							iMsg(rs.getString("name")+"�� �α��εǾ����ϴ�.");
							isLogin = true;
							if (type.equals("user")) {
								uno = rs.getInt(1);
								Main.btn[3].setText("�̷¼�");
								Main.btnLogin.setText("�α׾ƿ�");
								Main.btnSign.setText("ȸ������");
							} else {
								mno = rs.getInt(1);
								Main.btn[3].setText("������");
								Main.btnLogin.setText("�α׾ƿ�");
								Main.btnSign.setText("�������");
							}
							this.dispose();
						} else {
							eMsg("���̵� �Ǵ� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
							return;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				} else {
					new SignUp();
				}
			}), 1, 30));
		}

		setEmpty(n, 0, 0, 0, 100);
		setEmpty(c_c, 10, 0, 10, 0);
		setEmpty((JPanel) getContentPane(), 20, 20, 20, 20);

		rbtn[0].setSelected(true);

		this.setVisible(true);
	}
	
//	public static String convert(String money) {
//        String[] han1 = { "", "��","��","��", "��", "��", "��", "ĥ", "��", "��" };
//        String[] han2 = { "", "��", "��", "õ" };
//        String[] han3 = { "", "��", "��", "��", "��" };
// 
//        StringBuffer result = new StringBuffer();
//        int leng = money.length();
//        int initInt=0;
//        
//        for (int i = leng-1; i >= 0; i--) {
//            initInt=Integer.parseInt(String.valueOf(money.charAt(leng-i-1)));
// 
//            if (initInt > 0) {
//                result.append(han1[initInt]);
//                result.append(han2[i % 4]); // ��,��,õ
//                
//            }
//            // ��, ��, �� ,�� ����
//            if (i % 4 == 0) {
//                result.append(han3[i / 4]); // õ����
//                result.append(" ");
//            }
//            
//        }
//        result.append("��");
//        return result.toString();
//    }
//
//	static String[] unit = { "", "��", "��", "õ", "��", "�ʸ�", "�鸸", "õ��", "��", "�ʾ�", "���", "õ��" };
//	
//	public static void main(String[] args) {
//		String text = "1@ddddddd";
//		
//		System.out.println(text.length()>=8 && text.length()<=16);
//		System.out.println(text.matches(".*\\d.*"));
//		System.out.println(text.matches(".*[a-zA-Z].*"));
//		System.out.println(text.matches(".*[!@#$%^&*].*"));
		
//		String phone = "01037651803";
//		System.out.println(phone.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3"));
		
//		System.out.println(convert("12344359"));
//		int num = 10000;
//		String stringNum = Integer.toString(num);
//		 
//        // ���� ����� ���� ����
//        int j = stringNum.length()-1;
//        
//        // ���ڿ��� ���� ��ŭ �ݺ��� ����
//        for (int i = 0; i < stringNum.length(); i++) {
//            int n = stringNum.charAt(i) - '0';        // ���ڿ��� �ִ� ���ڸ� �ϳ��� �����ͼ� int������ ��ȯ
//            if (readNum(n) != null) {    // ���ڰ� 0�� ���� ������� ����
//                System.out.print(readNum(n));    // ���ڸ� �ѱ۷� �о ���
//                System.out.print(unit[j]);        // ���� ���
//            }
//            j--;
//        }
//        System.out.println();
//		
//	}
//	
//	public static String readNum(int num) {
//        switch (num) {
//        case 1:
//            return "��";
//        case 2:
//            return "��";
//        case 3:
//            return "��";
//        case 4:
//            return "��";
//        case 5:
//            return "��";
//        case 6:
//            return "��";
//        case 7:
//            return "ĥ";
//        case 8:
//            return "��";
//        case 9:
//            return "��";
//        }
//        return null;
//    }

}
