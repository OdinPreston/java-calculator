public class JavaCalculator {
    public static void main(String args[]) {
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
