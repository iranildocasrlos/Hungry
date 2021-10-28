package br.com.localoeste.hungry.activy;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.localoeste.hungry.R;

public class PaymentActivity extends AppCompatActivity {

    private Button buttonPayment;
    private Double pagamento,frete;
    private EditText subTotal;
    private EditText total;
    private EditText txtFrete, disconto;
    private String idPedido;
    private String idEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        subTotal = findViewById(R.id.editSubTotal);
        total = findViewById(R.id.editTotalPayment);
        txtFrete =  findViewById(R.id.editFrete);
        disconto = findViewById(R.id.editDiscount);

        subTotal.setEnabled(false);
        total.setEnabled(false);
        txtFrete.setEnabled(false);
        disconto.setEnabled(false);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            if (bundle.containsKey("pagamento")){

                pagamento = (Double) bundle.getSerializable("pagamento");
                frete = (Double) bundle.getSerializable("frete");
                subTotal.setText(String.valueOf((pagamento-frete)));
                txtFrete.setText(String.valueOf(frete));
                total.setText(String.valueOf(pagamento));

            }
            if (bundle.containsKey("idPedido")){
                idPedido = (String) bundle.getSerializable("idPedido");

            }
            if (bundle.containsKey("idEmpresa")){
                idEmpresa = (String) bundle.getSerializable("idEmpresa");

            }


        }

        buttonPayment = findViewById(R.id.buttonPayment);


    }


    public void payWithStripe(View view){
        Intent intentStripe = new Intent(PaymentActivity.this, CheckoutActivityJava.class);
        intentStripe.putExtra("pagamento",pagamento);
        intentStripe.putExtra("idPedido",idPedido);
        intentStripe.putExtra("idEmpresa",idEmpresa);
        startActivity(intentStripe);
        finish();
    }


}