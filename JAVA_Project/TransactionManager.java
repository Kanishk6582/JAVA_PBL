import java.util.ArrayList;

public class TransactionManager {
    ArrayList<Transaction> transactionList = new ArrayList<Transaction>();

    public void addTransaction(Transaction t) {
        transactionList.add(t);
        System.out.println("Transaction added successfully.");
    }

    public void showAllTransactions() {
        if (transactionList.size() == 0) {
            System.out.println("No transactions found.");
            return;
        }

        for (int i = 0; i < transactionList.size(); i++) {
            transactionList.get(i).display();
        }
    }

    public void editTransaction(String oldMerchant, double newAmount, String newMerchant, String newDate) {
        boolean found = false;

        for (int i = 0; i < transactionList.size(); i++) {
            if (transactionList.get(i).merchant.equalsIgnoreCase(oldMerchant)) {
                transactionList.get(i).amount = newAmount;
                transactionList.get(i).merchant = newMerchant;
                transactionList.get(i).date = newDate;
                found = true;
                System.out.println("Transaction updated successfully.");
                break;
            }
        }

        if (!found) {
            System.out.println("Merchant not found.");
        }
    }

    public void changeCategory(String merchant, String newCategory) {
        boolean found = false;

        for (int i = 0; i < transactionList.size(); i++) {
            if (transactionList.get(i).merchant.equalsIgnoreCase(merchant)) {
                transactionList.get(i).category = newCategory;
                found = true;
                System.out.println("Category updated successfully.");
                break;
            }
        }

        if (!found) {
            System.out.println("Merchant not found.");
        }
    }
}
