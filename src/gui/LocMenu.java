package gui;

import javax.swing.JMenu;

public class LocMenu extends JMenu implements ILocalizationListener{

	String key;
	@Override
	public void localizationChanged() {
		// TODO Auto-generated method stub
		setText(Messages.getString(Messages.getString(key)));
	}
	
	public LocMenu(String key) {
		// TODO Auto-generated constructor stub
		super(Messages.getString(key));
		this.key=key;
		Messages.addLocalizationListener(this);
	}

}