package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class BrandAlba extends BaseFrame {

	public BrandAlba() {
		super("�귣��˹�", 700, 500);

		this.add(c = new JPanel(new GridLayout(0, 5)));

		try {
			var rs = stmt.executeQuery("select c_no, name from company where c_no <= 25 order by name");
			while (rs.next()) {
				int no = rs.getInt(1);
				JPanel jp = new JPanel(new BorderLayout());
				jp.add(new JLabel(img("./�����ڷ�/�귣��/" + rs.getString(2) + ".jpg", 130, 100)));
				c.add(jp);
				jp.setBorder(new LineBorder(Color.gray));

				jp.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (getOne(
								"select c.c_no, c.name from company c, recruitment r where c.c_no=r.c_no and c.c_no <= 25 and c.c_no="
										+ no + " and r.deadline >= curdate() order by name").isEmpty()) {
							eMsg("ä�������� �����ϴ�.");
							return;
						}
						// �ش� �귣���� ä�������� �˻��� [�׸� 8]�� ä������ ������ �̵��Ͻÿ�.
					}
				});

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setEmpty((JPanel) getContentPane(), 10, 10, 10, 10);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new BrandAlba();
	}

}
