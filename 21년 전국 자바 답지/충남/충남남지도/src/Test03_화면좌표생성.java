import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Test03_ȭ����ǥ���� {
	static ArrayList<String> list = new ArrayList<String>();
	
	public static void main(String[] args) {
		JFrame jf = new JFrame();
		jf.setSize(1600, 1000);
		jf.setLocationRelativeTo(null);
		
		//���1
		ImageIcon img = new ImageIcon(new ImageIcon("./metro.png").getImage());

		JPanel jp = new JPanel();
		JLabel jl = new JLabel(img);
		jp.add(jl);
		
		JScrollPane jsp = new JScrollPane(jp);
		jsp.setAutoscrolls(true);

		jf.add(jsp);
		jf.setVisible(true);
	
		//ȭ����ǥ �̺�Ʈ
		jl.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				list.add("{" + e.getX() + "," + e.getY() + "}");
				System.out.println("��ǥ���:" + String.join(",", list));
			}
		});
	}
}
