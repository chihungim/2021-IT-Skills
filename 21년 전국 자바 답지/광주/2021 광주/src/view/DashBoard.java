package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class DashBoard extends BaseDialog {

	String mail, number, pw, fee, about;
	JComboBox<String> cate = new JComboBox<String>();
	JLabel img;
	JTextArea area = new JTextArea();
	PlaceHolderField txt[] = { new PlaceHolderField(""), new PlaceHolderField(""), new PlaceHolderField("비밀번호"),
			new PlaceHolderField("상호명"), null, new PlaceHolderField("배달 수수료") };

	public DashBoard() {
		super("판매자 대시보드", 800, 600);
		data();
		ui();
		setVisible(true);
	}

	void ui() {
		setLayout(new BorderLayout(5, 5));
		var w = new JPanel(new BorderLayout());
		var w_c = new JPanel(new GridLayout(0, 1, 5, 5));
		var c = new JPanel(new BorderLayout(5, 5));
		var c_n = new JPanel(new GridLayout(0, 1, 5, 5));
		var c_c = new JPanel(new BorderLayout());
		var c_s = new JPanel(new GridLayout(0, 1, 5, 5));

		add(w, "West");
		add(c);

		w.add(w_c);

		c.add(c_n, "North");
		c.add(c_c);
		c.add(c_s, "South");

		img = new JLabel(sname, JLabel.CENTER);
		w.add(size(img, 180, 180), "North");
		for (var bcap : "메뉴관리,주문관리,통계".split(",")) {
			w_c.add(btn(bcap, a -> {
				if (a.getActionCommand().equals("메뉴관리")) {

				} else if (a.getActionCommand().equals("주문관리")) {
					new OrderManage().addWindowListener(new Before(this));
				} else {

				}
			}));
		}

		String arr[] = { mail, number, pw, sname, null, fee };

		txt[0].setEnabled(false);
		txt[1].setEnabled(false);

		for (int i = 0; i < txt.length; i++) {
			if (txt[i] != null) {
				c_n.add(txt[i]);
				txt[i].setText(arr[i]);
			} else {
				c_n.add(cate);
			}
		}

		c_c.add(lbl("가게 설명", JLabel.LEFT), "North");
		c_c.add(area);
		area.setText(about);
		for (var bcap : "저장,배경 사진 등록".split(",")) {
			c_s.add(btn(bcap, a -> {
				if (a.getActionCommand().equals("저장")) {
					for (int i = 0; i < txt.length; i++) {
						if (txt[i] != null) {
							if (txt[i].getText().equals("")) {
								eMsg("기본정보는 모두 입력해야 합니다.");
								return;
							}
						}
					}

					if (txt[5].getText().matches(".*[^0-9].*")) {
						eMsg("배달 수수료는 숫자로 입력해야합니다.");
						return;
					}
					execute("update seller set pw = '" + txt[2].getText() + "', name = '" + txt[3].getText()
							+ "', category = " + (cate.getSelectedIndex() + 1) + ", deliveryfee = " + txt[5].getText()
							+ ", about = '" + area.getText() + "' where no = " + sno);
					iMsg("저장 되었습니다.");

				} else {
					FileDialog file = new FileDialog(DashBoard.this, "", FileDialog.LOAD);
					file.setFile("*.png");
					file.setVisible(true);
					File f = new File(file.getDirectory() + file.getFile());
					String path = "./지급자료/프로필/" + sno + ".png";
					if (!f.exists())
						return;

					if (new File(path).exists()) {
						new File(path).delete();
					}

					Path from = Paths.get(f.getPath());
					Path to = Paths.get(path);

					try {
						Files.copy(from, to);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					img.setIcon(getIcon("./지급자료/프로필/" + sno + ".png", 120, 120));
					iMsg("프로필 사진이 등록되었습니다.");
				}
			}));
		}
		if (new File("./지급자료/프로필/" + sno + ".png").exists()) {
			img.setIcon(getIcon("./지급자료/프로필/" + sno + ".png", 120, 120));
		} else {
			img.setIcon(getIcon("./지급자료/프로필/upload.png", 120, 120));
		}
		img.setVerticalTextPosition(JLabel.BOTTOM);
		img.setHorizontalTextPosition(JLabel.CENTER);
		area.setBorder(new LineBorder(Color.LIGHT_GRAY));
		area.setLineWrap(true);
		w_c.setBorder(new EmptyBorder(0, 0, 250, 0));
		setBorder((javax.swing.JPanel) getContentPane(), new EmptyBorder(5, 5, 5, 5));
	}

	void data() {
		try {
			var rs = stmt.executeQuery("select * from category");
			while (rs.next()) {
				cate.addItem(rs.getString(2));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			var rs = stmt.executeQuery("select * from seller where no= " + sno);
			if (rs.next()) {
				mail = rs.getString("EMAIL");
				number = rs.getString("PHONE");
				pw = rs.getString("PW");
				about = rs.getString("ABOUT");
				cate.setSelectedIndex(rs.getInt("CATEGORY") - 1);
				fee = rs.getString("DELIVERYFEE");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		sname = "시;ㅈㅎㄱㄷ";
		sno = 1;
		new DashBoard();
	}
}
