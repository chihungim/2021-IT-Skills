package ui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;

import com.mysql.cj.x.protobuf.MysqlxSql.StmtExecute;

import base.BasePage;

public class RankItem extends JPanel {

	int s_serial;
	boolean titleSong;
	int rank;
	String title;
	boolean like;
	String length;
	float op = 1f;

	ArrayList<String> list = new ArrayList<String>();

	JPopupMenu menu;
	JMenuItem item;

	JPanel w = new JPanel(new GridLayout(1, 0));
	JPanel c = new JPanel(new BorderLayout(10, 10)) {
		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, op);
			g2.setComposite(ac);
			super.paint(g);
		}
	};
	JPanel e = new JPanel(new GridLayout(1, 0));

	JComboBox box;

	JLabel lbl;
	JLabel likelbl;

	public RankItem(int s_serial, boolean titleSong, int rank, String title, boolean like, String length) {
		this.s_serial = s_serial;
		this.titleSong = titleSong;
		this.rank = rank;
		this.title = title;
		this.like = like;
		this.length = length;

		print();
	}

	private void print() {
		System.out.println("-----------------------------------------");
		System.out.println("titleSong: " + titleSong);
		System.out.println("rank: " + rank);
		System.out.println("title: " + title);
		System.out.println("like: " + like);
		System.out.println("length: " + length);
	}

	public void init() {
		setLayout(new BorderLayout(10, 10));
//		setBorder(new LineBorder(Color.red));
		setBackground(Color.white);

		setOpaque(false);
//		w.setOpaque(false);
////		c.setOpaque(true);
//		e.setOpaque(false);

		w.setBackground(Color.black);
		c.setBackground(Color.black);
		e.setBackground(Color.black);

		lbl = BasePage.lbl(title, JLabel.LEFT, 0, 15);
		likelbl = like ? BasePage.lbl("♥", JLabel.CENTER, 0, 15) : BasePage.lbl("♡", JLabel.CENTER, 0, 15);

		menu = new JPopupMenu();
		item = new JMenuItem("플레이리스트 추가");
		item.addActionListener(a -> {
			box = new JComboBox(list.toArray());
			int ynMsg = JOptionPane.showConfirmDialog(null, box, "플레이리스트에 추가", JOptionPane.YES_NO_OPTION);
			if (ynMsg == 0) {
				if (box.getSelectedIndex() == -1) {
					BasePage.eMsg("플레이리스트를 선택해주세요.");
					return;
				}
				BasePage.iMsg("추가되었습니다.");
				BasePage.execute("insert into songlist values(0, (select serial from playlist where name like '"
						+ box.getSelectedItem() + "'), " + s_serial + ")");

			}
		});
		menu.add(item);

		add(c);

		c.add(w, "West");
		c.add(lbl);
		c.add(e, "East");

		w.add(BasePage.size(
				titleSong ? BasePage.lbl("★", JLabel.CENTER, 0, 15) : BasePage.lbl("  ", JLabel.CENTER, 0, 0), 15, 15));
		w.add(BasePage.lbl(rank + "", JLabel.RIGHT, Font.BOLD, 15));

		likelbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				like = !like;
				System.out.println(like);
				likelbl.setText(like ? "♥" : "♡");
				BasePage.execute(like ? "insert into favorite values(0, " + BasePage.u_serial + ", " + s_serial + ")"
						: "delete from favorite where song=" + s_serial + " and user = " + BasePage.u_serial);
				repaint();
				revalidate();
			}
		});
		e.add(likelbl);
		e.add(BasePage.lbl(length, JLabel.CENTER, Font.BOLD, 15));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				op = 0.5f;
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				op = 1f;
				repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					BasePage.iMsg("대기열에 추가되었습니다.");
					return;
				} else if (e.getButton() == 3) {
					try {
						System.out.println(
								"select pl.name from playlist pl, user u where u.serial = pl.user and u.serial="
										+ BasePage.u_serial);
						var rs = BasePage.stmt.executeQuery(
								"select pl.name from playlist pl, user u where u.serial = pl.user and u.serial="
										+ BasePage.u_serial);
						while (rs.next()) {
							list.add(rs.getString(1));
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					menu.show(((JComponent) e.getSource()), e.getX(), e.getY());
				}
			}
		});
	}
}
