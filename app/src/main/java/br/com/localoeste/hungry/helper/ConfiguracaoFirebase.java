package br.com.localoeste.hungry.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    private static DatabaseReference referenciaFirebase;
    private static StorageReference referenciaStorage;
    private static FirebaseAuth    referenciaAutenticacao;
    private static FirebaseFirestore referenciaFirestore;




    //retorna a referencia do database
    public static DatabaseReference getFirebase(){
        if( referenciaFirebase == null ){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
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





}
