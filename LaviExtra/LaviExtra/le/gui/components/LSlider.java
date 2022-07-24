package le.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import le.languages.AbstractTranslator;

/**
 * LSlider is a component which provides a label before the slider to the slider's subject, and also a text field next to the slider,
 * to let the user insert accurate values faster.
 * */
public class LSlider extends JPanel{

	private static final long serialVersionUID = 1L;
	public JSlider slider;
	public JTextField field;
	public JLabel subject;
	public LSlider(String subject, int minValue, int maxValue, int defultValue) {
		super(new BorderLayout());
		this.subject = new JLabel(subject);
		this.add(this.subject, AbstractTranslator.getTranslator().getBeforeTextBorder());
		this.slider = new JSlider(minValue, maxValue, defultValue);
		this.slider.setComponentOrientation(AbstractTranslator.getTranslator().getComponentOrientation());
		this.add(slider);
		this.field = new JTextField(defultValue + "");
		this.add(field, AbstractTranslator.getTranslator().getAfterTextBorder());
		this.slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				field.setText(slider.getValue() + "");
			}
		});
		field.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				slider.setValue(Integer.valueOf(field.getText()));
				
			}
		});
	}
	public int getValue() {
		return slider.getValue();
	}
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if (this.slider != null) {
			this.subject.setBackground(bg);
			this.slider.setBackground(bg);
			this.field.setBackground(bg.brighter());
		}
	}
	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (this.slider != null) {
			this.subject.setForeground(fg);
			this.slider.setForeground(fg);
			this.field.setForeground(fg);
		}
	}
}