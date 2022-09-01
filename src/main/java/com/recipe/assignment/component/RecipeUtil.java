package com.recipe.assignment.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Slf4j
@Component
public class RecipeUtil {
    
    @Value("${recipe.encryption.algorithm:AES}")
    private String AES;

    // TODO: need to put secret in secure store
    @Value("${recipe.encryption.secret:secret-key-12345}")
    private String SECRET;


    /**
     * generate the recipeKey with combination of recipeName and randomGeneratedString
     * Example:
     * RecipeName: tomatoRice -> substring of name = tom+"_"+generated_string
     *
     * @param recipeName : recipeName  which we get in request to generate recipeKey
     * @return :generated recipeKey
     */
    public String RecipeKeyGenerator(String recipeName) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String randomGeneratedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        log.info("RecipeKeyGenerator: recipeKey is generated");
        return recipeName.substring(0, 3) + "_" + randomGeneratedString;
    }

    /**
     * Encrypt the recipeKey to send in api response
     * Encryption is based on AES Algorithm and secret from config
     *
     * @param recipeKey recipeKey to be encrypted [to send in apiResponse]
     * @return :Encrypted recipeKey
     */
    public String EncryptRecipeKey(String recipeKey) {
        try {
            Key key = new SecretKeySpec(SECRET.getBytes(), AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bt = Base64.encodeBase64URLSafe(cipher.doFinal(recipeKey.getBytes(StandardCharsets.UTF_8)));
            log.info("EncryptRecipeKey: recipeKey is encrypted");
            return new String(bt, "UTF-8");
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.error("EncryptRecipeKey: unable to Encrypt the recipeKey:{} throw IllegalStateException", recipeKey);
            throw new IllegalStateException(e);
        }
    }

    /**
     * Decrypt the recipeKey to use in db operation
     * Decryption is based on AES Algorithm and secret from config
     *
     * @param encryptRecipeKey encryptRecipeKey to be decrypted [to use in DB operation]
     * @return :Decrypted recipeKey
     */
    public String DecryptRecipeKey(String encryptRecipeKey) {
        try {
            Key key = new SecretKeySpec(SECRET.getBytes(), AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] bt = cipher.doFinal(Base64.decodeBase64URLSafe(encryptRecipeKey));
            log.info("DecryptRecipeKey: recipeKey is decrypted");
            return new String(bt, "UTF-8");
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.error("EncryptRecipeKey: unable to Decrypt the recipeKey:{} throw IllegalStateException", encryptRecipeKey);
            throw new IllegalStateException(e);
        }
    }
}
