package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class StatementPrinter {
    private final Invoice invoice;
    private final Map<String, Play> plays;

    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    public String statement() {
        int totalAmount = 0;
        int volumeCredits = 0;
        StringBuilder result = new StringBuilder("Statement for " + invoice.getCustomer() + System.lineSeparator());
        NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

        for (Performance performance : invoice.getPerformances()) {
            int thisAmount = getAmount(performance);
            Play play = getPlay(performance);

            volumeCredits += Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
            if ("comedy".equals(play.getType())) {
                volumeCredits += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
            }

            result.append(String.format("  %s: %s (%s seats)%n", play.getName(), frmt.format(thisAmount / 100.0), performance.getAudience()));
            totalAmount += thisAmount;
        }

        result.append(String.format("Amount owed is %s%n", frmt.format(totalAmount / 100.0)));
        result.append(String.format("You earned %s credits%n", volumeCredits));
        return result.toString();
    }

    private Play getPlay(Performance performance) {
        return plays.get(performance.getPlayID());
    }

    private int getAmount(Performance performance) {
        Play play = getPlay(performance);
        int result;
        switch (play.getType()) {
            case "tragedy":
                result = Constants.TRAGEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON * (performance.getAudience() - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;
            case "comedy":
                result = Constants.COMEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT + Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON * (performance.getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD);
                }
                result += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.getAudience();
                break;
            default:
                throw new RuntimeException(String.format("unknown type: %s", play.getType()));
        }
        return result;
    }
}

