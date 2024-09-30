package br.ufpb.dcx.rodrigor.projetos.participante.model;

import org.bson.types.ObjectId;

public class Participante {
    private ObjectId id;
    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    private CategoriaParticipante categoria;
    private static final int MAX_NAME_LENGTH = 320;

    // Getters and Setters

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.length() > MAX_NAME_LENGTH)
            throw new IllegalArgumentException(
                    "Tamanho do nome invalido, o nome nao pode ser Null e deve ter no maximo" + MAX_NAME_LENGTH
                            + " caracteres.");
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        if (sobrenome == null || sobrenome.length() > MAX_NAME_LENGTH)
            throw new IllegalArgumentException(
                    "Tamanho do sobrenome invalido, o sobrenome nao pode ser Null e deve ter no maximo"
                            + MAX_NAME_LENGTH
                            + " caracteres.");
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (email == null || email.length() > MAX_NAME_LENGTH)
            throw new IllegalArgumentException(
                    "Tamanho do email invalido, o email nao pode ser Null e deve ter no maximo" + MAX_NAME_LENGTH
                            + " caracteres.");
        if (!email.matches(emailRegex))
            throw new IllegalArgumentException(
                    "Formato do email invalido");
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        String telefoneRegex = "\\d+";

        if (!telefone.matches(telefoneRegex))
            throw new IllegalArgumentException("Telefone deve conter apenas números.");

        if (telefone.length() < 8 || telefone.length() > 15)
            throw new IllegalArgumentException("Telefone deve ter entre 8 e 15 dígitos.");

        this.telefone = telefone;
    }

    public CategoriaParticipante getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaParticipante categoriaParticipante) {
        this.categoria = categoriaParticipante;
    }

    @Override
    public String toString() {
        return "Participante{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", categoria=" + categoria +
                '}';
    }
}