package Reglas;

public class CuentaAhorros extends CuentaCorriente{
    private double tasaIntereses;

    public void setTasaIntereses(double tasaIntereses) {
        this.tasaIntereses = tasaIntereses;
    }

    public double getTasaIntereses() {
        return tasaIntereses;
    }

    @Override
    public double getBalance() {
        double interesesMensuales = (balance * tasaIntereses / 100) / 12;
        balance += interesesMensuales;
        return balance;
    }

    public double getBalanceBase() {
        return super.getBalance();
    }
}
