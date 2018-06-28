import framework.WebRoute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MyRoutes {
    private static Random number = new Random();

    @WebRoute("/goat")
    public static String goat() {

        if (number.nextInt(100) <= 45) {
            return "mekk";
        } else if (number.nextInt(100) < 75 && number.nextInt(100) > 45) {
            return "graaa";
        } else {
            return "what is even happening?";
        }
    }

    @WebRoute("/test")
    public static String test() {
        List<String> strings = new ArrayList<>(
                Arrays.asList("string1","string2","string100"));

        return "this is your string: " + strings.get(number.nextInt(3));
    }
}

