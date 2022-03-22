package ������;

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

	String wbcap[] = "�޴�����,�ֹ�����,���".split(",");
	String sbcap[] = "����,��� ���� ���".split(",");
	JLabel name;
	JLabel profile;

	JComponent centerComponents[] = { new JTextField(20), new JTextField(20), new PlaceHolderTextField(20),
			new PlaceHolderTextField(20), new JComboBox<String>(), new PlaceHolderTextField(20),
			lbl("���� ����", JLabel.LEFT, 12), new JTextArea() };

	public DashBorard() {
		super("�Ǹ��� ��ú���", 700, 500);
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
				if (a.getActionCommand().equals("����")) {
					for (int i = 2; i < 6; i++) {
						if (i == 4)
							continue;

						if (((PlaceHolderTextField) centerComponents[i]).getText().isEmpty()) {
							eMsg("�⺻ ������ ��� �Է��ؾ��մϴ�.");
							return;
						}
					}
					if (((PlaceHolderTextField) centerComponents[5]).getText().matches(".*[^0-9].*")) {
						eMsg("��� ������� ���ڷ� �Է��ؾ��մϴ�.");
						return;
					}
					execute("update seller set PW = '" + ((PlaceHolderTextField) centerComponents[2]).getText()
							+ "' ,  name = '" + ((PlaceHolderTextField) centerComponents[3]).getText()
							+ "' ,  category = '" + (((JComboBox<String>) centerComponents[4]).getSelectedIndex() + 1)
							+ "',  deliveryfee = '" + ((PlaceHolderTextField) centerComponents[5]).getText()
							+ "',  about = '" + ((JTextArea) centerComponents[7]).getText() + "' where seller.no = '"
							+ sno + "' ");
					iMsg("������ �����Ǿ����ϴ�");

				} else {
					FileDialog df = new FileDialog(this, "", FileDialog.LOAD);
					df.setDirectory(System.getProperty("user.home") + File.separator + "Documents");
					df.setVisible(true);
					File file = new File(df.getDirectory() + df.getFile());
					String path = "./�����ڷ�/������/" + sno + ".png";

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

					profile.setIcon(new ImageIcon(getImage("./�����ڷ�/������/" + sno + ".png", 100, 100)));
					iMsg("������ ������ ��ϵǾ����ϴ�.");
				}
			}));
		}

		c.add(tmp);

		if (new File("./�����ڷ�/������/" + sno + ".png").exists()) {
			w.add(profile = new JLabel(new ImageIcon(getImage("./�����ڷ�/������/" + sno + ".png", 100, 100))));
		} else {
			w.add(profile = new JLabel(new ImageIcon(getImage("./�����ڷ�/������/upload.png", 100, 100))));
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
				case "�޴�����":
					new FoodManage().addWindowListener(new Before(this));
					break;
				case "�ֹ�����":
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
				((PlaceHolderTextField) centerComponents[2]).setPlaceHolder("��й�ȣ");
				((PlaceHolderTextField) centerComponents[3]).setPlaceHolder("��ȣ��");
				((PlaceHolderTextField) centerComponents[5]).setPlaceHolder("��޼�����");
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
		sname = "�󺥴� ���ñ�";
		new DashBorard();

	}
}
