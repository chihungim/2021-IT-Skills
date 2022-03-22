package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import tools.Tools;

public class OrderLogPage extends BasePage {

	JPanel scr_p;
	JScrollPane scr = new JScrollPane();
	int count = 0;
	JPopupMenu menu = new JPopupMenu();
	JMenuItem item = new JMenuItem("취소");
	int selc_puno;

	public OrderLogPage() {
		super();

		this.add(scr_p = new JPanel(new BorderLayout()));

		scr_p.add(Tools.lbl(uname + "님의 구매목록", 0, 25), "North");
		scr_p.add(scr = new JScrollPane(c = new JPanel(new GridLayout(0, 3, 10, 10))));

		scr_p.setOpaque(false);
		c.setBackground(Color.white);

		getNextPage();

		var scroll = scr.getVerticalScrollBar();
		scroll.addAdjustmentListener(new AdjustmentListener() {

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (scroll.getValue() != 0 && (scroll.getValue() + scroll.getHeight() == scroll.getMaximum())) {
					count += 9;
					getNextPage();
				}
			}
		});

		Tools.setEmpty(this, 50, 50, 50, 50);
		item.addActionListener(a -> {
			int yesno = JOptionPane.showConfirmDialog(null, "구매를 취소하시겠습니까?", "구매", JOptionPane.YES_NO_OPTION);
			if (yesno == JOptionPane.YES_OPTION) {
				Tools.iMsg("취소되었습니다.");
				// purchase row 삭제
				Tools.execute("delete from purchase where pu_No='" + selc_puno + "'");
				repaint();
				revalidate();
			}
		});
	}

	void getNextPage() {
		String sql = "SELECT pu.pu_No, s.s_No, s.s_Addr, s.s_Name, u.u_No, u.u_Addr, p.p_No, c.c_Name, p.p_Name, pu.pu_Count, pu.pu_Price, pu_Date, ifnull(r.r_No, 0), ifnull(r.r_Time, '1111-11-11 00:00:00') FROM purchase pu left join receive r on r.pu_No=pu.pu_No inner join seller s on s.s_No=pu.s_No inner join user u on u.u_No=pu.u_No inner join product p on p.p_No=pu.p_No inner join category c on c.c_No=p.c_No where u.u_No=1 order by pu_Date asc";
		sql += " limit " + count + ", 9";
		try {
			var rs = Tools.rs(sql);
			while (rs.next()) {
				c.add(new ProductItem(rs.getString(9), rs.getString(8), rs.getString(4), rs.getInt(10), rs.getInt(11),
						rs.getInt(13), rs.getString(12), rs.getInt(6), rs.getInt(3),
						LocalDateTime.parse(rs.getString(14), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
						rs.getInt(3), rs.getInt(1)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.repaint();
		c.revalidate();
	}

	class ProductItem extends JPanel {

		String name;
		JPanel c, w, s;
		DecimalFormat format = new DecimalFormat("#,##0");

		public ProductItem(String name, String category, String seller, int stock, int price, int receive, String date,
				int sNode, int eNode, LocalDateTime a, int saddr, int puno) {

			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Tools.iMsg(a.toString());
				}
			});

			this.setLayout(new BorderLayout());
			this.setPreferredSize(new Dimension(300, 200));
			this.setBackground(Color.white);
			this.setBorder(new LineBorder(Color.black));

			this.add(w = new JPanel(), "West");
			this.add(c = new JPanel(new FlowLayout(0)), "Center");
			this.add(s = new JPanel(new BorderLayout()), "South");
			w.setOpaque(false);
			c.setOpaque(false);
			s.setOpaque(false);

			JLabel lbl;
			w.add(Tools.size(lbl = new JLabel(Tools.img("./datafile/image/" + name + ".jpg", 130, 130)), 130, 130),
					"West");
			Tools.setLine(lbl);

			c.add(new JLabel("<html>" + name + "<br><br>판매자 : " + seller + "<br><br>개수 : " + stock + "개<br><br>가격 : "
					+ format.format(price) + "원<br><br>", JLabel.LEFT));

			Color color;
			String state = "배달 ";

			if (receive == 0) {
				state += "준비";
			} else {
				state += "현황";
			}

			JButton delivery;
			s.add(delivery = Tools.btn(state, e -> {
				sname = seller;
				usermodeStartTime = a;
				s_addr = saddr;
				new MapFrame("user").setVisible(true);
			}), "East");

			if (receive == 0)
				delivery.setEnabled(false);

			Tools.setEmpty(c, 0, 10, 0, 0);
			this.setBorder(new CompoundBorder(new LineBorder(Color.black), new EmptyBorder(5, 5, 5, 5)));

			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == 3) {
						var p = getMousePosition();
						menu.removeAll();

						if (!delivery.isEnabled()) {
							menu.add(item);
						}

						selc_puno = puno;
						menu.show(ProductItem.this, p.x, p.y);
					}
				}
			});

		}
	}

}
