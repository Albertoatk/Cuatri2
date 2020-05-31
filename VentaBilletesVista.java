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

import control.OyenteVista;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import modelo.Tupla;
import modelo.Viajes;

public class VentaBilletesVista implements ActionListener, PropertyChangeListener {
    public static final String VERSION = "Oficina Viajes 2.0";
    public static final String ERROR = "ERROR";
    private static final String INFO = "Información: ";
    private static final String NO_HAY_VIAJE = "No hay viajes para la fecha"
            + " seleccionada";
    private static final String NO_HAY_AUTOBUSES = "No hay autobuses para el viaje"
            + " seleccionado";
    private static final String ERROR_VIAJE = "Error encontrando el viaje";
    private static final String ERROR_VIAJEROS = "Error no coinciden los viajeros";
    private static final String ERROR_AUTOBUS = "Error encontrando el autobus";
    private static final String ERROR_ASIENTO = "Error encontrando el asiento";
    private static final String TXT_ASIENTO = "Asiento: ";
    private static final String SOLICITAR_DNI_VIAJERO = "Introduce el DNI del "
            + "viajero";
    private static final String SOLICITAR_NOMBRE_VIAJERO = "Introduce el nombre del"
            + " viajero";
    private static final String AVISO = "AVISO";
    private static final String DESEA_LIBERAR_ASIENTO = "Desea liberar el asiento: ";

    private GregorianCalendar fecha;
    private GregorianCalendar fechaSeleccionada;

    private JFrame ventana;
    private OyenteVista oyenteVista;

    /*Declaracion de los botones/desplegables...*/
    private JButton botonReservar;
    private JButton botonAsignar;
    private JButton botonHojaViaje;
    private JButton botonAsientos;
    private JButton botonLiberar;
    private JButton botonBuscarViaje;

    private JComboBox desplegableAnyo;
    private JComboBox desplegableMes;
    private JComboBox desplegableDia;

    private JTextField textoViajes;
    private JTextField autobuses;

    private JComboBox desplegableViajes;
    private JComboBox desplegableAutobuses;

    private JPanel panelAsientos;
    private JPanel panelSuperior;
    private JPanel panelInferior;

    public static final String MENU_ITEM_RESERVAR = "Reservar";
    public static final String MENU_ITEM_GENERAR_HOJA_VIAJE = "Generar hoja viaje";
    public static final String MENU_ITEM_ASIGNAR = "Asignar";
    public static final String MENU_ITEM_BUSCAR_VIAJE = "Buscar viaje";
    public static final String MENU_ITEM_VER_ASIENTOS = "Ver asientos";
    public static final String MENU_ITEM_LIBERAR = "Liberar";
    public static final String ASIGNAR = "Asignar";
    public static final String LIBERAR = "Liberar";
    public static final String RESERVAR = "Reservar";
    private static final String TEXTO_AUTOBUSES = "Autobus: ";
    private static final String TEXTO_VIAJES = "Viaje: ";
    public static final String CONTROL_ERRORES = "Control de errores";
    private String matriculaSeleccionada;

    private Integer codigoViajeSeleccionado;
    private int anyoSeleccionado = 0;
    private int mesSeleccionado = 0;
    private int diaSeleccionado = 0;

    private Viajes viajes;

    private AsientoVista asientoVistaSeleccionado;
    private AutobusVista autobusVista;
    private static VentaBilletesVista instancia;

    public enum meses {
        ENERO, FEBRERO, MARZO, ABRIL, MAYO, JUNIO, JULIO, AGOSTO,
        SEPTIEMBRE, OCTUBRE, NOVIEMBRE, DICIEMBRE
    }

    public Object[] anyos = {2020, 2021};

    
    /**
     * 
     * Crea la vista de la venta de billetes
     */
    private VentaBilletesVista(OyenteVista oyenteVista, Viajes viajes){
        ventana = new JFrame(VERSION);
        fechaSeleccionada = new GregorianCalendar(0, 0, 0);

        this.viajes = viajes;
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
           new VentanaExcepcion(e.getMessage());
        };
        
        this.oyenteVista = oyenteVista;
        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                oyenteVista.eventoProducido(OyenteVista.Evento.SALIR, null);
            }
        });
        fecha = new GregorianCalendar();
        ventana.getContentPane().setLayout(new BorderLayout());

        panelSuperior = new JPanel();
        panelSuperior.setLayout(new GridLayout(2, 1));
        crearBarraEleccionFecha(panelSuperior);
        crearBarraViaje(panelSuperior);
        ventana.getContentPane().add(panelSuperior, BorderLayout.NORTH);

        panelInferior = new JPanel();
        panelInferior.setLayout(new GridLayout(1, 1));
        crearBarraInferior(panelInferior);
        panelInferior.setLayout(new FlowLayout());
        ventana.getContentPane().add(panelInferior, BorderLayout.SOUTH);

        panelAsientos = new JPanel();
        autobusVista = new AutobusVista(this, true, viajes);
        panelAsientos.add(autobusVista);
        ventana.getContentPane().add(panelAsientos, BorderLayout.CENTER);

        ventana.setResizable(false);
        ventana.pack();
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
    }

    /**
     * Crea la barra inferior de la ventana
     * @param panel 
     */
    private void crearBarraInferior(JPanel panel) {
        botonReservar = crearBotonMenu(MENU_ITEM_RESERVAR);
        botonAsignar = crearBotonMenu(MENU_ITEM_ASIGNAR);
        botonHojaViaje = crearBotonMenu(MENU_ITEM_GENERAR_HOJA_VIAJE);
        botonLiberar = crearBotonMenu(MENU_ITEM_LIBERAR);
        botonLiberar.setEnabled(false);
        botonReservar.setEnabled(false);
        botonAsignar.setEnabled(false);
        botonHojaViaje.setEnabled(false);

        panel.add(botonLiberar);
        panel.add(botonReservar);
        panel.add(botonAsignar);
        panel.add(botonHojaViaje);

    }
    
    
  /**
   * Devuelve la instancia de la vista de la venta de billetes
   */        
    public static synchronized VentaBilletesVista devolverInstancia(
            OyenteVista oyenteVista, Viajes viajes) throws Exception {
        if (instancia == null) {
            instancia = new VentaBilletesVista(oyenteVista, viajes);
        }
        return instancia;
    }
    
    /**
     * Crea la barra de la eleccion de fechas
     * @param panelNorte 
     */
    private void crearBarraEleccionFecha(JPanel panelNorte) {
        JToolBar barra = new JToolBar();
        barra.setFloatable(false);
        desplegableDia = crearDesplegableDias();
        desplegableMes = crearDesplegableMeses(meses.values());
        desplegableAnyo = crearDesplegableAnyos(anyos);
        barra.add(desplegableAnyo);

        barra.add(new JToolBar.Separator());

        desplegableMes.setSelectedIndex(fecha.get(Calendar.MONTH));
        barra.add(desplegableMes);
        barra.add(new JToolBar.Separator());

        desplegableDia.setSelectedItem(fecha.get(Calendar.DAY_OF_MONTH));
        barra.add(desplegableDia);
        barra.add(new JToolBar.Separator());

        botonBuscarViaje = crearBotonMenu(MENU_ITEM_BUSCAR_VIAJE);
        barra.add(botonBuscarViaje);
        botonBuscarViaje.setEnabled(true);

        panelNorte.add(barra);
    }

    /**
     * Crea la barra de los viajes de la venta de billetes
     * @param panel 
     */
    private void crearBarraViaje(JPanel panel) {
        JToolBar barra = new JToolBar();
        barra.setFloatable(false);

        textoViajes = new JTextField(TEXTO_VIAJES);
        textoViajes.setMaximumSize(textoViajes.getPreferredSize());
        textoViajes.setEditable(false);
        autobuses = new JTextField(TEXTO_AUTOBUSES);
        autobuses.setMaximumSize(autobuses.getPreferredSize());
        autobuses.setEditable(false);

        barra.add(textoViajes);
        barra.add(new JToolBar.Separator());
        desplegableViajes = crearDesplegableViajes();
        desplegableViajes.setEnabled(false);

        barra.add(desplegableViajes);
        barra.add(new JToolBar.Separator());

        barra.add(autobuses);
        barra.add(new JToolBar.Separator());

        desplegableAutobuses = crearDesplegableAutobuses();
        desplegableAutobuses.setEnabled(false);
        barra.add(desplegableAutobuses);

        botonAsientos = crearBotonMenu(MENU_ITEM_VER_ASIENTOS);
        botonAsientos.setEnabled(false);
        barra.add(new JToolBar.Separator());
        barra.add(botonAsientos);

        panel.add(barra);
    }

    /**
     * Selecciona un asiento de la vista
     * @param asientoVista 
     */
    public void seleccionarAsientoVista(AsientoVista asientoVista) {
        if (asientoVistaSeleccionado != null) {
            asientoVistaSeleccionado.deseleccionar();
        }

        this.asientoVistaSeleccionado = asientoVista;
        asientoVista.seleccionar();

        if (!asientoVista.estaAsignado() && !asientoVista.estaReservado()) {
            botonAsignar.setEnabled(true);
            botonReservar.setEnabled(true);
            botonLiberar.setEnabled(false);
        } else if (asientoVista.estaAsignado()) {
            botonAsignar.setEnabled(false);
            botonReservar.setEnabled(false);
            botonLiberar.setEnabled(true);
        } else if (asientoVista.estaReservado()) {
            botonAsignar.setEnabled(true);
            botonReservar.setEnabled(false);
            botonLiberar.setEnabled(true);
        }
    }

    /**
     * Crea botón barra de herramientas
     */
    private JButton crearBotonMenu(String etiqueta) {
        JButton boton = new JButton(etiqueta);
        boton.addActionListener(this);
        boton.setActionCommand(etiqueta);
        return boton;
    }

    /**
     * Crea un desplegable de viajes de la ventana
     * @return 
     */
    private JComboBox crearDesplegableViajes() {
        JComboBox desplegable = new JComboBox();
        desplegable.setBackground(Color.red);
        desplegable.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Scanner scannerLinea = new Scanner((String) e.getItem());
                codigoViajeSeleccionado = scannerLinea.nextInt();
                botonHojaViaje.setEnabled(true);
                List listaAutobuses = viajes.obtenerAutobuses(
                        codigoViajeSeleccionado);
                desplegableAutobuses.removeAllItems();
                desplegableAutobuses.setEnabled(false);
                anyadirTextoDesplegable(desplegableAutobuses, listaAutobuses, false);
            }
        });
        return desplegable;
    }
    
    public void anyadirTextoDesplegable(JComboBox desplegable, List lista, 
            boolean esViaje){
            if(lista.isEmpty()){
                botonLiberar.setEnabled(false);
                botonReservar.setEnabled(false);
                botonAsignar.setEnabled(false);
                botonHojaViaje.setEnabled(false);
                if(esViaje){
                    JOptionPane.showMessageDialog(ventana, NO_HAY_VIAJE, INFO,
                        JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(ventana, NO_HAY_AUTOBUSES, INFO,
                        JOptionPane.INFORMATION_MESSAGE); 
                }
            }else{
                desplegable.setEnabled(true);
                lista.forEach((viaje)-> {
                    desplegable.addItem(viaje);
                });
            }
    }
    

    /**
     * Crea un desplegable de autobuses de la ventana
     * @return 
     */
    private JComboBox crearDesplegableAutobuses() {
        JComboBox desplegable = new JComboBox();
        desplegable.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                matriculaSeleccionada = (String) e.getItem();
                botonAsientos.setEnabled(true);
            }
        });
        return desplegable;
    }

    /**
     * Crea desplegable de años de la ventana
     * @param o
     * @return 
     */
    private JComboBox crearDesplegableAnyos(Object[] o) {
        JComboBox desplegable = new JComboBox(o);
        anyoSeleccionado = 2020;
        fechaSeleccionada.set(anyoSeleccionado, mesSeleccionado, diaSeleccionado);
        desplegable.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                anyoSeleccionado = (int) e.getItem();
                fechaSeleccionada.set(anyoSeleccionado, mesSeleccionado,
                        diaSeleccionado);
                desplegableMes.setSelectedItem(meses.ENERO);
            }
        });
        return desplegable;
    }

    /**
     * Crea el desplegable de los meses de la ventana
     * @param o
     * @return 
     */
    private JComboBox crearDesplegableMeses(Object[] o) {
        JComboBox desplegable = new JComboBox(o);
        desplegable.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                meses mes1 = meses.valueOf(String.valueOf(e.getItem()));
                mesSeleccionado = mes1.ordinal();                
                fechaSeleccionada.set(anyoSeleccionado, mesSeleccionado, 0);
                modificarDias(desplegableDia);
            }
        });
        return desplegable;
    }

    /**
     * Crea el desplegable de los dias de viaje
     * @return 
     */
    private JComboBox crearDesplegableDias() {
        JComboBox desplegable = new JComboBox();
        desplegable.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                diaSeleccionado = (int) e.getItem();
                fechaSeleccionada.set(anyoSeleccionado, mesSeleccionado, 
                        diaSeleccionado);
            }
        });
        return desplegable;
    }

    /**
     * Modifica los días del desplegable de los días
     * @param desplegable 
     */
    private void modificarDias(JComboBox desplegable) {
        desplegable.removeAllItems();
        for (int j = 1; j
                <= fechaSeleccionada.getActualMaximum(Calendar.DAY_OF_MONTH); j++) {
            desplegable.addItem(j);
        }
    }

    /**
     * Sobreescribe el método actionPerformed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case MENU_ITEM_VER_ASIENTOS:
                autobusVista.ponerAsientos(codigoViajeSeleccionado,
                        matriculaSeleccionada);
                botonLiberar.setEnabled(false);
                botonAsignar.setEnabled(false);
                botonReservar.setEnabled(false);
                panelAsientos.setVisible(true);
                break;
            case MENU_ITEM_BUSCAR_VIAJE:
                List listaViajes = viajes.buscarViaje(fechaSeleccionada);
                limpiarDesplegableViajes();
                anyadirTextoDesplegable(desplegableViajes, listaViajes, true);
                break;
            case MENU_ITEM_RESERVAR:
                Tupla<String, String> tuplaReservar = consultarDatos();
                if (tuplaReservar != null) {
                    oyenteVista.eventoProducido(OyenteVista.Evento.RESERVAR,
                            generarQuintupla(tuplaReservar.a, tuplaReservar.b));
                }
                break;
            case MENU_ITEM_ASIGNAR:
                Tupla<String, String> tuplaAsignar = consultarDatos();
                if (tuplaAsignar != null) {
                    oyenteVista.eventoProducido(OyenteVista.Evento.ASIGNAR,
                            generarQuintupla(tuplaAsignar.a, tuplaAsignar.b));
                }
                break;
            case MENU_ITEM_GENERAR_HOJA_VIAJE:
                oyenteVista.eventoProducido(OyenteVista.Evento.GENERAR_HOJA_VIAJE,
                        codigoViajeSeleccionado);
                break;
            case MENU_ITEM_LIBERAR:
                int confirmacion = JOptionPane.showConfirmDialog(ventana,
                        DESEA_LIBERAR_ASIENTO
                        + asientoVistaSeleccionado.toString(), AVISO,
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirmacion == 0) {
                    oyenteVista.eventoProducido(OyenteVista.Evento.LIBERAR,
                            new Tupla<Tupla, Integer>(
                                    new Tupla<Integer, String>(
                                            codigoViajeSeleccionado,
                                            matriculaSeleccionada),
                                    asientoVistaSeleccionado.obtenerIdAsiento()));
                }
                break;
        }
    }

    
    /**
     * 
     * Devuelve una tupla con los datos del viajero 
     */

    public Tupla<String, String> consultarDatos() {
        String nombreViajero = JOptionPane
                .showInputDialog(ventana, SOLICITAR_NOMBRE_VIAJERO, TXT_ASIENTO
                        + asientoVistaSeleccionado.toString(),
                        JOptionPane.QUESTION_MESSAGE);
        String textoDNI = JOptionPane
                .showInputDialog(ventana, SOLICITAR_DNI_VIAJERO,
                        TXT_ASIENTO
                        + asientoVistaSeleccionado.toString(),
                        JOptionPane.QUESTION_MESSAGE);
        if ((nombreViajero != null && !nombreViajero.equals(""))
                && (textoDNI != null && !textoDNI.equals(""))) {
            return new Tupla<String, String>(nombreViajero, textoDNI);
        }
        return null;
    }
    
    /**
     * 
     * Genera una quintupla
     */
    public Tupla<Tupla, Integer> generarQuintupla(String nombreViajero,
            String textoDNI) {
        return new Tupla<Tupla, Integer>(new Tupla<Tupla, Tupla>(
                new Tupla<String, String>(nombreViajero, textoDNI),
                new Tupla<Integer, String>(codigoViajeSeleccionado,
                        matriculaSeleccionada)),
                asientoVistaSeleccionado.obtenerIdAsiento());
    }

    /**
     * Sobreescribe el método propertyChange
     * @param evt 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ASIGNAR:
                asientoVistaSeleccionado.ponerAsignado();
                break;
            case RESERVAR:
                asientoVistaSeleccionado.ponerReservado();
                break;
            case LIBERAR:
                asientoVistaSeleccionado.liberarAsiento();
                break;
            case CONTROL_ERRORES:
                int error = (int) evt.getNewValue();
                casosError(error);
            break;
        }
    }

    /**
     * Pone los casos de posible error
     */
    private void casosError(int error) {
        switch (error) {
            case modelo.Viajes.ERROR_VIAJE_NO_ENCONTRADO:
                JOptionPane.showMessageDialog(ventana, ERROR_VIAJE, ERROR,
                        JOptionPane.ERROR_MESSAGE);
                break;
            case modelo.Viaje.ERROR_AUTOBUS_NO_ENCONTRADO:
                JOptionPane.showMessageDialog(ventana, ERROR_AUTOBUS, ERROR,
                        JOptionPane.ERROR_MESSAGE);
                break;
            case modelo.Autobus.ASIENTO_NO_ENCONTRADO:
                JOptionPane.showMessageDialog(ventana, ERROR_ASIENTO, ERROR,
                        JOptionPane.ERROR_MESSAGE);
                break;
            case modelo.Asiento.ERROR_VIAJERO_NO_ASIGNADO:
                JOptionPane.showMessageDialog(ventana, ERROR_VIAJEROS, ERROR,
                        JOptionPane.ERROR_MESSAGE);
                break;
        }
    }
    
    /**
     * Limpia el desplegable de viajes
     */
    private void limpiarDesplegableViajes(){
        desplegableViajes.removeAllItems();
        desplegableAutobuses.removeAllItems();
        desplegableViajes.setEnabled(false);
        desplegableAutobuses.setEnabled(false);
        botonAsientos.setEnabled(false);
        autobusVista.limpiarAsientos();
    }
}
