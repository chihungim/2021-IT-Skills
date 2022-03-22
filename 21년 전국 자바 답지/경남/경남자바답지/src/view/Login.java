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

	JRadioButton rbtn[] = { new JRadioButton("개인회원"), new JRadioButton("기업회원") };
	ButtonGroup bg = new ButtonGroup();
	String cap[] = "아이디,비밀번호".split(",");
	JTextField txt[] = { new JTextField(), new JPasswordField() };
	String bcap[] = "로그인,회원가입".split(",");

	public Login() {
		super("로그인", 400, 250);

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
				tmp.add(jl = new JLabel("숨김"));
				jl.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (jl.getText().contentEquals("숨김")) {
							jl.setText("보임");
							((JPasswordField) txt[1]).setEchoChar((char) 0);
						} else {
							jl.setText("숨김");
							((JPasswordField) txt[1]).setEchoChar('♥');
						}

					}
				});
			}
			c_c.add(tmp);
		}

		((JPasswordField) txt[1]).setEchoChar('♥');
		for (int i = 0; i < bcap.length; i++) {
			s.add(sz(btn(bcap[i], a -> {
				if (a.getActionCommand().contentEquals(bcap[0])) {
					String id = txt[0].getText(), pw = txt[1].getText();
					if (id.isEmpty()) {
						eMsg("아이디를 입력해주세요.");
						return;
					}
					if (pw.isEmpty()) {
						eMsg("비밀번호를 입력해주세요.");
						return;
					}
					
					// 로그인
					type = (rbtn[0].isSelected())?"user":"manager";
					try {
						var rs = DBManager.rs("select * from "+type+" where id='"+id+"' and pw ='"+pw+"'");
						if (rs.next()) {
							iMsg(rs.getString("name")+"님 로그인되었습니다.");
							isLogin = true;
							if (type.equals("user")) {
								uno = rs.getInt(1);
								Main.btn[3].setText("이력서");
								Main.btnLogin.setText("로그아웃");
								Main.btnSign.setText("회원수정");
							} else {
								mno = rs.getInt(1);
								Main.btn[3].setText("공고등록");
								Main.btnLogin.setText("로그아웃");
								Main.btnSign.setText("기업수정");
							}
							this.dispose();
						} else {
							eMsg("아이디 또는 비밀번호가 일치하지 않습니다.");
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
//        String[] han1 = { "", "일","이","삼", "사", "오", "육", "칠", "팔", "구" };
//        String[] han2 = { "", "십", "백", "천" };
//        String[] han3 = { "", "만", "억", "조", "경" };
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
//                result.append(han2[i % 4]); // 십,백,천
//                
//            }
//            // 만, 억, 조 ,경 단위
//            if (i % 4 == 0) {
//                result.append(han3[i / 4]); // 천단위
//                result.append(" ");
//            }
//            
//        }
//        result.append("원");
//        return result.toString();
//    }
//
//	static String[] unit = { "", "십", "백", "천", "만", "십만", "백만", "천만", "억", "십업", "백억", "천억" };
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
//        // 단위 출력을 위한 변수
//        int j = stringNum.length()-1;
//        
//        // 문자열의 길이 만큼 반복문 실행
//        for (int i = 0; i < stringNum.length(); i++) {
//            int n = stringNum.charAt(i) - '0';        // 문자열에 있는 문자를 하나씩 가져와서 int형으로 변환
//            if (readNum(n) != null) {    // 숫자가 0일 경우는 출력하지 않음
//                System.out.print(readNum(n));    // 숫자를 한글로 읽어서 출력
//                System.out.print(unit[j]);        // 단위 출력
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
//            return "일";
//        case 2:
//            return "이";
//        case 3:
//            return "삼";
//        case 4:
//            return "사";
//        case 5:
//            return "오";
//        case 6:
//            return "육";
//        case 7:
//            return "칠";
//        case 8:
//            return "팔";
//        case 9:
//            return "구";
//        }
//        return null;
//    }

}
