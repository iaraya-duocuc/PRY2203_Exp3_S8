package cl.speedfast.model;

import cl.speedfast.config.AppConfig;

/**
 * Representa un pedido de tipo comida.
 */
public class PedidoComida extends Pedido {

    private boolean conMochilaTermica;

    /**
     * Crea un pedido de comida.
     */
    public PedidoComida(String direccion, boolean conMochilaTermica, double distanciaKm) {
        super(direccion, AppConfig.TipoPedido.COMIDA, distanciaKm);
        this.conMochilaTermica = conMochilaTermica;
    }

    /**
     * Asignacion generica de repartidor para pedidos de comida.
     */
    @Override
    public void asignarRepartidor() {
        System.out.println("Pedido Comida: buscando repartidor con mochila térmica...");
    }

    /**
     * Asigna un repartidor validando mochila termica.
     */
    public void asignarRepartidor(String nombreRepartidor) {
        if (conMochilaTermica) {
            setRepartidorAsignado(nombreRepartidor);
            System.out.println("[Repartidor: " + getRepartidorAsignado() +
                    "] asignado al pedido de comida (tiene mochila térmica).");
        } else {
            System.out.println("Pedido #" + getIdPedido()  + " ERROR: " + nombreRepartidor +
                    " no tiene mochila térmica. No se puede asignar.");
        }
    }

    /**
     * Calcula el tiempo estimado de entrega.
     */
    @Override
    public int calcularTiempoEntrega() {
        return 15 + (int) (2 * getDistanciaKm());
    }

}
