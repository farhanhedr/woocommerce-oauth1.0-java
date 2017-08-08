package authentication;
/**
 * SHA1 SignatureGenerator
 */
import java.util.Base64;
import java.util.Base64.*;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class HMACSha1SignatureService implements SignatureService
{
    private static final String EMPTY_STRING = "";
    private static final String CARRIAGE_RETURN = "\r\n";
    private static final String UTF8 = "UTF-8";
    private static final String HMAC_SHA1 = "HmacSHA1";
    private static final String METHOD = "HMAC-SHA1";

    /**
     * {@inheritDoc}
     */
    public String getSignature(String baseString, String apiSecret, String tokenSecret)
    {
        try
        {
            Preconditions.checkEmptyString(baseString, "Base string cant be null or empty string");
            Preconditions.checkEmptyString(apiSecret, "Api secret cant be null or empty string");
            return doSign(baseString, OAuthEncoder.encode(apiSecret) + '&' + OAuthEncoder.encode(tokenSecret));
        }
        catch (Exception e)
        {
            throw new OAuthSignatureException(baseString, e);
        }
    }

    private String doSign(String toSign, String keyString) throws Exception
    {

//        Logger.getLogger("is it signing","----------------------"+toSign);
//        Logger.getLogger("is 22222222",keyString+"");

        SecretKeySpec key = new SecretKeySpec((keyString).getBytes(UTF8), HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(key);
        byte[] bytes = mac.doFinal(toSign.getBytes(UTF8));
        return bytesToBase64String(bytes).replace(CARRIAGE_RETURN, EMPTY_STRING);
    }

    private String bytesToBase64String(byte[] bytes)
    {
        return Base64.getEncoder().encodeToString(bytes);
        //return Base64.encodeToString(bytes,Base64.NO_WRAP);
    }

    /**
     * {@inheritDoc}
     */
    public String getSignatureMethod()
    {
        return METHOD;
    }
}
