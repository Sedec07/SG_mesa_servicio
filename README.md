# 🛠️ Sistema de Gestión de Mesa de Servicio de TI (Help Desk)

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![FlatLaf](https://img.shields.io/badge/UI-FlatLaf-2A2A2A?style=for-the-badge)](https://www.formdev.com/flatlaf/)
[![Architecture](https://img.shields.io/badge/Architecture-MVC%20%2B%20DAO-blue?style=for-the-badge)]()

Aplicación de escritorio para la gestión automatizada de incidentes y requerimientos de soporte de TI, estructurada bajo requerimientos de la norma **IEEE 830** y arquitectura multicapa (MVC + DAO).

---

## 🚀 Características Principales

- **Control de Acceso Basado en Roles (RBAC):**
  - **Administrador:** Gestión de usuarios, métricas globales y reportes.
  - **Técnico:** Panel de atención de solicitudes y actualización del ciclo de vida de tickets.
  - **Solicitante / Usuario:** Creación de solicitudes y seguimiento en tiempo real.
- **Balanceo de Carga Automatizado:** Algoritmo de asignación inteligente que distribuye las nuevas solicitudes al técnico activo con menor número de `tickets_asignados`.
- **Trazabilidad y Auditoría (RNF02):** Registro inalterable de comentarios, cambios de estado y fechas mediante la bitácora `historial_tickets`.
- **Interfaz Moderna:** Diseño de escritorio enriquecido mediante el Look and Feel de **FlatLaf**.

---

## 🛠️ Tecnologías y Herramientas

- **Lenguaje:** Java (JDK 17+)
- **Interfaz Gráfica:** Java Swing + FlatLaf (Tema Dark/Light)
- **Base de Datos:** MariaDB / MySQL
- **Conectividad:** JDBC (Driver `mysql-connector-j`)
- **Patrones de Diseño:** Model-View-Controller (MVC), Data Access Object (DAO), Singleton.
- **IDE:** NetBeans IDE

---

## 📁 Estructura del Proyecto

```text
src/
├── DAO/                 # Capa de Persistencia y Consultas SQL (CRUD)
│   ├── MySQLConnection.java
│   ├── UsuarioDAO.java
│   ├── TicketDAO.java
│   ├── HistorialTicketDAO.java
│   └── CategoriaDAO.java
├── model/               # Entidades del Dominio
│   ├── Usuario.java
│   ├── Ticket.java
│   ├── HistorialTicket.java
│   ├── Categoria.java
│   ├── Rol.java
│   └── EstadoTicket.java
├── controller/          # Lógica de Negocio y Controladores Swing
└── view/                # Vistas e Interfaces Gráficas con FlatLaf
