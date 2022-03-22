package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import db.DBManager;

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
	JLabel lblImg;
	JPopupMenu pop = new JPopupMenu();
	JMenuItem menu = new JMenuItem("삭제");

	public MyPage() {
		super("MyPage", 780, 280);

		pop.add(menu);
		this.add(c = new JPanel(new BorderLayout()));
		this.add(e = new JPanel(new BorderLayout()), "East");

		c.setOpaque(false);
		e.setOpaque(false);

		c.add(new JScrollPane(t));
		e.add(lblImg = new JLabel());
		setLine(lblImg, Color.black);
		sz(e, 250, 250);
		setEmpty(e, 0, 10, 0, 0);

		menu.addActionListener(a -> {
			int r = t.getSelectedRow();
			iMsg("삭제가 완료되었습니다.");
			DBManager.execute("delete from ticket where r_no=" + t.getValueAt(r, 0));
		});

		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					int c = t.columnAtPoint(e.getPoint());
					int r = t.rowAtPoint(e.getPoint());
					t.changeSelection(r, c, false, false);
					var p = getMousePosition();
					pop.show(MyPage.this, p.x, p.y);
				} else {
					int r = t.getSelectedRow();
					lblImg.setIcon(new ImageIcon(
							Toolkit.getDefaultToolkit().getImage("./datafiles/이미지/" + t.getValueAt(r, 1) + ".jpg")));
				}
			}
		});

		addRow(m, "SELECT t.t_no, r.r_name, t.t_date, r.r_explation FROM ticket t, ride r where t.r_no=r.r_no and u_no="
				+ uno);

		setEmpty((JPanel) getContentPane(), 5, 5, 5, 5);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		setLogin(1);
		new MyPage();
	}

}
