import java.util.ArrayDeque;
import java.lang.Math;

class PostfixNotation extends Notation {
    @Override
    Tuple<ArrayDeque<Character>, ArrayDeque<Double>> tokenize(String string)
    throws WrongExpressionException {
	ArrayDeque<Character> operators = new ArrayDeque<Character>();
	ArrayDeque<Double> operands = new ArrayDeque<Double>();
	Tuple<ArrayDeque<Character>,ArrayDeque<Double>> result = new Tuple<ArrayDeque<Character>,ArrayDeque<Double>>(operators,operands);
	char expr[] = string.toCharArray();
	int i;
	for(i = 0; i < expr.length; ++i) {
	    switch(expr[i]) {
	    case '^':
	    case '*':
	    case '/':
	    case '+':
	    case '-':
		operators.add(expr[i]);
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
		while(isNumber(expr[i]) && i+1 < expr.length)
		    valueString += expr[i++];
		if(expr[i] == '.') {
		    valueString += expr[++i];
		    while(isNumber(expr[i]) && i+1 < expr.length)
			valueString += expr[i++];
		}
		double value = Double.parseDouble(valueString);
		operands.addFirst(value);
		if(i != expr.length-1)
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
    double evaluate(Tuple<ArrayDeque<Character>,ArrayDeque<Double>> tokens)
    throws WrongExpressionException {
	ArrayDeque<Character> operators = tokens.getFirst();
	ArrayDeque<Double> operands = tokens.getSecond();
	char op;
	double val;
	double result = 0;
	while(!operators.isEmpty() && operands.size() >= 2) {
	    op = operators.removeFirst();
	    val = operands.removeFirst();
	    switch(op) {
	    case '^':
		operands.addFirst(Math.pow(operands.removeFirst(), val));
		break;
	    case '*':
		operands.addFirst(operands.removeFirst() * val);
		break;
	    case '/':
		operands.addFirst(operands.removeFirst() / val);
		break;
	    case '+':
		operands.addFirst(operands.removeFirst() + val);
		break;
	    case '-':
		operands.addFirst(operands.removeFirst() - val);
		break;
	    }
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
