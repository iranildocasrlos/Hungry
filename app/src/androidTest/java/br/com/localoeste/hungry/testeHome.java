package br.com.localoeste.hungry;

import junit.framework.TestCase;

import br.com.localoeste.hungry.activy.HomeActivity;
import br.com.localoeste.hungry.model.Usuario;

public class testeHome extends TestCase {
    

    public void testLocalUsuario(){
        HomeActivity home = new HomeActivity();
        home.recuperarLocalizacaoUsuario();
        assertNotNull(home.localUsuario);

    }

}
