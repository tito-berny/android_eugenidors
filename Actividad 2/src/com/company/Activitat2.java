package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;

public class Activitat2 {

    public static void main(String[] args) throws Exception{

        File fitxerOriginal, fitxerEnviat,fitxerSignatura;
        byte[] resum;
        boolean verificarSignatura;

        //Generem les claus
        //L'emissor crea un parell de claus publica-privada.
        KeyPair claus=ParellDeClaus();
        PublicKey clauPublica=ClauPublica(claus);
        PrivateKey clauPrivada=ClauPrivada(claus);
        //Si volem obtenir les claus publica i privada a trav�s de fitxers
        //utilitzarem els m�todes saveKey,loadPublicKey i loadPrivatekey
        //veure: http://chuwiki.chuidiang.org/index.php?title=Encriptacion_con_Java

        //L'emissor obre i resumeix el fitxer original.
        //Despr�s signa el resultat amb la seva clau privada(fitxerSignatura)

        fitxerOriginal=new File ("arxiuOriginal.txt");
        resum=Resum(fitxerOriginal);
        fitxerSignatura=CreaSignatura(resum,clauPrivada);

        //El destinatari verifica la signatura, es a dir, comprova que:
        //el resum del fitxer(Datos.bin)coincideix amb
        //el resultat de desxifrar el fitxerSignatura amb la
        //clau p�blica de l'emissor(Mirar enunciat de l'activitat)

        //Aqu� contemplem dos casos: si modifiquem el fitxerOriginal, es a dir,
        //si no comentem la seg�ent l�nia(AfegirContingutAFitxer();), el fitxer
        //enviat al destinatari haur� estat modificat i per tant hi haur� problemes
        //per verificar la signatura.

        //Si, pel contrari,comentem la seg�ent l�nia, el fitxer enviat ser� el
        //mateix que l'original i aix� el destinatari veur� que la signatura es correcta.

        AfegirContingutAFitxer();
        fitxerEnviat=new File ("fitxerEnviat.txt");

        verificarSignatura=ComprovaSignatura(fitxerEnviat,fitxerSignatura,clauPublica);

        if(verificarSignatura){
            System.out.println("La signatura es correcta!!! \n");
        }
        else
        {
            System.out.println("Problemes en verificar la signatura!!!\n");
        }

    }


    //M�tode que retorna un parell de Claus amb xifrat "RSA"
    private static KeyPair ParellDeClaus() throws NoSuchAlgorithmException{

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    //M�tode que obt� la Clau Publica del parell de claus generat
    private static PublicKey ClauPublica(KeyPair keypair) throws Exception{

        PublicKey publicKey = keypair.getPublic();
        return publicKey;
    }

    //M�tode que obt� la Clau Privada del parell de claus generat
    private static PrivateKey ClauPrivada(KeyPair keypair) throws Exception{

        PrivateKey privateKey = keypair.getPrivate();
        return privateKey;
    }

    //M�tode que recupera la clau P�blica d'un fitxer especificat
    private static PublicKey loadPublicKey(String fileName) throws Exception {
        FileInputStream fis = new FileInputStream(fileName);
        int numBtyes = fis.available();
        byte[] bytes = new byte[numBtyes];
        fis.read(bytes);
        fis.close();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec keySpec = new X509EncodedKeySpec(bytes);
        PublicKey keyFromBytes = keyFactory.generatePublic(keySpec);
        return keyFromBytes;
    }

    //M�tode que recupera la clau Privada d'un fitxer especificat
    private static PrivateKey loadPrivateKey(String fileName) throws Exception {
        FileInputStream fis = new FileInputStream(fileName);
        int numBtyes = fis.available();
        byte[] bytes = new byte[numBtyes];
        fis.read(bytes);
        fis.close();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        PrivateKey keyFromBytes = keyFactory.generatePrivate(keySpec);
        return keyFromBytes;
    }

    private static void saveKey(Key key, String fileName) throws Exception {
        byte[] publicKeyBytes = key.getEncoded();
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(publicKeyBytes);
        fos.close();
    }

    //M�tode que retorna el resum del contingut d'un fitxer aplicant-li una funci� MD5
    public static byte[] Resum(File fitxer) {

        byte[] resum=null;

        try
        {
            // S'obre el fitxer original en mode lectura(lectura binaria)
            FileInputStream fileIn = new FileInputStream(fitxer);
            BufferedInputStream in = new BufferedInputStream(fileIn);

            //Creaci� d'objecte MessageDigest
            MessageDigest digest=MessageDigest.getInstance("MD5");


            // Bucle per llegir el fitxer(de 1024 en 1024 bytes),fins el final del fitxer,
            //i enmmagatzemar aquesta informaci� en l'objete de la clase MessageDigest.
            byte [] buffer = new byte[1024];
            int llegits=0;
            while((llegits=in.read(buffer,0,1024))!=-1){
                digest.update(buffer, 0, 1024);
            }
            //Tancament del fitxer
            in.close();

            //Resumeix la informaci� acumulada
            resum = digest.digest();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return resum;
    }

    //M�tode que guarda una signatura en un fitxer a partir d'un fitxer resumit

    public static File CreaSignatura(byte[] resum,PrivateKey clauPrivada) throws Exception{

        File fitxerSignatura;
        // Obtenir la classe per xifrar/Desxifrar
        Cipher rsa;
        rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        // Es xifra el contingut rebut per par�metre amb la clau privada
        rsa.init(Cipher.ENCRYPT_MODE, clauPrivada);
        byte[] xifrat = rsa.doFinal(resum);

        // SI VOLEM VISUALITZAR EL CONTINGUT XIFRAT AMB CAR�CTERS VISIBLES
        //for (byte b : encriptado) {
        //System.out.print(Integer.toHexString(0xFF & b));
        //}
        // System.out.println();

        // S'Obre el fitxer on es guardara la signatura
        fitxerSignatura=new File ("Signatura.txt");
        FileOutputStream fileOut = new FileOutputStream (fitxerSignatura);
        BufferedOutputStream out = new BufferedOutputStream(fileOut);
        out.write(xifrat);
        out.close();

        return fitxerSignatura;

    }

    public static boolean ComprovaSignatura(File datos,File fitxerSignatura, PublicKey clauPublica) {

        byte[] data,resum=null, desxifratSignatura=null;
        Cipher rsa;

        //Es resumeix el fitxer (Datos.bin) que rep el destinatari
        //i es desxifra el fitxerSignatura amb la clauPublica de l'emissor
        try {

            resum=Resum(datos);

            data=FitxerToBytes(fitxerSignatura);
            rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            rsa.init(Cipher.DECRYPT_MODE, clauPublica);
            desxifratSignatura = rsa.doFinal(data);

        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

        //es comparen els arrays resultants(fitxer resumit i fitxerSignatura desxifrat)
        return Arrays.equals(resum,desxifratSignatura);

    }

    public static void AfegirContingutAFitxer() {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter("C:\\ficheros_java\\Datos.bin", true));
            out.write("Hola");
            out.close();
        } catch (Exception e) {
            // error processing code
        }
    }

    public static byte[] FitxerToBytes(File f){

        byte[] data=null;

        try {
            String ruta=f.getAbsolutePath();
            Path path = Paths.get(ruta);
            data = Files.readAllBytes(path);

        }

        catch(Exception e)
        {
            e.printStackTrace();
        }

        return data;
    }
}
