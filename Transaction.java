
enum TRANSACTION_TYPE { WITHDROW, PUT, INTEREST, TRANSFER }

// Should use inheritance, but...

class Transaction {
	private static int transaction_count = 0;
	private int account_id;
	private TRANSACTION_TYPE type;
	private int branch_id;
	private int destination_branch_id;
	private int transaction_id;
	private double amount;
	private int destination_account_id;
	private CURRENCY_TYPE currency_type;



	public Transaction(int dest_acc, int account_id, int branch_id, double amount, TRANSACTION_TYPE type) {
		this.destination_account_id = dest_acc;
		this.account_id = account_id;
		this.amount = amount;
		this.transaction_id = transaction_count++;
		this.type = type;
	}

	public Transaction(int account_id, int branch_id, double amount, TRANSACTION_TYPE type, CURRENCY_TYPE currency) {
		this.account_id = account_id;
		this.amount = amount;
		this.transaction_id = transaction_count++;
		this.type = type;
		this.currency_type = currency;
	}

	public Transaction(int account_id, int branch_id, double amount, TRANSACTION_TYPE type) {
		this.account_id = account_id;
		this.amount = amount;
		this.transaction_id = transaction_count++;
		this.type = type;
	}

	// BEGIN ATTR_ACCESSORS
	
	public int getAccountId() {
		return account_id;
	}

	public int getBranchId() {
		return branch_id;
	}

	public CURRENCY_TYPE getCurrencyType() {
		return currency_type;
	}

	public TRANSACTION_TYPE getTransactionType() {
		return type;
	}

	public void setCurrencyType(CURRENCY_TYPE currency) {
		this.currency_type = currency;
	}

	public int getTransactionId() {
		return transaction_id;
	}

	public double getAmount() {
		return amount;
	}

	public int getDestinationAccountId() {
		return destination_account_id;
	}

	public void setDestinationBranchId(int dest_branch_id) {
		this.destination_branch_id = dest_branch_id;
	}

	public int getDestinationBranchId() {
		return this.destination_branch_id;
	}

	// END ATTR_ACCESSORS
}