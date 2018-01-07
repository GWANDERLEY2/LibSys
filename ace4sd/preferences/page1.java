package ace4sd.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import org.eclipse.jface.preference.FieldEditorPreferencePage;

import ace4sd.Activator;

// Preference's page in Eclipse to configure the general parameters of the plugin, such as
// Developer IP/port, OMAS IP/port, etc.
public class page1 extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{

	private RadioGroupFieldEditor editor;
	
	public page1() {
		super(GRID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	    setDescription("");
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		
	     addField(new StringFieldEditor("devName", "Developer name:",
			        getFieldEditorParent()));
		 addField(new StringFieldEditor("devLogin", "Developer login:",
			        getFieldEditorParent()));
	     addField(new StringFieldEditor("folderData", "Folder to save internal data:",
			        getFieldEditorParent()));
	     addField(new StringFieldEditor("server", "WebSockets server:",
			        getFieldEditorParent()));
	     // This concerns the period to send the extracted metrics to the XA in OMAS
	     //addField(new StringFieldEditor("metricsPeriod", "Period to extract metrics (ms):",
			//        getFieldEditorParent()));
	}
	
	@Override
	public boolean performOk()
	{
	  storeValues();

	  return super.performOk();
	}
	
	
	private void storeValues() {
	    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	    store.setValue("devName", store.getString("devName").trim());
	    store.setValue("devLogin", store.getString("devLogin").trim());
	    store.setValue("folderData", store.getString("folderData").trim());
	    store.setValue("server", store.getString("server").trim());
	   /* store.setValue("devIP", store.getString("devIP").trim());
	    store.setValue("tabPort", store.getString("tabPort"));
	    store.setValue("paWindowPort", store.getString("paWindowPort").trim());
	    store.setValue("omasIP", store.getString("omasIP").trim());
	    store.setValue("omasPort", store.getString("omasPort").trim());
	    store.setValue("metricsPeriod", store.getString("metricsPeriod").trim());*/
	    
	}
	
} // End of the class page1
