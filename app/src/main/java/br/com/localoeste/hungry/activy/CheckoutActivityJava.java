package br.com.localoeste.hungry.activy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import br.com.localoeste.hungry.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckoutActivityJava extends AppCompatActivity {
    // 10.0.2.2 is the Android emulator's alias to localhost
   // private static final String BACKEND_URL = "http://192.168.64.2/pagamentos/";
   private static final String BACKEND_URL = "https://localoeste.com.br/pagamentos/";

    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret,valorPago, valorPagoFormatado;
    private Stripe stripe;
    private Double pagamento;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            if (bundle.containsKey("pagamento")){

                pagamento = (Double) bundle.getSerializable("pagamento");
                valorPago = String.valueOf(pagamento);
                DecimalFormat df = new DecimalFormat("00.00");
                valorPagoFormatado = df.format(pagamento);
                valorPagoFormatado = valorPagoFormatado.replaceAll("\\D+","");

            }


        }

        // Configure the SDK with your Stripe publishable key so it can make requests to Stripe
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51Gwr7mDqeS0b1Gcl1BDHP26JbKm3jzbiSPMSbFKTiXVizgtn3qnnoVvDclV9HrKKOgI5lLcQKThxQmfF0cnqeJHX00nUBpqh2K")
        );
        startCheckout();
    }

    private void startCheckout() {
        Log.d("Stripe", "StartChekout");
        // Create a PaymentIntent by calling the server's endpoint.
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
//        String json = "{"
//                + "\"currency\":\" usd \","
//                + "\"items\":["
//                + "{\"id\":\"photo_subscription\"}"
//                + "]"
//                + "}";

        Map<String,Object> payMap=new HashMap<>();
        Map<String,Object> itemMap=new HashMap<>();
        List<Map<String,Object>> itemList =new ArrayList<>();
        payMap.put("currency","BRL");
        itemMap.put("description","Café & Cia");
        itemMap.put("amount",valorPagoFormatado);
        itemList.add(itemMap);
        payMap.put("items",itemList);
        String json = new Gson().toJson(payMap);
        Log.d("Stripe", "onActivityResult");


        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(BACKEND_URL + "create.php")
                .post(body)
                .build();
        httpClient.newCall(request)
                .enqueue(new PayCallback(this));

        // Hook up the pay button to the card widget and stripe instance
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
            CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                stripe.confirmPayment(this, confirmParams);
            }
        });

    }

    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);

        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Stripe", "onActivityResult");
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Log.d("Stripe", "Servidor voltou ok ");
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );

        paymentIntentClientSecret = responseMap.get("clientSecret");
    }

    private static final class PayCallback implements Callback {
        @NonNull private final WeakReference<CheckoutActivityJava> activityRef;

        PayCallback(@NonNull CheckoutActivityJava activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.d("Stripe", "onFailure");
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }

            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error: " + e.toString(), Toast.LENGTH_LONG
                    ).show()
            );
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {

            Log.d("Stripe", "onResponse");
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }

            if (!response.isSuccessful()) {
                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG
                        ).show()
                );
                Log.d("Stripe", response.toString());

            } else {
                activity.onPaymentSuccess(response);
                Log.d("Stripe", response.toString());
            }
        }
    }

    private static final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<CheckoutActivityJava> activityRef;

        PaymentResultCallback(@NonNull CheckoutActivityJava activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            Log.d("Stripe", "onSuccess");
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }

            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                activity.displayAlert(
                        "Payment completed",
                        gson.toJson(paymentIntent)

                );
                Log.d("Stripe", "Sucesso no Pagamento");

            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
                Log.d("Stripe", "Falha no Pagamento");
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            Log.d("Stripe", "Erro no pagamento");
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }
}

