package ace4sd;

public class listWindows {

	private dialogProblems dialog_problems;
	private String ID;
	
	
	
	listWindows(){}
	
	listWindows(String ID, dialogProblems dialog_problems){
		this.ID = ID;
		this.dialog_problems = dialog_problems;
	}
	
	//setters
	public void set_dialog_problems(dialogProblems dialog_problems){
		this.dialog_problems = dialog_problems;
	}
	
	public void set_ID(String ID){
		this.ID = ID;
	}
	
	//getters
	public dialogProblems get_dialog_problems(){
		return this.dialog_problems;
	}
	
	public String get_ID(){
		return this.ID;
	}

}
