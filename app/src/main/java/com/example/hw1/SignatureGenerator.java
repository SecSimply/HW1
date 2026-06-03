package com.example.hw1;
import android.content.Context;
import android.util.Log;


import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Base64;

import java.security.KeyStore;

public class SignatureGenerator {

    private static final String KEY_ALIAS = "MY_KEY";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    private Context context;

    public SignatureGenerator() {
    }

    public String signString(String inputString) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, IOException, InvalidKeyException, SignatureException, CertificateException, KeyStoreException, UnrecoverableEntryException {
        // Get the KeyStore instance
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        // Check if the key exists
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            throw new RuntimeException("Key with alias " + KEY_ALIAS + " not found in KeyStore");
        }

        // Sign the string within the KeyStore
        byte[] signatureBytes = signWithKeystore(keyStore, inputString);

        //Get public keybytes
        PublicKey publicKey = keyStore.getCertificate(KEY_ALIAS).getPublicKey();
        byte[] encodedPublicKey = publicKey.getEncoded();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String pubk = Base64.getEncoder().encodeToString(encodedPublicKey);
            Log.d("Public Key ", pubk);
        }



        // Encode the signature bytes as a Base64 string
        String output = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            output = Base64.getEncoder().encodeToString(signatureBytes);
        }
        return output;
    }

    private byte[] signWithKeystore(KeyStore keyStore, String inputString) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException, SignatureException, UnrecoverableEntryException, KeyStoreException {
        // Get a Signature object for signing within KeyStore
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
        // Initialize the signature object without the private key (remains in KeyStore)
        //signature.initSign(privateKeyEntry.getCertificateChain());
        signature.initSign(privateKeyEntry.getPrivateKey());
        // Update the signature with the input string
        signature.update(inputString.getBytes());

        // Generate the signature bytes within KeyStore
        return signature.sign();
    }
}
