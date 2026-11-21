package modelo;

import modelo.persistencia.DAOUsuarios;
import modelo.persistencia.DTOUsuario;

public class Sistema {
    private Asistente asistente;
    private DAOUsuarios sistemaUsuarios;
    private String nombre_usuario;

    /**
     * crea un Sistema sin asistente, a la espera de un inicio de secion satisfactorio
     * <b>Pre:</b> sistemaUsuarios debe ser no nulo
     * <b>Post:</b> todos los metodos del sistema quedan bloqueados hasta un inicio de secion satisfactorio
     * @param sistemaUsuarios DAO al que consultar por el inicio de secion
     */
    public Sistema(DAOUsuarios sistemaUsuarios)
    {
        assert sistemaUsuarios!=null : "constructor Sistema recibio un sistemaUsuarios==null";
        this.asistente = null;
        this.sistemaUsuarios = sistemaUsuarios;
        this.nombre_usuario = null;
    }

    /**
     * Intenta iniciar secion con el nombre y contraseña proveidos
     * <b>Pre:</b> nombre y apellido deben ser no nulos de longitud mayor a 0 y menor o igual a 50
     * @param nombre nombre de la cuenta
     * @param contrasenia contrasenia de la cuenta
     * @return true si se inicio secion satisfactoriamente false si no
     */
    public boolean iniciar_secion(String nombre, String contrasenia) {
        if(sistemaUsuarios.login(new DTOUsuario(nombre, contrasenia))){
            this.nombre_usuario = new String(nombre);
            return true;
        }
        else
            return false;
    }

    /**
     * Intenta registrar un nuevo usuario con el nombre y contraseña proveidos, si se pudo, tambien inicia secion con la cuenta creada
     * <b>Pre:</b> nombre y apellido deben ser no nulos de longitud mayor a 0 y menor o igual a 50
     * @param nombre nombre de la cuenta a crear
     * @param contrasenia contrasenia de la cuenta a crear
     * @return true si se creo la cuenta satisfactoriamente y se inicio secion false si no
     */
    public boolean registrar_usuario(String nombre, String contrasenia) {
        if(sistemaUsuarios.register(new DTOUsuario(nombre, contrasenia))){
            this.nombre_usuario = new String(nombre);
            return true;
        }
        else
            return false;
    }

    /**
     * modifica la referencia interna del asistente por el asistente proveido
     * <b>Pre:</b> se debio haber iniciado secion previamente
     */
    public void setAsistente(Asistente asistente){
        assert nombre_usuario != null : "se llamo a setAsistente de Sistema sin haber iniciado secion";
        this.asistente = asistente;
    }

    /**
     * retorna la referencia a asistente almacenada
     * <b>Pre:</b> se debio haber iniciado secion previamente
     */
    public Asistente getAsistente(){
        assert nombre_usuario != null : "se llamo a getAsistente de Sistema sin haber iniciado secion";
        return this.asistente;
    }
}
