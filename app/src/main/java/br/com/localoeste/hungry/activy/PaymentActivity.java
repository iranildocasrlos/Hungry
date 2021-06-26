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
    private Double pagamento;
    private EditText subTotal;
    private EditText total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        subTotal = findViewById(R.id.editSubTotal);
        total = findViewById(R.id.editTotalPayment);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            if (bundle.containsKey("pagamento")){

                pagamento = (Double) bundle.getSerializable("pagamento");
                subTotal.setText(String.valueOf(pagamento));
                total.setText(String.valueOf(pagamento));

            }


        }

        buttonPayment = findViewById(R.id.buttonPayment);


    }


    public void payWithStripe(View view){
        Intent intentStripe = new Intent(PaymentActivity.this, CheckoutActivityJava.class);
        intentStripe.putExtra("pagamento",pagamento);
        startActivity(intentStripe);
    }


}