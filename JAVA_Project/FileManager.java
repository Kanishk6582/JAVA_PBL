import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

    public void saveTransactions(ArrayList<Transaction> list, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));

            for (int i = 0; i < list.size(); i++) {
                Transaction t = list.get(i);
                writer.write(t.amount + "," + t.merchant + "," + t.date + "," + t.category);
                writer.newLine();
            }

            writer.close();
            System.out.println("Data saved in file.");
        } catch (IOException e) {
            System.out.println("Error while saving file.");
        }
    }

    public void saveBudgetReport(BudgetManager budgetManager, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));

            if (!budgetManager.planCreated) {
                writer.write("Salary plan is not set yet.");
            } else {
                writer.write("---------------------------------------");
                writer.newLine();
                writer.write("Total Salary: " + budgetManager.salary);
                writer.newLine();
                writer.write("Savings: " + budgetManager.savingsBudget);
                writer.newLine();
                writer.write("Food Budget: " + budgetManager.foodBudget + ", Used: " + budgetManager.foodSpent);
                writer.newLine();
                writer.write("Entertainment Budget: " + budgetManager.entertainmentBudget + ", Used: " + budgetManager.entertainmentSpent);
                writer.newLine();
                writer.write("Bills Budget: " + budgetManager.billsBudget + ", Used: " + budgetManager.billsSpent);
                writer.newLine();
                writer.write("Transport Budget: " + budgetManager.transportBudget + ", Used: " + budgetManager.transportSpent);
                writer.newLine();
                writer.write(budgetManager.getExtraCategoryName() + " Budget: " + budgetManager.extraBudget + ", Used: " + budgetManager.extraSpent);
                writer.newLine();
            }

            writer.close();
            System.out.println("Budget report saved in file.");
        } catch (IOException e) {
            System.out.println("Error while saving budget report.");
        }
    }
}
