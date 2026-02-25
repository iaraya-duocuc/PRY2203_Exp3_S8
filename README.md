# SpeedFast - Pedidos en Java

Proyecto Java que simula un sistema de pedidos para una empresa de delivery.

## Tipos de pedidos

- Express
- Comida
- Encomienda

## Tecnologías y conceptos

- Herencia
- Interfaces
- Clases abstractas
- Polimorfismo
- Listas (`List`)
- Interfaz gráfica (Swing)
- Maven
- MySQL 8 (`mysql-connector-j`)

## Estructura del proyecto

```text
config      -> configuraciones y enums
controller  -> lógica de coordinación y casos de uso
dao         -> acceso a datos (persistencia)
              - ConexionDB
              - PedidoDAO
              - EntregaDAO
              - RepartidorDAO
              - ClienteDAO
model       -> lógica de negocio
              - Pedido (abstracta), PedidoExpress, PedidoComida, PedidoEncomienda
              - Repartidor, Entrega, Cliente, ZonaDeCarga
              - dto/EntregaDTO
ui          -> interfaz de usuario
              - SistemaUI, Main
              - PedidosPanel, EntregasPanel, RepartidoresPanel
              - VentanaPedido, VentanaEntrega, VentanaRepartidor
```

## Montaje de base de datos (pruebas)

El proyecto se conecta a:
- URL: `jdbc:mysql://localhost:3306/speedfast_db`
- Usuario: `user`
- Password: `pass`

Estos valores están definidos en `ConexionDB.java`.

1. En caso de no contar con BBDD, levantar MySQL con Docker:

```bash
docker compose up -d
```

2. Crear tablas de prueba:

```sql
CREATE DATABASE IF NOT EXISTS speedfast_db;
USE speedfast_db;

CREATE TABLE repartidores (
 id INT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(100) NOT NULL
);

CREATE TABLE pedidos (
 id INT AUTO_INCREMENT PRIMARY KEY,
 direccion VARCHAR(100) NOT NULL,
 tipo ENUM('COMIDA','ENCOMIENDA','EXPRESS'),
 estado ENUM('PENDIENTE','EN_REPARTO','ENTREGADO')
);

CREATE TABLE entregas (
 id INT AUTO_INCREMENT PRIMARY KEY,
 id_pedido INT,
 id_repartidor INT,
 fecha DATE,
 hora TIME,
 FOREIGN KEY (id_pedido) REFERENCES pedidos(id),
 FOREIGN KEY (id_repartidor) REFERENCES repartidores(id)
);
```

3. (Opcional) Datos mínimos para probar UI:

```sql
INSERT INTO repartidores(nombre) VALUES ('Pedro'), ('Camila');
INSERT INTO pedidos(direccion, tipo, estado) VALUES
('Av. Vitacura 123', 'EXPRESS', 'PENDIENTE'),
('Calle 456', 'COMIDA', 'PENDIENTE');
```

## Ejecución de la aplicación

1. Clonar repositorio:

```bash
git clone https://github.com/iaraya-duocuc/PRY2203_Exp3_S8.git
```

2. Ejecutar la aplicación:

```bash
mvn clean compile
```

3. Abrir en IntelliJ y ejecutar `cl.speedfast.ui.Main`.
