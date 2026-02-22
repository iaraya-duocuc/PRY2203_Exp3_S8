package cl.speedfast.model;

import cl.speedfast.config.AppConfig;
import cl.speedfast.controller.ControladorPedidos;

/**
 * Representa un repartidor que procesa pedidos de forma concurrente.
 */
public class Repartidor implements Runnable {

    private int id;
    private String nombre;
    private ZonaDeCarga zonaDeCarga;

    public Repartidor(int id, String nombre, ZonaDeCarga zonaDeCarga) {
        this.id = id;
        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
    }

    public String getNombre() {return nombre;}
    public int getId() {
        return id;
    }

    @Override
    public void run() {
        while (true) {
            Pedido pedido = zonaDeCarga.retirarPedido();

            if (pedido == null) {
                break;
            }

            // Solo asignar si aún no tiene repartidor
            if (!pedido.tieneRepartidorAsignado()) {
                pedido.asignarRepartidor(nombre);

                if (!pedido.tieneRepartidorAsignado()) {
                    // No se puede asignar, se omite
                    continue;
                }

                // Cambiar estado a en reparto y registrar en DB
                pedido.setEstado(AppConfig.EstadoPedido.EN_REPARTO);
                ControladorPedidos.actualizarEstadoPedido(
                        pedido.getIdPedido(),
                        AppConfig.EstadoPedido.EN_REPARTO
                );
                ControladorPedidos.registrarEntrega(
                        pedido.getIdPedido(),
                        this.id
                );
            }

            /*
            pedido.asignarRepartidor(nombre);

            if (!pedido.tieneRepartidorAsignado()) {
                // pedido sin repartidor disponible, se omite
                continue;
            }

            pedido.setEstado(AppConfig.EstadoPedido.EN_REPARTO);
            ControladorPedidos.actualizarEstadoPedido(
                    pedido.getIdPedido(),
                    AppConfig.EstadoPedido.EN_REPARTO
            );
            // Registrar entrega en BD
            ControladorPedidos.registrarEntrega(
                    pedido.getIdPedido(),
                    this.id
            );
            System.out.println(
                    "[Repartidor - " + nombre + "] Estado: " + pedido.getEstado().obtenerNombre() + "."
            );
            */
            try {
                System.out.println(
                        "[Repartidor - " + nombre + "] Entregando pedido #" + pedido.getIdPedido() + "..."
                );
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            pedido.setEstado(AppConfig.EstadoPedido.ENTREGADO);
            ControladorPedidos.actualizarEstadoPedido(
                    pedido.getIdPedido(),
                    AppConfig.EstadoPedido.ENTREGADO
            );
            System.out.println(
                    "[Repartidor - " + nombre + "] Estado: " + pedido.getEstado().obtenerNombre() + "."
            );
        }
    }
}
