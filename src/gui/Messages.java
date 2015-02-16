package gui;


import java.beans.Beans;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	////////////////////////////////////////////////////////////////////////////
	//
	// Constructor
	//
	////////////////////////////////////////////////////////////////////////////
	private Messages() {
		// do not instantiate
	}
	
	private static List<ILocalizationListener> listeners = new ArrayList<>();
	
	/**
	 * Dodaje zadani listener u listu listenera
	 */
	public static void addLocalizationListener(ILocalizationListener l) {
		if(!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	/**
	 * Brise zadani listener iz liste listenera
	 */
	public static void removeLocalizationListener(ILocalizationListener l) {
		if(listeners.contains(l)) {
			listeners.remove(l);
		}
	}
	
	/**
	 * Javlja svim listenerima da je doslo do promjene lokalizacije
	 */
	public static void fire() {
		ILocalizationListener[] array = listeners.toArray(new ILocalizationListener[listeners.size()]);
		for(ILocalizationListener l : array) {
			l.localizationChanged();
		}
	}
	////////////////////////////////////////////////////////////////////////////
	//
	// Bundle access
	//
	////////////////////////////////////////////////////////////////////////////
	private static final String BUNDLE_NAME = "gui.messages"; //$NON-NLS-1$
	private static ResourceBundle RESOURCE_BUNDLE = loadBundle();
	private static ResourceBundle loadBundle() {
		return ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
	}
	public static void changeBundle(){
		RESOURCE_BUNDLE = loadBundle();
	}
	public static void printStuff(){
		Enumeration<String> e=RESOURCE_BUNDLE.getKeys();
		while (e.hasMoreElements()){
			String key = (String) e.nextElement();
		    System.out.println(RESOURCE_BUNDLE.getString(key));
		}
	}
	////////////////////////////////////////////////////////////////////////////
	//
	// Strings access
	//
	////////////////////////////////////////////////////////////////////////////
	public static String getString(String key) {
		try {
			ResourceBundle bundle = Beans.isDesignTime() ? loadBundle() : RESOURCE_BUNDLE;
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}
}
