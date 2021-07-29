package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InterestOffer {
    
    public int size(){
        return 3;
    }
    public int insertSize(){
        return 3;
    }
    
    private int m_id;
    private String duration;
    private double interest_rate;
    
    public InterestOffer() {}
    
    public InterestOffer(int m_id, String duration, double interest_rate) {
    	this.m_id = m_id;
    	this.duration = duration;
    	this.interest_rate = interest_rate;
    }
    
    public int getM_id(){
        return this.m_id;
    }
    public void setM_id(int m_id){
        this.m_id = m_id;
    }
    
    public String getDuration(){
        return this.duration;
    }
    public void setDuration(String duration){
        this.duration = duration;
    }
    
    public double getInterest_rate(){
        return this.interest_rate;
    }
    public void setInterest_rate(double interest_rate){
        this.interest_rate = interest_rate;
    }
    
}
