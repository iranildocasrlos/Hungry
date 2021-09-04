package br.com.localoeste.hungry;


import junit.framework.TestCase;

import br.com.localoeste.hungry.helper.ConfiguracaoFirebase;

public class ConfiguracaoFiresttoreTeste extends TestCase {

    public void testReferenciaFirestore(){

        assertNotNull(ConfiguracaoFirebase.getReferenciaFirestore());
    }


}
