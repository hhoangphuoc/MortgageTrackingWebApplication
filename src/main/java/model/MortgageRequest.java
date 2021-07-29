package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MortgageRequest {
    
    public static int size(){
        return 4;
    }
    public static int insertSize(){
        return 4;
    }
    
    private int m_id;
    private int c_id;
    private int s_id;
    private int index;
    
    public int getM_id(){
        return this.m_id;
    }
    public void setM_id(int m_id){
        this.m_id = m_id;
    }
    
    public int getC_id(){
        return this.c_id;
    }
    public void setC_id(int c_id){
        this.c_id = c_id;
    }
    
    public int getS_id(){
        return this.s_id;
    }
    public void setS_id(int s_id){
        this.s_id = s_id;
    }
    
    public int getIndex(){
        return this.index;
    }
    public void setIndex(int index){
        this.index = index;
    }
    
}
