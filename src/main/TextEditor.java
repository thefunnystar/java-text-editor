package main;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TextEditor extends JFrame implements ActionListener {

	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JButton openButton;
	private JButton saveButton;

	protected boolean textEdited;

	public TextEditor() {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Java Text Editor");
		this.setSize(904, 555);
		this.setLayout(new FlowLayout());
		this.setLocationRelativeTo(null);

		textArea = new JTextArea();
		textArea.setFont(new Font("Serif", Font.PLAIN, 14));

		scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(875, 475));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		openButton = new JButton("Open");
		saveButton = new JButton("Save");

		openButton.addActionListener(this);
		saveButton.addActionListener(this);

		this.add(openButton);
		this.add(saveButton);
		this.add(scrollPane);
		this.setVisible(true);

		textEdited = false;

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(this);
		this.add(exitButton);

		if (e.getSource() == openButton) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("."));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("text files", "txt");
			fileChooser.setFileFilter(filter);

			int response = fileChooser.showOpenDialog(null);

			if (response == JFileChooser.APPROVE_OPTION) {
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

				try (Scanner fileIn = new Scanner(file)) {
					fileIn.useDelimiter("\\Z");
					String content = fileIn.next();
					textArea.setText(content);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}

			textEdited = false;
		}

		if (e.getSource() == saveButton) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("."));

			int response = fileChooser.showSaveDialog(null);

			if (response == JFileChooser.APPROVE_OPTION) {
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

				try (PrintWriter fileOut = new PrintWriter(file)) {
					fileOut.println(textArea.getText());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}

		if (e.getSource() == exitButton) {
			if (textEdited) {
				int response = JOptionPane.showConfirmDialog(this,
						"There are unsaved changes. Are you sure you want to exit?", "Unsaved changes",
						JOptionPane.YES_NO_OPTION);

				if (response == JOptionPane.NO_OPTION) {
					return;
				}
			}
			System.exit(0);
		}

	}
}
