enum STATUS {
	OK, CONNECTION_ERROR, ACCOUNT_NOT_FOUND, ACCSESS_DENIED, WA
}

/** Answer from Bank to request fo transaction */
class Answer {
	private static final int ANSWERS_COUNTER = 0;
	private int answer_id;
	private STATUS status;

	public Answer() {}

	public Answer(STATUS status) {
		this.status = status;
	}

	// BEGIN ATTR_ACCESSORS

	public int getAnswerID() {
		return answer_id;
	}

	public STATUS getStatus() {
		return status;
	}

	public void setAnswerStatus(STATUS status) {
		this.status = status;
	}

	// END ATTR_ACCESSORS

}