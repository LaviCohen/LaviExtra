package le.gui.dialogs;

import java.awt.Dialog.ModalityType;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import le.gui.ColorTheme;
import le.gui.components.LTextField;
import le.utils.VarHolder;

/**
 * Replacement for @see java.swing.JOptionPane, to support another LaviExtra
 * features, such as translation and themes.
 */
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

	static {
		try {
			warningIcon = getIcon(ImageIO.read(LDialogs.class.getResourceAsStream("/le/resources/Warning.png")), 50,
					50);
			errorIcon = getIcon(ImageIO.read(LDialogs.class.getResourceAsStream("/le/resources/Error.png")), 50, 50);
			infoIcon = getIcon(ImageIO.read(LDialogs.class.getResourceAsStream("/le/resources/Info.png")), 50, 50);
			questionIcon = getIcon(ImageIO.read(LDialogs.class.getResourceAsStream("/le/resources/Question.png")), 50,
					50);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ImageIcon getIcon(Image image, int w, int h) {
		return new ImageIcon(getScaledImage(image, w, h));
	}

	public static BufferedImage getScaledImage(Image srcImg, int width, int height) {
		BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, width, height, null);
		g2.dispose();
		return resizedImg;
	}

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
		showOptionDialog(owner, message, title, 0, type, new String[] { "OK" }, 0);
	}

	public static int showConfirmDialog(Component owner, Object message) {
		return showConfirmDialog(owner, message, null);
	}

	public static int showConfirmDialog(Component owner, Object message, String title) {
		return showOptionDialog(owner, message, title, 0, QUESTION_MESSAGE, new String[] { "Yes", "No", "Cancel" }, 0);
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
		button.setBounds(145, label.getPreferredSize().height + 40, 60, 30);

		d.setSize(350, label.getPreferredSize().height + 130);
		if (owner == null) {
			d.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - d.getWidth() / 2,
					Toolkit.getDefaultToolkit().getScreenSize().height / 2 - d.getHeight() / 2);
		}
		d.setResizable(false);
		d.setVisible(true);
		return textField.getText();
	}

	public static int showOptionDialog(Component owner, Object message, String title, int optionType, int messageType,
			Object[] options, int initialValue) {
		JDialog d = createDialog(owner, title);
		d.setLayout(new BorderLayout());
		d.setBackground(theme.getBackgroundColor());
		JLabel label = new JLabel(message.toString());
		label.setIcon(getIcon(messageType));
		label.setBorder(new EmptyBorder(20, 30, 10, 30));
		d.add(label);
		label.setBounds(0, 0, label.getPreferredSize().width, label.getPreferredSize().height);
		theme.affect(label);
		JPanel buttons = new JPanel(new GridLayout(1, options.length + 2, 40, 20));
		theme.affect(buttons);
		buttons.add(new JComponent() {
			private static final long serialVersionUID = 1L;
		});
		JButton focused = null;
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
				if (i == initialValue) {
					focused = button;
				}
				buttons.add(button);
				button.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.BLACK),
						new EmptyBorder(8, 4, 8, 4)));
			}
		}
		buttons.add(new JComponent() {
			private static final long serialVersionUID = 1L;
		});
		buttons.setBorder(new EmptyBorder(15, 0, 15, 0));
		d.add(buttons, BorderLayout.SOUTH);
		d.pack();
		if (owner == null) {
			d.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - d.getWidth() / 2,
					Toolkit.getDefaultToolkit().getScreenSize().height / 2 - d.getHeight() / 2);
		}else {
			d.setLocation(owner.getWidth() / 2 - d.getWidth() / 2 + owner.getX(),
					owner.getHeight() / 2 - d.getHeight() / 2 + owner.getY());
		}
		d.setResizable(false);
		focused.grabFocus();
		d.setVisible(true);
		return option.getValue();
	}
}
