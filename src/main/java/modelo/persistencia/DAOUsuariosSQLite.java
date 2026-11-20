package modelo.persistencia;

import java.sql.*;

public class DAOUsuariosSQLite implements DAOUsuarios {
    Connection c;

    public DAOUsuariosSQLite(){
        c=null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            prepararDB();
        } catch ( Exception e ) {
            throw new RuntimeException("DAOUsuariosSQLite no pudo inciarse: "+e.getMessage());
        }
    }

    private void prepararDB() {
        if(!tablaExiste("USUARIOS"))
        {
            String sql = "CREATE TABLE USUARIOS (NOMBRE VARCHAR(51) PRIMARY KEY NOT NULL, CONTRASENIA VARCHAR(51) NOT NULL)";
            try {
                PreparedStatement stmt = this.c.prepareStatement(sql);
                stmt.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e); // nada que hacer si la db falla inesperadamente
            }
        }
    }

    private boolean tablaExiste(String nombre_tabla) {
        String sql = "SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = ?";

        try {
            PreparedStatement pstmt = this.c.prepareStatement(sql);
            pstmt.setString(1, nombre_tabla);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e); // nada que hacer si la db falla inesperadamente
        }
    }

    public boolean usuarioCumpleFormatoMinimo(DTOUsuario usuario) {
        return usuario!=null && usuario.getNombre()!=null && usuario.getNombre().length()>=1 && usuario.getNombre().length()<=50 && usuario.getContrasenia()!=null && usuario.getContrasenia().length()>=1 && usuario.getContrasenia().length()<=50;
    }

    @Override
    public boolean login(DTOUsuario usuario) {
        assert usuarioCumpleFormatoMinimo(usuario) : "login recibio un usuario nulo o con atributos nulos, usuario : "+usuario;

        String sql = "SELECT * FROM USUARIOS WHERE NOMBRE=? AND CONTRASENIA=?";
        try {
            PreparedStatement pstmt = this.c.prepareStatement(sql);
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getContrasenia());
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e); // nada que hacer si la db falla inesperadamente
        }
    }

    @Override
    public boolean register(DTOUsuario usuario) {
        assert usuarioCumpleFormatoMinimo(usuario) : "register recibio un usuario nulo o con atributos nulos, usuario : "+usuario;

        String sql = "INSERT INTO USUARIOS(NOMBRE, CONTRASENIA) VALUES(?,?)";
        try {
            PreparedStatement pstmt = this.c.prepareStatement(sql);
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getContrasenia());
            pstmt.execute();
            return login(usuario);
        } catch (SQLException e) {
            if(e.getErrorCode()==19) // errorCode=19 => SQLITE_CONSTRAINT, no se pudo insertar porque yas habia un usuario con la misma primary key nombre
                return false;
            else
                throw new RuntimeException(e); // nada que hacer si la db falla inesperadamente
        }
    }
}
