import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class RSA {

    private final static BigInteger one      = new BigInteger("1");
     BigInteger privateKey;
     BigInteger publicKey;
     static BigInteger N;

    public static void RSAInit(int n)
    {
        Random r = new Random();

        BigInteger p;
        BigInteger q;

        do {
            p = new BigInteger(n/2, r);
        }while (!millerRabin(p.intValue(),2));
        do {
            q = new BigInteger(n/2, r);
        }while (!millerRabin(q.intValue(),2));
        N = p.multiply(q);

        BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));

    }

    public static void extendedEuclid(long a, long b)
    {
        long x = 0, y = 1, lastX = 1, lastY = 0, temp;
        while (b != 0)
        {
            long q = a / b;
            long r = a % b;

            a = b;
            b = r;

            temp = x;
            x = lastX - q * x;
            lastX = temp;

            temp = y;
            y = lastY - q * y;
            lastY = temp;
        }
        System.out.println("x : "+ lastX +" y :"+ lastY);
    }
    public static boolean millerRabin(int n, int k)
    {
        if ((n < 2) || (n % 2 == 0))
        {
            return (n == 2);
        }

        int s = n - 1;

        while (s % 2 == 0)
        {
            s >>= 1;
        }

        Random r = new Random();

        for (int i = 0; i < k; i++) {
            int a = r.nextInt(n - 1) + 1;
            int tempS = s;
            int mod = 1;//

            for (int j = 0; j < tempS; ++j)
            {
                mod = (mod * a) % n;
            }

            while (tempS != n - 1 && mod != 1 && mod != n - 1)
            {
                mod = (mod * mod) % n;
                tempS *= 2;
            }

            if (mod != n - 1 && tempS % 2 == 0) return false;
        }
        return true;
    }

    //(a^b) % m
    public static int fastPow(int a, int b, int m)
    {
        int res = 1;
        for (int i = 0; i < b; i++)
        {
            res = res * a;
            res = res % m;
        }
        return res % m;
    }


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Message:");
        String message = sc.nextLine();
        byte[] messageBytes = message.getBytes();
        BigInteger bMessage = new BigInteger(messageBytes);
    }
}