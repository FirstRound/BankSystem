
//Own realisation of Vector. Doubled when (size == capacity/2)
class Vector {
	private Object[] array;
	private int capacity;
	private int size;
	private static int min_size = 10;

	public Vector() {
		this.capacity = this.min_size;
		array = new Object[this.min_size];
		size = 0;
	}

	public void pushBack(Object o) {
		checkSize();
		this.array[size++] = o;
	}

	public Object getElement(int ind) {
		return this.array[ind];
	}

	private void checkSize() {
		Object[] arr = null;
		if (this.size > this.capacity/2) {
			arr = new Object[this.capacity * 2];
			this.capacity *= 2;
			for (int i = 0; i < this.size; i++) {
				arr[i] = this.array[i];
			}
			this.array = arr;
		}
	}

	public int size() {
		return this.size;
	}

	public void deleteElementById(int ind) {
		for (int i = ind+1; i < size; i++) {
			this.array[i] = this.array[i-1];
		}
		this.array[size-1] = null;
	}

}