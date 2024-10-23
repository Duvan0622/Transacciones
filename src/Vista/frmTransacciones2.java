package Vista;

import Reglas.CuentaAhorros;
import Reglas.CuentaCorriente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class frmTransacciones2 extends JFrame{
    private JRadioButton consignar;
    private JRadioButton retirar;
    private JTextField txtValor;
    private JButton enviar;
    private JPanel panel2;
    private JLabel transaccion;
    private JLabel valor;
    private JTable movimientos;
    private JScrollPane scrol;
    private boolean primeraVez = true;
    private int secuencia = 1;

    private CuentaCorriente cuenta;

    public frmTransacciones2(CuentaCorriente cuenta) {
        super("Transaccion");
        this.cuenta = cuenta;
        setContentPane(panel2);

        // Agrupar los Jradio para seleccionar solo uno
        ButtonGroup group = new ButtonGroup();
        group.add(consignar);
        group.add(retirar);

        String[] columnNames = {"Num", "Movimiento", "Fecha", "Valor", "Saldo", "Interés"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0); // Modelo con los headers
        movimientos.setModel(tableModel);
        JTableHeader header = movimientos.getTableHeader();
        Font headerFont = new Font("Arial", Font.BOLD, 16); // Fuente Arial, estilo bold, tamaño 16
        header.setFont(headerFont);

        //Boton enviar
        enviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verificar si se seleccionó una opción (consignar o retirar)
                if (!consignar.isSelected() && !retirar.isSelected()) {
                    JOptionPane.showMessageDialog(frmTransacciones2.this, "Debe seleccionar Consignar o Retirar.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Si es la primera vez, no permitir retirar
                if (primeraVez && retirar.isSelected()) {
                    JOptionPane.showMessageDialog(frmTransacciones2.this, "No puede retirar en la primera transacción.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar que el valor ingresado sea un número mayor a 0
                double valorTransaccion;
                try {
                    valorTransaccion = Double.parseDouble(txtValor.getText());
                    if (valorTransaccion <= 0) {
                        JOptionPane.showMessageDialog(frmTransacciones2.this, "El valor debe ser mayor que 0.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frmTransacciones2.this, "Debe ingresar un valor numérico.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                procesarTransaccion(valorTransaccion);
            }
        });
    }

    private void procesarTransaccion(double valorTransaccion) {
        // Procesar la transacción
        DefaultTableModel model = (DefaultTableModel) movimientos.getModel();
        String movimientoTipo = primeraVez ? "Abrir" : (consignar.isSelected() ? "Consignar" : "Retirar");
        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        double saldo;

        // Si es la primera vez, agrega el movimiento de abrir
        if (primeraVez) {
            cuenta.setCuenta(valorTransaccion);
            saldo = (cuenta instanceof CuentaAhorros) ? ((CuentaAhorros) cuenta).getBalanceBase() : cuenta.getBalance();
            model.addRow(new Object[]{secuencia++, "Abrir", fecha, valorTransaccion, saldo, null});
            primeraVez = false;
        } else {
            boolean transaccionExitosa = true;
            if (consignar.isSelected()) {
                cuenta.depositar(valorTransaccion); // Consigna el valor
            } else {
                transaccionExitosa = cuenta.retirar(valorTransaccion);
                if (!transaccionExitosa) {
                    JOptionPane.showMessageDialog(this, "Fondos insuficientes para realizar el retiro.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            saldo = (cuenta instanceof CuentaAhorros) ? ((CuentaAhorros) cuenta).getBalance() : cuenta.getBalance();
            double interes = (cuenta instanceof CuentaAhorros) ? ((CuentaAhorros) cuenta).getTasaIntereses() : 0;
            model.addRow(new Object[]{secuencia++, movimientoTipo, fecha, valorTransaccion, saldo, interes});
        }

        txtValor.setText(""); // Limpiar el campo de texto
    }

    public static void main(String[] args) {
        CuentaCorriente c = new CuentaCorriente();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame f= new frmTransacciones2(c);
                f.setSize(700, 400);
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }
}
