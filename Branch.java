import java.lang.*;
import java.util.*;
import java.io.*;

/* Branch can make base operations from user. In general it is вriver from user to bank. Just generates a 
transaction or check rights
*/

class Branch {
    private static int card_counter= 0;
	private static int branch_counter = 0;
    private int cards_limit;
	private int branch_id;
    private Bank bank;
    private int card_id;
    private int account_id;
    private Scanner sc = new Scanner(System.in);
    private String manager;
    private String address;
    private String phone;
    private String business_time;

    public Branch(String manager, String addr, String phone, String time) {
    	branch_id = branch_counter++;
        account_id = -1;
        this.manager = manager;
        this.address = addr;
        this.phone = phone;
        this.business_time = time;
        this.cards_limit = 100;
    }

    public Card createCard(String last_name, String first_name, String password, String passport_id, String patronym, int birth, boolean sex) {
        if (cards_limit > 0) {
            Card card = new Card(last_name, first_name, password, branch_id, passport_id, patronym, birth, sex);
            bank.addCard(card);
            cards_limit--;
            return card;
        }
        else {
            return null;
        }
    }

    public Account createAccount(double start_balance, CURRENCY_TYPE currency_type, int card_id, ACCOUNT_TYPE type) {
        if (start_balance < Account.getTypeMinBalance(type)) {
            return null; // if user want to create this type of account, but haven't enough money
        }
        Account account = new Account(start_balance, currency_type, card_id, type, branch_counter++);
        bank.addAccount(account);
        return account;
    }

    public boolean isCardBlocked() {
        return bank.isCardBlocked(card_id);
    }

    public boolean authorization(int card_id, String password) {
        if (bank.isCardAuthorizationCorrect(card_id, password).getStatus() == STATUS.OK) {
            this.card_id = card_id;
            return true;
        }
        return false;
    }

    // If card not blocked and password is correct
    public boolean isAuthorizationCorrect(int card_id, String password){
		if (bank.isCardBlocked(card_id)) {
			return false;
		}
		if (bank.isCardAuthorizationCorrect(card_id, password).getStatus() == STATUS.OK) {
            this.card_id = card_id;
			return true;
		}
		return false;
    }

    public Answer transferMoney(int dest_acc, double amount) {
        Transaction transfer = new Transaction(dest_acc, this.account_id, this.branch_id, 
            amount, TRANSACTION_TYPE.TRANSFER);
        Answer ans = bank.transferMoney(transfer);
        return ans;
    }

    public Answer withdrawMoney(double amount) {
    	Transaction transfer = new Transaction(this.account_id, this.branch_id, amount, TRANSACTION_TYPE.WITHDROW);
        Answer ans = bank.moneyOperation(transfer);
        return ans;
    }

    public Answer putMoney(double amount) {
        Transaction transfer = new Transaction(this.account_id, this.branch_id, amount, TRANSACTION_TYPE.PUT);
        Answer ans = bank.moneyOperation(transfer);
        return ans;
    }

    public double checkBalance() {
    	return bank.checkBalance(account_id);
    }

    // Work with current card account at moment 
    public void selectAccount(int acc) {
    	Vector accounts = bank.getAllCardAccountIds(this.card_id);
        this.account_id = (int)accounts.getElement(acc);
    }

    public boolean isAccountTypeChanged() {
        return bank.isAccountTypeChanged(account_id);
    }

    // If account was changed to Business, but user want to leave Saving type.
    public void rollBackAccountType() {
        bank.rollBackAccountType(account_id);
    }

    // BEGIN ATTR_ACCESSORS

    public String getManager() {
        return manager;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getBusinessTime() {
        return business_time;
    }

    public void setBank(Bank my_bank) {
        bank = my_bank;
    }

    public int getId() {
        return branch_id;
    }

    public int getAccountId() {
        return account_id;
    }


    public Report getUserReport() {
        return bank.getUserReport(card_id);
    }

    public Report __getBranchReport() { // Only for test.
        return bank.getBranchReport(branch_id);
    }

    // END ATTR_ACCESSORS
}
