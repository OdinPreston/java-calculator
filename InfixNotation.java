import java.util.ArrayDeque;
import java.math.BigDecimal;
import java.lang.Math;

class InfixNotation extends Notation {

    private static int precedence(char c) {
	switch(c) {
	case '^':
	    return 2;
	case '*':
	case '/':
	    return 3;
	case '+':
	case '-':
	    return 4;
	case '(': // for first check in tokenize
	    return 5;
	}
	return -1;
    }

    private static BigDecimal operation(char op, BigDecimal val1, BigDecimal val2)
    throws WrongExpressionException {
	switch(op) {
	case '^':
	    return new BigDecimal(Math.pow(val2.doubleValue(), val1.doubleValue()));
	case '*':
	    return val2.multiply(val1);
	case '/':
	    return val2.divide(val1);
	case '+':
	    return val2.add(val1);
	case '-':
	    return val2.subtract(val1);
	}
	throw new WrongExpressionException("Wrong operand: " + op);
    }
    
    @Override
    Tuple<ArrayDeque<Character>, ArrayDeque<BigDecimal>> tokenize(String string)
    throws WrongExpressionException {
	ArrayDeque<Character> operators = new ArrayDeque<Character>();
	ArrayDeque<BigDecimal> operands = new ArrayDeque<BigDecimal>();
	Tuple<ArrayDeque<Character>,ArrayDeque<BigDecimal>> result = new Tuple<ArrayDeque<Character>,ArrayDeque<BigDecimal>>(operators,operands);
	char expr[] = string.toCharArray();
	int i;
	for(i = 0; i < expr.length; ++i) {
	    switch(expr[i]) {
	    case '^':
	    case '*':
	    case '/':
	    case '+':
	    case '-':		
		if(operators.isEmpty() || precedence(expr[i]) <= precedence(operators.peek())) {
		    operators.addFirst(expr[i]);		    
		    break;
		}
		while(!operators.isEmpty() && operands.size() >= 2 && precedence(expr[i]) >= precedence(operators.peek())) {
		    char op = operators.removeFirst();
		    BigDecimal val1 = operands.removeFirst();
		    BigDecimal val2 = operands.removeFirst();
		    operands.addFirst(operation(op, val1, val2));
		}
		operators.addFirst(expr[i]);
		break;
	    case '(':
		operators.addFirst(expr[i]);
		break;
	    case ')':
		while(!operators.isEmpty() && operands.size() >= 2 && operators.peek() != '(') {
		    char op = operators.removeFirst();
		    BigDecimal val1 = operands.removeFirst();
		    BigDecimal val2 = operands.removeFirst();
		    operands.addFirst(operation(op, val1, val2));
		}
		if(operators.peek() == '(')
		    operators.removeFirst();
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
		String valueString = new String();
		while(i < expr.length && isNumber(expr[i]))
		    valueString += expr[i++];
		if(i < expr.length-1 && expr[i] == '.') {
		    valueString += expr[i++];
		    while(isNumber(expr[i]) && i+1 < expr.length)
			valueString += expr[i++];
		}
		BigDecimal value = new BigDecimal(valueString);
		operands.addFirst(value);
		if(i != expr.length)
		    --i;
		break;
	    case ' ':
		break;
	    default:
		throw new WrongExpressionException("Unknown token '" + expr[i] + "'. Expression '" + string + "' invalid.");
	    }
	}
	return result;
    }
    @Override
    BigDecimal evaluate(Tuple<ArrayDeque<Character>,ArrayDeque<BigDecimal>> tokens)
    throws WrongExpressionException {
	ArrayDeque<Character> operators = tokens.getFirst();
	ArrayDeque<BigDecimal> operands = tokens.getSecond();
	while(!operators.isEmpty() && operands.size() >= 2) {
	    char op = operators.removeFirst();
	    BigDecimal val1 = operands.removeFirst();
	    BigDecimal val2 = operands.removeFirst();
	    operands.addFirst(operation(op, val1, val2));
	}
	if(operands.size() > 1)
	    throw new WrongExpressionException("Operands left after evaluation, expression is invalid.");
	else if(operands.size() < 1)
	    throw new WrongExpressionException("No operands left after evaluation, expression is invalid.");
	else if(!operators.isEmpty())
	    throw new WrongExpressionException("Operators left after evaluation, expression is invalid.");
	return operands.removeFirst();
    }
}
