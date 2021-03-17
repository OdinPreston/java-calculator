import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public ArrayList<Tuple<String, String>> tokenizer(String toTokenize) {
	ArrayList<Tuple<String, String>> lexeme = new ArrayList<Tuple<String, String>>();

	ArrayList<Tuple<String, Pattern>> tokens = new ArrayList<Tuple<String, Pattern>>();

	tokens.add(new Tuple<String, Pattern>("LPAREN", Pattern.compile("^\\(", Pattern.CASE_INSENSITIVE)));
	tokens.add(new Tuple<String, Pattern>("RPAREN", Pattern.compile("^\\)", Pattern.CASE_INSENSITIVE)));
	tokens.add(new Tuple<String, Pattern>("MULTIPLICATION", Pattern.compile("^\\*", Pattern.CASE_INSENSITIVE)));
	tokens.add(new Tuple<String, Pattern>("DIVISION", Pattern.compile("^\\/", Pattern.CASE_INSENSITIVE)));
	tokens.add(new Tuple<String, Pattern>("MODULO", Pattern.compile("^[%]", Pattern.CASE_INSENSITIVE)));
	tokens.add(new Tuple<String, Pattern>("DOUBLE", Pattern.compile("[+-]?(^\\d+\\.\\d+|^\\.\\d+|^\\d+\\.|^\\d+)([eE]\\d+)?", Pattern.CASE_INSENSITIVE)));

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
		    lexeme.add(new Tuple<String, String>(tokens.get(j).getFirst(), matcher.group(0)));
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
	return lexeme;
    }
}

public class JavaCalculator {
    public static void main(String args[]) {
	Solver solver = new Solver();
	ArrayList<Tuple<String, String>> lexeme = new ArrayList<Tuple<String, String>>();
	lexeme = solver.tokenizer("(102.3*20) this is 20*30 a word 2 / 5+( 20 % 5 * 50)");
	for(int i = 0; i < lexeme.size(); ++i) {
	    System.out.println(lexeme.get(i).getFirst() + " " + lexeme.get(i).getSecond());
	}
    }
}
