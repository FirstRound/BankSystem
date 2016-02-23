import java.lang.*;
import java.util.*;
import java.io.*;

// Class for semi-random test data reneration

class Emulator {

	private int NAME_LENGTH_MAX = 20;
	private int BALANCE_MAX = 1000000;
	private int TRANSACTION_AMOUNT_MAX = 100000;
	private int BALANCE_MIN = 1000;
	private int LETTERS_COUNT = 56;
	private int ITEMS_COUNT = 4;

	private Bank bank;
	private Branch current_branch;
	private Random rnd = new Random();
	private Scanner sc = new Scanner(System.in);

	private Vector cards = new Vector();

	private Card current_card;

	public void run() {
		bank = new Bank();
		generateData(bank);
		useApp();
	}

	// BEGIN PRIVATE

	// Generate data by semi-random
	private void generateData(Bank bank) {
		Branch new_branch = bank.createBranch("Mr.Ship", "RF, Innopolis, Uni 3", "992-92-42", "7-22");
		if (new_branch == null) {
			System.out.println("Can't create branch!");
			return;
		}
		current_branch = new_branch;
		for (int i = 0; i < ITEMS_COUNT; i++) {
			Card card = current_branch.createCard(getString(), getString(), getString(), getPassportId(), getString(), (int)getStartAmount()%10+1, getSex());
			if (card != null) {
				cards.pushBack(card);
				for (int j = 0; j < ITEMS_COUNT; j++) {
					if ((current_branch.createAccount(getStartAmount(), getCurrencyType(), card.getId(), getAccountType()) == null)) {
						System.out.println("Not enough money for this account type"); 
					}
				}
			}
			else {
				System.out.println("Can't create user!");
			}
		}
		Card card = current_branch.createCard("Петров", "Петр", "root", getPassportId(), getString(), (int)getStartAmount()%10+1, getSex());
		current_card = card;
		current_branch.createAccount(1000000, getCurrencyType(), current_card.getId(), getAccountType());
		current_branch.createAccount(getStartAmount(), getCurrencyType(), current_card.getId(), getAccountType());
		current_branch.createAccount(getStartAmount(), getCurrencyType(), current_card.getId(), getAccountType());
		current_branch.createAccount(getStartAmount(), getCurrencyType(), current_card.getId(), getAccountType());
		current_branch.createAccount(getStartAmount(), getCurrencyType(), current_card.getId(), getAccountType());

	}


	//Emulate ten cycles
	private void useApp(){
		int times = 10;
		while(times-- > 0) {
			if (authorization()) {
				chooseAccount();
				randomActions();
			}
		}
		Report rp1 = current_branch.getUserReport();
		try {
			rp1.printToFile();
		} catch(IOException e) {
			e.printStackTrace();
		}
		Report rp2 = current_branch.__getBranchReport();
		try {
			rp2.printToFile();
		} catch(IOException e) {
			e.printStackTrace();
		}
		Report rp3 = bank.getBankReport(); // Never use it/ Unsafe/ only for test
		try {
			rp3.printToFile();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	// Make some random actions to test system
	private void randomActions() {
		int times = 5;
		int choise = 0;
		Answer ans;
		double amount = 0;
			while(times-- > 0) {
				choise = (Math.abs(rnd.nextInt()) % 5) + 1;
				amount = Math.abs(getAmount());
			switch (choise) {
				case 1:
					int dest_id = selectDestinationAccountID();
					ans = current_branch.transferMoney(dest_id, amount);
					showOperationInfo(dest_id, current_branch.getAccountId(), amount, ans);
					break;
				case 2:
					ans = current_branch.withdrawMoney(amount);
					showOperationInfo(-1, current_branch.getAccountId(), amount, ans);
					break;
				case 3:
					ans = current_branch.putMoney(amount);
					showOperationInfo(-1, current_branch.getAccountId(), amount, ans);
					break;
				case 4:
	                System.out.println(current_branch.checkBalance());
					break;
				case 5:
				  System.out.println("Daily interest has been charged");
					bank.chargeDailyInteres();
					break;
	            default:
	                break;
			}
		}
	}

	// Try to authirize in system current user
    private boolean authorization() {
    	if (current_branch.authorization(4, "root")) {
    		System.out.println("here");
			return true;
    	}
    	else {	
    		return false;
    	}
    }

    // Choose account and look if account was upgraded to business
    private void chooseAccount() {
    	current_branch.selectAccount(0);
    	if (current_branch.isAccountTypeChanged()) {
    		System.out.println("Your account has been upgradet to Business. Do you wand to leave it?");
			if (Math.abs(rnd.nextInt()) % 2 == 0) {
				System.out.println("No!");
				current_branch.rollBackAccountType();
				System.out.println("Account have been rooled back");
			}
			else {
				System.out.println("Yes");
			}
		}
    }
    
    // Print operation information to screen 
    private void showOperationInfo(int dest_id, int cur_id, double amount, Answer ans) {
   		showResponse(ans);
   		if (ans.getStatus() != STATUS.OK) {
   			return;
   		}
   		System.out.print(current_card.getLastName() + " " + current_card.getLastName());
   		if(amount < 0) {
   			System.out.print(" withdrow ");
   			amount*=-1;
   		} 
   		else {
   			System.out.print(" put ");
   		}
   		System.out.print (amount + " from " + cur_id);
   		if (dest_id != -1) {
   			System.out.println(" to " + dest_id);
   		}
   		else {
   			System.out.println("");
   		}
   }
   
    private void showResponse(Answer ans) {
        if (ans.getStatus() == STATUS.OK)
            System.out.println("Its OK transfer");
        else {
            System.out.println("Smth went wrong!");
        }
    }

    private int selectDestinationAccountID() {
        return Math.abs(rnd.nextInt()) % bank.__getAccountCount();
    }

    // BEGIN GENERATORS

    private String getString() {
    	int name_length = Math.abs(rnd.nextInt()) % NAME_LENGTH_MAX;
    	char[] name = new char[name_length];
    	for (int i = 0; i < name_length; i++) {
    		name[i] = (char)(Math.abs(rnd.nextInt()) % LETTERS_COUNT + 'A');
    	}
    	return new String(name);
    }
    private double getAmount() {
    	double balance = (double)(rnd.nextInt()) % TRANSACTION_AMOUNT_MAX;
    	return balance;
    }
    private double getStartAmount() {
    	double balance = (double)Math.abs(rnd.nextInt()) % BALANCE_MAX + BALANCE_MIN;
    	return balance;
    }
    private String getPassportId() {
    	int id_length = Math.abs(rnd.nextInt()) % 6;
    	char[] id = new char[id_length];
    	for (int i = 0; i < id_length; i++) {
    		id[i] = (char)(rnd.nextInt() % 9);
    	}
    	return new String(id);
    }
    private boolean getSex() {
    	boolean sex = (boolean)((Math.abs(rnd.nextInt()) % 2) != 0);
    	return sex;
    }
    private CURRENCY_TYPE getCurrencyType() {
    	if (Math.abs(rnd.nextInt()) % 10 < 7)
    		return CURRENCY_TYPE.RUB;
    	else if (Math.abs(rnd.nextInt()) % 2 == 1) {
    		return CURRENCY_TYPE.USD;
    	}
    	else {
    		return CURRENCY_TYPE.EUR;
    	}
    }
    private ACCOUNT_TYPE getAccountType() {
    	return ACCOUNT_TYPE.SAVING;
    }

    // END GANARATORS
    
    // END PRIVATE
}