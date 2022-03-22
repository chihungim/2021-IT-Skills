package view;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class OrderManage extends BaseDialog {

	DefaultTableModel m1 = model("주문번호,주문일시,주문금액,주문상태".split(","));
	JTable t1 = table(m1, SwingConstants.CENTER);

	DefaultTableModel m2 = model("번호,메뉴명,주문금액,옵션".split(","));
	JTable t2 = table(m2, SwingConstants.CENTER);

	public OrderManage() {
		super("주문관리", 700, 500);
		var c = new JPanel(new GridLayout(0, 1, 5, 5));
		add(c);

		c.add(new JScrollPane(t1));
		c.add(new JScrollPane(t2));

		addrow("select r.NO, r.RECEIPT_TIME , format(r.price, '#,##0'), if(r.status = 0, '결제완료', if(r.status = 1,'조리완료', if(r.status = 2, '배달중', '배달완료'))) from receipt r, seller s where r.SELLER = s.NO and s.no = '"
				+ sno + "'", m1);

		t1.addMouseListener(new MouseAdapter() {

			int cnt = 0;

			@Override
			public void mouseClicked(MouseEvent e) {
				if (t1.getSelectedRow() == -1)
					return;
				addrow("SELECT rd.no, m.name, rd.count, o.name FROM receipt r, receipt_detail rd, receipt_options ro, menu m, options o WHERE rd.NO = ro.RECEIPT_DETAIL and m.NO = rd.MENU and ro.OPTIONS = o.NO and r.NO = rd.RECEIPT and r.no = "
						+ t1.getValueAt(t1.getSelectedRow(), 0) + "  group by rd.no", m2);

				if (t1.getValueAt(t1.getSelectedRow(), 3).equals("결제완료"))
					cnt++;

				if (cnt == 2) {
					int yn = JOptionPane.showConfirmDialog(null, "조리를 완료 했습니까?", "메시지", JOptionPane.YES_NO_OPTION);
					if (yn == JOptionPane.YES_OPTION) {
						execute("update receipt set status = 1 where no = " + t1.getValueAt(t1.getSelectedRow(), 0));
						addrow("select r.NO, r.RECEIPT_TIME , format(r.price, '#,##0'), if(r.status = 0, '결제완료', if(r.status = 1,'조리완료', if(r.status = 2, '배달중', '배달완료'))) from receipt r, seller s where r.SELLER = s.NO and s.no = '"
								+ sno + "'", m1);
					}
					cnt = 0;
				}
			}
		});

		((javax.swing.JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));

		setVisible(true);
	}

}
