import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.Math;

@WebServlet("/CalculateServlet")
public class CalculateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String button = request.getParameter("button");
        String displayValue = request.getParameter("display");

        // Get or initialize the current expression
        String expression = (displayValue == null || displayValue.isEmpty()) ? "0" : displayValue;

        // Handle calculation or update expression based on the button pressed
        String result;
        try {
            switch (button) {
                case "=":
                    result = evaluateExpression(expression);
                    break;
                case "C":
                    result = "0";
                    break;
                case "BS":
                    result = expression.length() > 1 ? expression.substring(0, expression.length() - 1) : "0";
                    break;
                case "+/-":
                    result = expression.startsWith("-") ? expression.substring(1) : "-" + expression;
                    break;
                case "sqrt":
                    result = Double.toString(Math.sqrt(parseDouble(expression)));
                    break;
                case "pow":
                    result = expression + "^";
                    break;
                case "log":
                    result = Double.toString(Math.log(parseDouble(expression)));
                    break;
                case "sin":
                    result = Double.toString(Math.sin(Math.toRadians(parseDouble(expression))));
                    break;
                case "cos":
                    result = Double.toString(Math.cos(Math.toRadians(parseDouble(expression))));
                    break;
                default:
                    result = expression.equals("0") ? button : expression + button;
                    break;
            }
        } catch (NumberFormatException e) {
            result = "Error: Invalid number";
        } catch (ArithmeticException e) {
            result = "Error: " + e.getMessage();
        }

        // Set the result to the display
        request.setAttribute("result", result);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    private String evaluateExpression(String expression) {
        try {
            double result = evaluateBasicExpression(expression);
            if (result % 1 == 0) {
                return Integer.toString((int) result);
            } else {
                return Double.toString(result);
            }
        } catch (Exception e) {
            return "Error";
        }
    }

    private double evaluateBasicExpression(String expression) throws Exception {
        double result = 0.0;
        char operator = '+';
        String[] tokens = expression.split("(?=[-+*/])|(?<=[^-+*/][+*/])");
        double currentValue = 0.0;

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                operator = token.charAt(0);
            } else {
                currentValue = Double.parseDouble(token);
                switch (operator) {
                    case '+': result += currentValue; break;
                    case '-': result -= currentValue; break;
                    case '*': result *= currentValue; break;
                    case '/': 
                        if (currentValue != 0) {
                            result /= currentValue; 
                        } else {
                            throw new ArithmeticException("Division by zero");
                        }
                        break;
                }
            }
        }
        return result;
    }

    private double parseDouble(String value) throws NumberFormatException {
        return Double.parseDouble(value);
    }
}
