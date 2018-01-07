package ace4sd.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.graphics.Color;

import ace4sd.Activator;
import ace4sd.metricsTimer;
import ace4sd.monitorMetrics;
import ace4sd.socketUDP_Informations;

import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.commands.ICommandService;

// describes the "Eclipse TAB information's" view
public class ace4sdView extends ViewPart {

private Label label;
Action addItemAction, deleteItemAction, selectAllAction;
private Text text;
private StyledText styledText;
private IPreferenceStore store;

private StyledText styledTextInput;
	
public ace4sdView() {
   super();
   this.store = Activator.getDefault().getPreferenceStore();
}

@Override
public void createPartControl(Composite parent) {
		
	// get the current shell
	Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	//shell.setLayout(new FillLayout());
	
	Color blue = shell.getDisplay().getSystemColor(SWT.COLOR_BLUE);
    
	parent.setLayout(new RowLayout());//SWT.VERTICAL));
	
	/*ScrolledComposite sc = new ScrolledComposite(shell, SWT.V_SCROLL | 
			SWT.H_SCROLL);
			Composite c = new Composite(sc, SWT.NONE);
			c.setLayout(new GridLayout());*/
	
//	Label label = new Label(parent, SWT.LEFT);
	//label.
	
	 /*Canvas canvas = new Canvas(parent, SWT.NONE);

	    canvas.addPaintListener(new PaintListener()
	      {
	        @Override
	        public void paintControl(final PaintEvent event)
	        {
	          GC gc = event.gc;
	          
	         
	          gc.drawString("Quality Problems", 0, 0);

	          
	          //gc.drawLine(0, 0, 100, 100);
	          //gc.drawLine(100, 0, 0, 100);
	        }
	      });*/
	
	styledText = new StyledText(parent, SWT.LEFT | SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.WRAP);//(parent, SWT.WRAP | SWT.BORDER);
    styledText.setEditable(false);
    styledText.setBackground(new Color(shell.getDisplay(), 245, 255, 250));
    styledText.setLayoutData(new RowData(1030, 240));
    //styledText.setBounds(10,10,100,100);
    //GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
    //data.widthHint = 350;
    //styledText.setLayoutData(data);
    
 
    
    
    styledTextInput = new StyledText(parent, SWT.LEFT | SWT.SINGLE | SWT.BORDER); //| SWT.V_SCROLL | SWT.H_SCROLL);//(parent, SWT.WRAP | SWT.BORDER);
    //styledTextInput.setEditable(false);
    styledTextInput.setBackground(new Color(shell.getDisplay(), 245, 255, 250));
    styledTextInput.setLayoutData(new RowData(980, 20));
    //styledTextInput.setBounds(10,300,100,100);//(0, 100, 400, 50);
    //setBounds(10,300,100,100);*/
    
    
    Button button = new Button(parent, SWT.PUSH);
    button.setLayoutData(new RowData(60, 27));
    button.setText("SEND");
   
    
   // blue = shell.getDisplay().getSystemColor(SWT.COLOR_BLUE);
        /*styledText.setText("Bonjour");
		StyleRange styleRange = new StyleRange();
	  	styleRange.start = 0;
	  	styleRange.length = 4;
	  	styleRange.fontStyle = SWT.BOLD;
	  	styleRange.foreground = blue;
	  	styledText.setStyleRange(styleRange);*/
	
	
	/*styledText.setText("Bonjour");
	
	
	StyleRange styleRange = new StyleRange();
	styleRange.start = 0;
	styleRange.length = 4;
	styleRange.fontStyle = SWT.BOLD;
	styleRange.foreground = blue;
	styledText.setStyleRange(styleRange);*/
	
	
	//text = new Text(parent, SWT.LEFT | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
    // text.setEditable(false);
    
  //  text2 = new Text(parent, SWT.LEFT | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
   // text2.setEditable(false);
    
    
    /*Display display = Display.getCurrent();
    Color blue = display.getSystemColor(SWT.COLOR_BLUE);
   
    text.setForeground(blue);*/
    
   
	createActions();
         	
    // Thread to control the receiving of informations from the OMAS, in the "Information Tab" 
    // of the Eclipse OMAS plugin
       socketUDP_Informations informations_tab = null;
	try {
		informations_tab = new socketUDP_Informations(store.getString("devLogin"), shell, styledText, styledTextInput, button, store.getString("server"), store.getString("folderData"));
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
        
    // build the thread
       Thread thread_Informations =  new Thread(informations_tab);
       thread_Informations.setDaemon(true);
       
       
       
    // Thread to monitor whether new metric files were created. If so, then send it to OMAS in JSON format.
      // monitorMetrics monitor = new monitorMetrics(store.getString("folderData"), store.getString("devName"), store.getString("devLogin"));
         	
    // build the thread
      // Thread thread_monitor = new Thread(monitor);
       
    // Thread to monitor whether new metric files were created. If so, then send it to OMAS in JSON format.
      // metricsTimer mTimer = new metricsTimer();
         	
    // build the thread
      // Thread thread_timer = new Thread(mTimer);
       
       
         	
    // start the threads
       thread_Informations.start();
       //thread_monitor.start();
       //thread_timer.start();
         	
       System.out.println("Thread \"Informations Tab\" is running");
       //System.out.println("Thread \"Monitor metric files\" is running");
}
	
public void createActions() {
   		
	   // Creating the button to the PA dialogue window 
	   // Custom Action for the View's Menu
	   speakPA lCustomAction = new speakPA(store.getString("devLogin"));
	   lCustomAction.setText("SpeakPA");
	   lCustomAction.setImageDescriptor(Activator.getImageDescriptor("icons/voice.png")); //speaking-icon.png"));
       IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
       mgr.add(lCustomAction);
        
}
	
@Override
public void setFocus() {
// TODO Auto-generated method stub
	styledTextInput.setFocus();
}
	
} // end of the class ace4sdView