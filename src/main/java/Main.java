package main.java;

import java.io.*;
import java.math.BigInteger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final BigInteger n = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3DC2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F83655D23DCA3AD961C62F356208552BB9ED529077096966D670C354E4ABC9804F1746C08CA18217C32905E462E36CE3BE39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9DE2BCBF6955817183995497CEA956AE515D2261898FA051015728E5A8AACAA68FFFFFFFFFFFFFFFF", 16);
    private static final BigInteger erzeuger = BigInteger.valueOf(2);

    public static void main(String[] args) throws IOException {
        genPubKey();

        String chiffre = getFile("chiffre.txt");
        List<String> arr = List.of(chiffre.replaceAll("[()]", "").split(";"));
        List<List<BigInteger>> chif = new ArrayList<>();

        for (String elem:arr) {
            String[] chifarr = elem.split(",");
            chif.add(List.of(new BigInteger(chifarr[0]), new BigInteger(chifarr[1])));
        }

        StringBuilder sb = new StringBuilder();

        for (List<BigInteger> chit : chif) {
            BigInteger c = decrypt(chit.get(0), chit.get(1));

            sb.append("\n");
            sb.append("new char apparently:");
            sb.append((char) c.intValue());
        }

        System.out.println(sb.toString());
        //System.out.println(chif.get(0));

    }

    public static String getFile(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
            br.close();

            return String.valueOf(content);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BigInteger getFileBigInt(String fileName) {
        return new BigInteger(getFile(fileName));
    }


    public static void genPubKey() throws IOException{
        BufferedWriter fr = new BufferedWriter(new FileWriter("pk.txt"));
        BigInteger gb;
        gb = erzeuger.modPow(getFileBigInt("sk.txt"), n);
        fr.write(gb.toString());
        fr.close();
    }


    public static BigInteger decrypt(BigInteger y1, BigInteger y2){
        BigInteger y1_hoch_b = y1.modPow(getFileBigInt("sk.txt"), n);
        //BigInteger invers = new EuklidAlgorithm().getGcdExtended(y1_hoch_b ,n);
        BigInteger invers = y1.modInverse(n);

        return y2.modPow(invers, n);
    }
}