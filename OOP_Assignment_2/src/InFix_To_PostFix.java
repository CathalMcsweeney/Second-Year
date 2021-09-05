import java.awt.*;
import javax.swing.JOptionPane;

public class InFix_To_PostFix {
	
	private static String inFix, postFix;
	private static double ans;
	private static char[] validChars = {'0','1','2','3','4','5','6','7','8','9','*','/','-','^','+','(',')'};
	private static ArrayStack s = new ArrayStack(20);
	
	public static void main(String[] args) {
	
		boolean validated = false;
		
		//loop that will only run once if the entered calculation is correct. wrong entries result in requesting again input off user
		while(validated == false) {
		inFix = JOptionPane.showInputDialog("Enter Calculation: ");
		
		//Validates user input in method
		validated = validateString(inFix);
		}
		
		//shows user what they have ipput to be calculated
		JOptionPane.showMessageDialog(null,"You have entered "+inFix+" to be calculated");
		
		//changing inFix to postFix
		postFix = inFixToPostFix();
		
		//calculate user input
		ans = calculate(postFix);
		
		//final output showing the user the result of their postfix and result of their input
		JOptionPane.showMessageDialog(null,"The result of the expression is: \nInFix: "+inFix+"\nPostFix: "+postFix+"\nResult: "+ans);
		
	}

	//validation method checks string input of user to ensure its valid
	public static boolean validateString(String check) {
		boolean valid = false;
		boolean tmo = false; //too many operators 
		
		//checks length of input is correct
		if(check.length()<3 || check.length()>20) {
			return valid;
		}
		
		//loops through chars in input string checking each one is valid
		for(int i=0;i<inFix.length();i++) {
			char nextInChar = inFix.charAt(i);
			int count=1;
			
			//checks to make sure that its single digits only
			if(i!=(check.length()-1)) {
				if(Character.isDigit(nextInChar) && Character.isDigit(inFix.charAt(i+1))) {
					valid = false;
					break;
				}
			}
			
			//ensure that users have not entered 2 operators together other than parentheses
			for(int j = 10; j<15;j++) {
				if(nextInChar==validChars[j]) {
					for(int k = 10; k<15;k++) {
						if(inFix.charAt(i+1)==validChars[k]) {
							tmo = true;
							break;
						}
					}
					if(tmo == true) {break;}
				}
			}
			if(tmo == true) {
				valid = false;
				break;
			}
			
			//nested loop to compare current char against each valid character in array
			for(char v: validChars ) {
				
				count++;//count used to identify if loop has iterated through length of valid char array & no valid char is found
				
				//if valid char is found breaks from nested loop onto next char in string
				if(nextInChar==v) {
					valid = true;
					break;
				}
				
				//if no valid char is found valid flag set to false and breaks from nester loop
				if(count == validChars.length) {
					valid = false;
				}
			}
			//if valid flag is set to false breaks from loop and returns a false for the boolean
			if(valid == false) {
				break;
			}
		}
		return valid;		
	}

	
	//inFix to postFix function
	public static String inFixToPostFix() {
		
		String nums = "";
		
		//for loop iterates through all the chars in the infix String 
		for(int i = 0 ; i < inFix.length() ; i++) {
			char curChar = inFix.charAt(i);
			
			//if current char in string is a number adds it to the string
			if(Character.isDigit(curChar)) {
				nums += curChar;
			}
			
			//use the stack to implement the correct order of operators for the calculation
			else {
				
				//adds the current char in the string to the stack depending on precedence & what may be currently on it
				if(s.isEmpty() || precedence(curChar)>precedence((char)s.top()) || (char)s.top()=='(') {
					s.push(curChar);
				}
				
				//pops off operators appends to string depending on current character & what is already on the stack
				else {
					while(precedence(curChar)<=precedence((char)s.top()) && curChar!='(' && curChar!=')') {
						
						//breaks out of loop if a parentheses is met on the stack
						if((char)s.top()=='(') {
							break;
						}
						
						//continues to pop operators off the stack until the while == false OR the stack is empty
						nums+=(char)s.pop();
						if(s.isEmpty()==true) {break;}
					}
					s.push(curChar);
				}
			}
			
			//pushes a multiply operator to the stack if opening brackets are found & its not the start of the equation
			if(curChar=='(' && s.size()!=1) {
				s.push('*');
				s.push(curChar);
				}
			
			//when a closing parentheses is met the contents of the stack are appended to the string until a opening bracket is met
			if (curChar==')') {
				while((char)s.top()!='(' && (char)s.top()!=')') {
					nums+=(char)s.pop();
				}
				//the opening parentheses is then popped from stack
				s.pop();
				
			}
		}
		//any operators left on the stack are popped and appended to the string in their order of precedence
			while(s.isEmpty()!=true) {
				if((char)s.top()=='(' || (char)s.top()==')') {
					s.pop();
					continue;
				}
				nums+=(char)s.pop();
				}
		return nums;
	}
	
	//calculate the postFix equation
	public static double calculate(String postFix) {
		//shows the postfix equation that needs to be calculated
		JOptionPane.showMessageDialog(null,postFix);
		
		double num1,num2,result=0;
		
		for(int i=0;i<postFix.length();i++) {
			char curChar = postFix.charAt(i);
			
			//if the current character is a digit its added to the stack
			if(Character.isDigit(curChar)) {
				s.push((double) Character.getNumericValue(curChar));
			}
			
			else {
				//when an operator is met the next 2 numbers on the stack are assigned to variables and used in a calculation
				num1=(double)s.pop();
				num2=(double)s.pop();
				switch(curChar) {
				case '*':
					result = num1*num2;
					System.out.println(num1+"*"+num2+"="+result);
					break;
				case '/':
					result = num2/num1;
					System.out.println(num2+"/"+num1+"="+result);
					break;
				case '+':
					result = num1+num2;
					System.out.println(num1+"+"+num2+"="+result);
					break;
				case '-':
					result = num2-num1;
					System.out.println(num2+"-"+num1+"="+result);
					break;
				case '^':
					result = (int)Math.pow(num1, num2);
					break;
				}
				//the result is then pushed to the stack for further calculations
				s.push(result);
			}
		}
		return result;
	}
	
	//gives each operator a value to identify its precedence as per 'BOMDAS'

	public static int precedence(char x) {
		char check = x;
		int y=0;
		
		if(check == '^') {
			y= 3;
		}
		if(check == '*' || check =='/') {
			y= 2;
		}
		if(check == '+' || check == '-') {
			y= 1;
		}
		return y; 
	}
}	
