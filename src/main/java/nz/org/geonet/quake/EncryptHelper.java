package nz.org.geonet.quake;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.*;
import java.util.Properties;

/**
 * Created by baishan on 21/04/15.
 */
public class EncryptHelper {
    private String encryptKey;
    /**
     * Decrypt a string
     */
    public String decode(String s) {
        return new String(xorWithKey(base64Decode(s), encryptKey.getBytes()));
    }

    private static byte[] xorWithKey(byte[] a, byte[] key) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ key[i % key.length]);
        }
        return out;
    }

    private static byte[] base64Decode(String s) {
        try {
            return Base64.decode(s);
        } catch (Base64DecodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encrypt a string
     *
     * @return The encrypted string
     */
    public String encode(String s) {
        return base64Encode(xorWithKey(s.getBytes(), encryptKey.getBytes()));
    }

    private String base64Encode(byte[] bytes) {
        return new String(Base64.encode(bytes));
    }

    /**
     * print out the encrypted strings for use in AwsClientmanager
     */
    public void testCrypt() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("aws.properties"));
        } catch (IOException io) {
            io.printStackTrace();
        }
        encryptKey = prop.getProperty("encrypt-key");
        String awsAccountId = prop.getProperty("aws-account-id");
        String identityPoolId = prop.getProperty("identity-pool-id");
        String authRoleArn = prop.getProperty("auth-role-arn");
        String sqsQuakeName = prop.getProperty("sqs-queue-name");
        String facebookAppId = prop.getProperty("facebook-app-id");
        String googleClientId = prop.getProperty("google-client-id");

        String awsAccountIdEncode = encode(awsAccountId);
        String identityPoolIdEncode = encode(identityPoolId);
        String authRoleArnEncode = encode(authRoleArn);
        String sqsQuakeNameEncode = encode(sqsQuakeName);
        String facebookAppIdEncode = encode(facebookAppId);
        String googleClientIdEncode = encode(googleClientId);

        String awsAccountIdDncode = decode(awsAccountIdEncode);
        String identityPoolIdDncode = decode(identityPoolIdEncode);
        String authRoleArnDncode = decode(authRoleArnEncode);
        String sqsQuakeNameDncode = decode(sqsQuakeNameEncode);
        String facebookAppIdDncode = decode(facebookAppIdEncode);
        String googleClientIdDncode = decode(googleClientIdEncode);
        System.out.println("--------------------------------------------------------");
        System.out.println("###### DECRYPTED STRINGS ######");
        System.out.println("awsAccountIdDncode " + awsAccountIdDncode);
        System.out.println("identityPoolIdDncode " + identityPoolIdDncode);
        System.out.println("authRoleArnDncode " + authRoleArnDncode);
        System.out.println("sqsQuakeNameDncode " + sqsQuakeNameDncode);
        System.out.println("facebookAppIdDncode " + facebookAppIdDncode);
        System.out.println("googleClientIdDncode " + googleClientIdDncode);

        System.out.println("--------------------------------------------------------");
        System.out.println("###### ENCRYPTED STRINGS ######");

        System.out.println("private static final String AWS_ACCOUNT_ID = " + awsAccountIdEncode);
        System.out.println("private static final String AWS_IDENTITY_POOL_ID = " + identityPoolIdEncode);
        System.out.println("private static final String AWS_AUTH_ROLE_ARN = " + authRoleArnEncode);
        System.out.println("private static final String SQS_QUEUE_NAME = " + sqsQuakeNameEncode);
        System.out.println("private static final String FACEBOOK_APP_ID = " + facebookAppIdEncode);
        System.out.println("private static final String GOOGLE_SERVICE_CLIENT_ID = " + googleClientIdEncode);
        System.out.println("--------------------------------------------------------");

    }

    public static void main(String[] args) {
        new EncryptHelper().testCrypt();
    }
}
