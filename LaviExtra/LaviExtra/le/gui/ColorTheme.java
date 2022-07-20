package le.gui;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

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
		@Override
		public JComponent affect(JComponent component) {
			if (component instanceof JTextComponent) {
				component.setBackground(getBackgroundColor().brighter());
				component.setForeground(getTextColor());
			}else if(component instanceof JButton){	
				component.setBackground(getBackgroundColor().darker());
				component.setForeground(getTextColor());
			}else{
				component.setOpaque(true);
				ColorTheme.super.affect(component);
			}
			return component;
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
