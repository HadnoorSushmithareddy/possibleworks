import org.json.JSONObject;
import java.math.BigInteger;
import java.util.*;

public class ShamirSecretSharing {

    // Method to decode a value from a given base to decimal (base 10)
    public static BigInteger decodeValue(String base, String value) {
        int radix = Integer.parseInt(base);
        return new BigInteger(value, radix);  // Decode using the given base
    }

    // Method to compute Lagrange basis polynomial at x=0 for given x-values and y-values
    public static BigInteger lagrangeAtZero(List<BigInteger> xValues, List<BigInteger> yValues, int i) {
        BigInteger numerator = BigInteger.ONE;
        BigInteger denominator = BigInteger.ONE;

        for (int j = 0; j < xValues.size(); j++) {
            if (i != j) {
                numerator = numerator.multiply(BigInteger.ZERO.subtract(xValues.get(j)));
                denominator = denominator.multiply(xValues.get(i).subtract(xValues.get(j)));
            }
        }

        // Return the corresponding Lagrange polynomial evaluation at x = 0
        return yValues.get(i).multiply(numerator).divide(denominator);
    }

    // Method to calculate the constant term (c) of the polynomial
    public static BigInteger findConstantTerm(Map<String, JSONObject> json) {
        List<BigInteger> xValues = new ArrayList<>();
        List<BigInteger> yValues = new ArrayList<>();

        // Parse the input JSON object and collect the x-values and decoded y-values
        for (String key : json.keySet()) {
            if (key.equals("keys")) continue;
            JSONObject root = json.get(key);
            String base = root.getString("base");
            String value = root.getString("value");

            // Decode the y-value from the given base
            BigInteger y = decodeValue(base, value);
            int x = Integer.parseInt(key);  // x is the key of the JSON object

            xValues.add(BigInteger.valueOf(x));
            yValues.add(y);
        }

        // Now use Lagrange interpolation to compute the constant term at x = 0
        BigInteger constantTerm = BigInteger.ZERO;
        for (int i = 0; i < xValues.size(); i++) {
            constantTerm = constantTerm.add(lagrangeAtZero(xValues, yValues, i));
        }

        return constantTerm;
    }

    public static void main(String[] args) {
        // Example of how to read input JSON and parse it (as per the problem statement)
        String jsonInput = "{\n" +
                "\"keys\": {\n" +
                "\"n\": 10,\n" +
                "\"k\": 7\n" +
                "},\n" +
                "\"1\": {\n" +
                "\"base\": \"7\",\n" +
                "\"value\": \"420020006424065463\"\n" +
                "},\n" +
                "\"2\": {\n" +
                "\"base\": \"7\",\n" +
                "\"value\": \"10511630252064643035\"\n" +
                "},\n" +
                "\"3\": {\n" +
                "\"base\": \"2\",\n" +
                "\"value\": \"101010101001100101011100000001000111010010111101100100010\"\n" +
                "},\n" +
                "\"4\": {\n" +
                "\"base\": \"8\",\n" +
                "\"value\": \"31261003022226126015\"\n" +
                "},\n" +
                "\"5\": {\n" +
                "\"base\": \"7\",\n" +
                "\"value\": \"2564201006101516132035\"\n" +
                "},\n" +
                "\"6\": {\n" +
                "\"base\": \"15\",\n" +
                "\"value\": \"a3c97ed550c69484\"\n" +
                "},\n" +
                "\"7\": {\n" +
                "\"base\": \"13\",\n" +
                "\"value\": \"134b08c8739552a734\"\n" +
                "},\n" +
                "\"8\": {\n" +
                "\"base\": \"10\",\n" +
                "\"value\": \"23600283241050447333\"\n" +
                "},\n" +
                "\"9\": {\n" +
                "\"base\": \"9\",\n" +
                "\"value\": \"375870320616068547135\"\n" +
                "},\n" +
                "\"10\": {\n" +
                "\"base\": \"6\",\n" +
                "\"value\": \"30140555423010311322515333\"\n" +
                "}\n" +
                "}";

        // Parse the JSON input
        JSONObject jsonObject = new JSONObject(jsonInput);

        // Find the constant term (secret)
        BigInteger secret = findConstantTerm(jsonObject.toMap());

        // Output the result
        System.out.println("The secret (constant term) is: " + secret);
    }
}
