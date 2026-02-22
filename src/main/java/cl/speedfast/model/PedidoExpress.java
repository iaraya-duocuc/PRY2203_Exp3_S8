package cl.speedfast.model;

import cl.speedfast.config.AppConfig;

/**
 * Representa un pedido de tipo express.
 */
public class PedidoExpress extends Pedido {

    private boolean hayRepartidorDisponible;

    /**
     * Crea un pedido express.
     */
    public PedidoExpress(String direccion, boolean disponible, double distanciaKm) {
        super(direccion, AppConfig.TipoPedido.EXPRESS, distanciaKm);
        this.hayRepartidorDisponible = disponible;
    }

    /**
     * Asignacion generica de repartidor para pedidos express.
     */
    @Override
    public void asignarRepartidor() {
        System.out.println("Pedido Express: buscando repartidor más cercano disponible...");
    }

    /**
     * Asigna un repartidor si hay disponibilidad inmediata.
     */
    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        if (hayRepartidorDisponible) {
            setRepartidorAsignado(nombreRepartidor);
            System.out.println("[Repartidor: " + getRepartidorAsignado() +
                    "] asignado por cercanía inmediata.");
        } else {
            System.out.println("Pedido #" + getIdPedido()  + " ERROR: no hay repartidor cercano disponible.");
        }
    }

    /**
     * Calcula el tiempo estimado de entrega.
     */
    @Override
    public int calcularTiempoEntrega() {
        int tiempo = 10;
        if (getDistanciaKm() > 5) {
            tiempo += 5;
        }
        return tiempo;
    }

}
