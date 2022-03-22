package view;

import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class IdPwFind extends BaseDialog {

	int xywh[][] = { { 18, 28, 401, 217 }, { 18, 257, 401, 269 } };

	JPanel findID, findPW;

	JTextField idField[] = { HintText(new JTextField(), "Name"), HintText(new JTextField(), "E-mail") };
	JTextField pwField[] = { HintText(new JTextField(), "Name"), HintText(new JTextField(), "Id"),
			HintText(new JTextField(), "E-mail") };

	JComponent jc[] = { findID = new JPanel(), findPW = new JPanel() };

	{
		findID.setLayout(new GridLayout(0, 1, 5, 5));
		findPW.setLayout(new GridLayout(0, 1, 5, 5));

		findID.add(lbl("아이디 찾기", JLabel.LEFT, 20));
		for (int i = 0; i < idField.length; i++) {
			findID.add(idField[i]);
		}
		findID.add(btn("계속", a -> findID()));

		findPW.add(lbl("아이디 찾기", JLabel.LEFT, 20));
		for (int i = 0; i < pwField.length; i++) {
			findPW.add(pwField[i]);
		}
		findPW.add(btn("계속", a -> findPW()));
	}

	public IdPwFind(BaseFrame f) {
		super("아이디/비밀번호 찾기", 450, 600, f);
		setUI();
		setVisible(true);
	}

	void findID() {
		if ((idField[0].getText().contentEquals("") || idField[0].getText().equals("Name"))
				|| (idField[1].getText().contentEquals("") || idField[1].getText().equals("E-mail"))) {
			emsg = new eMsg("공란을 확인해주세요.");
			return;
		}

		try {
			var rs = stmt.executeQuery("select * from user where name = '" + idField[0].getText() + "' and email = '"
					+ idField[1].getText() + "'");
			if (rs.next()) {
				imsg = new iMsg("귀하의 Id는 " + rs.getString(2) + "입니다.");
			} else {
				emsg = new eMsg("존재하지 않는 정보입니다.");
				return;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	void findPW() {
		if ((pwField[0].getText().contentEquals("") || pwField[0].getText().equals("Name"))
				|| (pwField[1].getText().contentEquals("") || pwField[1].getText().equals("Id"))
				|| (pwField[2].getText().contentEquals("") || pwField[2].getText().equals("E-mail"))) {
			emsg = new eMsg("공란을 확인해주세요.");
			return;
		}

		try {
			var rs = stmt.executeQuery("select * from user where name = '" + pwField[0].getText() + "' and id = '"
					+ pwField[1].getText() + "' and email = '" + pwField[2].getText() + "'");
			if (rs.next()) {
				imsg = new iMsg("귀하의 Id에 PW는 " + rs.getString(3) + "입니다.");
			} else {
				emsg = new eMsg("존재하지 않는 정보입니다.");
				return;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	void setUI() {
		setLayout(null);
		for (int i = 0; i < jc.length; i++) {
			add(jc[i]);
			jc[i].setBounds(xywh[i][0], xywh[i][1], xywh[i][2], xywh[i][3]);
		}
	}
}
