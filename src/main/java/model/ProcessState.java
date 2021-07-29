package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProcessState {
	private int mortgageID;
	private String state;
	private String status;
	
	public ProcessState() {}
	
	public ProcessState(int mortgageID, String state, String status) {
		this.mortgageID = mortgageID;
		this.state = state;
		this.status = status;
	}

	public int getMortgageID() {
		return this.mortgageID;
	}
	
	public void setMortgageID(int mortgageID) {
		this.mortgageID = mortgageID;
	}
	
	public String getState() {
		return this.state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
}
