package com.rich.data;

import java.util.LinkedList;

public class StackLinkedList<T> {
	private LinkedList<T> datas;  
    
    public StackLinkedList() {  
        datas = new LinkedList<T>();  
    }  
      
    // 入栈  
    public void push(T data) {  
        datas.addLast(data);  
    }  
      
    // 出栈  
    public T pop() {  
    	if(isEmpty()){
    		return null;
    	}
    	 return datas.removeLast();  
    }  
      
    // 查看栈顶  
    public T peek() {  
        return datas.getLast();  
    }  
      
    //栈是否为空  
    public boolean isEmpty() {  
        return datas.isEmpty();  
    }  
      
    //size  
    public int size() {  
        return datas.size();  
    }  
}
