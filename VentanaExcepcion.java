/**
 * OficinaTestFinal.java
 *
 * Prototipo 3 03/05/2020
 *
 * Roberto Jiménez López
 * Alberto Pérez Blasco
 *
 *
 */
package vista;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class VentanaExcepcion {

    public VentanaExcepcion(String mensaje) {
        JFrame ventana = new JFrame();
        JOptionPane.showMessageDialog(ventana, mensaje,
                    VentaBilletesVista.ERROR, JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
    
}
