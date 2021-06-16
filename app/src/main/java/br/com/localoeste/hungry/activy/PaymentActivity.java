package br.com.localoeste.hungry.activy;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.localoeste.hungry.R;

public class PaymentActivity extends AppCompatActivity {

    private Button buttonPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        buttonPayment = findViewById(R.id.buttonPayment);


    }


    public void payWithStripe(View view){
        Intent intentStripe = new Intent(PaymentActivity.this, CheckoutActivityJava.class);
        startActivity(intentStripe);
    }


}