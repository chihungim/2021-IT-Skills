package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Ride_apply_edit extends BaseFrame {

	static final int EDIT = 0, APPLY = 1;
	int mode, x, y;
	String title, floor, name;
	Ride r;
	JTextField txt[] = { new JTextField(), new JTextField(), new JTextField(), new JTextField() };
	JComboBox box[] = { new JComboBox(), new JComboBox() };
	JComponent comp[] = { txt[0], txt[1], txt[2], box[0], box[1], txt[3] };
	ArrayList<String> list = new ArrayList<String>();
	JLabel lbl_img;
	JCheckBox chk = new JCheckBox("�����");
	JTextArea area = new JTextArea();
	String path;
	String cap[] = "�ⱸ��,��,ž���ο�,Ű ����,���� ����,�ݾ�".split(",");
	Icon icon;

	public Ride_apply_edit(int mode, String title, String floor, Ride r) {
		super("���̱ⱸ " + (mode == EDIT ? "����" : "���"), 800, 350);
		this.mode = mode;
		this.floor = floor;
		this.name = title;
		System.out.println(floor);
		this.r = r;
		ui();
		data();
		setVisible(true);
	}

	private void data() {
		if (mode == EDIT) {
			var rno = getOne("SELECT r_no FROM adventure.ride where r_name like '%" + name + "%'");
			try {
				var rs = stmt.executeQuery("select r_img from ride where r_no = '" + rno + "'");
				if (rs.next()) {
					lbl_img.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(rs.getBytes(1))
							.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			chk.setSelected(getOne("select r_disable from ride where r_no = " + rno).equals("1"));

			txt[0].setText(name);
			txt[2].setText(getOne("select r_max from ride where r_name = '" + name + "'"));
			txt[3].setText(getOne("select r_money from ride where r_name = '" + name + "'"));
			var info = getOne("select r_explation from ride where r_name = '" + name + "'");
			area.setText(info.split("#")[0]);
			x = toInt(info.split("#")[1]);
			y = toInt(info.split("#")[2]);

			icon = lbl_img.getIcon();
			for (int i = 0; i < txt.length; i++) {
				list.add(txt[i].getText());
			}
			for (int i = 0; i < box.length; i++) {
				list.add(box[i].getSelectedItem().toString());
			}
			list.add(area.getText());
			list.add(chk.isSelected() ? "1" : "0");
		}
	}

	private void ui() {
		var w = new JPanel(new BorderLayout());
		var c = new JPanel(new BorderLayout());
		var s = new JPanel();

		add(w, "West");
		add(c);
		add(s, "South");

		w.add(lbl_img = new JLabel());
//		setBorder(lbl_img, new LineBorder(Color.BLACK));
		w.add(btn("���� ���", a -> {
			JFileChooser jfc = new JFileChooser();
			jfc.setMultiSelectionEnabled(false);
			if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				path = jfc.getSelectedFile().toString();
				lbl_img.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(jfc.getSelectedFile().toString())
						.getScaledInstance(200, 200, Image.SCs
		}), "South");

		var c_c = new JPanel(new GridLayout(0, 2));
		c.add(c_c);
		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel(new FlowLayout(0));
			c_c.add(tmp);

			tmp.add(sz(new JLabel(cap[i] + " : "), 100, 25));
			tmp.add(sz(comp[i], 150, 25));
		}
		c_c.add(chk);

		var c_s = new JPanel();
		sz(c_s, 1, 100);
		c.add(c_s, "South");
		c_s.add(new JLabel("�ⱸ���� : "));
		c_s.add(sz(area, 350, 80));
m		area.setLineWrap(true);
		box[0].setModel(new DefaultComboBoxModel<>("90cm�̻�,110cm�̻�".split(",")));
		box[1].setModel(new DefaultComboBoxModel<>("6���̻�,11���̻�,20���̻�".split(",")));

		txt[1].setEnabled(false);
		txt[1].setText(floor + "");
		sz(w, 200, 1);
		s.add(btn((mode == EDIT ? "����" : "���"), a -> btn_event()));
		this.setVisible(true);
	}

	void btn_event() {
		for (int i = 0; i < txt.length; i++) {
			if (comp[i] instanceof JTextField) {
				if (((JTextField) comp[i]).getText().isEmpty()) {
					eMsg(cap[i] + "�� �Է����ּ���.");
					return;
				}
			} else {
				if ((((JComboBox) comp[i])).getSelectedIndex() == -1) {
					eMsg(cap[i] + "�� �Է����ּ���.");
					return;
				}
			}
		}
		if (lbl_img.getIcon() == null) {
			eMsg("������ �������ּ���.");
			return;
		}

		var r_no = getOne("select * from ride where r_name like '%" + name + "%'");

		if (!getOne("select * from ride where r_name ='" + txt[0].getText() + "' and r_no <> " + r_no).isEmpty()) {
			eMsg("���̱ⱸ���� �ߺ��Ǿ����ϴ�.");
			return;
		}

		if (txt[3].getText().matches(".*[^0-9].*")) {
			eMsg("�ݾ��� ���ڷ� �Է��ϼ���.");
			return;
		}

		// ����

		if (mode == EDIT) {

			if (txt[0].getText().contentEquals(list.get(0)) && txt[1].getText().contentEquals(list.get(1))
					&& txt[2].getText().contentEquals(list.get(2)) && txt[3].getText().contentEquals(list.get(3))
					&& box[0].getSelectedItem().toString().contentEquals(list.get(4))
					&& box[1].getSelectedItem().toString().contentEquals(list.get(5))
					&& area.getText().contentEquals(list.get(6)) && icon.equals(lbl_img.getIcon())
					&& list.get(7).equals(chk.isSelected() ? "1" : "0")) {
				eMsg("���������� �����ϴ�.");
				return;
			}

			iMsg("������ �Ϸ�Ǿ����ϴ�.");
			execute("update ride set r_name ='" + txt[0].getText() + "', r_floor = '" + txt[1].getText()
					+ "', r_max = '" + txt[2].getText() + "', r_height = '" + box[0].getSelectedItem() + "', r_old ='"
					+ box[1].getSelectedItem() + "', r_money = '" + txt[3].getText() + "', r_disable ="
					+ ((chk.isSelected()) ? 1 : 0) + ", r_explation ='" + area.getText() + "#" + x + "#" + y
					+ "' where r_no ='" + r_no + "'");
			System.out.println(((chk.isSelected()) ? 1 : 0));
			if (path != null && !path.isEmpty()) {
				try (var pst = con.prepareStatement("update ride set r_img = ? where r_name =?")) {
					try {
						pst.setBlob(1, new FileInputStream(path));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pst.setString(2, txt[0].getText());
					pst.execute();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			dispose();
			r.clear();
		} else {
			iMsg("����� �Ϸ�Ǿ����ϴ�.");

			try {
				if (!path.isEmpty()) {
					ImageIO.write(ImageIO.read(new File(path)), "jpg",
							new File("./datafiles/�̹���/" + txt[0].getText() + ".jpg"));
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try (var pst = BaseFrame.con.prepareStatement("insert into ride values(0, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
				pst.setInt(1, toInt(txt[0].getText()));
				pst.setString(2, txt[1].getText());
				pst.setString(3, txt[2].getText());
				pst.setInt(4, toInt(box[0].getSelectedItem()));
				pst.setString(5, box[1].getSelectedItem() + "");
				pst.setString(6, txt[3].getText());
				pst.setInt(7, ((chk.isSelected()) ? 1 : 0));
				pst.setString(8, area.getText() + "#" + x + "#" + y);
				pst.setObject(9, new FileInputStream(new File(path)));
				pst.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}

			r.clear();
			r.revalidate();
			r.repaint();
			this.dispose();
		}
	}
}
