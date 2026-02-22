package cl.speedfast.model;

import java.util.ArrayList;
import java.util.List;

public class ZonaDeCarga {

    public ZonaDeCarga() {
        System.out.println("[Zona de carga inicializada]");
    }

    private final List<Pedido> pedidos = new ArrayList<>();

    public synchronized void agregarPedido(Pedido p) {
        // Solo agregar si aún no tiene repartidor asignado
        if (!p.tieneRepartidorAsignado() && !pedidos.contains(p)) {
            pedidos.add(p);
            System.out.println(
                    "Pedido #" + p.getIdPedido() +
                            " agregado. Destino: " + p.getDireccion()
            );
        }
    }

    public synchronized Pedido retirarPedido() {
        if (pedidos.isEmpty()) {
            return null;
        }
        return pedidos.remove(0);
    }

    public synchronized void limpiar() {
        pedidos.clear();
    }

    public synchronized boolean estaVacia() {
        return pedidos.isEmpty();
    }
}
