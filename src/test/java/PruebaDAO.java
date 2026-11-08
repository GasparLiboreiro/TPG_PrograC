import modelo.persistencia.DAOUsuarios;
import modelo.persistencia.DTOUsuario;
import modelo.persistencia.SistemaCuentasConXML;

public class PruebaDAO {
    public static void main(String[] args) {
        DAOUsuarios DAO = new SistemaCuentasConXML();
        DTOUsuario a = new DTOUsuario("Sofia", "1234");
        if(DAO.login(a))
            System.out.println("Se pudo iniciar secion");
        else
            System.out.println("No se pudo iniciar secion");
        if(DAO.register(a))
            System.out.println("Se pudo registrar");
        else
            System.out.println("No se pudo registrar");
        if(DAO.login(a))
            System.out.println("Se pudo iniciar secion");
        else
            System.out.println("No se pudo iniciar secion");
        if(DAO.register(a))
            System.out.println("Se pudo registrar");
        else
            System.out.println("No se pudo registrar");

    }
}
