package com.ruoyi.system.utils;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;


/**
 * @Desc
 * @Author 方糖
 * @Date 2023-06-11 21:49
 **/
public class WxUtils {
    public static final String file = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCwYiY1u+TCL5BX\n" +
            "x3Dov9GSIEhNN/PcoWNNVc3TCHeZZOZnDaMBtwZZ/3gwgHbVi11RPaere51+F5yH\n" +
            "1TbnbbDv9SrLW+QnCGl1vtr44VDBnP5gsHLx3yRlMjOQBUidXe1ajVf0jPPhE5OR\n" +
            "7oNG/5MtC80qquD2fuJy0T68XKbGrTxgzcdelQXAeNVmCC9U/rGWAIwh+zDbcCgw\n" +
            "DBLb91r464j8RU8zQJC33FQ1yramA3g4zlwTastnDauDEadkDOgIziqWhp+Z2mne\n" +
            "+Uz13u3+NBFwbIL1xbYx84ip8qXWyVavuxmOtKwlqhh4bz6C/UAo5cvHTlOERIHm\n" +
            "qpQwm1StAgMBAAECggEBAI7X/d/Yegu1X21nTI6CAiQYVtWKcckFPpl72sOEgO9O\n" +
            "YrWkupWB9vfdyCVndFW+luVbTDW5V7OtRYxP5TYJhJjgcTbeBWiDDH1aApY6Sx9x\n" +
            "mCUP4Rb2ws/dlxqU3NjtH3MNMuUPXday9vjhM5nFExkRDKf4WVXnLlcUFH2Rf1M3\n" +
            "0da6qOXSv8wU+wzj+bY4VMPVlrTyMdVWkNNaWGIM9AzmnF8Q3kQhvt48uPs4tCQ4\n" +
            "J2xCjhwCDeC3fbeqOeMtyd9oFgKEoaLXBqbG7teWSTQHTnmZFRnz8sbhQd9GeQI9\n" +
            "JLfAJbbMi3sevZBLw3lK72JCCG/H9NZ+XHXHLrVMRzUCgYEA4voFnmq0fgJ4TZsp\n" +
            "TLvP6JH5CtfwQnLCg5vtzDBs7rQH07daGX+L50C7vtqlzPH5Eqb3BuFSURMqfQV1\n" +
            "n3Tp09FduZNsSZ6kjfjXx3CbzJ0BVr568Sn2V8N7KLMK9usOIBxDwJ2lDukyDgs/\n" +
            "Q5w4bOPsosRLZON+B5mvN/cYeYsCgYEAxu/645rVrLgKnY5chqoEHilp2wovyaSF\n" +
            "E95R74AXGcQkHjK+Ql49kEDyy095IcP+r/aXdwE3AWW94qDIH56ldEhUsgmsjPD1\n" +
            "IcQtLTi4O6apalQkGH9+mluZ3pbzwnYvabzrOL722/6eK8K2tlKVYSDgx5IyJ5qv\n" +
            "8Dn4S4nPgacCgYEAt+j6EIhJQyjhLZChgClVpw5QCnc5l73A2j7ICYDbYvbAuM2B\n" +
            "1dNBGtmaxmQHn+4YcQKUeiCsQEEVVkPMMK3coOKt3KYVQfmnP9n3jQQzWTGGWjDx\n" +
            "EON9VVNxQWMBaVzAD0iXpUSW72QISNTYKAI2vhRYMvJ2XILwJVxjWyd9z00CgYAg\n" +
            "AmMbFkHFTcOfgFWQ8XBCLiWzMdtolYk4HjCO2cxdv/qTxVRpt2IugNTQM9Row3GK\n" +
            "fKH4awCfv/Ais/4fYFZ89Fp0XywdbXPz3+3o9dRm/LULv8gjDPig+5Yxg5ZrNrsA\n" +
            "R36NneR4l2p53hWqFnWksDaaxgI97xzaCWnTNbRmxwKBgDEWUfxfWv0fWlpyIrnM\n" +
            "xvmTQf1y42JNOsQmkUVoGp185e2cEGNdEZnnnVDiatWa7H4b7mYxfyvzNrnuqNK5\n" +
            "pDc4+ESsmrfF5BFtHMQC8XVm7V7xzQIfwP3U5BfcEqbBYqXBvvgN9RwLOqDQ/nhN\n" +
            "movoIjeSOyOFMHjRAtSFB695\n" +
            "-----END PRIVATE KEY-----\n";

    public static PrivateKey getPrivateKey() throws IOException {
        try {
            String privateKey = file.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }
}
