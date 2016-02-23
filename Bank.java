import java.lang.*;
import java.util.*;

/* Central class where all logic is encapsulated. Store all accounts, cards, transaction data and allow to
// use it to branch only after request. Send Answers to requests with Status to user or branch. Generate all reports.
*/

class Bank { // can be a singletone
	private static int bank_counter = 0;
    private static int card_counter= 0;
    private static int accounts_counter = 0;
    private final int branch_limit;
	private int bank_id;
	private Vector branches;
    private Vector cards;
    private Vector accounts;
    private Vector transactions;

    public Bank() {
    	bank_id = bank_counter++;
    	branches = new Vector();
    	cards = new Vector();
    	accounts = new Vector();
    	transactions = new Vector();
        branch_limit = 5;
    }

    public Branch createBranch(String manager, String addr, String phone, String time) {
        if (branches.size() < branch_limit) {
            Branch branch = new Branch(manager, addr, phone, time);
            branch.setBank(this);
            branches.pushBack(branch);
            return branch;
        }
        return null;
    }

    public int __getAccountCount() { // Only for test. Should be private
        return accounts.size();
    }

    public void rollBackAccountType(int account_id) { // If user prefere Saving account
        Account acc = findAccountById(account_id);
        acc.rollBackType();
    }

    public Report getUserReport(int card_id) {
        Vector card_accounts = getAllCardAccountIds(card_id);
        Vector card_transactions = selectUserTransactions(card_accounts);
        Report card_report = new Report(findCardById(card_id), card_transactions);
        return card_report;
    }

    public Report getBranchReport(int branch_id) {
        Branch branch = findBranchById(branch_id);
        Vector branch_transactions = selectBranchTransactions(branch_id);
        Report branch_report = new Report(branch, branch_transactions);
        return branch_report;
    }

    public Report getBankReport() {
        Report report = new Report(this, transactions);
        return report;
    }

    public int getId() {
    	return this.bank_id;
    }

    public boolean isAccountTypeChanged(int account_id) {
        Account acc = findAccountById(account_id);
        return acc.isTypeChanged();
    }

    // Charge daily interest depending on the account.
    public void chargeDailyInteres() {
        for (int i = 0; i < accounts.size(); i++) {
            Card card = findCardById(((Account)accounts.getElement(i)).getCardId());
            if (card.isBlocked()) {
                double interest_amount = 0;
                double current_balance = ((Account)accounts.getElement(i)).getBalance();;
                double interest = ((Account)accounts.getElement(i)).getDailyInterest();
                Transaction transfer;
                if (((Account)accounts.getElement(i)).getType() == ACCOUNT_TYPE.SAVING || ((Account)accounts.getElement(i)).getType() == ACCOUNT_TYPE.BUSINESS) {
                    interest_amount = current_balance + current_balance / (30 * 100); // for SAVING and BUSINESS
                    transfer = new Transaction(((Account)accounts.getElement(i)).getId(), card.getBranchId(), interest_amount, TRANSACTION_TYPE.INTEREST, ((Account)accounts.getElement(i)).getCurrencyType());
                }
                else {
                    interest_amount = current_balance - current_balance / (30); // for CHEQUING
                    transfer = new Transaction(((Account)accounts.getElement(i)).getId(), card.getBranchId(), interest_amount, TRANSACTION_TYPE.INTEREST, ((Account)accounts.getElement(i)).getCurrencyType());
                }
                this.transactions.pushBack(transfer);
                if (moneyOperation(transfer).getStatus() == STATUS.OK) {
                    this.transactions.pushBack(transfer); // push it back to vector, if money was transfere
                }
                card.blockCard(); // if you can't charge daily interest - card will be blocked
            }
     }
    }

    public boolean isCardBlocked(int card_id) {
    	Card card = this.findCardById(card_id);
    	if (card == null) {
    		throw new Error("No such card in system");
    	}
    	return card.isBlocked();
    }

    public void addCard(Card new_card) {
        new_card.setId(card_counter++);
    	this.cards.pushBack(new_card);
    }

    public void addAccount(Account new_account) {
        new_account.setId(accounts_counter++);
    	this.accounts.pushBack(new_account);
    }

    /** Check for correct authorization */
    public Answer isCardAuthorizationCorrect(int card_id, String password) {
    	Card card = this.findCardById(card_id);
        if (card != null) {
            if (card.comparePassword(password)) {
                return new Answer(STATUS.OK);
            }
        }
    	return new Answer(STATUS.WA);

    }

    // To commit a transaction for PUT and WITHDROW transaction types
    public Answer moneyOperation(Transaction tt) {
		Account from = this.findAccountById(tt.getAccountId());
        if (!from.canMakeTransaction()) { // if account have transaction limit error
            return new Answer(STATUS.WA);
        }
        double amount = tt.getAmount();
        Answer ans = new Answer();
        if (from.canChangeBalance(amount)) { // if account have enough money for this operation
            from.changeBalance(amount);
            ans.setAnswerStatus(STATUS.OK);
            tt.setCurrencyType(from.getCurrencyType());
            transactions.pushBack(tt);
            if (from.canBeBusiness()) {
                from.changeAccountTypeToBusines();
            }
        }
        else 
            ans.setAnswerStatus(STATUS.WA);
        return ans;
    }

    // Transfere money to another account
    public Answer transferMoney(Transaction tt) {
    	Account from = this.findAccountById(tt.getAccountId());
    	Account to = this.findAccountById(tt.getDestinationAccountId());
        tt.setDestinationBranchId(to.getBranchId());
        if (!from.canMakeTransaction() || !to.canMakeTransaction()) { // if one of then have transaction limit error
            return new Answer(STATUS.WA);
        }
    	double amount = tt.getAmount();
    	Answer ans = new Answer();
    	if ( from.canChangeBalance(amount) && to.canChangeBalance(amount) && isCommonCurrencyType(from, to)  
    			&& (from.getId() != to.getId())) { // if account have enough money for this operation and have common currency types
    		from.changeBalance(-amount);
            tt.setCurrencyType(from.getCurrencyType());
            tt.setDestinationBranchId(to.getBranchId());
    		to.changeBalance(amount);
    		transactions.pushBack(tt);
    		ans.setAnswerStatus(STATUS.OK);
    	}
    	else 
    		ans.setAnswerStatus(STATUS.WA);
    	return ans;
    }

    public double checkBalance(int account_id) {
    	Account acc = findAccountById(account_id);
        if (acc != null) {
    	   return acc.getBalance();
        }
        else
            return -1;
    }

    public Vector getAllCardAccountIds(int user_id) {
    	Vector user_accounts = new Vector();
    	for (int i = 0; i < accounts.size(); i++) {
    		if ( ((Account)(accounts.getElement(i))).getCardId() == user_id )
    			user_accounts.pushBack( ((Account)(accounts.getElement(i))).getId() );
    	}
    	return user_accounts;
    }

    public Vector getAllCardAccounts(int user_id) { // return Vector of cards account ids for choosing.
        Vector user_accounts = new Vector();
        for (int i = 0; i < accounts.size(); i++) {
            if ( ((Account)(accounts.getElement(i))).getCardId() == user_id )
                user_accounts.pushBack( ((Account)(accounts.getElement(i))) );
        }
        return user_accounts;
    }

    // BEGIN PRIVATE 
    
    private Card findCardById(int card_id) {
    	for (int i = 0; i < cards.size(); i++) {
    		if (((Card)(this.cards.getElement(i))).getId() == card_id) {
    			return (Card)this.cards.getElement(i);
    		}
    	}
    	return null;
    }

    private Account findAccountById(int acc_id) {
    	for (int i = 0; i < accounts.size(); i++) {
    		if (((Account)(this.accounts.getElement(i))).getId() == acc_id) {
    			return (Account)this.accounts.getElement(i);
    		}
    	}
    	return null;
    }

    private Branch findBranchById(int branch_id) {
       for (int i = 0; i < accounts.size(); i++) {
            if (((Branch)(this.branches.getElement(i))).getId() == branch_id) {
                return (Branch)this.branches.getElement(i);
            }
        }
        return null; 
    }

    private boolean isCommonCurrencyType(Account first_acc, Account second_acc) {
    	return (first_acc.getCurrencyType() == second_acc.getCurrencyType());
    }

    private Vector selectBranchTransactions(int branch_id) {
        Vector branch_transactions = new Vector();
        for (int i = 0; i < transactions.size(); i++){
            Transaction trans = ((Transaction)(transactions.getElement(i)));
            if (trans.getBranchId() == branch_id || trans.getDestinationBranchId() == branch_id) {
                branch_transactions.pushBack(trans);
            }
        }
        return branch_transactions;
    }

    private Vector selectUserTransactions(Vector accounts) {
        Vector card_transactions = new Vector();
        for (int i = 0; i < transactions.size(); i++){
            for (int j = 0; j < accounts.size(); j++) {
                Transaction trans = ((Transaction)(transactions.getElement(i)));
                int acc = (int)accounts.getElement(j);
                if ( ((trans.getAccountId()) == (acc)) || ((trans.getDestinationAccountId()) == (acc)) ){
                    card_transactions.pushBack(trans);
                }
            }
        }
        return card_transactions;
    }
}