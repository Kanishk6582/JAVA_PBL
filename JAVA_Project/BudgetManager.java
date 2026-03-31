import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

public class BudgetManager {
    double salary;
    boolean loanActive;
    boolean planCreated;

    double savingsBudget;
    double foodBudget;
    double entertainmentBudget;
    double billsBudget;
    double transportBudget;
    double extraBudget;

    double foodSpent;
    double entertainmentSpent;
    double billsSpent;
    double transportSpent;
    double extraSpent;

    public void setSalaryPlan(double salary, boolean loanActive) {
        setSalaryPlan(salary, loanActive, 15, 15, 5, 30, 15, 20);
    }

    public void setSalaryPlan(double salary, boolean loanActive,
                              double savingsPercent, double foodPercent, double entertainmentPercent,
                              double billsPercent, double transportPercent, double extraPercent) {
        this.salary = salary;
        this.loanActive = loanActive;
        this.planCreated = true;

        savingsBudget = salary * savingsPercent / 100.0;
        foodBudget = salary * foodPercent / 100.0;
        entertainmentBudget = salary * entertainmentPercent / 100.0;
        billsBudget = salary * billsPercent / 100.0;
        transportBudget = salary * transportPercent / 100.0;
        extraBudget = salary * extraPercent / 100.0;

        resetSpentData();
    }

    public void resetSpentData() {
        foodSpent = 0;
        entertainmentSpent = 0;
        billsSpent = 0;
        transportSpent = 0;
        extraSpent = 0;
    }

    public void recalculate(ArrayList<Transaction> transactionList) {
        resetSpentData();

        for (int i = 0; i < transactionList.size(); i++) {
            Transaction t = transactionList.get(i);
            recordExpense(t.category, t.amount);
        }
    }

    public void recordExpense(String category, double amount) {
        String finalCategory = normalizeCategory(category);

        if (finalCategory.equals(Category.FOOD)) {
            foodSpent = foodSpent + amount;
        } else if (finalCategory.equals(Category.ENTERTAINMENT)) {
            entertainmentSpent = entertainmentSpent + amount;
        } else if (finalCategory.equals(Category.BILLS)) {
            billsSpent = billsSpent + amount;
        } else if (finalCategory.equals(Category.TRANSPORT)) {
            transportSpent = transportSpent + amount;
        } else {
            extraSpent = extraSpent + amount;
        }
    }

    public String normalizeCategory(String category) {
        if (category == null) {
            return Category.OTHER;
        }

        if (category.equalsIgnoreCase(Category.FOOD)) {
            return Category.FOOD;
        } else if (category.equalsIgnoreCase(Category.ENTERTAINMENT)) {
            return Category.ENTERTAINMENT;
        } else if (category.equalsIgnoreCase(Category.BILLS)) {
            return Category.BILLS;
        } else if (category.equalsIgnoreCase(Category.TRANSPORT)) {
            return Category.TRANSPORT;
        } else {
            return Category.OTHER;
        }
    }

    public void showSalaryDistribution() {
        if (!planCreated) {
            System.out.println("Salary plan is not set yet.");
            return;
        }

        System.out.println("===== Monthly Salary Distribution =====");
        System.out.println("Total Salary: " + salary);
        System.out.println("Savings: " + savingsBudget);
        System.out.println("Food: " + foodBudget);
        System.out.println("Entertainment: " + entertainmentBudget);
        System.out.println("Bills: " + billsBudget);
        System.out.println("Transport: " + transportBudget);

        if (loanActive) {
            System.out.println("Loan: " + extraBudget);
        } else {
            System.out.println("Other: " + extraBudget);
        }

        System.out.println("---------------------------------------");
    }

    public boolean isValidPercentTotal(double savingsPercent, double foodPercent, double entertainmentPercent,
                                       double billsPercent, double transportPercent, double extraPercent) {
        double total = savingsPercent + foodPercent + entertainmentPercent
                + billsPercent + transportPercent + extraPercent;
        return total == 100.0;
    }

    public void showUsageForCategory(String category) {
        showUsageForCategory(category, "Not Found");
    }

    public void showUsageForCategory(String category, String expenseDate) {
        if (!planCreated) {
            System.out.println("Salary plan is not set yet.");
            return;
        }

        String finalCategory = normalizeCategory(category);
        double budget = getBudget(finalCategory);
        double spent = getSpent(finalCategory);

        System.out.println("Used from " + finalCategory + ": " + spent + " out of " + budget);
        System.out.println("Remaining in " + finalCategory + ": " + (budget - spent));

        showAlert(finalCategory, spent, budget, expenseDate);
    }

    public void showCompleteUsage() {
        if (!planCreated) {
            System.out.println("Salary plan is not set yet.");
            return;
        }

        System.out.println("===== Budget Usage =====");
        printUsageLine(Category.FOOD, foodSpent, foodBudget);
        printUsageLine(Category.ENTERTAINMENT, entertainmentSpent, entertainmentBudget);
        printUsageLine(Category.BILLS, billsSpent, billsBudget);
        printUsageLine(Category.TRANSPORT, transportSpent, transportBudget);

        if (loanActive) {
            printUsageLine(Category.LOAN, extraSpent, extraBudget);
        } else {
            printUsageLine(Category.OTHER, extraSpent, extraBudget);
        }

        System.out.println("---------------------------------------");
    }

    private void printUsageLine(String category, double spent, double budget) {
        System.out.println(category + " -> Used: " + spent + " / Budget: " + budget + " / Remaining: " + (budget - spent));
    }

    private double getBudget(String category) {
        if (category.equals(Category.FOOD)) {
            return foodBudget;
        } else if (category.equals(Category.ENTERTAINMENT)) {
            return entertainmentBudget;
        } else if (category.equals(Category.BILLS)) {
            return billsBudget;
        } else if (category.equals(Category.TRANSPORT)) {
            return transportBudget;
        } else {
            return extraBudget;
        }
    }

    private double getSpent(String category) {
        if (category.equals(Category.FOOD)) {
            return foodSpent;
        } else if (category.equals(Category.ENTERTAINMENT)) {
            return entertainmentSpent;
        } else if (category.equals(Category.BILLS)) {
            return billsSpent;
        } else if (category.equals(Category.TRANSPORT)) {
            return transportSpent;
        } else {
            return extraSpent;
        }
    }

    private void showAlert(String category, double spent, double budget, String expenseDate) {
        if (budget <= 0) {
            System.out.println("No budget available for this category.");
            return;
        }

        double percentUsed = spent * 100.0 / budget;
        LocalDate alertDate = getAlertDate(expenseDate);
        int currentDay = alertDate.getDayOfMonth();
        int totalDays = YearMonth.from(alertDate).lengthOfMonth();
        boolean earlyMonthOrMidMonth = currentDay <= (totalDays - 3);

        if (spent > budget) {
            System.out.println("Alert: You have crossed the budget of " + category + ".");
        } else if (percentUsed >= 80 && earlyMonthOrMidMonth) {
            System.out.println("Alert: Month is not over yet and " + category + " budget is almost finished.");
        } else if (percentUsed >= 60 && earlyMonthOrMidMonth) {
            System.out.println("Warning: You have already used a large part of " + category + " budget.");
        }
    }

    private LocalDate getAlertDate(String expenseDate) {
        if (expenseDate == null || expenseDate.equals("Not Found")) {
            return LocalDate.now();
        }

        String[] dashParts = expenseDate.split("-");
        if (dashParts.length == 3) {
            try {
                if (dashParts[0].length() == 4) {
                    int year = Integer.parseInt(dashParts[0]);
                    int month = Integer.parseInt(dashParts[1]);
                    int day = Integer.parseInt(dashParts[2]);
                    return LocalDate.of(year, month, day);
                } else {
                    int day = Integer.parseInt(dashParts[0]);
                    int month = Integer.parseInt(dashParts[1]);
                    int year = Integer.parseInt(dashParts[2]);
                    return LocalDate.of(year, month, day);
                }
            } catch (Exception e) {
                return LocalDate.now();
            }
        }

        String[] slashParts = expenseDate.split("/");
        if (slashParts.length == 3) {
            try {
                int day = Integer.parseInt(slashParts[0]);
                int month = Integer.parseInt(slashParts[1]);
                int year = Integer.parseInt(slashParts[2]);
                return LocalDate.of(year, month, day);
            } catch (Exception e) {
                return LocalDate.now();
            }
        }

        return LocalDate.now();
    }

    public String getExtraCategoryName() {
        if (loanActive) {
            return Category.LOAN;
        }
        return Category.OTHER;
    }
}
