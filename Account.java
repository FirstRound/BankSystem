enum CURRENCY_TYPE { RUB, USD, EUR }
enum ACCOUNT_TYPE { SAVING, CHEQUING, BUSINESS  }

// Account class. Better to use inheritance.
class Account {

	private int account_id;
	private ACCOUNT_TYPE type;
	private CURRENCY_TYPE currency_type;
    private double balance;
    private double interest;
    private double start_balance;
    private int bank_id;
    private int branch_id;
    private int card_id;
    private boolean is_business_disabled;
    private int transaction_limit;
    private boolean is_type_changed;

    private static final double SAVING_START_BALANCE = 50000;
    private static final double CHEQUING_START_BALANCE = 1000;
    private static final double BUSINESS_START_BALANCE = 5000000;

    private static final double SAVING_INTEREST = 0.01;
    private static final double BUSINESS_INTEREST = 0.05;
    private static final double CHEQUING_INTEREST = 1000;

    public Account(double start_balance, CURRENCY_TYPE currency_type, int card_id, ACCOUNT_TYPE type, int branch_id) {
 		this.balance = start_balance;
 		this.bank_id = bank_id;
        this.branch_id = branch_id;
 		this.card_id = card_id;
 		this.is_type_changed = false;
 		this.is_business_disabled = false;
 		this.currency_type = currency_type;
 		this.transaction_limit = 100;
 		setAccountType(type);
    }

    public static double getTypeMinBalance(ACCOUNT_TYPE at) {
        switch(at) {
            case SAVING:
                return SAVING_START_BALANCE;
            case CHEQUING: 
                return CHEQUING_START_BALANCE;
            case BUSINESS:
                return BUSINESS_START_BALANCE;
        }
        return 0;
    }

    public boolean canMakeTransaction() {
        return transaction_limit > 0;
    }

    public boolean canBeBusiness() {
    	if (!is_business_disabled) {
    		if (balance >= BUSINESS_START_BALANCE) {
    			if (type != ACCOUNT_TYPE.BUSINESS) {
    				return true;
    			}
    		}
    	}
    	return false;
    }

    public void changeAccountTypeToBusines() {
    	type = ACCOUNT_TYPE.BUSINESS;
    	this.interest = BUSINESS_INTEREST;
    	is_type_changed = true; // flag of account type changing. For notifacation to user.
    }

    public boolean isTypeChanged() {  
    	boolean b = is_type_changed;  
    	is_type_changed = false;                  
    	return b;
    }

    public void rollBackType() {
    	type = ACCOUNT_TYPE.SAVING;
    	is_business_disabled = true;
    	this.interest = SAVING_INTEREST;
    }

    public boolean changeBalance(double amount) {
    	if (balance + amount > 0) {
    		balance += amount;
    		transaction_limit--;
    		return true;
    	}
    	return false;
    }

    public boolean canChangeBalance(double amount) {
    	if (balance + amount >= 0 && transaction_limit > 0) {
    		return true;
    	}
    	return false;
    }

    // BEGIN ATTR_ACCESSORS

    public double getDailyInterest() {
		return interest;
    }

    public void setBankId(int bank_id) {
    	this.bank_id = bank_id;
    }

    public void setCardId(int card_id) {
    	this.card_id = card_id;
    }

    public double getBalance() {
    	return balance;
    }

    public CURRENCY_TYPE getCurrencyType() {
    	return currency_type;
    }

    public ACCOUNT_TYPE getType() {
    	return type;
    }

    public int getBranchId() {
        return branch_id;
    }

    public int getId() {
        return this.account_id;
    }

    public void setId(int id) {
        account_id = id;
    }

    public int getCardId() {
        return this.card_id;
    }

    // END ATTR_ACCESSORS

    // BEGIN PRIVATE

    private void setAccountType(ACCOUNT_TYPE type) {
        switch (type) {
            case SAVING:
                this.start_balance = SAVING_START_BALANCE;
                this.interest = SAVING_INTEREST;
            break;
            case BUSINESS:
                this.start_balance = BUSINESS_START_BALANCE;
                this.interest = BUSINESS_INTEREST;
            break;
            case CHEQUING:
                this.start_balance = CHEQUING_START_BALANCE;
                this.interest = CHEQUING_INTEREST;
            break;
            default:
            break;
        }
    }

    // END PRIVATE
}