package pagingExecutive;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Dynamic extends JFrame {
	private JTextField[] textField;
	private JTextField[] textField1;
	public int count = 0;
	int block = 0;
	int[] seq;
	Object[][] data;
	JTextArea currentstep;
	JTextArea currentpage;
	ClockThread clockThread;

	public Dynamic(final int[] seq1, final int block, final Object[][] data1) {
		seq = new int[seq1.length];
		data = new Object[block][129];
		for (int i = 0; i < seq1.length; i++)
			seq[i] = seq1[i];
		for (int a = 0; a < block; a++)
			for (int b = 0; b < 129; b++)
				data[a][b] = data1[a][b];

		// 实例化一个窗体;
		JFrame dynamicframe = new JFrame();
		dynamicframe.setTitle("页面置换算法动态演示界面");
		dynamicframe.setBounds(550, 260, 541, 562);
		dynamicframe.setDefaultCloseOperation(2);
		dynamicframe.getContentPane().setLayout(null);
		// 实例化运行显示面板
		JPanel displayPanel = new JPanel();
		displayPanel.setBackground(Color.WHITE);
		displayPanel.setToolTipText("");
		displayPanel.setLayout(null);
		JScrollPane jspdisplay = new JScrollPane(displayPanel);
		jspdisplay.setBounds(192, 15, 312, 486);
		dynamicframe.getContentPane().add(jspdisplay);

		// 实例化运行功能面板
		JPanel functionPanel = new JPanel();
		functionPanel.setBackground(Color.WHITE);
		functionPanel.setToolTipText("");
		functionPanel.setLayout(null);
		functionPanel.setBounds(15, 15, 168, 486);
		dynamicframe.getContentPane().add(functionPanel);
		String labels[] = { "FIFO", "LRU", "OPT" };

		JLabel current = new JLabel();
		current.setFont(UIManager.getFont("Label.font"));
		current.setText("当前访问页面：");
		current.setBounds(15, 290, 148, 37);
		functionPanel.add(current);
		currentpage = new JTextArea();
		currentpage.setText("");
		currentpage.setBounds(15, 329, 133, 37);
		functionPanel.add(currentpage);

		JButton autoyunxing = new JButton("自动运行");
		autoyunxing.setBounds(15, 124, 133, 37);
		functionPanel.add(autoyunxing);

		JLabel lbldisplay = new JLabel();
		lbldisplay.setText("选择演示方式：");
		lbldisplay.setFont(UIManager.getFont("Label.font"));
		lbldisplay.setBounds(15, 30, 148, 37);
		functionPanel.add(lbldisplay);

		JButton handyunxing = new JButton("手动运行");
		handyunxing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		handyunxing.setBounds(15, 72, 133, 37);
		functionPanel.add(handyunxing);

		JLabel lblstep = new JLabel();
		lblstep.setText("当前运行步数：");
		lblstep.setFont(UIManager.getFont("Label.font"));
		lblstep.setBounds(15, 381, 148, 37);
		functionPanel.add(lblstep);

		currentstep = new JTextArea();
		currentstep.setText("");
		currentstep.setBounds(15, 421, 133, 37);
		functionPanel.add(currentstep);
		dynamicframe.setVisible(true);

		int y = (displayPanel.getHeight() - block * 40) / 2;

		JTextField textFieldleft = new JTextField();
		textFieldleft.setBounds(0, y, 20, block * 40);
		JTextField textFieldright = new JTextField();
		textFieldright.setBounds(292, y, 20, block * 40);

		textField = new JTextField[block];
		textField1 = new JTextField[block];
		for (int i = 0; i < block; i++) {
			textField[i] = new JTextField();
			textField1[i] = new JTextField();

			textField[i].setText("");
			textField[i].setBounds(44, y + i * 40, 100, 40);
			displayPanel.add(textField[i]);

			textField1[i].setText("");
			textField1[i].setBounds(168, y + i * 40, 100, 40);
			displayPanel.add(textField1[i]);
		}
		displayPanel.add(textFieldright);
		displayPanel.add(textFieldleft);

		handyunxing.addActionListener(new ActionListener() {// 手动运行，点击一次执行一步
			public void actionPerformed(ActionEvent arg0) {
				autoyunxing.setText("自动运行");
				onestep();
			}
		});

		JButton stop = new JButton("停止");
		stop.setBounds(15, 176, 133, 37);
		functionPanel.add(stop);
		stop.setVisible(false);
		
		stop.addActionListener(new ActionListener() {// 暂停
			public void actionPerformed(ActionEvent arg0) {
				clockThread.exit = true;
			}
		});

		autoyunxing.addActionListener(new ActionListener() {// 自动运行
			public void actionPerformed(ActionEvent arg0) {
				//functionPanel.add(stop);
				stop.setVisible(true);
				autoyunxing.setText("继续");
				clockThread = new ClockThread(Dynamic.this);
				clockThread.start();
			}
		});

	}

	void onestep() {// 执行一步
		if (count < 128) {// 未执行完
			count++;
			if (count != 1) {
				int change = -1;
				for (int i = 0; i < textField.length; i++) {
					textField[i].setForeground(Color.BLACK);
					textField[i].setFont(new Font("SimSun", Font.PLAIN, 16));
					textField1[i].setForeground(Color.BLACK);
					textField1[i].setFont(new Font("SimSun", Font.PLAIN, 24));
					textField[i].setText(String.valueOf(data[i][count - 1]));// 上一次页面存放情况
					textField1[i].setText(String.valueOf(data[i][count]));// 当前页面存放情况
System.out.println("自动运行第"+count +"次\n");
					if (data[i][count].equals(seq[count - 1])) {
						change = i;
						textField1[change].setForeground(Color.RED);
					}
				}

				currentpage.setText(String.valueOf(seq[count - 1]));
				currentstep.setText(String.valueOf(count));// 显示当前执行步数
			} else {// 执行的是第一步
				//int pagepos = seq[count];// 获取当前访问页面

				for (int i = 0; i < textField.length; i++) {
					textField[i].setForeground(Color.BLACK);
					textField[i].setFont(new Font("SimSun", Font.PLAIN, 24));
					textField1[i].setForeground(Color.BLACK);
					textField1[i].setFont(new Font("SimSun", Font.PLAIN, 16));
					textField[i].setText(String.valueOf(data[i][count]));
					textField1[i].setText("");
				}
				textField[0].setForeground(Color.RED);
				currentpage.setText(String.valueOf(seq[count - 1]));// 指令序列下标从0开始
				currentstep.setText(String.valueOf(count));// 显示当前执行步数
			}

		} else {// 执行完了
			JOptionPane.showMessageDialog(null, "页面置换算法已结束!");
			clockThread.exit = true;
		}
	}
}

class ClockThread extends Thread {
	Dynamic dy;
	public volatile boolean exit = false;
	public ClockThread(Dynamic dy1) {
		dy = dy1;
	}

	public void run() {
		super.run();
		while (!exit) {
			dy.onestep();
			//dy.currentstep.setText(String.valueOf(dy.count));
			try {
				Thread.sleep(1000);// 暂停一秒
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}