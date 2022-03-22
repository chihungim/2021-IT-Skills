package 전국대전자바;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Sign extends BaseFrame {
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel w = new JPanel(new BorderLayout()), e = new JPanel(new BorderLayout());
	JPanel w_c = new JPanel(), w_s = new JPanel(), e_c = new JPanel(), e_s = new JPanel(new FlowLayout(2));
	JPanel e_c_w = new JPanel(new GridLayout(0, 1, 25, 25)), e_c_e = new JPanel(new GridLayout(0, 1, 25, 25));
	JLabel imglbl = new JLabel(), namelbl = new JLabel("이름 :"), idlbl = new JLabel("ID :"), pwlbl = new JLabel("PW :");
	JTextField txt[] = { new JTextField(17), new JTextField(17), new JPasswordField(17) };
	JButton imgbtn = new JButton("사진 등록"), signbtn = new JButton("회원가입"), cancelbtn = new JButton("취소");
	JFileChooser chooser = new JFileChooser();
	boolean imageUpdate;

	public Sign() {
		super("회원가입", 430, 230);
		setLayout(new FlowLayout(0));

		ui();
		data();
		event();
		setVisible(true);
	}

	private void data() {
		e.setBorder(new EmptyBorder(5, 5, 0, 0));
		e_c_w.setBorder(new EmptyBorder(5, 5, 0, 20));
		imglbl.setPreferredSize(new Dimension(130, 130));
		imglbl.setBorder(new LineBorder(Color.black));
		imgbtn.setPreferredSize(new Dimension(130, 30));
		chooser.setCurrentDirectory(new File("./Datafiles/회원사진"));
	}

	private void event() {
		imgbtn.addActionListener(a -> {
			int choose = chooser.showOpenDialog(null);
			if (choose == JFileChooser.APPROVE_OPTION) {
				String path = chooser.getSelectedFile().getPath();
				imglbl.setIcon(new ImageIcon(
						Toolkit.getDefaultToolkit().getImage(path).getScaledInstance(130, 130, Image.SCALE_SMOOTH)));
				imageUpdate = true;
			}
		});

		signbtn.addActionListener(a -> {
			if (txt[0].getText().equals("") || txt[1].getText().equals("") || txt[2].getText().equals("")) {
				eMsg("빈칸이 있습니다.");
				return;
			} else if (!(txt[2].getText().matches(".*[a-zA-Z].*") && txt[2].getText().matches(".*[0-9].*")
					&& txt[2].getText().matches(".*[!@\\^].*")) || txt[2].getText().length() < 4) {
				eMsg("비밀번호를 확인해주세요.");
				return;
			} else if (imageUpdate == false) {
				eMsg("사진을 등록해주세요.");
				return;
			}
			try {
				ResultSet rs = stmt.executeQuery("select * from user where u_id = '" + txt[1] + "'");
				if (rs.next()) {
					eMsg("이미 존재하는 아이디입니다.");
					txt[0].setText("");
					txt[1].setText("");
					txt[2].setText("");
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			iMsg("회원가입이 완료되었습니다.");
			execute("insert into user values (0,'" + txt[0].getText() + "','" + txt[1].getText() + "','"
					+ txt[2].getText() + "', LOAD_FILE('" + chooser.getSelectedFile().toPath() + "'))");

			dispose();
		});

		cancelbtn.addActionListener(a -> {
			dispose();
		});
	}

	private void ui() {
		add(mainp);
		mainp.add(w, "West");
		mainp.add(e, "East");
		w.add(w_c, "Center");
		w.add(w_s, "South");
		e.add(e_c, "Center");
		e.add(e_s, "South");
		e_c.add(e_c_w, "West");
		e_c.add(e_c_e, "East");

		w_c.add(imglbl);
		w_s.add(imgbtn);
		e_c_w.add(namelbl);
		e_c_w.add(idlbl);
		e_c_w.add(pwlbl);
		for (int i = 0; i < txt.length; i++) {
			e_c_e.add(txt[i]);
		}
		e_s.add(signbtn);
		e_s.add(cancelbtn);

	}

	public static void main(String[] args) {
		new Sign();
	}
}
