package 광광주;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ReviewEdit extends BaseFrame {

	PlaceHolderTextField txt = new PlaceHolderTextField(30);
	JTextArea area = new JTextArea();
	int star = -1;

	JLabel[] stars = new JLabel[5];
	{
		for (int i = 0; i < 5; i++) {
			stars[i] = lbl("☆", JLabel.CENTER, 15);
			stars[i].setName(i + "");
			stars[i].setForeground(Color.YELLOW);
		}
	}

	public ReviewEdit(Review r) {
		super(r.t.getValueAt(r.t.getSelectedRow(), 6) != null ? "리뷰 수정" : "리뷰 등록 ", 300, 400);

		if (getTitle().equals("리뷰 수정")) {
			try {
				var rs = stmt
						.executeQuery("select * from review where no = " + r.t.getValueAt(r.t.getSelectedRow(), 5));
				if (rs.next()) {
					star = rs.getInt(4);
					txt.setText(rs.getString(2));
					area.setText(rs.getString(3));
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		setLayout(new BorderLayout(5, 5));

		var n = new JPanel(new BorderLayout());
		var c = new JPanel(new BorderLayout());
		var s = new JPanel(new BorderLayout());
		var s_w = new JPanel(new FlowLayout(FlowLayout.LEFT));
		var s_e = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		add(n, "North");
		add(c);
		add(s, "South");
		s.add(s_w, "West");
		s.add(s_e, "East");

		n.add(txt);
		c.add(area);

		txt.setPlaceHolder("제목");
		area.setForeground(Color.GRAY);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				area.grabFocus();
				area.transferFocus();
			}
		});
		// area용 textholder가 필요함
//		area.addFocusListener(new FocusListener() {
//
//			@Override
//			public void focusGained(FocusEvent e) {
//				if (area.getText().equals("내용")) {
//					area.setFont(new Font("맑은 고딕", Font.BOLD, 10));
//					area.setText("");
//					area.setForeground(Color.BLACK);
//				}
//			}
//
//			@Override
//			public void focusLost(FocusEvent e) {
//				if (area.getText().isEmpty()) {
//					area.setForeground(Color.LIGHT_GRAY);
//					area.setFont(new Font("맑은 고딕", Font.BOLD, 10));
//					area.setText("내용");
//				}
//			}
//		});

		for (int i = 0; i < stars.length; i++) {
			stars[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					for (int i = 0; i < stars.length; i++) {
						stars[i].setText("☆");
					}

					star = toInt(((JLabel) e.getSource()).getName());

					for (int i = 0; i <= star; i++) {
						stars[i].setText("★");
					}

					super.mouseClicked(e);
				}
			});
			s_w.add(stars[i]);
		}

		if (star != -1)
			for (int i = 0; i < star; i++) {
				stars[i].setText("★");
			}

		s_e.add(btn(getTitle().equals("리뷰 수정") ? "수정" : "등록", a -> {
			if (a.getActionCommand().equals("수정")) {
				if (txt.getText().isEmpty() || area.getText().isEmpty()) {
					eMsg("내용을 입력해주세요.");
				}
				iMsg("리뷰를 수정했습니다.");
				execute("update review set title = '" + txt.getText() + "',  content = '" + area.getText()
						+ "', rate ='" + star + "' where no = '" + r.t.getValueAt(r.t.getSelectedRow(), 4) + "' ");
				r.setData();
				dispose();
			} else if (a.getActionCommand().equals("등록")) {
				if (txt.getText().isEmpty() || area.getText().isEmpty()) {
					eMsg("내용을 입력해주세요.");
					return;
				}
				if (star == -1) {
					eMsg("평점을 선택해주세요.");
					return;
				}

				iMsg("리뷰를 등록했습니다.");
					
				System.out.println("insert review values(0,'" + txt.getText() + "','" + area.getText() + "'," + star + "," + uno + ","
						+ r.t.getValueAt(r.t.getSelectedRow(), 3) + "," + r.t.getValueAt(r.t.getSelectedRow(), 0)
						+ ")");
				
				execute("insert review values(0,'" + txt.getText() + "','" + area.getText() + "'," + star + "," + uno + ","
						+ r.t.getValueAt(r.t.getSelectedRow(), 3) + "," + r.t.getValueAt(r.t.getSelectedRow(), 0)
						+ ")");
				r.setData();
				dispose();
				
			}
		}));

		area.grabFocus();
		area.setLineWrap(true);
		area.setBorder(new LineBorder(Color.LIGHT_GRAY));
		txt.setBorder(new LineBorder(Color.LIGHT_GRAY));

		txt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyCode() == 8)
					return;
				
				if(txt.getText().length() > 29) {
					e.consume();
				}
			}
		});
		
		((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));

		setVisible(true);
	}
}
