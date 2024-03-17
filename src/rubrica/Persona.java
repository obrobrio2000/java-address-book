package rubrica;

public class Persona {
    private String nome;
    private String cognome;
    private String indirizzo;
    private String telefono;
    private int eta;

    public Persona(String nome, String cognome, String indirizzo, String telefono, int eta) {
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.eta = eta;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getEta() {
        return eta;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", eta=" + eta +
                '}';
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Persona persona = (Persona) o;

        if (eta != persona.eta)
            return false;
        if (nome != null ? !nome.equals(persona.nome) : persona.nome != null)
            return false;
        if (cognome != null ? !cognome.equals(persona.cognome) : persona.cognome != null)
            return false;
        if (indirizzo != null ? !indirizzo.equals(persona.indirizzo) : persona.indirizzo != null)
            return false;
        return telefono != null ? telefono.equals(persona.telefono) : persona.telefono == null;
    }

    public int hashCode() {
        int result = nome != null ? nome.hashCode() : 0;
        result = 31 * result + (cognome != null ? cognome.hashCode() : 0);
        result = 31 * result + (indirizzo != null ? indirizzo.hashCode() : 0);
        result = 31 * result + (telefono != null ? telefono.hashCode() : 0);
        result = 31 * result + eta;
        return result;
    }
}
