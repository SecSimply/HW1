package com.example.hw1;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast; // Import Toast for user feedback

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.text.ParseException; // Keep if ECDHKA.generateKeys() can throw it
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncDecActivity extends AppCompatActivity implements View.OnClickListener { // Implement OnClickListener

    private EnCryptor encryptor;
    // private DeCryptor decryptor; // Not used in the current button actions
    private static final String TAG = EncDecActivity.class.getSimpleName(); // Corrected TAG
    private static final String SAMPLE_ALIAS = "MY_AES_KEY";

    private EditText inputText;
    private TextView outputText; // Simplified from TextView[]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enc_dec);

        inputText = findViewById(R.id.input_text_1);
        outputText = findViewById(R.id.output_text); // Assign directly

        // Find buttons
        Button buttonOne = findViewById(R.id.button_1);
        Button buttonTwo = findViewById(R.id.button_2);
        Button buttonThree = findViewById(R.id.button_3);
        Button buttonFour = findViewById(R.id.button_4);
        Button buttonFive = findViewById(R.id.button_5);
        Button buttonSix = findViewById(R.id.button_6);

        // Set click listeners
        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);
        buttonThree.setOnClickListener(this);
        buttonFour.setOnClickListener(this);
        buttonFive.setOnClickListener(this);
        buttonSix.setOnClickListener(this);

        encryptor = new EnCryptor();
        // decryptor = new DeCryptor(); // Initialize if needed elsewhere
    }

    @RequiresApi(api = Build.VERSION_CODES.S) // Keep for encryptText if it requires S
    @Override
    public void onClick(View v) {
        // Use a switch statement for cleaner handling of multiple button clicks
        int viewId = v.getId();
        if (viewId == R.id.button_1) {
            handleEncryptAndEccKeyGen();
        } else if (viewId == R.id.button_2) {
            handleRsaKeyGen();
        } else if (viewId == R.id.button_3) {
            handleSignData();
        } else if (viewId == R.id.button_4) {
            handleButtonFour();
        } else if (viewId == R.id.button_5) {
            handleListAliases();
        } else if (viewId == R.id.button_6) {
            handleDeleteEntry();
        }
    }

    // --- Helper methods for each button's action ---

    @RequiresApi(api = Build.VERSION_CODES.S) // Keep if encryptText requires S
    private void handleEncryptAndEccKeyGen() {
        String input1 = inputText.getText().toString();
        if (input1.isEmpty()) {
            Toast.makeText(this, "Please enter text to encrypt", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Encryption
            // Consider initializing encryptor here if it's lightweight and stateful per operation
            // or ensure it's thread-safe if initialized once in onCreate
            String encryptedText = String.valueOf(encryptor.encryptText(SAMPLE_ALIAS, input1));
            outputText.setText(encryptedText);
            Log.d(TAG, "Encrypted Text: " + encryptedText);

            // ECC Key Generation
            KeyPair eccKeyPair = ECDHKA.generateKeys();
            Log.d(TAG, "ECC Public Key: " + (eccKeyPair != null ? eccKeyPair.getPublic().toString() : "null"));

        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | KeyStoreException |
                 NoSuchProviderException | NoSuchPaddingException | InvalidKeyException |
                 IOException | InvalidAlgorithmParameterException | SignatureException |
                 BadPaddingException | IllegalBlockSizeException | ParseException e) { // Combined catch for ParseException
            Log.e(TAG, "Error in encryption or ECC key gen: " + e.getMessage(), e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void handleRsaKeyGen() {
        try {
            KeyPairGeneratorHelper.generateRsaKeyPair();
            Log.d(TAG, "RSA Key pair generated successfully (Button 2).");
            Toast.makeText(this, "RSA Key pair generated", Toast.LENGTH_SHORT).show();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            Log.e(TAG, "Error generating RSA key pair: " + e.getMessage(), e);
            Toast.makeText(this, "Error generating RSA keys: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void handleSignData() {
        try {
            SignatureGenerator signatureGenerator = new SignatureGenerator(); // Consider if this can be a member variable
            String messageToSign = "The message to be signed"; // Or get from inputText
            String signature = signatureGenerator.signString(messageToSign);
            Log.d(TAG, "Signature: " + signature);
            outputText.setText("Signature: " + signature); // Display signature
            Toast.makeText(this, "Data signed successfully", Toast.LENGTH_SHORT).show();
        } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 IOException | InvalidKeyException | SignatureException | UnrecoverableEntryException |
                 CertificateException | KeyStoreException e) {
            Log.e(TAG, "Error signing data: " + e.getMessage(), e);
            Toast.makeText(this, "Error signing data: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void handleButtonFour() {
        Log.d(TAG, "Button 4 clicked. Performing system checks.");
        Toast.makeText(this, "Checking system settings... See Logcat for results.", Toast.LENGTH_SHORT).show();

        // Call the static method from the CheckSystem class
        CheckSystem.performSystemChecks(this);
        CheckPackages.logInstalledPackagesAndPermissions(this);


        outputText.setText("System checks initiated. See Logcat (tag: CheckSystem).");
    }

    private void handleListAliases() {
        try {
            List<String> aliases = KeystoreAliasReader.getAllAliases();
            StringBuilder aliasListBuilder = new StringBuilder("Keystore Aliases:\n");
            if (aliases.isEmpty()) {
                aliasListBuilder.append("No aliases found.");
            } else {
                for (String alias : aliases) {
                    Log.d(TAG, "Keystore Alias: " + alias);
                    aliasListBuilder.append(alias).append("\n");
                }
            }
            outputText.setText(aliasListBuilder.toString());
            Toast.makeText(this, "Listed aliases", Toast.LENGTH_SHORT).show();
        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException e) {
            Log.e(TAG, "Error accessing keystore to list aliases: " + e.getMessage(), e);
            Toast.makeText(this, "Error listing aliases: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void handleDeleteEntry() {
        String aliasToDelete = inputText.getText().toString();
        if (aliasToDelete.isEmpty()) {
            Toast.makeText(this, "Please enter an alias to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            boolean deleted = KeystoreEntryDeleter.deleteEntry(aliasToDelete);
            if (deleted) {
                Log.d(TAG, "Keystore entry deleted successfully: " + aliasToDelete);
                outputText.setText("Deleted alias: " + aliasToDelete);
                Toast.makeText(this, "Alias '" + aliasToDelete + "' deleted", Toast.LENGTH_SHORT).show();
            } else {
                Log.w(TAG, "Failed to delete keystore entry or entry did not exist: " + aliasToDelete);
                outputText.setText("Could not delete alias: " + aliasToDelete);
                Toast.makeText(this, "Could not delete alias '" + aliasToDelete + "'", Toast.LENGTH_SHORT).show();
            }
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            Log.e(TAG, "Error deleting keystore entry '" + aliasToDelete + "': " + e.getMessage(), e);
            Toast.makeText(this, "Error deleting alias: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
