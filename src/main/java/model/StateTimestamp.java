package model;

public class StateTimestamp {
    
    public int size(){
        return 4;
    }
    public int insertSize(){
        return 4;
    }
    
    private int m_id;
    private String state;
    private String started_time;
    private String end_time;
    
    public StateTimestamp() {}
    
    public int getM_id(){
        return this.m_id;
    }
    public void setM_id(int m_id){
        this.m_id = m_id;
    }
    
    public String getState(){
        return this.state;
    }
    public void setState(String state){
        this.state = state;
    }
    
    public String getStarted_time(){
        return this.started_time;
    }
    public void setStarted_time(String started_time){
        this.started_time = started_time;
    }
    
    public String getEnd_time(){
        return this.end_time;
    }
    public void setEnd_time(String end_time){
        this.end_time = end_time;
    }
    
}
