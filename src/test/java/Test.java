import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.tokenizer.ParseException;

public class Test {
    public static void main(String[] args) throws ParseException {
        for (int i = 0; i < 100; i++) {
            Expression expression = Parser.parse("5000*(1+0.4)^" + i + "");

            System.out.println("第" + i + "次: " + (int) expression.evaluate());
        }
    }
}
