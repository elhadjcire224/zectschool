package com.zectschool.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Elhadj-Cire-Bah
 */
public class MD5 {

    public static String hash(String brutePwd) {
        try {
            // Créer une instance de MessageDigest avec l'algorithme MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Calculer le hachage du mot de passe
            byte[] messageDigest = md.digest(brutePwd.getBytes());

            // Convertir le tableau de bytes en représentation hexadécimale
            BigInteger no = new BigInteger(1, messageDigest);
            String hashedPwd = no.toString(16);

            // S'assurer que la représentation hexadécimale a une longueur de 32 caractères
            while (hashedPwd.length() < 32) {
                hashedPwd = "0" + hashedPwd;
            }

            return hashedPwd;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }


}
