package com.vertium.arboles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private Button btScan, button, btLimpiar;
    private TextView mTextViewData;
    private TextView Cientifico;
    private TextView AMax;
    private TextView Ori;
    private TextView Link;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewData = (TextView) findViewById(R.id.etResultado);
        Cientifico = (TextView) findViewById(R.id.Ncientifico);
        AMax = (TextView) findViewById(R.id.AlturaMaxima);
        Ori = (TextView) findViewById(R.id.Origen);
        Link = (TextView) findViewById(R.id.Imagen);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btScan = (Button) findViewById(R.id.btScan);
        button = (Button) findViewById(R.id.button);
        btLimpiar = (Button) findViewById(R.id.btLimpiar);
        //holaaaa


        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrador = new IntentIntegrator(MainActivity.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Lector - CDP");
                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();

            }
        });

        btLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewData.setText("");
                Cientifico.setText("");
                AMax.setText("");
                Ori.setText("");
                Link.setText("");

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //mDatabase.child("Pino").addValueEventListener(new ValueEventListener() {
                    mDatabase.child(mTextViewData.getText().toString()).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            String Nombre = dataSnapshot.child("Nombre").getValue().toString();
                            String Ncientifico = dataSnapshot.child("Ncientifico").getValue().toString();
                            String AlturaMaxima = dataSnapshot.child("AlturaMaxima").getValue().toString();
                            String Origen = dataSnapshot.child("Origen").getValue().toString();
                            String Imagen = dataSnapshot.child("Imagen").getValue().toString();
                            mTextViewData.setText("El nombre es: " + Nombre);
                            Cientifico.setText("El nombre cientifico es: " + Ncientifico);
                            AMax.setText("El altura max es: " + AlturaMaxima);
                            Ori.setText("Su origen es: " + Origen);
                            Link.setText("Imagenes Arbol:"+ "\n" + Imagen);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Lectura cancelada", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                mTextViewData.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

