package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import view.BaseFrame.Before;

public class Quiz extends BaseFrame {
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel n = new JPanel(new BorderLayout()), c = new JPanel(), s = new JPanel(new BorderLayout());
	JPanel n_w = new JPanel(new FlowLayout(0)), n_e = new JPanel(new FlowLayout(2)), s_n = new JPanel(),
			s_c = new JPanel();
	JLabel lbl[] = { lbl("기회 : ", 2, 13), lbl("3", 2, 13), lbl("번", 2, 13), lbl(" / ", 2, 13), lbl("변경 횟수 : ", 2, 13),
			lbl("1", 2, 13), lbl("번", 2, 13) };
	JLabel timer = new JLabel();
	JLabel imglbl = new JLabel();
	JTextField txt = new JTextField(17);
	JButton btn[] = { new JButton("확인"), new JButton("다음 문제 풀기") };
	ArrayList<String> list = new ArrayList<String>();

	public Quiz() {
		super("퀴즈", 500, 400);

		ui();
		data();
		event();
		setVisible(true);
	}

	void event() {
	}

	void RandomImage() {
		try {
			ResultSet rs1 = stmt.executeQuery("select t_answer from test");
			while (rs1.next()) {
				list.add(rs1.getString(1));
			}
		} catch (SQLException e) {
		}
		int random = (int) ((Math.random() * 24) + 1);
		imglbl.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("Datafiles/퀴즈/" + random + ".jpg")
				.getScaledInstance(480, 250, Image.SCALE_SMOOTH)));
		imglbl.setName(random + "");
		int randomn = (int) ((Math.random() * 24) + 1); // Random r = new Random(): r.nextInt(25)+1;
		for (int i = 0; i < btn.length; i++) {
			btn[i].addActionListener(a -> {
				if (a.getActionCommand().equals("확인")) {
					if (txt.getText().equals("")) {
						eMsg("답을 입력해주세요.");
						return;
					}
					if (!list.contains(txt.getText())) {
						eMsg("정답이 아닙니다.");
					} else {
						if (lbl[1].getText().equals("3")) {
							iMsg("축하합니다.");
							new Main().addWindowListener(new Before(Quiz.this));
							return;
						} else {
							iMsg("정답입니다!");
							imglbl.setIcon(new ImageIcon(
									Toolkit.getDefaultToolkit().getImage("Datafiles/퀴즈/" + random + ".jpg")
											.getScaledInstance(480, 250, Image.SCALE_SMOOTH)));
						}
					}
				} else {
					imglbl.setIcon(
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("Datafiles/퀴즈/" + randomn + ".jpg")
									.getScaledInstance(480, 250, Image.SCALE_SMOOTH)));
					System.out.println(random);
					lbl[5].setText(0 + "");
					repaint();
					revalidate();
				}
				
			});
		}
	}

	void data() {
		RandomImage();
	}

	void ui() {
		add(mainp);
		mainp.add(n, "North");
		mainp.add(c, "Center");
		mainp.add(s, "South");
		n.add(n_w, "West");
		n.add(n_e, "East");
		s.add(s_n, "North");
		s.add(s_c, "Center");

		for (int i = 0; i < lbl.length; i++) {
			n_w.add(lbl[i]);
		}
		n_e.add(timer);
		c.add(imglbl);
		s_n.add(txt);
		for (int i = 0; i < btn.length; i++) {
			s_c.add(btn[i]);
			btn[i].setBackground(Color.white);
		}

	}

	public static void main(String[] args) {
		new Quiz();
	}
}
