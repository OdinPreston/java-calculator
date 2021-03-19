import java.lang.ArrayIndexOutOfBoundsException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum Tokens {
    LPAREN, RPAREN, ADDITION, SUBTRACTION, MULTIPLICATION,
    DIVISION, MODULO, DOUBLE
}

class Tuple<L,R> {
    private L left;
    private R right;
    public Tuple(L left, R right) {
	this.left = left;
	this.right = right;
    }
    public L getFirst() {
	return left;
    }
    public R getSecond() {
	return right;
    }
    public void setFirst(L left) {
	this.left = left;
    }
    public void setSecond(R right) {
	this.right = right;
    }
}

class Solver {

    private boolean isOperator(char c) {
	switch(c) {
	case '*':
	case '/':
	case '+':
	case '-':
	    return true;
	}
	return false;
    }

    private boolean isNumber(char c) {
	switch(c) {
	case '0':
	case '1':
	case '2':
	case '3':
	case '4':
	case '5':
	case '6':
	case '7':
	case '8':
	case '9':
	    return true;
	}
	return false;
    }

    public boolean isValid(String string) {
	// remove whitespace, since it's insignificant
	string = string.replaceAll("\\s+","");
	System.out.println("string: " + string);
	boolean result = true;
	int i;
	char expr[] = string.toCharArray();
	tobreak:
	for(i = 0; i < expr.length; ++i) {
	    switch(expr[i]) {
	    case '+':
	    case '-':
	    case '*':
	    case '/':
		try {
		    if( (!isNumber(expr[i-1]) && expr[i-1] != ')') ||
			(!isNumber(expr[i+1]) && expr[i+1] != '(') ) {
			System.out.println("operator " + expr[i] + " at index " + i + " check failed");
			result = false;
			break tobreak;
		    }
		} catch (ArrayIndexOutOfBoundsException e) {
		    System.out.println("operator " + expr[i] + " at index " + i + " out of bounds");
		    result = false;
		    break tobreak;
		}
		break;
	    case '0':
	    case '1':
	    case '2':
	    case '3':
	    case '4':
	    case '5':
	    case '6':
	    case '7':
	    case '8':
	    case '9':
		try {
		    System.out.println("expr[i]: " + expr[i]);
		    int start = (i-1 < 0) ? 0 : i-1;
		    while(isNumber(expr[i]) && i+1 < expr.length)
			++i;
		    if(expr[i] == '.') {
			++i;
			while(isNumber(expr[i]) && i+1 < expr.length)
			    ++i;
		    }
		    int end = i;
		    System.out.println("start: " + start + " end: " + end);
		    if( (start != 0 && !isOperator(expr[start]) && expr[start] != '(') ||
			(end != expr.length-1 && !isOperator(expr[end]) && expr[end] != ')') ) {
			System.out.println("number check failed at index " + start + " to " + end);
			System.out.println(!isOperator(expr[start]));
			System.out.println(expr[start]);
			result = false;
			break tobreak;
		    }
		} catch(ArrayIndexOutOfBoundsException e) {
		    System.out.println("number out of bounds (length " + expr.length + ")");
		    result = false;
		    break tobreak;
		}
		if(i != expr.length-1)
		    --i; // to offset the loop increment
		break;
	    case '(':
		try {
		    if( (i != 0 && !isOperator(expr[i-1]) && expr[i-1] != '(') ||
			(!isNumber(expr[i+1]) && expr[i+1] != ')' && expr[i+1] != '(') ) {
			System.out.println("lparen check failed at index " + i);
			result = false;
			break tobreak;
		    }
		} catch(ArrayIndexOutOfBoundsException e) {
		    System.out.println("lparen out of bounds");
		    result = false;
		    break tobreak;
		}
		break;
	    case ')':
		try {
		    if( (i != expr.length-1 && !isOperator(expr[i+1]) && expr[i+1] != ')') ||
			(!isNumber(expr[i-1]) && expr[i-1] != '(' && expr[i-1] != ')') ) {
			System.out.println("rparen check failed at index " + i);
			result = false;
			break tobreak;
		    }
		} catch(ArrayIndexOutOfBoundsException e) {
		    System.out.println("rparen out of bounds");
		    result = false;
		    break tobreak;
		}
		break;
	    default:
		System.out.println("invalid character: " + expr[i]);
		result = false;
		break tobreak;
	    }
	}
	return result;
    }
    
    public ArrayList<Tuple<Tokens, String>> tokenizer(String... arguments) {

	ArrayList<Tuple<Tokens, String>> lexeme = new ArrayList<Tuple<Tokens, String>>();

	//	ArrayList<Tuple<String, Pattern>> tokens = new ArrayList<Tuple<String, Pattern>>();
	ArrayList<Tuple<Tokens, Pattern>> tokens = new ArrayList<Tuple<Tokens, Pattern>>();

	tokens.add(new Tuple<Tokens, Pattern>(Tokens.LPAREN, Pattern.compile("^\\(", Pattern.CASE_INSENSITIVE)));
	tokens.add(new Tuple<Tokens, Pattern>(Tokens.RPAREN, Pattern.compile("^\\)", Pattern.CASE_INSENSITIVE)));
	tokens.add(new Tuple<Tokens, Pattern>(Tokens.ADDITION, Pattern.compile("^\\+", Pattern.CASE_INSENSITIVE)));
	tokens.add(new Tuple<Tokens, Pattern>(Tokens.SUBTRACTION, Pattern.compile("^\\-", Pattern.CASE_INSENSITIVE)));
	tokens.add(new Tuple<Tokens, Pattern>(Tokens.MULTIPLICATION, Pattern.compile("^\\*", Pattern.CASE_INSENSITIVE)));
	tokens.add(new Tuple<Tokens, Pattern>(Tokens.DIVISION, Pattern.compile("^\\/", Pattern.CASE_INSENSITIVE)));
	tokens.add(new Tuple<Tokens, Pattern>(Tokens.MODULO, Pattern.compile("^\\%", Pattern.CASE_INSENSITIVE)));
	tokens.add(new Tuple<Tokens, Pattern>(Tokens.DOUBLE, Pattern.compile("[+-]?(^\\d+\\.\\d+|^\\.\\d+|^\\d+\\.|^\\d+)([eE]\\d+)?", Pattern.CASE_INSENSITIVE)));

	for(String toTokenize : arguments) {	   
	    boolean match;
	    int length = toTokenize.length();
	    int total = 0;
	    int i;
	    for(i = 0; total < length;) {
		System.out.println("toTokenize: " + toTokenize);
		match = false;
		for(int j = 0; j < tokens.size(); ++j) {
		    Matcher matcher = tokens.get(j).getSecond().matcher(toTokenize);
		    if(matcher.find()) {
			lexeme.add(new Tuple<Tokens, String>(tokens.get(j).getFirst(), matcher.group(0)));
			System.out.println("type: "+  lexeme.get(lexeme.size()-1).getFirst() + " val: " + lexeme.get(lexeme.size()-1).getSecond());
			total += matcher.end(0);
			toTokenize = toTokenize.substring(matcher.end(0));
			match = true;
			break;
		    }		
		}
		if(!match) { // do exception later maybe?
		    toTokenize = toTokenize.substring(1);
		    ++total;
		    System.out.println("bad");
		}
	    }
	}
	return lexeme;
    }
    public void validateLexeme(ArrayList<Tuple<Tokens, String>> lexeme) {
	int i;
	int j;
	int lpc = 0;
	int rpc = 0;
	for(i = 0, j = 1; j < lexeme.size(); ++j, ++i) {
	    Tokens first = lexeme.get(i).getFirst();
	    Tokens second = lexeme.get(j).getFirst();
	    if(first == second && first != Tokens.LPAREN && first != Tokens.RPAREN ||
	       (first != Tokens.LPAREN && first != Tokens.RPAREN && first != Tokens.DOUBLE) &&
	       (second != Tokens.LPAREN && second != Tokens.RPAREN && second != Tokens.DOUBLE)) {
		System.out.printf("%d and %d: illegal input\n", i, j);
	    }
	    System.out.println("first: " + first);
	    switch(first) {
	    case LPAREN:
		System.out.println("incrementing lpc");
		++lpc;
		break;
	    case RPAREN:
		if(lpc <= rpc)
		    System.out.printf("%d and %d: RPAREN precedes LPAREN\n", i, j);
		else {
		    ++rpc;
		    System.out.println("incrementing lpc");
		}

		break;
	    }
	}
	switch(lexeme.get(i).getFirst()) {
	case LPAREN:
	    ++lpc;
	    break;
	case RPAREN:
	    ++rpc;
	    break;
	case ADDITION:
	case SUBTRACTION:
	case MULTIPLICATION:
	case DIVISION:
	case MODULO:
	    System.out.println("unmatched operand\n");
	break;
	}
	if(lpc != rpc)
	    System.out.printf("parentheses don't match\n");
    }
	/*    public double solve(ArrayList<Tuple<String, String>> lexeme) {
	double solution = 0;
	for(int i = 0; i < lexeme.size(); ++i) {
	    ;
	}
	return solution;
	}*/
}

public class JavaCalculator {
    public static void main(String args[]) {
	Solver solver = new Solver();
	System.out.println(solver.isValid(args[0]));
	//	ArrayList<Tuple<Tokens, String>> lexeme = new ArrayList<Tuple<Tokens, String>>();
	//	lexeme = solver.tokenizer("(102.3*20) this is 20**30 */ a word 2 / 5+( 20 % 5 * 50");
	//	lexeme = solver.tokenizer(args);
	//	solver.validateLexeme(lexeme);
	/*	for(int i = 0; i < lexeme.size(); ++i) {
	    System.out.println(lexeme.get(i).getFirst() + " " + lexeme.get(i).getSecond());
	    }*/
    }
}
