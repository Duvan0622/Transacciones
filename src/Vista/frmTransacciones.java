package Vista;

import Reglas.CuentaAhorros;
import Reglas.CuentaCorriente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmTransacciones extends JFrame{
    private JPanel panel1;
    private JRadioButton corriente;
    private JRadioButton ahorro;
    private JTextField txtInteres;
    private JLabel titulo;
    private JLabel intereses;
    private JButton siguiente;

    private CuentaAhorros cuentaAhorros;
    private CuentaCorriente cuentaCorriente;

    public frmTransacciones() {
        super("Transaccion");
        setContentPane(panel1);

        // Agrupar los Jradio para seleccionar solo uno
        ButtonGroup group = new ButtonGroup();
        group.add(corriente);
        group.add(ahorro);

        // Inicializar las cuentas
        cuentaAhorros = new CuentaAhorros();
        cuentaCorriente = new CuentaCorriente();

        // Agregar ActionListener al botón "Siguiente"
        siguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validar si algún JRadioButton fue seleccionado
                if (!corriente.isSelected() && !ahorro.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una opción (Corriente o Ahorro)", "Error de Selección", JOptionPane.ERROR_MESSAGE);
                } else if (ahorro.isSelected()) {
                    // Validar que el campo de interés no esté vacío
                    String interesStr = txtInteres.getText().trim();
                    if (interesStr.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Debe ingresar un valor para el interés", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                            // Convertir el texto a un número y validar el rango
                            double interes = Double.parseDouble(interesStr);
                            if (interes < 0 || interes > 100) {
                                JOptionPane.showMessageDialog(null, "El interés debe estar entre 0 y 100", "Error de Rango", JOptionPane.ERROR_MESSAGE);
                            } else {
                                cuentaAhorros.setTasaIntereses(interes);
                                abrirFrmTransacciones2(cuentaAhorros);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Debe ingresar un valor numérico para el interés (se usa punto en caso de decimal)", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    abrirFrmTransacciones2(cuentaCorriente);
                }
            }
        });
    }

    // Método para abrir la nueva ventana y cerrar la actual
    private void abrirFrmTransacciones2(CuentaCorriente cuenta) {
        // Cerrar esta ventana
        dispose();

        // Crear y abrir la nueva ventana (frmTransacciones2)
        frmTransacciones2 nuevaVentana = new frmTransacciones2(cuenta);
        nuevaVentana.setSize(700, 400);
        nuevaVentana.setLocationRelativeTo(null);
        nuevaVentana.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame f= new frmTransacciones();
                f.setSize(700, 400);
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }
}
