import java.util.ArrayDeque;
import java.math.BigDecimal;

abstract class Notation {

    private static boolean isOperator(char c) {
	switch(c) {
	case '^':
	case '*':
	case '/':
	case '+':
	case '-':
	    return true;
	}
	return false;
    }

    static boolean isNumber(char c) {
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

    static int precedingNonWhitespace(int start, char s[]) {
	// end is index of non-ws character
	// returns -1 if not found
	do
	    --start;
	while(start > -1 && s[start] == ' ');
	return start;
    }

    static int followingNonWhitespace(int end, char s[]) {
	// start is index of non-ws character
	// returns -1 if not found
	do
	    ++end;
	while(end < s.length && s[end] == ' ');
	return (end >= s.length) ? -1 : end;	
    }

    static Notation getType(String string)
    throws WrongExpressionException {
	char expr[] = string.toCharArray();
	int temp;
	int start = ((temp = followingNonWhitespace(-1, expr)) == -1) ? 0 : temp;
	int end = ((temp = precedingNonWhitespace(expr.length, expr)) == -1) ? expr.length-1 : temp;
	if(expr.length <= 0) {
	    throw new WrongExpressionException("Length of expression <= 0.");
	} else if(isOperator(expr[start])) {
	    System.out.println(string + ": Prefix Notation.");
	    return new PrefixNotation();
	} else if(isOperator(expr[end])) {
	    System.out.println(string + ": Postfix Notation.");
	    return new PostfixNotation();
	} else if(isNumber(expr[start]) || expr[start] == '(') {
	    System.out.println(string + ": Infix Notation.");
	    return new InfixNotation();
	}
	throw new WrongExpressionException(string + ": Unknown notation format.");
    }

    abstract Tuple<ArrayDeque<Character>, ArrayDeque<BigDecimal>> tokenize(String expr) throws WrongExpressionException;
    abstract BigDecimal evaluate(Tuple<ArrayDeque<Character>,ArrayDeque<BigDecimal>> tokens) throws WrongExpressionException;
}
