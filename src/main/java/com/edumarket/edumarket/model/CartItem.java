package com.edumarket.edumarket.model;

public class CartItem {
    private Course course;
    private int quantity;

    public CartItem(Course course, int quantity) {
        this.course = course;
        this.quantity = quantity;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
