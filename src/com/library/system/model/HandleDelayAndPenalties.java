package com.library.system.model;

public class HandleDelayAndPenalties {

    // Attributs
    private int penaltyRatePerDay;  // Taux de pénalité par jour de retard


    // Constructeur
    public HandleDelayAndPenalties(int penaltyRatePerDay) {
        this.penaltyRatePerDay = penaltyRatePerDay;
    }

    // Getters et Setters
    public int getPenaltyRatePerDay() {
        return penaltyRatePerDay;
    }
}
