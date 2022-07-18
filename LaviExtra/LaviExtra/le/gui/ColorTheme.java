package le.gui;

import java.awt.Color;

import javax.swing.JComponent;

public interface ColorTheme {
	public Color getBackgroundColor();
	public Color getTextColor();
	public default JComponent affect(JComponent component) {
		component.setBackground(getBackgroundColor());
		component.setForeground(getTextColor());
		return component;
	}
}
