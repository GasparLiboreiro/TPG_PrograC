package modelo.persistencia;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;

public class SistemaCuentasConXML implements DAOUsuarios {
    protected static final String NOMBRE_ARCHIVO_XML = "usersdb.xml";

    private ArrayList<DTOUsuario> getUsuarios() {
        XMLDecoder decoder = null;
        try {
            decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(NOMBRE_ARCHIVO_XML)));
        } catch (FileNotFoundException e) {
            return new ArrayList<DTOUsuario>(); // si no se encontro el archivo, se interpreta como que aún nadie fue registrado
        }
        ArrayList<DTOUsuario> usuarios = (ArrayList<DTOUsuario>) decoder.readObject();
        decoder.close();
        return usuarios;
    }

    @Override
    public boolean login(DTOUsuario usuario) {
        assert usuario!=null : "DAOUsuarios.register recibio usuario=null";
        assert usuario.getNombre()!=null : "DAOUsuarios.register recibio usuario.nombre=null";
        assert usuario.getNombre().length()>0 : "DAOUsuarios.register recibio usuario.nombre=\"\"";
        assert usuario.getContrasenia()!=null : "DAOUsuarios.register recibio usuario.contrasenia=null";
        assert usuario.getContrasenia().length()>0 : "DAOUsuarios.register recibio usuario.contrasenia=\"\"";

        for(DTOUsuario usuario_db : getUsuarios()) {
            if(usuario_db.getNombre().equals(usuario.getNombre()) && usuario_db.getContrasenia().equals(usuario.getContrasenia()))
                return true; // se encontro el usuario en la db
        }
        return false;// no se encontro el usuario en la db
    }

    @Override
    public boolean register(DTOUsuario usuario) {
        assert usuario!=null : "DAOUsuarios.register recibio usuario=null";
        assert usuario.getNombre()!=null : "DAOUsuarios.register recibio usuario.nombre=null";
        assert usuario.getNombre().length()>0 : "DAOUsuarios.register recibio usuario.nombre=\"\"";
        assert usuario.getContrasenia()!=null : "DAOUsuarios.register recibio usuario.contrasenia=null";
        assert usuario.getContrasenia().length()>0 : "DAOUsuarios.register recibio usuario.contrasenia=\"\"";

        ArrayList<DTOUsuario> usuarios = getUsuarios();

        for(DTOUsuario usuario_db : getUsuarios()) {
            if(usuario_db.getNombre().equals(usuario.getNombre()))
                return false; // habia otro con el mismo nombre
        }
        // si salio del for significa que es un usuario valido
        usuarios.add(usuario);

        XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(NOMBRE_ARCHIVO_XML)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e); // si llega a aca es porque no encontro el archivo *Y* no lo pudo crear automaticamente, no hay nada que hacer ni forma de solucionarlo
        }
        encoder.writeObject(usuarios);
        encoder.close();
        return true;
    }
}
