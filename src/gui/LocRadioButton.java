package gui;

import javax.swing.JRadioButton;

public class LocRadioButton extends JRadioButton implements ILocalizationListener{

	String key;
	@Override
	public void localizationChanged() {
		// TODO Auto-generated method stub
		setText(Messages.getString(Messages.getString(key)));
	}
	
	public LocRadioButton(String key) {
		// TODO Auto-generated constructor stub
		super(Messages.getString(key));
		this.key=key;
		Messages.addLocalizationListener(this);
	}

}
