import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Recommend extends JPanel {
	JPanel c, s;
	JPanel c_c, s_s;
	JPanel tmp[] = new JPanel[5];
	JPanel fl = BaseFrame.fPanel(0);
	JPanel in = BaseFrame.gPanel(0, 1);

	JScrollPane jsc = new JScrollPane(in);

	JLabel imgLbl[] = new JLabel[5];
	JLabel lbl[] = new JLabel[5];

	JButton btn[] = new JButton[16];

	JPopupMenu jscPop = new JPopupMenu();
	JPopupMenu recPop = new JPopupMenu();
	JPopupMenu explainPop = new JPopupMenu();
	JMenuItem item[] = { new JMenuItem("이미지 설정"), new JMenuItem("설명 설정") };
	JMenuItem explainItem[] = { new JMenuItem("삭제"), new JMenuItem("설명 텍스트 입력") };

	int last, sel = 0, sel2;
	int imgNum[] = new int[5];
	String explan = "";

	public Recommend() {
		ui();
	}

	void ui() {
		setLayout(new BorderLayout());

		jscPop.add(jsc);
		for (int i = 0; i < 2; i++) {
			recPop.add(item[i]);
			explainPop.add(explainItem[i]);
		}

		try {
			var rs = BaseFrame.stmt.executeQuery("select * from location");
			int cnt = 0;
			while (rs.next()) {
				in.add(btn[cnt] = new JButton(rs.getString("name")));
				cnt++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		add(c = BaseFrame.bPanel(0));
		add(s = BaseFrame.bPanel(0), "South");

		c.add(BaseFrame.lbl("추천 여행지 관리", 2, 25), "North");
		c.add(c_c = BaseFrame.gPanel(1, 0));

		s.add(fl, "North");
		s.add(s_s = BaseFrame.fPanel(0));

		fl.add(BaseFrame.lbl("설명 설정", 2, 25));
		fl.add(BaseFrame.btn("추가", e -> addEvnt()));

		c_c.setBorder(new EmptyBorder(0, 0, 50, 0));

		BaseFrame.size(s_s, 0, 250);
		BaseFrame.size(jscPop, 120, 280);

		try {
			var rs = BaseFrame.stmt.executeQuery(
					"select * from recommend_info ri, recommend r, location l where ri.recommend_no = r.no and r.location_no = l.no group by name order by r.no");
			int i = 0;
			while (rs.next()) {
				c_c.add(tmp[i] = BaseFrame.bPanel(0));
				TitledBorder border = new TitledBorder(new LineBorder(Color.BLACK), rs.getString("name"));
				tmp[i].setBorder(border);
				tmp[i].add(
						lbl[i] = new JLabel(new ImageIcon(Toolkit
								.getDefaultToolkit().getImage("지급파일/images/recommend/"
										+ BaseFrame.map.get(rs.getString("name")) + "/" + rs.getString(2) + ".jpg")
								.getScaledInstance(200, 200, Image.SCALE_SMOOTH))));
				imgNum[i] = rs.getInt(2);
				int idx = i;
				tmp[i].addMouseListener(new MouseAdapter() {
					public void mousePressed(java.awt.event.MouseEvent e) {
						if (e.getClickCount() == 2) {
							jscPop.show(tmp[idx], e.getX(), e.getY());

							for (int j = 0; j < btn.length; j++) {
								int k = j;
								btn[k].addActionListener(e2 -> {
									border.setTitle(btn[k].getText());
									tmp[idx].setBorder(border);
									BaseFrame.execute("update recommend set location_no = " + (k + 1) + " where no = "
											+ (sel + 1));
									repaint();
									revalidate();
								});
							}
						}

						if (e.getButton() == 3) {
							sel = idx;
							recPop.show(tmp[idx], e.getX(), e.getY());
						}
					};
				});
				i++;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int i = 0; i < 2; i++) {
			item[i].addActionListener(e -> {
				if (e.getSource().equals(item[0])) {
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & png Images",
							"jpg,png".split(","));
					chooser.setFileFilter(filter);
					chooser.setMultiSelectionEnabled(false);
					if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
						lbl[sel].setIcon(
								new ImageIcon(Toolkit.getDefaultToolkit().getImage(chooser.getSelectedFile().getPath())
										.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
						String title = ((TitledBorder) tmp[sel].getBorder()).getTitle();

						if (title.equals(explan)) {
							imgLbl[imgNum[sel] - 1].setIcon(new ImageIcon(
									Toolkit.getDefaultToolkit().getImage(chooser.getSelectedFile().getPath())
											.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
							File f = new File(
									"지급파일/images/recommend/" + BaseFrame.map.get(title) + "/" + imgNum[sel] + ".jpg");
							try {
								ImageIO.write(ImageIO.read(new File(chooser.getSelectedFile().getPath())), ".jpg", f);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}

				if (e.getSource().equals(item[1])) {
					explan = ((TitledBorder) tmp[sel].getBorder()).getTitle();
					try {
						ResultSet rs = BaseFrame.stmt.executeQuery(
								"SELECT * FROM busticketbooking.recommend_info ri inner join recommend r on ri.recommend_no = r.no inner join location l on r.location_no = l.no where name = '"
										+ ((TitledBorder) tmp[sel].getBorder()).getTitle() + "' order by r.no");
						int j = 0;
						while (rs.next()) {
							imgLbl[j].setIcon(new ImageIcon(Toolkit
									.getDefaultToolkit().getImage("지급파일/images/recommend/"
											+ BaseFrame.map.get(rs.getString("name")) + "/" + (j + 1) + ".jpg")
									.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
							imgLbl[j].setBorder(new LineBorder(Color.BLACK));
							repaint();
							revalidate();
							j++;
						}
						last = j;
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			});
		}

		for (int i = 0; i < explainItem.length; i++) {
			explainItem[i].addActionListener(a -> {
				if (a.getSource().equals(explainItem[0])) {
					String title = ((TitledBorder) tmp[sel].getBorder()).getTitle();
					File f = new File(
							"./지급파일/images/recommend/" + BaseFrame.map.get(title) + "/" + (sel2 + 1) + ".jpg");
					f.delete();
					imgLbl[sel2].setBorder(new LineBorder(Color.WHITE));
					imgLbl[sel2].setIcon(null);
				}
				if (a.getSource().equals(explainItem[1])) {
					String inp = JOptionPane.showInputDialog(null, "설명 텍스트를 입력해주세요.", "입력",
							JOptionPane.QUESTION_MESSAGE);
					BaseFrame.execute("update recommend_info set descrption = '" + inp + "' where recommend_no = "
							+ (sel + 1) + " and title = " + (sel2 + 1));
				}
			});
		}
		for (int i = 0; i < imgLbl.length; i++) {
			s_s.add(imgLbl[i] = BaseDialog.size(new JLabel(), 150, 150));
			int j = i;
			imgLbl[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getButton() == 3) {
						explainPop.show(imgLbl[j], e.getX(), e.getY());
						sel2 = j;
					}
				}
			});
		}

	}

	void addEvnt() {
		JFileChooser file = new JFileChooser(new File("지급파일/images/"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & png Images", "jpg,png".split(","));
		file.setFileFilter(filter);
		file.setMultiSelectionEnabled(false);
		if (file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			imgLbl[last].setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(file.getSelectedFile().getPath())
					.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
			try {
				ImageIO.write(ImageIO.read(new File(file.getSelectedFile().getPath())), "jpg",
						new File("지급파일/images/recommend/busan/4.jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
