package org.openactive.PomReporter.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Base64;

/**
 * Created by mohadib on 2/5/17.
 */
public class EncyrptionUtil
{
  public String encrypt(String property, char[] secret, byte[] salt) throws GeneralSecurityException, UnsupportedEncodingException
  {
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    SecretKey key = keyFactory.generateSecret(new PBEKeySpec(secret));
    Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
    pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(salt, 20));
    return Base64.getEncoder().encodeToString( pbeCipher.doFinal(property.getBytes("UTF-8")) );
  }

  public String decrypt(String property, char[] secret, byte[] salt) throws GeneralSecurityException, IOException
  {
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    SecretKey key = keyFactory.generateSecret(new PBEKeySpec(secret));
    Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
    pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(salt, 20));
    return new String(pbeCipher.doFinal(Base64.getDecoder().decode(property)), "UTF-8");
  }
}
