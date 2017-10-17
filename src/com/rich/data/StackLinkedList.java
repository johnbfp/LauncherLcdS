package com.rich.data;

import java.util.LinkedList;

public class StackLinkedList<T> {
	private LinkedList<T> datas;  
    
    public StackLinkedList() {  
        datas = new LinkedList<T>();  
    }  
      
    // ��ջ  
    public void push(T data) {  
        datas.addLast(data);  
    }  
      
    // ��ջ  
    public T pop() {  
    	if(isEmpty()){
    		return null;
    	}
    	 return datas.removeLast();  
    }  
      
    // �鿴ջ��  
    public T peek() {  
        return datas.getLast();  
    }  
      
    //ջ�Ƿ�Ϊ��  
    public boolean isEmpty() {  
        return datas.isEmpty();  
    }  
      
    //size  
    public int size() {  
        return datas.size();  
    }  
}
