import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Test02_투명버튼 {
	public static void main(String[] args) {
		JFrame jf = new JFrame();
		jf.setSize(300, 300);
		jf.setLocationRelativeTo(null);
		jf.setLayout(null);
		
		JButton jb = new JButton() {
			@Override
			protected void fireActionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(null, "클릭");
			}
		};
		jb.setBounds(10, 10, 10, 10);
		jb.setBorderPainted(false);
		jb.setContentAreaFilled(false);
		
		jf.add(jb);
		jf.setVisible(true);
	}
	}
}
