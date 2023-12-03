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

    public static void main(String[] args){
        // gen Public Key from sk.txt
        genPubKey("sk.txt", "pk.txt");
        // Decrypt chiffre.txt and write to text-d.txt
        writeFile("text-d.txt", decryptFile("chiffre.txt", "sk.txt"));

        // gen Private Key
        genPrivKey("sk_2.txt");
        // gen Public Key
        genPubKey("sk_2.txt", "pk_2.txt");

        // encrypt chif.txt
        encrypt("chif.txt", getFile("text.txt"), "pk_2.txt");
        //decrypt file chif.txt and write to text-ours.txt
        writeFile("text-ours.txt", decryptFile("chif.txt", "sk_2.txt"));


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


    public static void genPubKey(String skFilname, String pkFilename){
        BigInteger gb = erzeuger.modPow(getFileBigInt(skFilname), n);
        writeFile(pkFilename, gb.toString());
    }

    public static void genPrivKey(String filename){
        BigInteger gb = new BigInteger(n.subtract(BigInteger.valueOf(2)).bitLength(), new Random());
        writeFile(filename, gb.toString());
    }

    public static void writeFile(String Filename, String content){
        try {
            BufferedWriter fr = new BufferedWriter(new FileWriter(Filename));
            fr.write(content);
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void encrypt(String fileName, String content, String pk) {
        StringBuilder res = new StringBuilder();
        for(char c : content.toCharArray()) {
            BigInteger a = new BigInteger(2048, new Random());
            BigInteger y1 = erzeuger.modPow(a, n);
            BigInteger y2 = getFileBigInt(pk).modPow(a, n).multiply(BigInteger.valueOf(c)).mod(n);

            res.append("(").append(y1).append(",").append(y2).append(");");
        }
        writeFile(fileName, res.toString());
    }

    public static String decryptFile(String fileName, String sk) {
        String chiffre = getFile(fileName);
        assert chiffre != null;
        List<String> chiffreArr = List.of(chiffre.replaceAll("[()]", "").split(";"));
        List<List<BigInteger>> chiff_split = new ArrayList<>();

        for (String elem:chiffreArr) {
            String[] inner_arr = elem.split(",");
            chiff_split.add(List.of(new BigInteger(inner_arr[0]), new BigInteger(inner_arr[1])));
        }

        StringBuilder sb = new StringBuilder();

        for (List<BigInteger> elems : chiff_split) {
            BigInteger c = decrypt(elems.get(0), elems.get(1), sk);
            sb.append((char) c.intValue());
        }
        return sb.toString();
    }

    public static BigInteger decrypt(BigInteger y1, BigInteger y2, String sk){
        BigInteger y1_hoch_b = y1.modPow(getFileBigInt(sk), n);
        BigInteger invers = y1_hoch_b.modInverse(n);

        return y2.modPow(invers, n);
    }
}