package rubrica;

public class Utente {
    private String username;
    private String password;

    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Utente utente = (Utente) o;

        if (username != null ? !username.equals(utente.username) : utente.username != null)
            return false;
        return password != null ? password.equals(utente.password) : utente.password == null;
    }

    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (checkPassword(oldPassword))
            password = newPassword;
    }

    public void changeUsername(String newUsername) {
        username = newUsername;
    }
}
