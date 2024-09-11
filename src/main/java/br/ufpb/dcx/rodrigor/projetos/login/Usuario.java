package br.ufpb.dcx.rodrigor.projetos.login;

public class Usuario {
    private String login;
    private String nome;
    private String senha;
    private Role role;

    public Usuario(String login, String nome, String senha, Role role) {
        this.login = login;
        this.nome = nome;
        this.senha = senha;
        this.role = role;
    }

    public Usuario() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}