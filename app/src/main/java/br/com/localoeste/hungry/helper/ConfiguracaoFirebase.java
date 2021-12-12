package br.com.localoeste.hungry.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    private static StorageReference referenciaStorage;
    private static FirebaseAuth    referenciaAutenticacao;
    private static FirebaseFirestore referenciaFirestore;
    private static DatabaseReference database;
    private static FirebaseAuth auth;

    //retorna a instancia do FirebaseDatabase
    public static DatabaseReference getFirebaseDatabase(){

        if ( database == null ){
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }


    /// Retorna a instancia do Firestore
    public static  FirebaseFirestore getReferenciaFirestore(){
        if (referenciaFirestore == null){
            referenciaFirestore = FirebaseFirestore.getInstance();
        }
        return referenciaFirestore;
    }

   ///retorna a instancia Instancia da autenticação
    public static FirebaseAuth getReferenciaAutenticacao(){
        if (referenciaAutenticacao == null){
            referenciaAutenticacao = FirebaseAuth.getInstance();
        }
        return referenciaAutenticacao;
    }

    //Retorna instancia do FirebaseStorage
    public static StorageReference getFirebaseStorage(){
        if( referenciaStorage == null ){
            referenciaStorage = FirebaseStorage.getInstance().getReference();
        }
        return referenciaStorage;
    }

    //retorna a instancia do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){

        if( auth == null ){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }



}
