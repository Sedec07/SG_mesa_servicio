package controller;

import DAO.HistorialTicketDAO;
import DAO.TicketDAO;
import DAO.UsuarioDAO;
import java.util.List;
import model.HistorialTicket;
import model.Ticket;
import model.Usuario;
/**
 *
 * @author Melqui Erazo
 */
public class CtrlTicket {

    private final TicketDAO ticketDAO;
    private final UsuarioDAO usuarioDAO;
    private final HistorialTicketDAO historialDAO;

    public CtrlTicket() {
        this.ticketDAO = new TicketDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.historialDAO = new HistorialTicketDAO();
    }

    /**
     * Registra un nuevo ticket, aplica balanceo de carga automático para asignar técnico
     * y registra la creación en la bitácora de historial.
     */
    public boolean crearTicketConBalanceo(String titulo, String descripcion, String prioridad, int idCategoria, int idSolicitante) {
        String codigoUnico = ticketDAO.generarCodigoUnico();
        Ticket nuevoTicket = new Ticket(codigoUnico, titulo, descripcion, prioridad, idCategoria, idSolicitante);

        // Algoritmo de Balanceo: Obtener el técnico con menos carga activa
        Usuario tecnicoAsignado = usuarioDAO.obtenerTecnicoMenosCargado();
        if (tecnicoAsignado != null) {
            nuevoTicket.setIdTecnicoAsignado(tecnicoAsignado.getIdUsuario());
        }

        boolean insertado = ticketDAO.insertar(nuevoTicket);

        if (insertado) {
            // Incrementar contador del técnico asignado
            if (tecnicoAsignado != null) {
                usuarioDAO.incrementarTicketsAsignados(tecnicoAsignado.getIdUsuario());
            }

            // Registrar evento inicial en bitácora de auditoría
            Ticket ticketCreado = ticketDAO.buscarPorId(Integer.parseInt(codigoUnico) - 1000);
            int idTicketReg = (ticketCreado != null) ? ticketCreado.getIdTicket() : 1;

            HistorialTicket historial = new HistorialTicket(
                idTicketReg, 
                idSolicitante, 
                "Ticket creado e ingresado al sistema con estado NUEVO.", 
                null, 
                1 // Estado 1 = NUEVO
            );
            historialDAO.insertar(historial);
        }

        return insertado;
    }

    /**
     * Obtiene los tickets según el rol del usuario autenticado.
     */
    public List<Ticket> obtenerTicketsPorRol(Usuario usuario) {
        switch (usuario.getIdRol()) {
            case 1: // ADMIN
                return ticketDAO.listarTodos();
            case 2: // TECNICO
                return ticketDAO.listarPorTecnico(usuario.getIdUsuario());
            case 3: // USUARIO / SOLICITANTE
                return ticketDAO.listarPorSolicitante(usuario.getIdUsuario());
            default:
                return ticketDAO.listarPorSolicitante(usuario.getIdUsuario());
        }
    }

    /**
     * Cambia el estado de un ticket y registra la trazabilidad inalterable.
     */
    public boolean cambiarEstadoTicket(int idTicket, int idEstadoAnterior, int idEstadoNuevo, int idUsuarioAccion, String comentario) {
        boolean actualizado = ticketDAO.actualizarEstado(idTicket, idEstadoNuevo);

        if (actualizado) {
            HistorialTicket historial = new HistorialTicket(
                idTicket, 
                idUsuarioAccion, 
                comentario, 
                idEstadoAnterior, 
                idEstadoNuevo
            );
            historialDAO.insertar(historial);
        }

        return actualizado;
    }

    public List<HistorialTicket> obtenerHistorial(int idTicket) {
        return historialDAO.listarPorTicket(idTicket);
    }
}