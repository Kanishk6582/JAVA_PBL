import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactionParser {

    public Transaction parseText(String text) {
        double amount = findAmount(text);
        String date = findDate(text);
        String merchant = findMerchant(text);
        String category = guessCategory(merchant, text);

        return new Transaction(amount, merchant, date, category);
    }

    public double findAmount(String text) {
        Pattern moneyPattern = Pattern.compile("(rs\\.?|inr)?\\s*(\\d+(?:\\.\\d+)?)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = moneyPattern.matcher(text);

        while (matcher.find()) {
            String value = matcher.group(2);
            if (value != null && value.length() > 0) {
                return Double.parseDouble(value);
            }
        }

        return 0;
    }

    public String findMerchant(String text) {
        String merchant = findMerchantByKeyword(text);
        if (!merchant.equals("Unknown")) {
            return merchant;
        }

        merchant = findMerchantByCapitalWord(text);
        if (!merchant.equals("Unknown")) {
            return merchant;
        }

        return findMerchantByAnyWord(text);
    }

    private String findMerchantByKeyword(String text) {
        Pattern pattern = Pattern.compile(
                "(?:to|at|in|from|via|towards|for)\\s+([A-Za-z][A-Za-z&.\\- ]*[A-Za-z])",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String value = cleanMerchant(matcher.group(1));
            if (!value.equals("Unknown")) {
                return value;
            }
        }

        return "Unknown";
    }

    private String findMerchantByCapitalWord(String text) {
        Pattern pattern = Pattern.compile("\\b([A-Z][a-zA-Z&.-]+(?:\\s+[A-Z][a-zA-Z&.-]+)*)\\b");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String value = cleanMerchant(matcher.group(1));
            if (!value.equals("Unknown")) {
                return value;
            }
        }

        return "Unknown";
    }

    private String findMerchantByAnyWord(String text) {
        String[] words = text.split("\\s+");

        for (int i = 0; i < words.length; i++) {
            String word = words[i].replaceAll("[^A-Za-z]", "");
            if (word.length() > 2 && !isIgnoredWord(word)) {
                return makeTitleCase(word);
            }
        }

        return "Unknown";
    }

    public String findDate(String text) {
        Pattern pattern = Pattern.compile(
                "(\\d{1,2}/\\d{1,2}/\\d{4}|\\d{4}-\\d{1,2}-\\d{1,2}|\\d{1,2}-\\d{1,2}-\\d{4}|\\d{1,2}\\s+[A-Za-z]{3,9}\\s+\\d{4})",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "Not Found";
    }

    private String cleanMerchant(String merchant) {
        merchant = merchant.replaceAll("(on\\s+\\d{1,2}/\\d{1,2}/\\d{4})", "");
        merchant = merchant.replaceAll("(on\\s+\\d{4}-\\d{1,2}-\\d{1,2})", "");
        merchant = merchant.replaceAll("(on\\s+\\d{1,2}-\\d{1,2}-\\d{4})", "");
        merchant = merchant.replaceAll("(on\\s+\\d{1,2}\\s+[A-Za-z]{3,9}\\s+\\d{4})", "");
        merchant = merchant.replaceAll("(rs\\.?|inr|paid|payment|spent|debited|credited|transaction|for|of)", "");
        merchant = merchant.replaceAll("\\bon\\b", "");
        merchant = merchant.replaceAll("[^A-Za-z&.\\- ]", "");
        merchant = merchant.trim().replaceAll("\\s+", " ");

        if (merchant.length() == 0 || isIgnoredWord(merchant)) {
            return "Unknown";
        }

        return makeTitleCase(merchant);
    }

    private boolean isIgnoredWord(String word) {
        String value = word.toLowerCase();

        return value.equals("paid")
                || value.equals("payment")
                || value.equals("spent")
                || value.equals("debited")
                || value.equals("credited")
                || value.equals("transaction")
                || value.equals("amount")
                || value.equals("money")
                || value.equals("received")
                || value.equals("send")
                || value.equals("sent")
                || value.equals("from")
                || value.equals("into")
                || value.equals("bank")
                || value.equals("account")
                || value.equals("cash")
                || value.equals("upi")
                || value.equals("card")
                || value.equals("wallet")
                || value.equals("online")
                || value.equals("purchase")
                || value.equals("done")
                || value.equals("date")
                || value.equals("time")
                || value.equals("today")
                || value.equals("yesterday")
                || value.equals("tomorrow")
                || value.equals("jan")
                || value.equals("feb")
                || value.equals("mar")
                || value.equals("apr")
                || value.equals("may")
                || value.equals("jun")
                || value.equals("jul")
                || value.equals("aug")
                || value.equals("sep")
                || value.equals("oct")
                || value.equals("nov")
                || value.equals("dec");
    }

    private String makeTitleCase(String text) {
        String[] parts = text.toLowerCase().split("\\s+");
        String result = "";

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].length() == 0) {
                continue;
            }

            String word = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
            if (result.length() == 0) {
                result = word;
            } else {
                result = result + " " + word;
            }
        }

        return result;
    }

    public String guessCategory(String merchant, String text) {
        String data = (merchant + " " + text).toLowerCase();

        if (data.contains("zomato") || data.contains("swiggy") || data.contains("restaurant")
                || data.contains("cafe") || data.contains("food")) {
            return Category.FOOD;
        } else if (data.contains("uber") || data.contains("ola") || data.contains("metro")
                || data.contains("petrol") || data.contains("bus") || data.contains("vehicle service")
                || data.contains("service center") || data.contains("car service") || data.contains("bike service")) {
            return Category.TRANSPORT;
        } else if (data.contains("recharge") || data.contains("electricity") || data.contains("bill")
                || data.contains("water") || data.contains("gas") || data.contains("fees")
                || data.contains("rent") || data.contains("school")) {
            return Category.BILLS;
        } else if (data.contains("netflix") || data.contains("spotify") || data.contains("movie")
                || data.contains("entertainment")) {
            return Category.ENTERTAINMENT;
        } else {
            return Category.OTHER;
        }
    }

    public boolean isSalaryText(String text) {
        String value = text.toLowerCase();
        return value.contains("salary") || value.contains("credited") || value.contains("payroll") || value.contains("income");
    }
}
