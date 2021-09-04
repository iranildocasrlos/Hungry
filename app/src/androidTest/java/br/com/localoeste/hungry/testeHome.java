package br.com.localoeste.hungry;

import junit.framework.TestCase;

import br.com.localoeste.hungry.activy.HomeActivity;
import br.com.localoeste.hungry.model.Usuario;

public class testeHome extends TestCase {



    public void testPegouLocalizacao(){
        HomeActivity home = new HomeActivity();
       assertNotNull(home.recuperarLocalizacaoUsuario());
    }

    public void testGeoCoder(){
        HomeActivity home = new HomeActivity();
        assertNotNull(home.recuperarEndereco("Rua pedro Bolgione, 116"));


    }

    public void testLocalUsuario(){
        HomeActivity home = new HomeActivity();
        assertNotNull(home.recuperarLocalizacaoUsuario());

    }

}
