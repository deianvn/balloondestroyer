package io.github.deianvn.balloondestroyer.utils.helper;

import io.github.deianvn.gameutils.scene.Game;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class CryptHelper {

    private static Base64 base64;

    public static void injectMembers(final Game game) {
        base64 = game.inject(Base64.class);
    }

    public static String sha1(String text) {
        String sha1 = "";

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(text.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }

        return sha1;
    }

    public static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();

        for (byte b : hash) {
            formatter.format("%02x", b);
        }

        String result = formatter.toString();
        formatter.close();

        return result;
    }

    public static String base64Encode(byte[] data) {
        return base64.encode(data);
    }

    public static byte[] base64Decode(String data) {
        return base64.decode(data);
    }

}
