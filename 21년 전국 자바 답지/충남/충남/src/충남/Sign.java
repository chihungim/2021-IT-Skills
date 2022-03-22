package 충남;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Sign extends BaseFrame {
	JTextField tt[] = new JTextField[3], tt2[] = new JTextField[3], tt3[] = new JTextField[3];
	String str[] = "아이디,비밀번호,이름,생년월일,휴대전화".split(","), aa[] = "월,일".split(","),bb[] = "yyyy,mm,dd".split(",");
	JPanel cc = new JPanel(new GridLayout(0, 1));
	
	public Sign() {
		super("회원가입", 380, 350);
		
		add(c = new JPanel(new BorderLayout()));
		add(s = new JPanel(new FlowLayout(2)),"South");
		
		c.add(cc);
		
		for (int i = 0; i < tt.length; i++) {
			JPanel temp = new JPanel(new FlowLayout(0));
			temp.add(siz(label(str[i], 2), 60, 25));
			temp.add(siz(tt[i] = new JTextField(), 260, 30));
			cc.add(temp);
		}
		
		JPanel date = new JPanel(new FlowLayout(0));
		date.add(siz(label(str[3], 2), 60, 25));
		
		for (int i = 0; i < tt2.length; i++) {
			if(i == 0) {
				date.add(siz(tt2[i] = new JTextField(), 90, 30));
			}else {
				date.add(siz(tt2[i] = new JTextField(), 65, 30));
				date.add(label(aa[i - 1], 2));
			}
			hold(tt2[i], bb[i]);
		}
		
		cc.add(date);
		
		JPanel p = new JPanel(new FlowLayout(0));
		p.add(siz(label(str[4], 2), 60, 25));
		
		for (int i = 0; i < tt3.length; i++) {
			p.add(siz(tt3[i] = new JTextField(), 78, 30));
			
			if(i < tt3.length - 1) {
				p.add(new JLabel("-"));
			}
			hold(tt3[i], i==0?"000":"0000");
		}
		
		cc.add(p);
		
		s.add(btn("회원가입", a->{
			for (int i = 0; i < tt.length; i++) {
				if(tt[i].getText().isEmpty() || tt2[i].getText().isEmpty() || tt3[i].getText().isEmpty()) {
					errmsg("빈칸을 모두 입력해야 합니다.");
					return;
				}
			}
			
			if(!getone("select * from user where id = '"+tt[0].getText()+"'").equals("")) {
				errmsg("이미 사용중인 아이디 입니다.");
				return;
			}
			if(!(tt[1].getText().matches(".*[0-9].*") && tt[1].getText().matches(".*[a-zA-Z].*") && tt[1].getText().matches(".*[!@#$].*"))) {
				errmsg("비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.");
				return;
			}
			try {
				LocalDate day = LocalDate.of(toint(tt2[0].getText()), toint(tt2[1].getText()), toint(tt2[2].getText()));
				if(LocalDate.now().isBefore(day)) {
					errmsg("생년월일은 미래가 아니어야 합니다.");
					return;
				}
			String asd = tt3[0].getText() + "-" + tt3[1].getText()+"-"+tt3[2].getText();
			if(!asd.matches("\\d{3}-\\d{4}-\\d{4}")) {
				errmsg("전화번호를 올바른 형식으로 입력해야 합니다.");
				return;
			}
			
			msg("회원가입이 완료되었습니다.");
			execute("insert into user values(0,'"+tt[0].getText()+"','"+tt[1].getText()+"','"+tt[2].getText()+"','"+day+"','"+asd+"')");
			dispose();
			
			} catch (Exception e) {
				errmsg("생년월일은 올바른 형식으로 입력해야 합니다.");
				return;
			}
		}));
		
		setVisible(true);
	}

}
