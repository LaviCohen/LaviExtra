package le.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import le.languages.AbstractTranslator;

/**
 * LSlider is a component which provides a label before the slider to the slider's subject, and also a text field next to the slider,
 * to let the user insert accurate values faster.
 * */
public class LSlider extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private JSlider slider;
	private JTextField field;
	private JLabel subject;
	private double valueFactor = 1;
	
	public LSlider(String subject, double minValue, double maxValue, double defaultValue, double valueFactor) {
		this(subject, (int) (minValue / valueFactor), (int) (maxValue / valueFactor),
				(int) (defaultValue / valueFactor));
		this.valueFactor = valueFactor;
	}
	public LSlider(String subject, int minValue, int maxValue, int rotation) {
		super(new BorderLayout());
		this.subject = new JLabel(subject);
		this.add(this.getSubject(), AbstractTranslator.getTranslator().getBeforeTextBorder());
		this.slider = new JSlider(minValue, maxValue, rotation);
		this.getSlider().setComponentOrientation(AbstractTranslator.getTranslator().getComponentOrientation());
		this.add(getSlider());
		this.field = new JTextField(rotation + "");
		this.add(field, AbstractTranslator.getTranslator().getAfterTextBorder());
		this.getSlider().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				field.setText(LSlider.this.getValue() + "");
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						LSlider.this.doLayout();
					}
				});
			}
		});
		field.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getSlider().setValue(Integer.valueOf(field.getText()));
				LSlider.this.doLayout();
			}
		});
	}
	public double getValue() {
		return getSlider().getValue() * valueFactor;
	}
	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		if (this.getSlider() != null) {
			this.getSubject().setBackground(bg);
			this.getSlider().setBackground(bg);
			this.field.setBackground(bg.brighter());
		}
	}
	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		if (this.getSlider() != null) {
			this.getSubject().setForeground(fg);
			this.getSlider().setForeground(fg);
			this.field.setForeground(fg);
		}
	}
	public JLabel getSubject() {
		return subject;
	}
	public JSlider getSlider() {
		return slider;
	}
}