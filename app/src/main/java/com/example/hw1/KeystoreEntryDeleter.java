package com.example.hw1;


import android.util.Log;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class KeystoreEntryDeleter {

    public static boolean deleteEntry( String alias) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        keyStore.deleteEntry(alias);
        Log.d("Deleted ", "Alias: " + alias); // Log or display the aliases
        return true; // Return true on successful deletion (assuming no exception)
    }
}