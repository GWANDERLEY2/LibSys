package ace4sd;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class dialogProblemsThread  implements Runnable{

	private Shell shell;
	private ArrayList<codeProblems> code_problemsList;
	private listWindows xx;
	
	dialogProblemsThread(Shell shell, ArrayList<codeProblems> code_problemsList){
		this.shell = shell;
		this.code_problemsList = code_problemsList;
	}
	
	dialogProblemsThread(listWindows xx){
		this.xx = xx;
	}
	
	
	public void open(){

		Display.getDefault().asyncExec(new Runnable() {
		    public void run() {
		  	 xx.get_dialog_problems().open();
		    }
		    
		});
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		open();
	}
}
