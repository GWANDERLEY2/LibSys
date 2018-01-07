package ace4sd;

import java.util.ArrayList;

public class codeProblems {

private String part_of_code;
private String type;
private String a_project;
private String a_package;
private String source;
private String login;
private String developer;
private String ID;
private String date;

private ArrayList<String> solutions;

//constructor for types "method" and "class"
	public codeProblems (String part_of_code, String type, String a_project, 
			            String a_package,  String source, String ID, String date){
		
		this.part_of_code = part_of_code;
		this.type = type;
		this.a_project = a_project;
		this.a_package = a_package;
		this.source = source;
		//this.login = login;
		//this.developer = developer;
		this.ID = ID;
		this.date = date;
		
		solutions = new ArrayList<String>();
	}
	
	// constructor for type "package"
	public codeProblems (String part_of_code, String type, String a_project, 
            String a_package, String ID, String date){

		this.part_of_code = part_of_code;
		this.type = type;
		this.a_project = a_project;
		this.a_package = a_package;
		//this.login = login;
		//this.developer = developer;
		this.ID = ID;
		this.date = date;

		solutions = new ArrayList<String>();
}
	
	// constructor for type "project"
	public codeProblems (String part_of_code, String type, String a_project, 
                         String ID, String date){

		this.part_of_code = part_of_code;
		this.type = type;
		this.a_project = a_project;
		//this.login = login;
		//this.developer = developer;
		this.ID = ID;
		this.date = date;

		solutions = new ArrayList<String>();
}
	
	public codeProblems(){}
	
	// Setters
	public void set_part_of_code (String part_of_code){
		this.part_of_code = part_of_code;
	}
	
	public void set_type (String type){
		this.type = type;
	}
	
	public void set_a_project (String a_project){
		this.a_project = a_project;
	}
	
	public void set_a_package (String a_package){
		this.a_package = a_package;
	}
	
	public void set_source (String source){
		this.source = source;
	}
	
	public void set_login (String login){
		this.login = login;
	}

	public void set_developer (String developer){
		this.developer = developer;
	}
	
	public void set_ID (String ID){
		this.ID = ID;
	}
	
	public void set_date (String date){
		this.date = date;
	}
	
	//getters
	public String get_part_of_code(){
		return this.part_of_code;
	}
	
	public String get_type(){
		return this.type;
	}
	
	public String get_a_project(){
		return this.a_project;
	}
	
	public String get_a_package(){
		return this.a_package;
	}
	
	public String get_source(){
		return this.source;
	}
	
	public String get_login(){
		return this.login;
	}
	
	public String get_developer(){
		return this.developer;
	}
	
	public String get_ID(){
		return this.ID;
	}
	
	public String get_date(){
		return this.date;
	}
	
	public ArrayList<String> get_solutions(){
		return this.solutions;
	}
}
