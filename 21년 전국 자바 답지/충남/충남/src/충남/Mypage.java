package �泲;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.xdevapi.Result;

public class Mypage extends BaseFrame {

	JTextField txt[] = { new JTextField(), new JPasswordField(), new JTextField() };
	JTextField dateTxt[] = { new JTextField(), new JTextField(), new JTextField() };
	JTextField phoneTxt[] = { new JTextField(), new JTextField(), new JTextField() };
	String cap[] = "���̵�,��й�ȣ,�̸�,�������,�޴���ȭ".split(",");
	String dateCap[] = "��,��".split(",");
	String dateHolder[] = "yyyy,mm,dd".split(",");
	String bcap[] = "ȸ������ ����,��������,Ƽ�����".split(",");

	public static void main(String[] args) {
		NO = 1;
		NAME = "���ο�";
		new Mypage();
	}

	public Mypage() {
		super("����������", 500, 380);
		try {
			data();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		this.add(w = new JPanel(new GridLayout(0, 1, 10, 10)), "West");
		this.add(c = new JPanel(new BorderLayout()));
		this.add(s = new JPanel(new FlowLayout(2)), "South");

		for (int i = 0; i < bcap.length; i++) {
			w.add(btn(bcap[i], a -> {
				if (a.getActionCommand().contentEquals(bcap[0])) {
					for (int j = 0; j < txt.length; j++) {
						if (txt[j].getText().isEmpty() || dateTxt[j].getText().isEmpty()
								|| phoneTxt[j].getText().isEmpty()) {
							errmsg("��ĭ�� ��� �Է��ؾ� �մϴ�.");
							return;
						}
					}

					if (!getone("select * from user where id = '" + txt[0].getText() + "'").isEmpty()) {
						errmsg("�̹� ������� ���̵� �Դϴ�.");
						return;
					}

					// ��й�ȣ ���� ���� Ư������
					if (!(txt[1].getText().matches(".*[0-9].*") && txt[1].getText().matches(".*[a-zA-Z].*")
							&& txt[1].getText().matches(".*[^0-9a-zA-Z��-?].*"))) {
						errmsg("��й�ȣ�� ����, ����, Ư�����ڸ� �����ؾ� �մϴ�.");
						return;
					}

					try {
						LocalDate date = LocalDate.of(toint(dateTxt[0]), toint(dateTxt[1]), toint(dateTxt[2]));
						if (LocalDate.now().isBefore(date)) {
							errmsg("��������� �̷��� �ƴϾ�� �մϴ�.");
							return;
						}

						// ��ȭ��ȣ ����
						String phone = phoneTxt[0].getText() + "-" + phoneTxt[1].getText() + "-"
								+ phoneTxt[2].getText();
						if (!phone.matches("^\\d{3}-\\d{4}-\\d{4}$")) {
							errmsg("��ȭ��ȣ�� �ùٸ� �������� �Է��ؾ� �մϴ�.");
							return;
						}

						msg("ȸ�������� �����Ǿ����ϴ�!");
						execute("update user set pw = '" + txt[1].getText() + "', name ='" + txt[2].getText()
								+ "', birth = '" + date + "', phone ='" + phone + "' where serial ='" + NO + "'");

					} catch (Exception e) {
						errmsg("��������� �ùٸ� �������� �Է��ؾ� �մϴ�.");
						return;
					}

				} else if (a.getActionCommand().contentEquals(bcap[1])) {
					new PurchaseList().addWindowListener(new be(this));
				} else {
					new KeyPad().addWindowListener(new be(this));
				}
			}));
		}

		var c_c = new JPanel(new GridLayout(0, 1));
		c.add(c_c);

		for (int i = 0; i < txt.length; i++) {
			var tmp = new JPanel(new FlowLayout(0));
			tmp.add(siz(new JLabel(cap[i]), 60, 25));
			tmp.add(siz(txt[i], 260, 30));
			c_c.add(tmp);
		}

		var dateP = new JPanel(new FlowLayout(0));
		dateP.add(siz(new JLabel(cap[3]), 60, 25));
		for (int i = 0; i < dateTxt.length; i++) {
			if (i == 0) {
				dateP.add(siz(dateTxt[0], 90, 30));
			} else {
				dateP.add(siz(dateTxt[i], 65, 30));
				dateP.add(new JLabel(dateCap[i - 1]));
			}
			c_c.add(dateP);
		}

		var phoneP = new JPanel(new FlowLayout(0));
		phoneP.add(siz(new JLabel(cap[4]), 60, 25));
		for (int i = 0; i < phoneTxt.length; i++) {
			phoneP.add(siz(phoneTxt[i], 78, 30));
			if (i < phoneTxt.length - 1) {
				phoneP.add(new JLabel("-"));
			}
			c_c.add(phoneP);
		}

		txt[0].setEnabled(false);
		try {
			var rs = stmt.executeQuery("select * from user where serial=" + NO);
			if (rs.next()) {
				for (int i = 0; i < 3; i++) {
					txt[i].setText(rs.getString(i + 2));
				}
				var birth = rs.getString(5).split("-");
				for (int i = 0; i < birth.length; i++) {
					dateTxt[i].setText(birth[i]);
				}
				var phone = rs.getString(6).split("-");
				for (int i = 0; i < phone.length; i++) {
					phoneTxt[i].setText(phone[i]);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.setVisible(true);
	}

}
