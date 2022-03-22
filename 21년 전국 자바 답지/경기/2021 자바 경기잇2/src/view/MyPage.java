package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class MyPage extends BaseFrame {
	DefaultTableModel m = model("예매번호,기구이름,예약날짜,기구설명".split(","));
	JTable t = new JTable(m) {
		public java.awt.Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
			var comp = (JComponent) super.prepareRenderer(renderer, row, column);
			if (row % 2 == 0) {
				comp.setBackground(Color.blue);
				comp.setForeground(Color.white);
			} else {
				comp.setBackground(Color.white);
				comp.setForeground(Color.black);
			}
			return comp;
		};
	};
	JLabel lbl_img;

	public MyPage() {
		super("Mypage", 780, 280);
		data();
		ui();
		events();
		setVisible(true);
	}

	void data() {
		addRow(m,
				"SELECT t.t_no, r.r_name, t.t_date, substring_index(r.r_explation,'#',1) FROM ticket t, ride r where t.r_no=r.r_no and u_no="
						+ uno);
	}

	void ui() {
		var c = new JPanel(new BorderLayout());
		var e = new JPanel(new BorderLayout());

		add(c);
		add(e, "East");

		c.add(new JScrollPane(t));
		e.add(lbl_img = new JLabel());
		setBorder(lbl_img, new LineBorder(Color.BLACK));
		sz(e, 250, 250);
		setBorder(e, new EmptyBorder(0, 10, 0, 0));

		c.setOpaque(false);
		e.setOpaque(false);

		setBorder((JPanel) getContentPane(), new EmptyBorder(5, 5, 5, 5));
	}

	void events() {
		var pop = new JPopupMenu();
		var menu = new JMenuItem("삭제");
		pop.add(menu);
		t.setComponentPopupMenu(pop);

		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (t.getSelectedRow() == -1)
					return;

				try {
					var rs = stmt
							.executeQuery("select r.r_img from ticket t, ride r where t.r_no = r.r_no  and t.t_no = "
									+ t.getValueAt(t.getSelectedRow(), 0));
					if (rs.next()) {
						lbl_img.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(rs.getBytes(1))
								.getScaledInstance(250, 250, Image.SCALE_SMOOTH)));
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				super.mousePressed(e);
			}
		});

		menu.addActionListener(a -> {
			int r = t.getSelectedRow();

			iMsg("삭제가 완료되었습니다.");
			execute("delete from ticket where t_no=" + t.getValueAt(r, 0));
			addRow(m,
					"SELECT t.t_no, r.r_name, t.t_date, r.r_explation FROM ticket t, ride r where t.r_no=r.r_no and u_no="
							+ uno);
		});
	}
}
