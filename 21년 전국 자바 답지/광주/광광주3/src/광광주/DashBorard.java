package 광광주;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

public class DashBorard extends BaseFrame {

	JPanel w, c;

	String wbcap[] = "메뉴관리,주문관리,통계".split(",");
	String sbcap[] = "저장,배경 사진 등록".split(",");
	JLabel name;
	JLabel profile;

	JComponent centerComponents[] = { new JTextField(20), new JTextField(20), new PlaceHolderTextField(20),
			new PlaceHolderTextField(20), new JComboBox<String>(), new PlaceHolderTextField(20),
			lbl("가게 설명", JLabel.LEFT, 12), new JTextArea() };

	public DashBorard() {
		super("판매자 대시보드", 700, 500);
		add(size(w = new JPanel(), 200, 500), "West");
		add(c = new JPanel());
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		w.setLayout(new BoxLayout(w, BoxLayout.Y_AXIS));

		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		c.add(Box.createVerticalStrut(10));
		w.add(Box.createVerticalStrut(10));

		for (int i = 0; i < centerComponents.length; i++) {
			if (i < 6)
				centerComponents[i].setMaximumSize(new Dimension(500, 25));
			if (i != 6)
				centerComponents[i].setBorder(new LineBorder(Color.LIGHT_GRAY));
			if (i == 6) {
				centerComponents[i].setMaximumSize(new Dimension(500, 25));
				centerComponents[i].setAlignmentX(RIGHT_ALIGNMENT);
			}
			c.add(centerComponents[i]);
			c.add(Box.createVerticalStrut(5));
		}

		var tmp = new JPanel(new GridLayout(0, 1, 5, 5));
		for (String sb : sbcap) {
			tmp.add(btn(sb, a -> {
				if (a.getActionCommand().equals("저장")) {
					for (int i = 2; i < 6; i++) {
						if (i == 4)
							continue;

						if (((PlaceHolderTextField) centerComponents[i]).getText().isEmpty()) {
							eMsg("기본 정보는 모두 입력해야합니다.");
							return;
						}
					}
					if (((PlaceHolderTextField) centerComponents[5]).getText().matches(".*[^0-9].*")) {
						eMsg("배달 수수료는 숫자로 입력해야합니다.");
						return;
					}
					execute("update seller set PW = '" + ((PlaceHolderTextField) centerComponents[2]).getText()
							+ "' ,  name = '" + ((PlaceHolderTextField) centerComponents[3]).getText()
							+ "' ,  category = '" + (((JComboBox<String>) centerComponents[4]).getSelectedIndex() + 1)
							+ "',  deliveryfee = '" + ((PlaceHolderTextField) centerComponents[5]).getText()
							+ "',  about = '" + ((JTextArea) centerComponents[7]).getText() + "' where seller.no = '"
							+ sno + "' ");
					iMsg("정보가 수정되었습니다");

				} else {
					FileDialog df = new FileDialog(this, "", FileDialog.LOAD);
					df.setDirectory(System.getProperty("user.home") + File.separator + "Documents");
					df.setVisible(true);
					File file = new File(df.getDirectory() + df.getFile());
					String path = "./지급자료/프로필/" + sno + ".png";

					if (!file.exists()) {
						return;
					}

					if (new File(path).exists()) {
						new File(path).delete();
						Path from = Paths.get(file.getPath());
						Path to = Paths.get(path);
						try {
							Files.copy(from, to);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						Path from = Paths.get(file.getPath());
						Path to = Paths.get(path);
						try {
							Files.copy(from, to);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					profile.setIcon(new ImageIcon(getImage("./지급자료/프로필/" + sno + ".png", 100, 100)));
					iMsg("프로필 사진이 등록되었습니다.");
				}
			}));
		}

		c.add(tmp);

		if (new File("./지급자료/프로필/" + sno + ".png").exists()) {
			w.add(profile = new JLabel(new ImageIcon(getImage("./지급자료/프로필/" + sno + ".png", 100, 100))));
		} else {
			w.add(profile = new JLabel(new ImageIcon(getImage("./지급자료/프로필/upload.png", 100, 100))));
		}

		profile.setAlignmentX(Component.CENTER_ALIGNMENT);
		profile.setPreferredSize(new Dimension(150, 150));

		w.add(name = lbl(sname, JLabel.CENTER));

		name.setAlignmentX(Component.CENTER_ALIGNMENT);

		for (String wb : wbcap) {
			JButton btn;
			w.add(Box.createVerticalStrut(5));
			w.add(btn = btn(wb, a -> {
				switch (a.getActionCommand()) {
				case "메뉴관리":
					new FoodManage().addWindowListener(new Before(this));
					break;
				case "주문관리":
					new OrderMange().addWindowListener(new Before(this));
					break;
				default:
					new Chart().addWindowListener(new Before(this));
					break;
				}
			}));
			btn.setMaximumSize(new Dimension(170, 30));

			btn.setAlignmentX(Component.CENTER_ALIGNMENT);

		}

		((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));
		setData();
		setVisible(true);
	}

	void setData() {
		try {
			var rs = stmt.executeQuery("select * from category");
			while (rs.next()) {
				((JComboBox<String>) centerComponents[4]).addItem(rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			var rs = stmt.executeQuery("SELECT * FROM eats.seller s where s.no = " + sno + ";");
			if (rs.next()) {
				((JTextField) centerComponents[0]).setText(rs.getString(2));
				((JTextField) centerComponents[1]).setText(rs.getString(3));
				((JTextField) centerComponents[0]).setEnabled(false);
				((JTextField) centerComponents[1]).setEnabled(false);
				((PlaceHolderTextField) centerComponents[2]).setText(rs.getString(4));
				((PlaceHolderTextField) centerComponents[3]).setText(rs.getString(5));
				((PlaceHolderTextField) centerComponents[5]).setText(rs.getString(8));
				((PlaceHolderTextField) centerComponents[2]).setPlaceHolder("비밀번호");
				((PlaceHolderTextField) centerComponents[3]).setPlaceHolder("상호명");
				((PlaceHolderTextField) centerComponents[5]).setPlaceHolder("배달수수료");
				((JComboBox<String>) centerComponents[4]).setSelectedIndex(rs.getInt(7) - 1);
				((JTextArea) centerComponents[7]).setText(rs.getString(6));
				((JTextArea) centerComponents[7]).setLineWrap(true);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		sno = 1;
		sname = "라벤더 뭐시기";
		new DashBorard();

	}
}
