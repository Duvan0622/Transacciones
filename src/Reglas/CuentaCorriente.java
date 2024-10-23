package Reglas;

public class CuentaCorriente {
    public double balance = 0;

    public void setCuenta(double saldo){
        balance = saldo;
    }

    public void depositar(double consignacion){
        balance  += consignacion;
    }

    public boolean retirar(double retiro) {
        if (retiro > balance) {
            System.out.println("Fondos insuficientes.");
            return false;
        } else {
            balance -= retiro;
            return true;
        }
    }
    public double getBalance(){
        return balance;
    }
}
