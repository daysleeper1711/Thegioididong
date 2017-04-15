/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daysleeper.project.thegioididongproduct;

/**
 *
 * @author DaySLeePer
 */
public class Item {
    private String header;

    public Item() {
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return String.format("header: %s", this.header);
    }
    
}
