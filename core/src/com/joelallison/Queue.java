package com.joelallison;

public class Queue { //static circular queue

    private String[] array;

    private int front = 0;
    private int back = -1;
    private int size;
    private int itemsInArray = 0;

    public Queue(int size) {
        this.array = new String[size];
        this.size = size;
    }

    public void enQueue(String item) {
        itemsInArray++;
        back++;
        back = back % size;
        array[back] = item;
    }

    public String deQueue() {
        itemsInArray--;
        String item = array[front];
        array[front] = null;
        front++;
        front = front % size;
        return item;
    }

    public String readFront(){
        return array[front];
    }

    public String[] getArray() {
        return array;
    }

    public int size(){
        return itemsInArray;
    }

    public boolean isEmpty(){
        return itemsInArray == 0;
    }

    public boolean isFull(){
        return itemsInArray == size;
    }
}
