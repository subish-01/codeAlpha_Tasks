import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Stock {
    String symbol;
    double price;

    Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }
}

class Portfolio {
    HashMap<String, Integer> holdings = new HashMap<>();
    double balance;

    Portfolio(double balance) {
        this.balance = balance;
    }

    void buyStock(Stock stock, int quantity) {
        double totalCost = stock.price * quantity;
        if (totalCost <= balance) {
            balance -= totalCost;
            holdings.put(stock.symbol, holdings.getOrDefault(stock.symbol, 0) + quantity);
            System.out.println("Stock purchased successfully.");
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    void sellStock(Stock stock, int quantity) {
        if (holdings.containsKey(stock.symbol) && holdings.get(stock.symbol) >= quantity) {
            holdings.put(stock.symbol, holdings.get(stock.symbol) - quantity);
            balance += stock.price * quantity;
            System.out.println("Stock sold successfully.");
        } else {
            System.out.println("Not enough shares to sell.");
        }
    }

    void viewPortfolio(ArrayList<Stock> market) {
        System.out.println("\n----- Portfolio -----");
        double totalValue = balance;

        for (String symbol : holdings.keySet()) {
            int qty = holdings.get(symbol);
            double price = 0;
            for (Stock s : market) {
                if (s.symbol.equals(symbol)) {
                    price = s.price;
                    break;
                }
            }
            double value = qty * price;
            totalValue += value;
            System.out.println(symbol + " | Quantity: " + qty + " | Value: " + value);
        }

        System.out.println("Available Balance: " + balance);
        System.out.println("Total Portfolio Value: " + totalValue);
    }
}

public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ArrayList<Stock> market = new ArrayList<>();
        market.add(new Stock("TCS", 3500));
        market.add(new Stock("INFY", 1500));
        market.add(new Stock("RELIANCE", 2500));

        Portfolio user = new Portfolio(100000);

        int choice;

        do {
            System.out.println("\n===== Stock Trading Platform =====");
            System.out.println("1. View Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\n--- Market Data ---");
                    for (Stock s : market) {
                        System.out.println(s.symbol + " : ₹" + s.price);
                    }
                    break;

                case 2:
                    System.out.print("Enter stock symbol: ");
                    String buySymbol = sc.next();
                    System.out.print("Enter quantity: ");
                    int buyQty = sc.nextInt();

                    for (Stock s : market) {
                        if (s.symbol.equalsIgnoreCase(buySymbol)) {
                            user.buyStock(s, buyQty);
                        }
                    }
                    break;

                case 3:
                    System.out.print("Enter stock symbol: ");
                    String sellSymbol = sc.next();
                    System.out.print("Enter quantity: ");
                    int sellQty = sc.nextInt();

                    for (Stock s : market) {
                        if (s.symbol.equalsIgnoreCase(sellSymbol)) {
                            user.sellStock(s, sellQty);
                        }
                    }
                    break;

                case 4:
                    user.viewPortfolio(market);
                    break;

                case 5:
                    System.out.println("Exiting platform...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);
    }
}