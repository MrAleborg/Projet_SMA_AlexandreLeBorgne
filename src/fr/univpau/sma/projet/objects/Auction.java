package fr.univpau.sma.projet.objects;

import jade.util.leap.Serializable;

@SuppressWarnings("serial")
public class Auction implements Serializable, Comparable<Auction> {
	
	private int _price = 0;
	private int _initPrice = 0;
	private int _minPrice = 0;
	private int _increaseStep = 1;
	private int _decreaseStep = 1;
	private String _name = "";
	private String _dealerName = "";
	private int _bids = 0;
	private long timer = 0;
	
	public Auction(){}
	
	public Auction(int _price, int _initPrice, int _minPrice,
			int _increaseStep, int _decreaseStep, String _name,
			String _dealerName, int _bids, long timer) {
		this._price = _price;
		this._initPrice = _initPrice;
		this._minPrice = _minPrice;
		this._increaseStep = _increaseStep;
		this._decreaseStep = _decreaseStep;
		this._name = _name;
		this._dealerName = _dealerName;
		this._bids = _bids;
		this.setTimer(timer);
	}
	
	public Auction(int _initPrice, int _minPrice,
			int _increaseStep, int _decreaseStep, String _name,
			long timer) {
		this._initPrice = _initPrice;
		this._minPrice = _minPrice;
		this._increaseStep = _increaseStep;
		this._decreaseStep = _decreaseStep;
		this._name = _name;
		this._bids = 0;
		this.setTimer(timer);
	}

	public int get_price() {
		return _price;
	}

	public void set_price(int _price) {
		this._price = _price;
	}

	public int get_initPrice() {
		return _initPrice;
	}

	public void set_initPrice(int _initPrice) {
		this._initPrice = _initPrice;
	}

	public int get_minPrice() {
		return _minPrice;
	}

	public void set_minPrice(int _minPrice) {
		this._minPrice = _minPrice;
	}

	public int get_increaseStep() {
		return _increaseStep;
	}

	public void set_increaseStep(int _increaseStep) {
		this._increaseStep = _increaseStep;
	}

	public int get_decreaseStep() {
		return _decreaseStep;
	}

	public void set_decreaseStep(int _decreaseStep) {
		this._decreaseStep = _decreaseStep;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public String get_dealerName() {
		return _dealerName;
	}

	public void set_dealerName(String _dealerName) {
		this._dealerName = _dealerName;
	}

	public int get_bids() {
		return _bids;
	}

	public void set_bids(int _bids) {
		this._bids = _bids;
	}

	@Override
	public int compareTo(Auction o) {
		return (this._name.equals(o.get_name()) && this._dealerName.equals(o.get_dealerName()))? 
				0 : -1;
	}
	
	public int increasePrice(){
		return this._price = this._price + this._increaseStep;
	}
	
	public int decreasePrice(){
		return this._price = this._price + this._decreaseStep;
	}
	
	public void initBids(){
		this._bids = 0;
	}
	
	public void bid(){
		this._bids++;
	}

	public long getTimer() {
		return timer;
	}

	public void setTimer(long timer) {
		this.timer = timer;
	}
	
}
