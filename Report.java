import java.lang.*;
import java.util.*;
import java.io.*;

// Allow to store transaction and current info string by string and print it to screen or file
class Report {

	private Vector data;
	private static int report_counter = 1;
	private static String separator = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=";

	public Report(Card card, Vector transactions) {
		data = new Vector();
		data.pushBack(separator);
		String card_info = card.getFirstName() + " " + card.getLastName() + " opened this card in " + 
		card.getBranchId() + " branch, have "  + card.getAccountsNumber() + " accounts and made " + transactions.size()
				+ " transactions";
		data.pushBack(card_info);
		data.pushBack(separator);
		appendTransactions(transactions);
	}

	public Report(Branch branch, Vector transactions) {
		data = new Vector();
		data.pushBack(separator);
		String branch_info = "Branch number " + branch.getId() + ". \nManager: " + branch.getManager() + 
		". \nAddress:  " + branch.getAddress() + ". \nPhone number: " + branch.getPhone() + ". \nBusiness time: " +
		branch.getBusinessTime();
		data.pushBack(branch_info);
		data.pushBack(separator);
		appendTransactions(transactions);
	}

	public Report(Bank bank, Vector transactions) {
		data = new Vector();
		data.pushBack(separator);
		String bank_info = "Banck number " + bank.getId();
		data.pushBack(bank_info);
		data.pushBack(separator);
		appendTransactions(transactions);
	}

	private void appendTransactions(Vector transactions) {
		for (int i = 0; i < transactions.size(); i++) {
			String transaction_str = "";
			Transaction transaction = (Transaction)transactions.getElement(i);
			String currency = transaction.getCurrencyType().toString();
			switch(transaction.getTransactionType()) {
				case PUT:
					transaction_str += "Put " + transaction.getAmount() +" "+ currency + " to " + transaction.getAccountId();
					break;
				case WITHDROW:
					transaction_str += "Withdrow " + transaction.getAmount() +" "+ currency + " from " + transaction.getAccountId();
					break;
				case INTEREST:
					transaction_str += "Charged " + transaction.getAmount() +" "+ currency + " interest from " + transaction.getAccountId();
					break;
				case TRANSFER:
					transaction_str += "Transact " + transaction.getAmount() +" "+ currency + " from " + transaction.getAccountId();
					break;
			}
			transaction_str += " account in " + transaction.getBranchId() + " branch ";

			if (transaction.getTransactionType() == TRANSACTION_TYPE.TRANSFER) {
				transaction_str += " to " + transaction.getDestinationAccountId() + " account in " 
				+ transaction.getDestinationBranchId() + " branch ";
			}
			data.pushBack(transaction_str);
			data.pushBack(separator);
		}
	}

	public void printToScreen() {
		for (int i = 0; i < data.size(); i++) {
			System.out.println((String)data.getElement(i));
		}
	}

	public void printToFile() throws IOException {
		File file = new File("report-" + report_counter++ + ".txt");
		file.createNewFile();
		try {
	    	try{
	    		file.createNewFile();
	    	} catch(IOException e) {
	    		e.printStackTrace();
	    	}
	        PrintWriter out = new PrintWriter(file.getAbsoluteFile());
	        try {
	        	for (int i = 0; i < data.size(); i++) {
					out.println((String)data.getElement(i));
				}
	        } 
	        finally {
	            out.close();
	        }
    	} catch(IOException e) {
        	throw new RuntimeException(e);
    	}
	}
}