import java.lang.management.BufferPoolMXBean;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class RSA {

    public static final BigInteger ZERO = BigInteger.ZERO;
    public static final BigInteger ONE = BigInteger.ONE;
    public static final BigInteger TWO = BigInteger.TWO;

    static BigInteger pubP;
    static BigInteger pubQ;
    static BigInteger privateKey;
    static BigInteger publicKey;
    static BigInteger pubModulus;
    static BigInteger pubX;
    static BigInteger pubY;
    static BigInteger pubEncrypt;
    static BigInteger pubDecrypt;

    public static void RSAInit(int n)
    {
        Random r = new Random();

        BigInteger p;
        BigInteger q;

       do {
            p = new BigInteger(n/2, r);
            pubP = p;
        }while (!millerRabin(p,2));

        do {
            q = new BigInteger(n/2, r);
            pubQ = q;
        }while (!millerRabin(q,2));

        pubModulus = pubP.multiply(pubQ);

        BigInteger phi = (pubP.subtract(ONE)).multiply(pubQ.subtract(ONE));

        BigInteger e = new BigInteger("65567");
        publicKey = e;  //PK(N,e)

        extendedEuclid(phi,e);

        BigInteger d;
        if(pubY.compareTo(new BigInteger("0")) < 0)
        {
            d = phi.add(pubY);
        }
        else
        {
            d = pubY.mod(phi);
        }

        privateKey = d; //SK(N,d)
    }

    public static BigInteger RSAEncrypt(BigInteger message)
    {
        return fastModPow(message,publicKey,pubModulus);
    }

    public static BigInteger RSADecrypt(BigInteger encryptedMessage)
    {
        //return fastModPow(encryptedMessage,privateKey,pubModulus);
        return CRT(encryptedMessage,pubP,pubQ,privateKey);
    }

    public static BigInteger CRT(BigInteger c, BigInteger p, BigInteger q, BigInteger d)
    {
        BigInteger mp = fastModPow(c,d.mod(p.subtract(ONE)),p);
        BigInteger mq = fastModPow(c,d.mod(q.subtract(ONE)),q);

        BigInteger M = p.multiply(q);
        extendedEuclid(q,p);

        BigInteger m = ((mp.multiply(pubX)).multiply(q)).add((mq.multiply(pubY)).multiply(p));
        m = m.mod(M);

        return m;
    }

    public static BigInteger Euclid(BigInteger a, BigInteger b)
    {
        if (b.compareTo(ZERO) == 0) return a;
        else return Euclid(b, a.mod(b));
    }

    public static void extendedEuclid(BigInteger a, BigInteger b)
    {
        BigInteger x = ZERO;
        BigInteger y = ONE;
        BigInteger lastX = ONE;
        BigInteger lastY = ZERO;
        BigInteger temp;

        while (b.compareTo(ZERO) != 0)
        {
            BigInteger q = a.divide(b);
            BigInteger r = a.mod(b);

            a = b;
            b = r;

            temp = x;
            x = lastX.subtract(q.multiply(x));
            lastX = temp;

            temp = y;
            y = lastY.subtract(q.multiply(y));
            lastY = temp;
        }
        pubX = lastX;
        pubY = lastY;
    }

    public static boolean millerRabin(BigInteger n, int k)
    {
        BigInteger d = n.subtract(ONE);
        int s = 0;
        while (d.mod(TWO).equals(ZERO)) {
            s++;
            d = d.divide(TWO);
        }

        for (int i = 0; i < k; i++) {
            Random r = new Random();
            BigInteger a = BigInteger.valueOf(r.nextInt());
            boolean pr = testPrime(n, a, s, d);
            if (!pr) {
                return false;
            }
        }
        return true;
    }

    public static boolean testPrime(BigInteger n, BigInteger a, int s, BigInteger d) {
        for (int i = 0; i < s; i++) {
            BigInteger exp = TWO.pow(i);
            exp = exp.multiply(d);
            BigInteger res = fastModPow(a,exp,n);
            if (res.equals(n.subtract(ONE)) || res.equals(ONE)) {
                return true;
            }
        }
        return false;
    }

    //(a^b) % m
    public static BigInteger fastModPow(BigInteger base, BigInteger exponent, final BigInteger modulo) {
        BigInteger result = BigInteger.ONE;

        while (exponent.compareTo(BigInteger.ZERO) > 0)
        {
            if (exponent.testBit(0)){
                result = (result.multiply(base)).mod(modulo);
            }
            exponent = exponent.shiftRight(1);
            base = (base.multiply(base)).mod(modulo);
        }
        return result.mod(modulo);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Message:");
        String message = sc.nextLine();
        byte[] messageBytes = message.getBytes();
        BigInteger bMessage = new BigInteger(messageBytes);

        //BigInteger bMessage = new BigInteger("1814");

        RSAInit(2048);

        BigInteger encrypted = RSAEncrypt(bMessage);
        pubEncrypt = encrypted;

        BigInteger decrypted = RSADecrypt(encrypted);
        pubDecrypt = decrypted;

        System.out.println("Message: " + bMessage);
        System.out.println("Public: " + publicKey);
        System.out.println("Private: " + privateKey);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
    }
}