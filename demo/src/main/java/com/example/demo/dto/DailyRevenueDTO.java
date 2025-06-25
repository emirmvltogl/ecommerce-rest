package com.example.demo.dto;

public class DailyRevenueDTO {
    private String date;
    private double totalRevenue;

    public DailyRevenueDTO(String date, double totalRevenue) {
        this.date = date;
        this.totalRevenue = totalRevenue;
    }

    // Getter ve setterlar
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
