package br.com.localoeste.hungry.activy;

public class requisicaoFirebase {

    private String email;
    private String endereco;

    private requisicaoFirebase(){}

    private requisicaoFirebase(String email,String endereco){
        this.email = email;
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
