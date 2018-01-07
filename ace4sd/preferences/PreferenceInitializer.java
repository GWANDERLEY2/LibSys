package ace4sd.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import ace4sd.Activator;


// Initialize the preference's page with default values
public class PreferenceInitializer extends AbstractPreferenceInitializer {
    @Override
    public void initializeDefaultPreferences() {
    	
    	IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault("devName", "");
        store.setDefault("devLogin", "");
        store.setDefault("folderData", "C:\\ACE4SD\\");
        store.setDefault("server", "172.17.130.185");
        /*store.setDefault("devIP", "127.0.0.1");
        store.setDefault("tabPort", "50528");
        store.setDefault("paWindowPort", "50620");
        store.setDefault("omasIP", "127.0.0.1");
        store.setDefault("omasPort", "40001");
        store.setDefault("metricsPeriod", "30000");*/
    }
}