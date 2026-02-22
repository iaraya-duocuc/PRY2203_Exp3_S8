package cl.speedfast.model;

import java.util.List;

/**
 * Interfaz que permite rastrear acciones o eventos.
 */
public interface Rastreable {

    /**
     * Retorna el historial de acciones registradas.
     *
     * @return lista de eventos en formato texto
     */
    List<String> verHistorial();
}