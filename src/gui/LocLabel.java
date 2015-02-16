package gui;

import javax.swing.JLabel;

public class LocLabel extends JLabel implements ILocalizationListener{

	String key;
	@Override
	public void localizationChanged() {
		// TODO Auto-generated method stub
		setText(Messages.getString(Messages.getString(key)));
	}
	
	public LocLabel(String key) {
		// TODO Auto-generated constructor stub
		super(Messages.getString(key));
		this.key=key;
		Messages.addLocalizationListener(this);
	}

}
