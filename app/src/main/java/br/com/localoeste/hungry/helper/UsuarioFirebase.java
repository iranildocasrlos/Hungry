package br.com.localoeste.hungry.helper;

import android.location.Address;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.com.localoeste.hungry.model.Usuario;


public class UsuarioFirebase{
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore referenciaFirestore;

    private static String idUsuario,nome,email,endereco,telefone,cep,cpf,tipoUsuario,cidade, bairro;

    public UsuarioFirebase() {
    }

    public static Usuario getDadosUsuarioLogado() {
        FirebaseUser firebaseUser = getUsuarioAtual();

        Usuario usuario = new Usuario();
        //usuario.setId( firebaseUser.getUid() );
        //usuario.setEmail( firebaseUser.getEmail() );
        //usuario.setNome( firebaseUser.getDisplayName() );
        Address addressDestino = null;

        usuario.setIdUsuario(idUsuario);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setEndereco(endereco);
        usuario.setTelefone(telefone);
        usuario.setBairro(bairro);
        usuario.setCep(cep);
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setCidade(cidade);


        return usuario;
    }



//++++++++++= Geters e Seters ++++++++++++++++++++++++++++++


    public  String getIdUsuario() {
        return idUsuario;
    }

    public  void setIdUsuario(String idUsuario) {
        UsuarioFirebase.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public  void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public  void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public  String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public  String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        UsuarioFirebase.tipoUsuario = tipoUsuario;
    }

    public  String getCep() {
        return cep;
    }

    public  void setCep(String cep) {
        UsuarioFirebase.cep = cep;
    }

    public static String getCidade() {  return cidade;    }

    public static void setCidade(String cidade) { UsuarioFirebase.cidade = cidade; }

    public static String getBairro() { return bairro; }

    public static void setBairro(String bairro) { UsuarioFirebase.bairro = bairro; }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    ///Retorna o id do Usuario logado
    public static String getId_Usuario(){
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
        return autenticacao.getCurrentUser().getUid();
    }

    ///Retorna o nome do Usuario logado
    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getReferenciaAutenticacao();
        return usuario.getCurrentUser();
    }


    ///Salva os dados no Firestore
    public void salvarDados(){
        DocumentReference documentRef = db.collection("usuarios").document(getId_Usuario());
        Map<String, Object> data = new HashMap<>();
        data.put("nome", nome);
        data.put("email", email);
        data.put("endereco", endereco);
        data.put("telefone",telefone);
        data.put("cpf", cpf);
        data.put("tipoUsuario", tipoUsuario);
        documentRef.set(data);



    }

    public void atualizarDados(){
        DocumentReference documentRef = db.collection("usuarios").document(idUsuario);
        Map<String, Object> data = new HashMap<>();

        data.put("nome", nome);
        data.put("email", email);
        data.put("endereco", endereco);
        data.put("telefone",telefone);
        data.put("cpf", cpf);
        data.put("cep", cep);
        documentRef.update(data);



    }
    public static void atualizarDadosLocalizacao(double lat, double lon){

        //Define nó de local de usuário
        DatabaseReference localUsuario = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("local_usuario");
        GeoFire geoFire = new GeoFire(localUsuario);

        //Recupera dados usuário logado
        Usuario usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        //Configura localização do usuário
        geoFire.setLocation(
                usuarioLogado.getIdUsuario(),
                new GeoLocation(lat, lon),
                new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if( error != null ){
                            Log.d("Erro", "Erro ao salvar local!");
                        }
                    }
                }
        );

    }




}
