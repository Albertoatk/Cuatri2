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

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import static modelo.Asiento.ESTADO_VIAJERO;
import static modelo.Asiento.IDENTIFICADOR_VIAJERO;
import modelo.estadoReserva;

public class AsientoVista extends JLabel {
    private static final Color COLOR_SELECCIONADO = new Color(255, 192, 81);
    private static final Color COLOR_ASIGNADO = new Color(190, 190, 190);
    private static final Color COLOR_RESERVADO = new Color(140, 200, 250);
    private VentaBilletesVista plano;
    private Integer idAsiento;
    private Color COLOR_NO_SELECCIONADO = Color.GRAY.brighter().brighter();
    private boolean seleccionado = false;
    private boolean asignado = false;
    private boolean reservado = false;
    private Color colorAsiento = new Color(141, 255, 125);

    /**
     * Inicialza la vista de un asiento
     * @param plano
     * @param recibeEventosRaton 
     */
    public AsientoVista(VentaBilletesVista plano, boolean recibeEventosRaton) {
        this.plano = plano;
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        setOpaque(true);
        setHorizontalAlignment(SwingConstants.CENTER);
        if (recibeEventosRaton) {
            recibirEventosRaton();
        }
    }
    
    /**
    * Recibe eventos del raton
    */
    public void recibirEventosRaton() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if ((AsientoVista.this.obtenerIdAsiento()) != null) {
                    plano.seleccionarAsientoVista(AsientoVista.this);
                }
            }
        });
    }
    
    /**
     * Vacía los campos del asiento
     */
    public void vaciarAsiento() {
        idAsiento = null;
        setText(null);
        setBackground(COLOR_NO_SELECCIONADO);

    }
    
    /**
     * Devueleve el identificador del asiento
     * @return 
     */
    public Integer obtenerIdAsiento() {
        return idAsiento;
    }

    /**
     * Iguala el identificador del asiento
     * @param idAsiento 
     */
    public void ponerIdAsiento(Integer idAsiento) {
        this.idAsiento = idAsiento;
        setText(String.valueOf(idAsiento));
        setBackground(colorAsiento);
        
        
    }
    /**
     * Selecciona un asiento
     */
    public void seleccionar() {
        seleccionado = true;
        if (idAsiento != null) {
            setBackground(COLOR_SELECCIONADO);
        }

    }

    /**
     * 
     * Devuelve verdadero si el asiento esta seleccionado
     */
    public boolean estaSeleccionado() {
        return seleccionado;
    }
    
    /**
     * Pone a asignado el asiento
     */
    public void ponerAsignado() {
        asignado = true;
        setBackground(COLOR_ASIGNADO);
    }
    
    /**
     * 
     * Devuelve verdadero si el asiento esta asignado 
     */
    public boolean estaAsignado() {
        return asignado;
    }
    /**
     * 
     * Devuelve verdadero si el asiento esta reservado 
     */
    public boolean estaReservado() {
        return reservado;
    }

    /**
     * Pone a reservado el asiento
     */
    public void ponerReservado() {
        reservado = true;
        setBackground(COLOR_RESERVADO);

    }

    /**
     * Libera el asiento 
     */
    public void liberarAsiento() {
        asignado = false;
        reservado = false;
        setBackground(colorAsiento);
    }
    /**
     *  Deselecciona un asiento
     */
    public void deseleccionar() {
        seleccionado = false;
        if (asignado) {
            setBackground(COLOR_ASIGNADO);
        } else if (reservado) {
            setBackground(COLOR_RESERVADO);
        } else {
            setBackground(colorAsiento);
        }
    }

    /**
     * Inicia un asiento
     */
    public void iniciar() {
        setText("");
        liberarAsiento();
        deseleccionar();
        idAsiento = 0;
    }
    
    /**
     * Devuelve los datos del asiento
     *
     */
    public String toString() {
        if (reservado) {
            return IDENTIFICADOR_VIAJERO + idAsiento + ESTADO_VIAJERO
                    + estadoReserva.RESERVADO;
        } else if (asignado) {
            return IDENTIFICADOR_VIAJERO + idAsiento + ESTADO_VIAJERO
                    + estadoReserva.ASIGNADO;
        } else {
            return IDENTIFICADOR_VIAJERO + idAsiento + ESTADO_VIAJERO
                    + estadoReserva.LIBRE;
        }
    }
}
