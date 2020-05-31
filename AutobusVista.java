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

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
import modelo.Asiento;
import modelo.Viajes;

public class AutobusVista extends JPanel {

    public static final int ALTURA_FILA = 900;
    public static final int ANCHURA_COLUMNA = 300;
    public static final String ERROR_ASIENTOS = "Error, al obtener asientos";
    private AsientoVista[][] asientos;
    private VentaBilletesVista ventaBilletesVista;
    private int filas;
    private int columnas;
    Viajes viajes;
    
    
    
    /**
     * Inicializa la vista de un autobús
     * 
     */
    public AutobusVista(VentaBilletesVista ventaBilletesVista, 
            boolean recibeEventosRaton,Viajes viajes) {
        this.viajes = viajes;
        this.filas = viajes.obtenerFilasDistribucion();
        this.columnas = viajes.obtenerColumnasDistribucion();
        this.ventaBilletesVista = ventaBilletesVista;
        //TODO INICIALIZAR VIAJERO
        crearAsientos(recibeEventosRaton);
        this.setPreferredSize(new Dimension(ALTURA_FILA,
                ANCHURA_COLUMNA));
    }
    
    
    /**
     * Crea la vista de los asientos del autobús
     *  
     */
    public void crearAsientos(boolean recibeEventosRaton) {
        setLayout(new GridLayout(columnas, filas));
        asientos = new AsientoVista[columnas][filas];

        for (int i = columnas; i > 0; i--) {
            for (int j = 0; j < filas; j++) {
                asientos[i-1][j] = new AsientoVista(ventaBilletesVista,
                        recibeEventosRaton);
                add(asientos[i-1][j]);

            }
        }
        
    }
    
    
    /**
     * Limpia los asientos del autobús
     */
    public void limpiarAsientos() {
        for (int i = columnas; i > 0; i--) {
            for (int j = 0; j < filas;j++) {

                asientos[i-1][j].vaciarAsiento();
                //crearAsientos(recibeEventosRaton,filas,columnas);
            }
        }
    }
    
    /**
     * Coloca la vista de los asientos del autobús
     *  
     */
    public void ponerAsientos(int codViaje, String matricula) {
        for (int col = columnas; col > 0; col--) {
            for (int fil = 0; fil < filas; fil++) {
                    Asiento asiento = viajes.obtenerAsientoAutobus
                                      (col,fil,codViaje,matricula);
                    if (asiento != null){
                        switch (asiento.obtenerEstado()) {
                            case LIBRE:
                                asientos[col-1][fil].ponerIdAsiento(
                                        asiento.obtenerId());
                                asientos[col-1][fil].liberarAsiento();
                                break;
                            case ASIGNADO:
                                asientos[col-1][fil].ponerIdAsiento(
                                        asiento.obtenerId());
                                asientos[col-1][fil].ponerAsignado();
                                break;
                            case RESERVADO:
                                asientos[col-1][fil].ponerIdAsiento(
                                        asiento.obtenerId());
                                asientos[col-1][fil].ponerReservado();
                                break;
                            }
                    }
                }
            }
        }
    }
