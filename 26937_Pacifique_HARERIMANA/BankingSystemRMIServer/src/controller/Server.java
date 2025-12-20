package controller;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import service.implementation.AccountServiceImpl;
import service.implementation.CardServiceImpl;
import service.implementation.CustomerServiceImpl;
import service.implementation.LoanRepaymentServiceImpl;
import service.implementation.LoanServiceImpl;
import service.implementation.RiskProfileServiceImpl;
import service.implementation.TransactionServiceImpl;

public class Server {
    public static void main(String[] args) {
        try{
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            Registry registry = LocateRegistry.createRegistry(3000);
            registry.rebind("account", new AccountServiceImpl());
            registry.rebind("cust", new CustomerServiceImpl());
            registry.rebind("card", new CardServiceImpl());
            registry.rebind("transaction", new TransactionServiceImpl());
            registry.rebind("loan", new LoanServiceImpl());
            registry.rebind("loanRepayment", new LoanRepaymentServiceImpl());
            registry.rebind("riskProfile", new RiskProfileServiceImpl());
            System.out.println("Server is running on port 3000");
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
