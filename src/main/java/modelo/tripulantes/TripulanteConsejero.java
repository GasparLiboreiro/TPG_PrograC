package modelo.tripulantes;

public class TripulanteConsejero extends TripulanteConcreto {

    private int consejos_dados; // el consejero tiene la mecanica de que gana un plus de plata por cada consejo dado entre liquidaciones de haberes

    public TripulanteConsejero(String Nombre, String identidad, int antiguedad) {
        super(Nombre, identidad, antiguedad);
    }

    @Override
    public String getCargo() {
        return "Consejero";
    }

    @Override
    public String getPlanetaDeOrigen() {
        return "";// no se sabe aca
    }

    public void registrarConsejo()
    {
        this.consejos_dados++;
    }

    @Override
    public double calcularHaberes() {
        double base_oficio = 600;
        double haberes_totales = base_oficio + base_oficio*0.05*this.getAntiguedad() + consejos_dados*2;
        consejos_dados = 0;
        return haberes_totales;
    }
    public String toString(){
        return super.toString()+"  Oficio:'Consejero'";
    }
}
