package cl.speedfast.model;

import cl.speedfast.config.AppConfig;

/**
 * Representa un pedido de tipo encomienda.
 */
public class PedidoEncomienda extends Pedido {

    private double peso;
    private boolean embalajeCorrecto;

    /**
     * Crea un pedido de encomienda.
     */
    public PedidoEncomienda(String direccion, double peso, boolean embalajeCorrecto, double distanciaKm) {
        super(direccion, AppConfig.TipoPedido.ENCOMIENDA, distanciaKm);
        this.peso = peso;
        this.embalajeCorrecto = embalajeCorrecto;
    }

    /**
     * Asignacion generica de repartidor para encomiendas.
     */
    @Override
    public void asignarRepartidor() {
        System.out.println("Pedido Encomienda: validando peso y embalaje...");
    }

    /**
     * Asigna un repartidor validando peso y embalaje.
     */
    @Override
    public void asignarRepartidor(String nombreRepartidor) {
        if (peso <= AppConfig.PESO_MAX_ENCOMIENDA && embalajeCorrecto) {
            setRepartidorAsignado(nombreRepartidor);
            System.out.println("[Repartidor: " + getRepartidorAsignado() +
                    "] asignado a encomienda (peso y embalaje válidos).");
        } else {
            System.out.println("Pedido #" + getIdPedido()  + " ERROR: encomienda inválida para asignación. Peso maximo: " + AppConfig.PESO_MAX_ENCOMIENDA + " kg. Peso medido: " + peso + " kg. ¿Embalaje correcto?: " + (embalajeCorrecto ? "Sí." : "No."));
        }
    }

    /**
     * Calcula el tiempo estimado de entrega.
     */
    @Override
    public int calcularTiempoEntrega() {
        return 20 + (int) Math.round(1.5 * getDistanciaKm());
    }

}
