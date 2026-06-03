package com.example.hw1;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.annotation.RequiresApi;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;
import java.text.DateFormat;
import java.text.ParseException;

import javax.crypto.KeyAgreement;

public class ECDHKA {
    private static final String EC_SPEC = "secp256r1";
    private static final String KEY_PAIR_NAME = "abcdefgh";
    private static final String MAC_ALG = "HMACSHA256";
    private static final String INFO_TAG = "ECDH p-256 AES-256-GCM-SIV\0";
    private static final String KEY_AGREEMENT_ALG = "ECDH";
    private static final String KEY_STORE_PROVIDER = "AndroidKeyStore";

    @RequiresApi(api = Build.VERSION_CODES.S)
    public static KeyPair generateKeys() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, ParseException, ParseException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_EC, KEY_STORE_PROVIDER);
        keyPairGenerator.initialize(
                new KeyGenParameterSpec.Builder(
                        KEY_PAIR_NAME,
                        //KeyProperties.PURPOSE_AGREE_KEY)
                        KeyProperties.PURPOSE_SIGN)
                        .setAlgorithmParameterSpec(new ECGenParameterSpec(EC_SPEC))
                        .setUserAuthenticationRequired(false)

                        .build());
        return keyPairGenerator.generateKeyPair();
    }

    //public static byte[] sharedSecret(PublicKey remote) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
    //    KeyAgreement keyAgreement = KeyAgreement.getInstance(KEY_AGREEMENT_ALG, KEY_STORE_PROVIDER);
    //    //Line 55 here ↓ where error occurs
    //    keyAgreement.init(mine);
    //    keyAgreement.doPhase(remote, true);
    //    return keyAgreement.generateSecret();
    //}
}
