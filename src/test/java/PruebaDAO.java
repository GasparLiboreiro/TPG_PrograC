import modelo.persistencia.DAOUsuarios;
import modelo.persistencia.DAOUsuariosSQLite;
import modelo.persistencia.DTOUsuario;

public class PruebaDAO {
    public static void main(String[] args) {
        DAOUsuarios dao = new DAOUsuariosSQLite();
        System.out.println(dao.login(new DTOUsuario("Ignacio", "321")));
        System.out.println(dao.register(new DTOUsuario("Ignacio", "321")));
        System.out.println(dao.login(new DTOUsuario("Ignacio", "123")));
    }
}
