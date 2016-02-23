import java.lang.*;
import java.util.*;

// Store info about user and accounts id. Can have many accounts with different types of currence type

class Card {
	private String last_name;
	private String first_name;
    private String password;
    private String passport_id;
    private String patronym;
    private int birth;
    private boolean sex;
    private int card_id;
    private int branch_id;
    private boolean is_bloked;
    private Vector account_ids;


    public Card(String last_name, String first_name, String password, int branch_id, String passport_id, String patronym, int birth, boolean sex) {
    	this.last_name = last_name;
    	this.first_name = first_name;
    	this.password = password;
    	this.passport_id = passport_id;
    	this.patronym = patronym;
    	this.birth = birth;
    	this.patronym = patronym;
    	this.sex = sex;
    	account_ids = new Vector();
    }

    public boolean comparePassword(String password) {
    	return (this.password).equals(password);
    }

    // Add acoount id to cards accounts Vector 
    public void addAccount(Account new_account) {
    	this.account_ids.pushBack(new_account.getId());
    	new_account.setCardId(this.card_id);
    }

    public void blockCard() {
    	is_bloked = true;
    }

    // BEGIN ATTR_ACCESSORS

    public void setLastName(String last_name) {
    	this.last_name = last_name;
    }

    public int getBranchId() {
    	return branch_id;
    }

    public void setFirstName(String first_name) {
    	this.first_name = first_name;
    }

    public void setPassword(String password) {
    	this.password = password;
    }

    public int getId() {
    	return card_id;
    }

    public void setId(int id) {
    	card_id = id;
    }

    public boolean isBlocked() {
    	return is_bloked;
    }

    public String getLastName() {
    	return last_name;
    }

    public String getFirstName() {
    	return first_name;
    }

    public int getAccountsNumber() {
    	return account_ids.size();
    }

    // END ATTR_ACCESSORS
}