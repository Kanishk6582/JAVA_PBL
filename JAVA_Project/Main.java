import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TransactionManager manager = new TransactionManager();
        TransactionParser parser = new TransactionParser();
        BudgetManager budgetManager = new BudgetManager();
        FileManager fileManager = new FileManager();
        int choice;

        do {
            System.out.println();
            System.out.println("===== Expense Tracker =====");
            System.out.println();
            System.out.println("1. Set salary and create monthly plan");
            System.out.println("2. Add expense from text");
            System.out.println("3. Edit transaction");
            System.out.println("4. Change category");
            System.out.println("5. Show all transactions");
            System.out.println("6. Show salary distribution");
            System.out.println("7. Show budget usage");
            System.out.println("8. Save data in file");
            System.out.println("0. Exit");
            System.out.println();
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            if (choice == 1) {
                System.out.println("Enter salary text/message:");
                String salaryText = sc.nextLine();
                double salary = parser.findAmount(salaryText);

                if (salary <= 0) {
                    System.out.println("Salary amount not found.");
                } else {
                    System.out.print("Do you have loan? (yes/no): ");
                    String loanInput = sc.nextLine();
                    boolean loanActive = loanInput.equalsIgnoreCase("yes");

                    System.out.print("Do you want automatic or manual distribution? ");
                    String planType = sc.nextLine();

                    if (planType.equalsIgnoreCase("manual")) {
                        System.out.print("Enter Savings percentage: ");
                        double savingsPercent = Double.parseDouble(sc.nextLine());
                        System.out.print("Enter Food percentage: ");
                        double foodPercent = Double.parseDouble(sc.nextLine());
                        System.out.print("Enter Entertainment percentage: ");
                        double entertainmentPercent = Double.parseDouble(sc.nextLine());
                        System.out.print("Enter Bills percentage: ");
                        double billsPercent = Double.parseDouble(sc.nextLine());
                        System.out.print("Enter Transport percentage: ");
                        double transportPercent = Double.parseDouble(sc.nextLine());

                        if (loanActive) {
                            System.out.print("Enter Loan percentage: ");
                        } else {
                            System.out.print("Enter Other percentage: ");
                        }
                        double extraPercent = Double.parseDouble(sc.nextLine());

                        if (budgetManager.isValidPercentTotal(savingsPercent, foodPercent, entertainmentPercent,
                                billsPercent, transportPercent, extraPercent)) {
                            budgetManager.setSalaryPlan(salary, loanActive, savingsPercent, foodPercent,
                                    entertainmentPercent, billsPercent, transportPercent, extraPercent);
                            System.out.println();
                            System.out.println("Salary plan created successfully.");
                            System.out.println();
                            budgetManager.showSalaryDistribution();
                            System.out.println();
                        } else {
                            System.out.println("Total percentage must be 100.");
                            System.out.println();
                        }
                    } else {
                        budgetManager.setSalaryPlan(salary, loanActive);
                        System.out.println();
                        System.out.println("Salary plan created successfully.");
                        System.out.println();
                        budgetManager.showSalaryDistribution();
                        System.out.println();
                    }
                }
            } else if (choice == 2) {
                System.out.println("Enter transaction text:");
                String text = sc.nextLine();
                if (parser.isSalaryText(text)) {
                    System.out.println("This looks like a salary message. Please use option 1.");
                } else {
                    Transaction t = parser.parseText(text);
                    manager.addTransaction(t);
                    budgetManager.recordExpense(t.category, t.amount);
                    System.out.println();
                    budgetManager.showUsageForCategory(t.category, t.date);
                    System.out.println();
                }
            } else if (choice == 3) {
                System.out.print("Enter old merchant name: ");
                String oldMerchant = sc.nextLine();
                System.out.print("Enter new amount: ");
                double amount = Double.parseDouble(sc.nextLine());
                System.out.print("Enter new merchant: ");
                String merchant = sc.nextLine();
                System.out.print("Enter new date: ");
                String date = sc.nextLine();
                manager.editTransaction(oldMerchant, amount, merchant, date);
                budgetManager.recalculate(manager.transactionList);
                System.out.println();
            } else if (choice == 4) {
                System.out.print("Enter merchant name: ");
                String merchant = sc.nextLine();
                System.out.print("Enter new category: ");
                String category = sc.nextLine();
                manager.changeCategory(merchant, category);
                budgetManager.recalculate(manager.transactionList);
                System.out.println();
            } else if (choice == 5) {
                manager.showAllTransactions();
                System.out.println();
            } else if (choice == 6) {
                budgetManager.showSalaryDistribution();
                System.out.println();
            } else if (choice == 7) {
                budgetManager.showCompleteUsage();
                System.out.println();
            } else if (choice == 8) {
                fileManager.saveTransactions(manager.transactionList, "transactions.txt");
                fileManager.saveBudgetReport(budgetManager, "budget_report.txt");
                System.out.println();
            } else if (choice == 0) {
                System.out.println("Program ended.");
            } else {
                System.out.println("Invalid choice.");
            }

        } while (choice != 0);

        sc.close();
    }
}
