package controller;

import DAO.CategoriaDAO;
import java.util.List;
import model.Categoria;
/**
 *
 * @author Melqui Erazo
 */
public class CtrlCategoria {

    private final CategoriaDAO categoriaDAO;

    
    public CtrlCategoria() {
        this.categoriaDAO = new CategoriaDAO();
    }

    public List<Categoria> obtenerCategorias() {
        return categoriaDAO.listar();
    }
}