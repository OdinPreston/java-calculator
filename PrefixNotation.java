import java.util.ArrayDeque;
import java.math.BigDecimal;
import java.lang.Math;
import java.math.RoundingMode;

class PrefixNotation extends Notation {
    @Override
    Tuple<ArrayDeque<Character>, ArrayDeque<BigDecimal>> tokenize(String string)
    throws WrongExpressionException {
	ArrayDeque<Character> operators = new ArrayDeque<Character>();
	ArrayDeque<BigDecimal> operands = new ArrayDeque<BigDecimal>();
	Tuple<ArrayDeque<Character>,ArrayDeque<BigDecimal>> result = new Tuple<ArrayDeque<Character>,ArrayDeque<BigDecimal>>(operators,operands);
	char expr[] = string.toCharArray();
	int i;
	boolean unary = false;
	for(i = 0; i < expr.length; ++i) {
	    switch(expr[i]) {
	    case '^':
	    case '*':
	    case '/':
	    case '+':
		operators.addFirst(expr[i]);
		break;
	    case '-':
		// technically i != expr.length-1 is reduntant,
		// since we are sure to get prefix expression
		// without any trailing operators. but i'm
		// still checking in case something changes
		// in the future.
		if(i != expr.length-1 && isNumber(expr[i+1]))
		    unary = true;
		else
		    operators.addFirst(expr[i]);
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
		    while(i < expr.length && isNumber(expr[i]))
			valueString += expr[i++];
		}
		valueString = (unary) ? ("-" + valueString) : valueString;
		BigDecimal value = new BigDecimal(valueString);
		// round at 100 digits
		value.setScale(value.scale() - value.precision() - 100, RoundingMode.HALF_EVEN);
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
    BigDecimal evaluate(Tuple<ArrayDeque<Character>,ArrayDeque<BigDecimal>> tokens)
    throws WrongExpressionException {
	ArrayDeque<Character> operators = tokens.getFirst();
	ArrayDeque<BigDecimal> operands = tokens.getSecond();
	char op;
	BigDecimal val;
	while(!operators.isEmpty() && operands.size() >= 2) {
	    op = operators.removeFirst();
	    val = operands.removeFirst();
	    switch(op) {
	    case '^':
		operands.addFirst(new BigDecimal(Math.pow(operands.removeFirst().doubleValue(), val.doubleValue())));
		break;
	    case '*':
		operands.addFirst(operands.removeFirst().multiply(val));
		break;
	    case '/':
		if(val.compareTo(new BigDecimal("0")) == 0)
		    throw new WrongExpressionException("Division by zero");
		operands.addFirst(operands.removeFirst().divide(val));
		break;
	    case '+':
		operands.addFirst(operands.removeFirst().add(val));
		break;
	    case '-':
		operands.addFirst(operands.removeFirst().subtract(val));
		break;
	    }
	}
	if(operands.size() > 1)
	    throw new WrongExpressionException("Operands left after evaluation, expression is invalid.");
	else if(operands.size() < 1)
	    throw new WrongExpressionException("No operands left after evaluation, expression is invalid.");
	else if(!operators.isEmpty())
	    throw new WrongExpressionException("Operators left after evaluation, expression is invalid.");
	return operands.removeFirst().stripTrailingZeros();
    }
}
