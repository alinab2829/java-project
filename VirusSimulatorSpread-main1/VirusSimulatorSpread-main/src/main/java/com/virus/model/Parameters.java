package com.virus.model;

public class Parameters {
    private double r0=2.5;
    private  double incubation =5.0;// дней
    private  double recovery = 14.0;// дней
    private double mortality = 0.02;// 2%
    private double maskEffect =0.0;
    private double vaccineRate = 0.0;// % в день
    private boolean useSEIR = true;

    // Насколько сильно заражение перекидывается на соседние клетки (0..1)
    public double neighborFactor = 0.05;

    // Максимальная длительность моделирования (в днях)
    private double maxDays = 365.0;

    /** Бетта - базовая скорость заражения
     *  r0 = beta * T_bol (без масок)
     *  здесь учтём ношение маски уменьшает beta
     *
    * */
    public double getBeta(){
        double effectiveR0 = r0 * (1.0 - maskEffect);
        return effectiveR0 / recovery;
    }
    /** Скорость E -> I */
    public double getSigma(){return useSEIR? 1.0 / incubation : 0;}

    /** Скорость выздоровления I -> R*/

    public double getRecoveryRate(){
        return (1.0 - mortality)/recovery;
    }

    /** Скорость смерти I -> D*/

    public double getDeathRate(){
        return mortality/recovery;
    }

    // Getters && Setters
    public double getR0(){return r0;}
    public void  setR0(double r0){this.r0 = r0;}

    public double getIncubation(){return incubation;}
    public void setIncubation(double incubation){this.incubation = incubation;}

    public double getRecovery(){return recovery;}
    public void setRecovery(double recovery){this.recovery= recovery;}

    public double getMortality(){return mortality;}
    public void  setMortality(double mortality){this.mortality = mortality;}

    public double getMaskEffect(){return maskEffect;}
    public void  setMaskEffect(double maskEffect){this.maskEffect=maskEffect;}

    public double getVaccineRate(){return vaccineRate;}
    public void setVaccineRate(double vaccineRate){this.vaccineRate =vaccineRate;}

    public boolean isUseSEIR(){return useSEIR;}
    public void setUseSEIR(boolean useSEIR){this.useSEIR = useSEIR;}

    public double getNeighborFactor(){return neighborFactor;}
    public void setNeighborFactor(double neighborFactor){this.neighborFactor = neighborFactor;}

    public double getMaxDays(){return  maxDays;}
    public void setMaxDays(double maxDays){this.maxDays = maxDays;}

}
