package gui;

import javax.swing.JCheckBox;

public class LocCheckBox extends JCheckBox implements ILocalizationListener{

	String key;
	@Override
	public void localizationChanged() {
		// TODO Auto-generated method stub
		setText(Messages.getString(Messages.getString(key)));
	}
	
	public LocCheckBox(String key) {
		// TODO Auto-generated constructor stub
		super(Messages.getString(key));
		this.key=key;
		Messages.addLocalizationListener(this);
	}

}
