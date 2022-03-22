package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import db.DBManager;

public class Register extends BaseFrame {

	String firstCap[] = "����ڵ�Ϲ�ȣ,�����,��ǥ��,����,�������,�þ�����,����ó,�����ġ".split(",");
	JTextField firstTxt[] = new JTextField[firstCap.length];
	JTextField lastTxt[] = { new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(),
			new JTextField(), new JTextField(), new JTextField(), new JTextField(), };
	String lastCap[] = "����ڵ�Ϲ�ȣ,�����,���̵�,��й�ȣ,��й�ȣ Ȯ��,�����,��ȭ��ȣ,�̸���".split(",");
	JLabel lblImg;
	JComboBox<String> firstCombo = new JComboBox<String>(), lastCombo = new JComboBox<String>();
	String firstInfo[] = new String[firstCap.length];
	String lastInfo[] = new String[lastCap.length];
	JPanel c_c, c_e;
	boolean state = false;
	String path="";

	public Register() {
		super("���", 700, 500);

		this.add(c = new JPanel(new BorderLayout()));
		c.setBorder(new TitledBorder(new LineBorder(Color.black), "��� ���", TitledBorder.LEFT, TitledBorder.TOP));
		c.add(c_c = new JPanel(new GridLayout(0, 1)));
		c.add(c_e = new JPanel(new BorderLayout()), "East");

		this.add(s = new JPanel(new FlowLayout(2)), "South");

		firstUI();

		this.setVisible(true);
	}

	void secondUI() {
		c_c.removeAll();
		c_e.removeAll();
		s.removeAll();
		
		

		s.add(btn("����", a -> {
			firstUI();
		}));
		s.add(btn("���", a -> {
			for (int i = 0; i < lastTxt.length; i++) {
				if (lastTxt[i].getText().isEmpty()) {
					eMsg("��ĭ�� �ֽ��ϴ�.");
					return;
				}
			}
			if (!state) {
				eMsg("�ߺ�Ȯ���� ���ּ���.");
				return;
			}
			String pw = lastTxt[3].getText();
			if (!(pw.matches(".*\\d.*") && pw.matches(".*[a-zA-Z].*") && pw.matches(".*[!@#$%^&*].*") && pw.length()>=8 && pw.length()<=16)) {
				eMsg("��й�ȣ ������ �ƴմϴ�.");
				return;
			}
			if (!pw.contentEquals(lastTxt[4].getText())) {
				eMsg("��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
				return;
			}
			if (lastTxt[6].getText().length()!=11) {
				eMsg("��ȭ��ȣ�� 11�ڸ����� �մϴ�.");
				return;
			}
			
			iMsg("����� �Ϸ�Ǿ����ϴ�.");
			
			try {
				if (!path.isEmpty()) {
					iMsg(path);
					ImageIO.write(ImageIO.read(new File(path)), "jpg", new File("./�����ڷ�/���/"+lastTxt[1].getText()+".jpg"));
				}
					
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int dno = rei(DBManager.getOne("select d_no from details where name ='"+firstCap[3]+"'"));
			String phone = lastTxt[6].getText().replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
			DBManager.execute("insert into company values(0, '"+lastTxt[0].getText()+"','"+lastTxt[1].getText()+"','"+dno+"','"+firstCap[2]+"','"+phone+"','"+firstCap[5]+"','"+firstCap[7]+"')");
			
		}));

		for (int i = 0; i < lastCap.length; i++) {
			var tmp = new JPanel(new FlowLayout(0));
			tmp.add(sz(new JLabel(lastCap[i], 2), 100, 25));
			if (i==7) {
				tmp.add(sz(lastTxt[7], 80, 25));
				tmp.add(new JLabel("@", 0));
				tmp.add(sz(lastTxt[8], 80, 25));
				tmp.add(sz(lastCombo, 100, 25));
			} else {
				tmp.add(lastTxt[i] = new JTextField(15));
				if (i==2) {
					tmp.add(sz(btn("�ߺ�Ȯ��", a->{
						if (lastTxt[2].getText().isEmpty()) {
							eMsg("��ĭ�Դϴ�.");
							return;
						}
						if (!DBManager.getOne("select * from user where id ='"+lastTxt[2].getText()+"'").isEmpty()) {
							eMsg("��� �Ұ����մϴ�.");
							lastTxt[2].setText("");
						} else {
							iMsg("��� �����մϴ�.");
							state = true;
						}
					}), 120, 25));
				}
			}
			c_c.add(tmp);
		}
		
		lastTxt[0].setText(firstCap[0]);
		lastTxt[1].setText(firstCap[1]);
		
		lastTxt[2].addKeyListener(new KeyAdapter() {
			 @Override
			public void keyPressed(KeyEvent e) {
				 state = false;
			 }
		});
		
		String mail[] = "��Ÿ,empal.com,gmail.com,hanmail.net,kebi.com,korea.com,nate.com,naver.com,yahoo.com".split(",");
		for (int i = 0; i < mail.length; i++) {
			lastCombo.addItem(mail[i]);
		}
		
		lastCombo.addItemListener(a->{
			lastTxt[8].setEditable(lastCombo.getSelectedIndex()==0);
		});

		repaint();
		revalidate();
	}

	void firstUI() {
		c_c.removeAll();
		c_e.removeAll();
		s.removeAll();

		for (int i = 0; i < firstCap.length; i++) {
			var tmp = new JPanel(new FlowLayout(0));
			tmp.add(sz(new JLabel(firstCap[i], 2), 100, 25));
			if (i == 7) {
				tmp.add(sz(firstCombo, 80, 25));
			}
			tmp.add(firstTxt[i] = new JTextField(18));
			c_c.add(tmp);
		}

		c_e.add(lblImg = new JLabel(img("./�����ڷ�/�̹�������.png", 200, 200)));
		setLine(lblImg);

		setEmpty(c_c, 0, 10, 0, 10);
		setEmpty(c_e, 20, 0, 180, 20);
		setEmpty((JPanel) getContentPane(), 10, 10, 10, 10);

		firstTxt[0].addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!isNumeric(e.getKeyChar())) {
					e.consume();
				}
			}
		});

		
		firstTxt[3].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Choice(Register.this.getTitle(), firstTxt[3]);
			}
		});
		
		firstTxt[6].addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!isNumeric(e.getKeyChar())) {
					e.consume();
				}
			}
		});

		lblImg.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setMultiSelectionEnabled(false);
				if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					path = jfc.getSelectedFile().toString();
					lblImg.setIcon(img(path, 200, 200));
				}
			}
		});

		for (int i = 0; i < addr.length; i++) {
			firstCombo.addItem(addr[i]);
		}

		s.add(sz(btn("����", a -> {
			
			if (firstTxt[0].getText().length()!=10) {
				eMsg("����ڵ�Ϲ�ȣ�� 10�ڸ����� �մϴ�.");
				return;
			}
			if (!DBManager.getOne("select * from company where number ='"+firstTxt[0].getText()+"'").isEmpty()) {
				eMsg("�̹� �ִ� ����� ��Ϲ�ȣ�Դϴ�.");
				return;
			}
			if (firstTxt[6].getText().length()!=11) {
				eMsg("����ó�� 11�ڸ��� �Է����ּ���.");
				return;
			}
			
			for (int i = 0; i < firstCap.length - 1; i++) {
				firstInfo[i] = firstTxt[i].getText();
			}
			firstInfo[7] = firstCombo.getSelectedItem() + " " + firstTxt[7].getText();
			secondUI();
		}), 100, 30));

		repaint();
		revalidate();
	}

	public static void main(String[] args) {
		new Register();
	}

}
