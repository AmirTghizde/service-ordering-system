package com.Maktab101.SpringProject.utils;

import org.apache.commons.codec.digest.DigestUtils;
import java.nio.charset.StandardCharsets;

public class HashUtils {

    public static String hash(String rawPassword) {
        return DigestUtils.sha1Hex(
                rawPassword.getBytes(StandardCharsets.UTF_8)
        );
    }

}
