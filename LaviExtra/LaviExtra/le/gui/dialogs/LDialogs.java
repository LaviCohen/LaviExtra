package le.gui.dialogs;

import java.awt.Dialog.ModalityType;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import le.gui.ColorTheme;
import le.gui.components.LTextField;
import le.utils.VarHolder;

public class LDialogs {
	
	public static final int WARNING_MESSAGE = 1;
	public static final int ERROR_MESSAGE = 2;
	public static final int INFO_MESSAGE = 3;
	public static final int QUESTION_MESSAGE = 4;
	
	public static final int YES_OPTION = 0;
	public static final int NO_OPTION = 1;
	public static final int CANCEL_OPTION = 2;
	
	public static ColorTheme theme = ColorTheme.DEFAULT_COLOR_THEME;
	
	private static Icon warningIcon;
	private static Icon errorIcon;
	private static Icon infoIcon;
	private static Icon questionIcon;
	
	
	private static JDialog createDialog(Component owner, String title) {
		JDialog d = new JDialog(getWindowParent(owner), title, ModalityType.APPLICATION_MODAL);
		d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		d.getContentPane().setBackground(theme.getBackgroundColor());
		return d;
	}
	private static Window getWindowParent(Component owner) {
		if (owner == null || owner instanceof Window) {
			return (Window) owner;
		}
		return getWindowParent(owner.getParent());
	}
	private static Icon getIcon(int type) {
		switch (type) {
		case ERROR_MESSAGE:
			return errorIcon;
		case WARNING_MESSAGE:
			return warningIcon;
		case INFO_MESSAGE:
			return infoIcon;
		case QUESTION_MESSAGE:
			return questionIcon;
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}
	public static void showMessageDialog(Component owner, String message, String title, int type) {
		showOptionDialog(owner, message, title, 0, type, new String[] {"OK"}, 0);
	}
	public static int showConfirmDialog(Component owner, Object message) {
		return showConfirmDialog(owner, message, null);
	}
	public static int showConfirmDialog(Component owner, Object message, String title) {
		return showOptionDialog(owner, message, title, 0, QUESTION_MESSAGE,
				new String[] {"Yes", "No", "Cancel"}, 0);
	}
	public static void showMessageDialog(Component owner, String message) {
		showMessageDialog(owner, null, message, INFO_MESSAGE);
	}
	public static String showInputDialog(Component owner, String message) {
		return showInputDialog(owner, message, null);
	}
	public static String showInputDialog(Component owner, String message, String title) {
		JDialog d = createDialog(owner, title);
		d.setLayout(null);
		d.setBackground(theme.getBackgroundColor());
		JLabel label = new JLabel(message);
		label.setIcon(getIcon(QUESTION_MESSAGE));
		label.setBorder(new EmptyBorder(20, 30, 10, 30));
		d.add(label);
		label.setBounds(0, 0, label.getPreferredSize().width, label.getPreferredSize().height);
		theme.affect(label);
		
		LTextField textField = new LTextField("Enter your answer here...");
		theme.affect(textField);
		textField.setBounds(30, label.getPreferredSize().height, 280, 25);
		
		d.add(textField);
		
		JButton button = new JButton("OK");
		theme.affect(button);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				d.dispose();
			}
		});
		d.add(button);
		button.setBorder(BorderFactory.createLineBorder(null));
		button.setBounds(145, label.getPreferredSize().height + 40,
				60, 30);
		
		d.setSize(350, label.getPreferredSize().height + 130);
		if (owner == null) {
			d.setLocation(
					Toolkit.getDefaultToolkit().getScreenSize().width/2 - d.getWidth()/2,
					Toolkit.getDefaultToolkit().getScreenSize().height/2 - d.getHeight()/2);
		}
		d.setResizable(false);
		d.setVisible(true);	
		return textField.getText();
	}
	public static int showOptionDialog(Component owner,
	        Object message, String title, int optionType, int messageType,
	        Object[] options, Object initialValue){
		JDialog d = createDialog(owner, title);
		d.setLayout(null);
		d.setBackground(theme.getBackgroundColor());
		JLabel label = new JLabel(message.toString());
		label.setIcon(getIcon(messageType));
		label.setBorder(new EmptyBorder(20, 30, 10, 30));
		d.add(label);
		label.setBounds(0, 0, label.getPreferredSize().width, label.getPreferredSize().height);
		theme.affect(label);
		int totalX = 0;
		VarHolder<Integer> option = new VarHolder<Integer>((Integer) initialValue);
		if (optionType == 0) {
			for (int i = 0; i < options.length; i++) {
				final int index = i;
				JButton button = new JButton(options[i].toString());
				theme.affect(button);
				button.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						d.dispose();
						option.setValue(index);
					}
				});
				d.add(button);
				button.setBorder(BorderFactory.createLineBorder(null));
				button.setBounds(60 + totalX, label.getPreferredSize().height,
						button.getPreferredSize().width + 10, 30);
				totalX += button.getPreferredSize().width + 30;
			}
		}
		d.setSize((totalX < 300? 300:totalX), label.getPreferredSize().height + 80);
		if (owner == null) {
			d.setLocation(
					Toolkit.getDefaultToolkit().getScreenSize().width/2 - d.getWidth()/2,
					Toolkit.getDefaultToolkit().getScreenSize().height/2 - d.getHeight()/2);
		}
		d.setResizable(false);
		d.setVisible(true);	
		return option.getValue();
	}
}
