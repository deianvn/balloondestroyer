package io.github.deianvn.balloondestroyer.utils.helper;

public interface Base64 {

    String encode(byte[] data);

    byte[] decode(String data);

}
