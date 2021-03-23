# Java Calculator
A simple calculator capable of evaluating infix, postfix and prefix expressions.
For now, '^', '*', '/', '+', '-' are supported. Additionally, '(' and ')' are valid in infix notation.
## Building
```bash
git clone https://github.com/OdinPreston/java-calculator.git
cd java-calculator
javac *.java
```
At this point you have two options. You can run the program using `java JavaCalculator ARGS` where `ARGS` are command line arguments. Or you can also:
### Create a .jar file
```bash
jar cvfe JavaCalculator.jar JavaCalculator *.class
```
You can then put JavaCalculator.jar in place of your choice, and run it with `java -jar /path/to/JavaCalculator.jar`. For convenience you can create a script file to launch it akin to a binary, like:
```bash
echo '#!/path/to/java -jar' > JavaCalculator
cat JavaCalculator.jar >> JavaCalculator
chmod +x JavaCalculator
```
Where `/path/to/java` is the absolute path to java executable.
Then you can use JavaCalculator to launch the program, as any other binary (even though it's not a binary).
## Usage
Run with command-line arguments, where each argument is a single expression in any of the supported notations. For example:
`JavaCalculator "2+2*50" "* / 9 3 20" "20 6 4 + *"`
will produce
```
2+2*50: Infix Notation.
102.0
* / 9 3 20: Prefix Notation.
60.0
20 6 4 + *: Postfix Notation.
200.0
```
## Todo
1. [x] ~~Add the exponent (^) operator.~~ 
2. [x] ~~Use BigDecimal instead of double.~~
3. [ ] Make a simple GUI interface launchable when ran with no arguments.
4. [x] ~~Add support for negative numbers.~~