package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class NewConfig extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField beforeId;
	private JTextField afterId;
	private JTextField beforeMessage;
	private JTextField afterMessage;
	private JTextField beforeBlock;
	private JTextField afterBlock;

	/**
	 * Create the frame.
	 */
	public NewConfig() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(250, 50, 450, 600);
		setTitle(Messages.getString("NewConfig.title"));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 11));
		textArea.setText(Messages.getString("NewConfig.textArea.text")); //$NON-NLS-1$
		textArea.setBounds(10, 11, 398, 79);
		contentPane.add(textArea);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"SHIFT", "<SHIFT>"},
				{"TAB", "<TAB>"},
				{"ALT", "<ALT>"},
				{"DELETE", "<DEL>"},
				{"CTRL", "<CONTROL>"},
				{"BACKSPACE", "<BACK_SPACE>"},
				{"INSERT", "<INSERT>"},
				{"ENTER", "<ENTER>"},
				{"SPACE", "<SPACE>"},
			},
			new String[] {
				"Key", "Representation"
			}
		));
		table.getColumnModel().getColumn(1).setPreferredWidth(112);
		table.setBounds(10, 101, 364, 144);
		contentPane.add(table);
		
		JLabel lblUserId = new JLabel(Messages.getString("Main.chckbxSenderId.text")); //$NON-NLS-1$
		lblUserId.setBounds(10, 255, 96, 14);
		contentPane.add(lblUserId);
		
		final JCheckBox beforeIdch = new JCheckBox(Messages.getString("NewConfig.chckbxBeforeAdd.text")); //$NON-NLS-1$
		beforeIdch.setBounds(10, 276, 96, 23);
		contentPane.add(beforeIdch);
		
		final JCheckBox afterIdch = new JCheckBox(Messages.getString("NewConfig.chckbxAfterAdd.text")); //$NON-NLS-1$
		afterIdch.setBounds(10, 302, 108, 23);
		contentPane.add(afterIdch);
		
		beforeId = new JTextField();
		beforeId.setText("");
		beforeId.setBounds(124, 277, 161, 20);
		contentPane.add(beforeId);
		beforeId.setColumns(10);
		
		afterId = new JTextField();
		afterId.setText("");
		afterId.setBounds(124, 303, 161, 20);
		contentPane.add(afterId);
		afterId.setColumns(10);
		
		JLabel lblMessage = new JLabel(Messages.getString("Main.chckbxMessage.text")); //$NON-NLS-1$
		lblMessage.setBounds(10, 332, 77, 14);
		contentPane.add(lblMessage);
		
		final JCheckBox beforeMsgch = new JCheckBox(Messages.getString("NewConfig.chckbxBeforeAdd.text")); //$NON-NLS-1$
		beforeMsgch.setBounds(10, 353, 90, 23);
		contentPane.add(beforeMsgch);
		
		final JCheckBox afterMsgch = new JCheckBox(Messages.getString("NewConfig.chckbxAfterAdd.text")); //$NON-NLS-1$
		afterMsgch.setBounds(10, 379, 109, 23);
		contentPane.add(afterMsgch);
		
		beforeMessage = new JTextField();
		beforeMessage.setText("");
		beforeMessage.setBounds(130, 354, 139, 20);
		contentPane.add(beforeMessage);
		beforeMessage.setColumns(10);
		
		afterMessage = new JTextField();
		afterMessage.setText("");
		afterMessage.setBounds(130, 380, 139, 20);
		contentPane.add(afterMessage);
		afterMessage.setColumns(10);
		
		final JCheckBox beforeLine = new JCheckBox(Messages.getString("NewConfig.chckbxBeforeEachLine.text")); //$NON-NLS-1$
		beforeLine.setBounds(285, 353, 123, 23);
		contentPane.add(beforeLine);
		
		final JCheckBox afterLine = new JCheckBox(Messages.getString("NewConfig.chckbxNewCheckBox_1.text")); //$NON-NLS-1$
		afterLine.setBounds(285, 379, 143, 23);
		contentPane.add(afterLine);
		
		JLabel lblAnswerBlock = new JLabel(Messages.getString("NewConfig.lblAnswerBlock.text")); //$NON-NLS-1$
		lblAnswerBlock.setBounds(10, 409, 96, 14);
		contentPane.add(lblAnswerBlock);
		
		final JCheckBox beforeBlockch = new JCheckBox(Messages.getString("NewConfig.chckbxBeforeAdd.text")); //$NON-NLS-1$
		beforeBlockch.setBounds(10, 430, 90, 23);
		contentPane.add(beforeBlockch);
		
		final JCheckBox afterBlockch = new JCheckBox(Messages.getString("NewConfig.chckbxAfterAdd.text")); //$NON-NLS-1$
		afterBlockch.setBounds(10, 456, 108, 23);
		contentPane.add(afterBlockch);
		
		beforeBlock = new JTextField();
		beforeBlock.setText("");
		beforeBlock.setBounds(124, 431, 161, 20);
		contentPane.add(beforeBlock);
		beforeBlock.setColumns(10);
		
		afterBlock = new JTextField();
		afterBlock.setText("");
		afterBlock.setBounds(124, 457, 161, 20);
		contentPane.add(afterBlock);
		afterBlock.setColumns(10);
		
		JButton btnSave = new JButton(Messages.getString("NewConfig.btnSave.text")); //$NON-NLS-1$
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList l=new ArrayList();
				String nl=System.getProperty("line.separator");
				if (beforeIdch.isSelected() && !beforeId.getText().equals("")) l.add(nl+"BeforeId:" + beforeId.getText());
				if (afterIdch.isSelected() && !afterId.getText().equals("")) l.add(nl+"AfterId:" + afterId.getText());
				if (beforeMsgch.isSelected() && !beforeMessage.getText().equals("")) l.add(nl+"BeforeMessage:" + beforeMessage.getText());
				if (afterMsgch.isSelected() && !afterMessage.getText().equals("")) l.add(nl+"AfterMessage:" + afterMessage.getText());
				if (beforeLine.isSelected() && beforeMsgch.isSelected() && !beforeMessage.getText().equals("")) l.add(nl+"BeforeLine");
				if (afterLine.isSelected() && afterMsgch.isSelected() && !afterMessage.getText().equals("")) l.add(nl+"AfterLine");
				if (beforeBlockch.isSelected() && !beforeBlock.getText().equals("")) l.add(nl+"BeforeBlock:" + beforeBlock.getText());
				if (afterBlockch.isSelected() && !afterBlock.getText().equals("")) l.add(nl+"AfterBlock:" + afterBlock.getText());
				final JFileChooser fc = new JFileChooser();
				int returnVal = fc.showSaveDialog(NewConfig.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						PrintWriter pw=new PrintWriter(file);
						for (int i=0; i<l.size(); i++){
							String s=(String) l.get(i);
							pw.write(s);
						}
						pw.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnSave.setBounds(10, 510, 89, 23);
		contentPane.add(btnSave);
		
		JButton btnClearAll = new JButton(Messages.getString("NewConfig.btnClearAll.text")); //$NON-NLS-1$
		btnClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				beforeId.setText("");
				afterId.setText("");
				beforeMessage.setText("");
				afterMessage.setText("");
				beforeBlock.setText("");
				afterBlock.setText("");
			}
		});
		btnClearAll.setBounds(149, 510, 125, 23);
		contentPane.add(btnClearAll);
		
		JButton btnClose = new JButton(Messages.getString("NewConfig.btnClose.text")); //$NON-NLS-1$
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnClose.setBounds(319, 510, 89, 23);
		contentPane.add(btnClose);
		setVisible(true);
	}
}
