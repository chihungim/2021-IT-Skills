package 광광주;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class OrderList extends BaseFrame {

	DefaultTableModel m = model("주문번호,주문일시,주문금액,음식점,결제수단,주문상태".split(","));
	JTable t = table(m);
	JScrollPane pane;

	JPopupMenu popupMenu = new JPopupMenu();
	JMenuItem menuItem = new JMenuItem("주문 취소");

	public OrderList() {
		super("주문내역", 700, 400);
		add(pane = new JScrollPane(t));
		popupMenu.add(menuItem);

		t.setComponentPopupMenu(popupMenu);

		menuItem.addActionListener(a -> {
			if(t.getSelectedRow() == -1)
				return;

			if (!t.getValueAt(t.getSelectedRow(), 5).equals("결제완료")) {
				eMsg("결제 이후의 상태는 취소 할 수 없습니다.");
				return;
			}
			
			iMsg("주문이 취소되었습니다.");
			setData();
		});

		setData();
		setVisible(true);
	}

	public static void main(String[] args) {
		uno = 1;
		new OrderList();
	}

	void setData() {
		addrow("select receipt.no, receipt.RECEIPT_TIME, format(receipt.PRICE,'#,##0'), SELLER.name, concat(payment.ISSUER ,' - ', right(payment.CARD,4)) as way, if(receipt.status = 0, '결제완료',if(receipt.status = 1, '조리완료', if(receipt.status = 2, '배달중', if(receipt.status = 3, '배달완료', '')))) from receipt, payment, seller where receipt.SELLER = seller.NO and payment.USER = receipt.USER and receipt.user = "
				+ uno, m);
	}
}
