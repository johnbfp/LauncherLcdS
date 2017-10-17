package com.rich.data;

public class SublcdStack <T> {
	private int maxSize;
	private T[] stackArray;
	private int top;
	@SuppressWarnings("unchecked")
	public SublcdStack(int maxSize) {
		this.maxSize = maxSize;
		stackArray = (T[]) new Object[maxSize]; 
		top = -1;
	}
	
	public void push (T t){
		if (!isFull())  
		stackArray[++top] = t;
	}
    
	public T pop(){
		 if (isEmpty()) {  
	            return null;  
	        } 
		return stackArray[top--];
	}
	
	public T peek(){
		return stackArray[top];
	}
	
	public boolean isEmpty(){
		return (top == -1);
	}
	public boolean isFull(){
		return (top == maxSize -1);
	}
	public int size() {  
        return top + 1;  
    } 
}
