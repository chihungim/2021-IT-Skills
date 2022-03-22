package view;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import db.DBManager;
import view.BaseFrame.Before;
import view.Locate.IconLabel;

public class Ride extends BaseFrame {

	HashMap<String, ArrayList<String>> ride = new HashMap<String, ArrayList<String>>();
	HashMap<String, ArrayList<JLabel>> icon = new HashMap<String, ArrayList<JLabel>>();
	int point[][][] = {
			{ { 105, 409 }, { 222, 327 }, { 694, 274 }, { 262, 527 }, { 317, 492 }, { 792, 262 }, { 734, 331 },
					{ 580, 232 }, { 574, 383 }, { 662, 336 } },
			{ { 782, 353 }, { 710, 224 } }, { { 636, 302 }, { 260, 372 } },
			{ { 355, 313 }, { 613, 319 }, { 753, 346 } },
			{ { 298, 294 }, { 519, 172 }, { 297, 432 }, { 731, 283 }, { 226, 349 }, { 459, 447 }, { 390, 440 },
					{ 427, 509 }, { 400, 299 }, { 295, 335 }, { 196, 458 }, { 358, 246 }, { 648, 229 } } };
	String cap[] = "1F,2F,3F,4F,외부".split(",");
	JComboBox<String> combo = new JComboBox<String>();
	JLabel lblImg[] = new JLabel[5];
	CardLayout card = new CardLayout();

	public Ride() {
		super("놀이기구 등록/수정", 1250, 650);

		this.add(c = new JPanel(card));
		this.add(e = new JPanel(), "East");

		for (int i = 0; i < cap.length; i++) {
			ride.put(cap[i], new ArrayList<String>());
			icon.put(cap[i], new ArrayList<JLabel>());
		}

		try {
			var rs = DBManager.rs("select * from ride");
			while (rs.next()) {
				ride.get(rs.getString(3)).add(rs.getString(2));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		e.add(combo, "North");
		sz(e, 230, 1);
		sz(combo, 180, 25);
		setEmpty(e, 0, 10, 0, 10);
		c.setOpaque(false);
		e.setOpaque(false);

		for (int i = 0; i < cap.length; i++) {
			int j = i;
			lblImg[i] = new JLabel() {
				@Override
				public void paint(Graphics g) {
					super.paint(g);
					Graphics2D g2 = (Graphics2D) g;
					Image img = new ImageIcon(Toolkit.getDefaultToolkit().getImage("./datafiles/지도/" + cap[j] + ".jpg"))
							.getImage();

					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
					g.drawImage(img, 0, 0, 950, 600, null);
				}

			};

			for (int k = 0; k < point[i].length; k++) {
				IconLabel ip = new IconLabel(ride.get(cap[i]).get(k));
				icon.get(cap[i]).add(ip);
				lblImg[i].add(ip);
				ip.setBounds(point[i][k][0] - 25, point[i][k][1] - 50, 50, 50);
				repaint();
			}
			c.add(cap[i], lblImg[i]);

			// TODO 좌표 찍기
			lblImg[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new Register("등록", "");
				}
			});
			lblImg[i].setOpaque(true);
			lblImg[i].setBackground(Color.white);
		}

		combo.addItemListener(a -> {
			card.show(c, cap[combo.getSelectedIndex()]);
		});

		for (int i = 0; i < cap.length; i++) {
			combo.addItem(cap[i]);
		}

		setEmpty((JPanel) getContentPane(), 0, 10, 0, 10);
		this.setVisible(true);
	}

	class Register extends BaseFrame {

		JTextField txt[] = { new JTextField(), new JTextField(), new JTextField(), new JTextField() };
		JComboBox box[] = { new JComboBox<String>(), new JComboBox<String>() };
		JComponent comp[] = { txt[0], txt[1], txt[2], box[0], box[1], txt[3] };
		JLabel lblImg;
		JCheckBox chk = new JCheckBox("장애인");
		JTextArea area = new JTextArea();
		String path;
		String cap[] = "기구명,층,탑승인원,키 제한,나이 제한,금액".split(",");
		ArrayList<String> list = new ArrayList<String>();
		Icon icon;

		public Register(String tit, String name) {
			super("놀이기구 " + tit, 800, 350);

			this.add(w = new JPanel(new BorderLayout()), "West");
			this.add(c = new JPanel(new BorderLayout()));
			this.add(s = new JPanel(), "South");

			w.add(lblImg = new JLabel());
			setLine(lblImg, Color.black);
			w.add(btn("사진 등록", a -> {
				JFileChooser jfc = new JFileChooser();
				jfc.setMultiSelectionEnabled(false);
				if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					path = jfc.getSelectedFile().toString();
					lblImg.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(jfc.getSelectedFile().toString())
							.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
				}
			}), "South");
			sz(w, 200, 1);

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
			c_s.add(new JLabel("기구설명 : "));
			c_s.add(sz(area, 350, 80));
			setLine(area, Color.black);
			area.setLineWrap(true);

			s.add(btn(tit, a -> {
				for (int i = 0; i < txt.length; i++) {
					if (comp[i] instanceof JTextField) {
						if (((JTextField) comp[i]).getText().isEmpty()) {
							eMsg(cap[i] + "를 입력해주세요.");
						}
					} else {
						if ((((JComboBox) comp[i])).getSelectedIndex() == -1) {
							eMsg(cap[i] + "를 입력해주세요.");
						}
					}
				}
				if (lblImg.getIcon().equals(null)) {
					eMsg("사진을 선택해주세요.");
					return;
				}
				if (!DBManager.getOne("select * from ride where r_name ='" + txt[0].getText() + "'").isEmpty()) {
					eMsg("놀이기구명이 중복되었습니다.");
					return;
				}

				if (tit.contentEquals("수정")) {
					if (icon.equals(lblImg.getIcon())) {
						iMsg("수정사항이 없습니다.");
						return;
					}
					for (int i = 0; i < txt.length; i++) {
						if (txt[i].getText().contentEquals(list.get(i))) {
							eMsg("수정사항이 없습니다.");
							return;
						}
					}
					for (int i = 0; i < box.length; i++) {
						if (box[i].getSelectedItem().toString().contentEquals(list.get(i + txt.length))) {
							eMsg("수정사항이 없습니다.");
							return;
						}
					}

					iMsg("수정이 완료되었습니다.");
					DBManager.execute("update ride set r_name ='" + txt[0].getText() + "', r_floor = '"
							+ txt[1].getText() + "', r_max = '" + txt[2].getText() + "', r_height = '"
							+ box[0].getSelectedItem() + "', r_old ='" + box[1].getSelectedItem() + "', r_money = '"
							+ txt[3].getText() + "', r_disable ='" + ((chk.isSelected()) ? 1 : 0) + "', r_explation ='"
							+ area.getText() + "' where r_name ='" + name + "'");
					try {
						if (!path.isEmpty()) {
							ImageIO.write(ImageIO.read(new File(path)), "jpg",
									new File("./datafiles/이미지/" + txt[0].getText() + ".jpg"));
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					this.dispose();
					return;
				}

				iMsg("등록이 완료되었습니다.");
				try {
					if (!path.isEmpty()) {
						ImageIO.write(ImageIO.read(new File(path)), "jpg",
								new File("./datafiles/이미지/" + txt[0].getText() + ".jpg"));
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try (var pst = DBManager.con
						.prepareStatement("insert into ride values(0, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
					pst.setObject(1, txt[0].getText());
					pst.setObject(2, txt[1].getText());
					pst.setObject(3, txt[2].getText());
					pst.setObject(4, box[0].getSelectedItem() + "");
					pst.setObject(5, box[1].getSelectedItem() + "");
					pst.setObject(6, txt[3].getText());
					pst.setObject(7, ((chk.isSelected()) ? 1 : 0));
					pst.setObject(8, area.getText());
					pst.setObject(9, new FileInputStream(new File(path)));
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.dispose();

			}));

			box[0].addItem("90cm이상");
			box[0].addItem("110cm이상");
			box[1].addItem("6세이상");
			box[1].addItem("11세이상");
			box[1].addItem("20세이상");
			txt[1].setEnabled(false);
			txt[1].setText(combo.getSelectedItem().toString());

			if (tit.contentEquals("수정")) {
				lblImg.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("./datafiles/이미지/" + name + ".jpg")
						.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
				path = "./datafiles/이미지/" + name + ".jpg";
				txt[0].setText(name);
				txt[2].setText(DBManager.getOne("select r_max from ride where r_name = '" + name + "'"));
				txt[3].setText(DBManager.getOne("select r_money from ride where r_name = '" + name + "'"));
				area.setText(DBManager.getOne("select r_explation from ride where r_name = '" + name + "'"));

				icon = lblImg.getIcon();
				for (int i = 0; i < txt.length; i++) {
					list.add(txt[i].getText());
				}
				for (int i = 0; i < box.length; i++) {
					list.add(box[i].getSelectedItem().toString());
				}
				list.add(area.getText());
			}

			setEmpty((JPanel) getContentPane(), 10, 10, 10, 10);
			this.setVisible(true);
		}

	}

	class IconLabel extends JLabel {
		public IconLabel(String tit) {
			super("", img("./datafiles/아이콘.png", 25, 30), 0);

			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new Register("수정", tit);
				}
			});

//			int vpo = JButton.TOP, hpo = JButton.CENTER;
//			
//			if (tit.contentEquals("플라이벤처") || tit.contentEquals("쁘띠빵빵") || tit.contentEquals("혜성특급") || tit.contentEquals("머킹의 회전목마")) {
//				vpo = JButton.BOTTOM;
//			} else if (tit.contentEquals("환타지드림")) {
//				hpo = JButton.RIGHT;
//			}
//			this.setHorizontalTextPosition(hpo);
//			this.setVerticalTextPosition(vpo);

		}
	}

	public static void main(String[] args) {
		new Ride();
	}

}
