package kaptainwutax.itraders.util;

import com.google.gson.annotations.Expose;

public class Trade {
	
	@Expose protected Product buy;
	@Expose protected Product extra;
	@Expose protected Product sell;
	private int hashCode;

	private Trade() {
		//Serialization.
	}
	
	public Trade(Product buy, Product extra, Product sell) {
		this.buy = buy;
		this.extra = extra;
		this.sell = sell;
	}

	
	public Product getBuy() {
		return this.buy;
	}		

	public Product getExtra() {
		return this.extra;
	}
	
	public Product getSell() {
		return this.sell;
	}	

	public boolean isValid() {
		if(this.buy == null || !this.buy.isValid())return false;
		if(this.sell == null || !this.sell.isValid())return false;
		if(this.extra != null && !this.extra.isValid())return false;
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)return false;
		else if(obj == this)return true;
		else if(this.getClass() != obj.getClass())return false;
		
		Trade trade = (Trade)obj;
		return trade.sell.equals(this.sell) && trade.buy.equals(this.buy);
	}
	
}
