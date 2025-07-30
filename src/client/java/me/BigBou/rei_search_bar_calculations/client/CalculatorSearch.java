package me.BigBou.rei_search_bar_calculations.client;

import me.BigBou.rei_search_bar_calculations.Calculator;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CalculatorSearch implements REIClientPlugin {

    private static final Logger log = LoggerFactory.getLogger(CalculatorSearch.class);
    static String lastInput = "";
    static String lastResult = null;

    public static String format(String text) {
        String calculate = calculateInSearchBar(text);
        return text + (calculate != null ? " §e= §a" + calculate : "");
    }

    private static String calculateInSearchBar(String input) {
        // Clean invisible formatting characters from the input
        input = sanitize(input);

        if (!lastInput.equals(input)) {
            lastInput = input;
            try {
                BigDecimal calculate = Calculator.calculate(input);
                String formatted = new DecimalFormat("#,##0.##").format(calculate);
                lastResult = sanitize(formatted);
            } catch (Calculator.CalculatorException ignored) {
                lastResult = null;
            }
        }

        return lastResult;
    }

    // Remove all invisible formatting/control characters (like LRM, RLM, etc.)
    private static String sanitize(String str) {
        return str == null ? null : str.replaceAll("\\p{Cf}", "");
    }
}
