public class Transaction {
    double amount;
    String merchant;
    String date;
    String category;

    public Transaction(double amount, String merchant, String date, String category) {
        this.amount = amount;
        this.merchant = merchant;
        this.date = date;
        this.category = category;
    }

    public void display() {
        System.out.println("Amount: " + amount);
        System.out.println("Merchant: " + merchant);
        System.out.println("Date: " + date);
        System.out.println("Category: " + category);
        System.out.println("---------------------------");
    }
}
