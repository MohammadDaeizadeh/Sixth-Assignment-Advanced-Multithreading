package Banking;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private final int id;
    private int balance;
    private final Lock lock = new ReentrantLock();

    public BankAccount(int id, int initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public int getId(){
        return  id;
    }
    public int getBalance() {
        // TODO: Consider locking (if needed)
        return balance;
    }

    public Lock getLock() {
        return lock;
    }

    public void deposit(int amount) {
        // TODO: Safely add to balance.
        getLock().lock();
        try {
            balance += amount;
            System.out.printf("deposit: %d $", amount);
            System.out.println();
        } finally {
            getLock().unlock();
        }
    }

    public void withdraw(int amount) {
        // TODO: Safely withdraw from balance.
        getLock().lock();
        try {
            balance -= amount;
            System.out.printf("withdraw: %d $", amount);
            System.out.println();
        } finally {
            getLock().unlock();
        }
    }

    public void transfer(BankAccount target, int amount) {
        // TODO: Safely make the changes
        // HINT: Both accounts need to be locked, while the changes are being made
        // HINT: Be cautious of potential deadlocks.
        BankAccount first = this.id < target.id ? this : target;
        BankAccount second = this.id < target.id ? target : this;

        first.getLock().lock();
        second.getLock().lock();
        try {
            this.withdraw(amount);
            target.deposit(amount);
            System.out.printf("Transfer from %d to %d : %d $", this.id, target.id, amount);
            System.out.println();
        } finally {
            second.getLock().unlock();
            first.getLock().unlock();
        }
    }
}
