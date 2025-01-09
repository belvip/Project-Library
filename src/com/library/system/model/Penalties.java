package com.library.system.model;


public class Penalties {


    // Attributs
    private int penaltyRatePerDay;  // Taux de pénalité par jour de retard 100fcfa


    // Constructeur
    public Penalties(int penaltyRatePerDay) {
        this.penaltyRatePerDay = penaltyRatePerDay;
    }

    // Getters et Setters
    public int getPenaltyRatePerDay() {
        return penaltyRatePerDay;
    }
}

