package le.gui;

import java.awt.Color;

import javax.swing.JComponent;

public interface ColorTheme {
	ColorTheme DEFAULT_COLOR_THEME = new ColorTheme() {
		
		Color backgroundColor = new Color(233, 233, 233);
		Color textColor = Color.BLACK;
		@Override
		public Color getTextColor() {
			return textColor;
		}
		
		@Override
		public Color getBackgroundColor() {
			return backgroundColor;
		}
	};
	public Color getBackgroundColor();
	public Color getTextColor();
	public default JComponent affect(JComponent component) {
		component.setBackground(getBackgroundColor());
		component.setForeground(getTextColor());
		return component;
	}
}
