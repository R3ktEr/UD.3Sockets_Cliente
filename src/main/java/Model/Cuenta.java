package Model;

import java.io.Serializable;

public class Cuenta implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	protected int id;
	protected Float money;
	protected String transactions;
	
	public Cuenta(int id, Float money, String transactions) {
		this.id = id;
		this.money = money;
		this.transactions = transactions;
	}
	
	public Cuenta() {
		this(-1,0F,"");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public String getTransactions() {
		return transactions;
	}

	public void setTransactions(String transactions) {
		this.transactions = transactions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((money == null) ? 0 : money.hashCode());
		result = prime * result + ((transactions == null) ? 0 : transactions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cuenta other = (Cuenta) obj;
		if (id != other.id)
			return false;
		if (money == null) {
			if (other.money != null)
				return false;
		} else if (!money.equals(other.money))
			return false;
		if (transactions == null) {
			if (other.transactions != null)
				return false;
		} else if (!transactions.equals(other.transactions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cuenta [id=" + id + ", money=" + money + ", transactions=" + transactions + "]";
	}
	
	
}
