class Tuple<L,R> {
    private L left;
    private R right;
    public Tuple(L left, R right) {
	this.left = left;
	this.right = right;
    }
    public L getFirst() {
	return left;
    }
    public R getSecond() {
	return right;
    }
    public void setFirst(L left) {
	this.left = left;
    }
    public void setSecond(R right) {
	this.right = right;
    }
}
