package com.example.helloapplication;

public class ExpenseRecord {

    private String periode;
    private double total;
    private double logement;
    private double nourriture;
    private double sorties;
    private double voiture;
    private double voyage;
    private double impots;
    private double autres;

    private double totalAffiche;
    private double logementAffiche;
    private double nourritureAffiche;
    private double sortiesAffiche;
    private double voitureAffiche;
    private double voyageAffiche;
    private double impotsAffiche;
    private double autresAffiche;

    public ExpenseRecord(String periode, double total, double logement, double nourriture, double sorties,
                         double voiture, double voyage, double impots, double autres) {
        this.periode = periode;
        this.total = total;
        this.logement = logement;
        this.nourriture = nourriture;
        this.sorties = sorties;
        this.voiture = voiture;
        this.voyage = voyage;
        this.impots = impots;
        this.autres = autres;

        // valeurs affichées initialement = en euros
        this.totalAffiche = total;
        this.logementAffiche = logement;
        this.nourritureAffiche = nourriture;
        this.sortiesAffiche = sorties;
        this.voitureAffiche = voiture;
        this.voyageAffiche = voyage;
        this.impotsAffiche = impots;
        this.autresAffiche = autres;
    }

    public String getPeriode() { return periode; }

    public double getTotal() { return total; }
    public double getLogement() { return logement; }
    public double getNourriture() { return nourriture; }
    public double getSorties() { return sorties; }
    public double getVoiture() { return voiture; }
    public double getVoyage() { return voyage; }
    public double getImpots() { return impots; }
    public double getAutres() { return autres; }

    public double getTotalAffiche() { return totalAffiche; }
    public void setTotalAffiche(double totalAffiche) { this.totalAffiche = totalAffiche; }

    public double getLogementAffiche() { return logementAffiche; }
    public void setLogementAffiche(double logementAffiche) { this.logementAffiche = logementAffiche; }

    public double getNourritureAffiche() { return nourritureAffiche; }
    public void setNourritureAffiche(double nourritureAffiche) { this.nourritureAffiche = nourritureAffiche; }

    public double getSortiesAffiche() { return sortiesAffiche; }
    public void setSortiesAffiche(double sortiesAffiche) { this.sortiesAffiche = sortiesAffiche; }

    public double getVoitureAffiche() { return voitureAffiche; }
    public void setVoitureAffiche(double voitureAffiche) { this.voitureAffiche = voitureAffiche; }

    public double getVoyageAffiche() { return voyageAffiche; }
    public void setVoyageAffiche(double voyageAffiche) { this.voyageAffiche = voyageAffiche; }

    public double getImpotsAffiche() { return impotsAffiche; }
    public void setImpotsAffiche(double impotsAffiche) { this.impotsAffiche = impotsAffiche; }

    public double getAutresAffiche() { return autresAffiche; }
    public void setAutresAffiche(double autresAffiche) { this.autresAffiche = autresAffiche; }
}
