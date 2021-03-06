package com.saahayak.saahayak;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.saahayak.saahayak.Utils.App;
import com.saahayak.saahayak.Utils.MyMVVM;

import java.util.Map;

public class JugaarActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    private Button check_progress;
    private String rzr_payment_Id, rzr_order_Id, rzr_signature;
    private MyMVVM myMVVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugaar);
        myMVVM = ViewModelProviders.of(JugaarActivity.this).get(MyMVVM.class);

    }

    private void ShowDialog() {
        final AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.payment_done, null);
        check_progress = dialogView.findViewById(R.id.check_progress);
        dialogBuilder1.setView(dialogView);
        final AlertDialog alertDialog1 = dialogBuilder1.create();
        alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog1.show();

        check_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();
                Intent intent=new Intent(JugaarActivity.this,JugaarActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        rzr_payment_Id = paymentData.getPaymentId();
        rzr_order_Id = paymentData.getOrderId();
        rzr_signature = paymentData.getSignature();
        myMVVM.PaymentStatus(this, rzr_order_Id, rzr_payment_Id, rzr_signature,App.getSingleton().getBookingId()).observe(this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (map.get("success").toString().equalsIgnoreCase("1")) {
                    ShowDialog();
                }
                Toast.makeText(JugaarActivity.this, map.get("message").toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(this, "Payment not done...", Toast.LENGTH_SHORT).show();
    }
}