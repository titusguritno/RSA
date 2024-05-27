
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;

public class RSA {

    private BigInteger n;
    private BigInteger d;
    private BigInteger e;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public static class PublicKey {

        private final BigInteger e;
        private final BigInteger n;

        public PublicKey(BigInteger e, BigInteger n) {
            this.e = e;
            this.n = n;
        }

        public BigInteger getE() {
            return e;
        }

        public BigInteger getN() {
            return n;
        }
    }

    public static class PrivateKey {

        private final BigInteger d;
        private final BigInteger n;

        public PrivateKey(BigInteger d, BigInteger n) {
            this.d = d;
            this.n = n;
        }

        public BigInteger getD() {
            return d;
        }

        public BigInteger getN() {
            return n;
        }
    }

    public RSA() {
        generateKeyPair(3072);
    }

    public RSA(int bits) {
        generateKeyPair(bits);
    }

    public final void generateKeyPair(int bits) {
        SecureRandom random = new SecureRandom();
        BigInteger p = new BigInteger(bits / 2, 100, random);
        BigInteger q = new BigInteger(bits / 2, 100, random);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        n = p.multiply(q);
        e = new BigInteger("3");

        while (phi.gcd(e).intValue() > 1) {
            e = e.add(new BigInteger("2"));
        }

        d = e.modInverse(phi);
        publicKey = new PublicKey(e, n);
        privateKey = new PrivateKey(d, n);
    }

    public String encrypt(String plainData, PublicKey publicKey) {
        byte[] plainBytes = plainData.getBytes();
        BigInteger message = new BigInteger(plainBytes);
        BigInteger encrypted = message.modPow(publicKey.getE(), publicKey.getN());
        return Base64.getEncoder().encodeToString(encrypted.toByteArray());
    }

    public String decrypt(String encryptedData) {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        BigInteger encrypted = new BigInteger(encryptedBytes);
        BigInteger decrypted = encrypted.modPow(d, n);
        return new String(decrypted.toByteArray());
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
