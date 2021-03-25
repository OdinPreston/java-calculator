public class JavaCalculator {
    public static void main(String args[]) {
	if(args.length <= 0) {
	    Gui gui = new Gui();	    
	} else {
	    Notation n;
	    for(String s : args) {
		try {
		    n = Notation.getType(s);
		    System.out.println(n.evaluate(n.tokenize(s)));
		} catch(WrongExpressionException e) {
		    continue;
		}
	    }	    
	}
    }
}
