package com.trimble.fsm.fieldmaster.service.common;

import android.os.Build;
import android.provider.Settings;

import com.trimble.fsm.fieldmaster.service.ApplicationContextProvider;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Util {
    private static SecretKey key = null;

    public static SecretKey generateKey() {
        String passPhrase = generatePassPhrase();
        if (key == null) {
            try {
                key = createKey(passPhrase.toCharArray());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
        return key;
    }

    public static SecretKey createKey(char[] passphraseOrPin) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Number of PBKDF2 hardening rounds to use. Larger values increase
        // computation time. You should select a value that causes computation
        // to take >100ms.
        final int iterations = 1000;
        // Generate a 256-bit key
        final int outputKeyLength = 256;
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(passphraseOrPin, Settings.Secure.getString(ApplicationContextProvider.getContext().getContentResolver(), Settings.Secure.ANDROID_ID).getBytes(), iterations, outputKeyLength);
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        return secretKey;
    }

    public static String generatePassPhrase() {
        //String input = "packageName";//TODO
        /*String input = Build.BOARD + Build.BRAND + Build.CPU_ABI + Build.DEVICE +
        Build.DISPLAY + Build.FINGERPRINT + Build.HOST + Build.ID + Build.MANUFACTURER
                + Build.MODEL + Build.PRODUCT + Build.TAGS + Build.TYPE + Build.USER;*/
        String input = Build.CPU_ABI + Build.DEVICE;
        char[] key = {'P', 'R', 'I', 'V', 'A', 'T', 'E'}; //Can be any chars, and any length array
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            output.append((char) (input.charAt(i) ^ key[i % key.length]));
        }
        return output.toString();
    }
}
