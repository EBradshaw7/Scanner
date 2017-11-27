package com.example.eoghan.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AdminAreaActivity extends AppCompatActivity implements View.OnClickListener  {

    private TextView contentTxt;
    private EditText quantityET, productET;
    private FirebaseAuth firebaseAuth;
    private Button buttonLogout, buttonSubmit;
    private DatabaseReference databaseReference;
    String scanContent, quantContent, nameContent;
    //String scanFormat;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_area);

        quantityET = (EditText) findViewById(R.id.quantET);
        contentTxt = (TextView) findViewById(R.id.scan_content);
        productET = (EditText) findViewById(R.id.productET);
        buttonLogout = (Button) findViewById(R.id.logout);
        buttonSubmit = (Button) findViewById(R.id.submit);


        databaseReference = FirebaseDatabase.getInstance().getReference();

        getSupportActionBar().setTitle("Retail Integration DB Management");
        firebaseAuth = FirebaseAuth.getInstance();


        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }



        //FirebaseUser user = firebaseAuth.getCurrentUser();
    }

    public void scanNow(View View) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setResultDisplayDuration(0);
        integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();

    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            scanContent = scanningResult.getContents();
            //scanFormat = scanningResult.getFormatName();

            // display it on screen
            // formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("Code: " + scanContent);



        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (v == buttonSubmit) {
            submit();
        }
    }

    private void submit() {
        databaseReference.child("Products").child(scanContent);
        databaseReference.child("Products").child(scanContent).child("Code").setValue(scanContent);



        quantContent = quantityET.getText().toString();
        nameContent = productET.getText().toString();

        databaseReference.child("Products").child(scanContent).child("Name").setValue(nameContent);
        databaseReference.child("Products").child(scanContent).child("Quantity").setValue(quantContent);

        Toast.makeText(this, scanContent + " has been added"  , Toast.LENGTH_LONG).show();

    }
}