package gui;

import javax.swing.JMenuItem;

public class LocMenuItem extends JMenuItem implements ILocalizationListener{

	String key;
	@Override
	public void localizationChanged() {
		// TODO Auto-generated method stub
		setText(Messages.getString(Messages.getString(key)));
	}
	
	public LocMenuItem(String key) {
		// TODO Auto-generated constructor stub
		super(Messages.getString(key));
		this.key=key;
		Messages.addLocalizationListener(this);
	}

}