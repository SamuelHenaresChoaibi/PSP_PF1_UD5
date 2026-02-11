import generadorclaves.AES_Simetric;
import generadorclaves.Hash;
import generadorclaves.Packet;
import generadorclaves.RSA_Asimetric;

void main() {
    try (ServerSocket ss = new ServerSocket(8888);) {
        Socket socket = ss.accept();
        System.out.println("Servidor en marcha: " + ss.getInetAddress().getHostName());

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        //2. Servidor: genera una conjunt de claus (públic i privat) i envia la clau pública al client
        KeyPair key = RSA_Asimetric.randomGenerate(2048);
        PublicKey publicKey = key.getPublic();
        PrivateKey privateKey = key.getPrivate();
        oos.writeObject(publicKey);
        oos.flush();

        /*
        4. Servidor rep el missatge xifrat, el desxifra utilitzant la seva clau privada, genera un hash sobre
        les dades rebudes que fan referència a la clau simètrica enviada per el client, comprova que el hash rebut i
        el hash generat son iguals per garantir integritat i finalment guarda la clau compartida a una variable SecretKey.
         */
        Packet packet = (Packet) ois.readObject();
        byte[] decryptedKeyBytes = RSA_Asimetric.decryptData(packet.getMessage(), privateKey);
        SecretKey clientSharedKey = new SecretKeySpec(decryptedKeyBytes, "AES");
        SecretKey serverHashKey = Hash.passwordKeyGeneration(new String(clientSharedKey.getEncoded()), 256);
        SecretKey clientHashKey = new SecretKeySpec(packet.getHash(), "AES");

        if (!Hash.compareHash(clientHashKey, serverHashKey)) {
            System.out.println("Hash Incorrecto, cerrrando servidor");
            socket.close();
        }

        /*
        6. Servidor rep el missatge xifrat, el desxifra utilitzant la seva clau compartida simetrica, genera un hash sobre
        les dades rebudes que fan referència a la paraula enviada per el client, comprova que el hash rebut i el hash generat
        son iguals per garantir integritat, imprimeix la paraula per pantalla i envia un acús de rebut al client.
         */
        while (true) {
            Packet messagePacket = (Packet) ois.readObject();
            byte[] decryptedMessageBytes = AES_Simetric.decryptData(clientSharedKey, messagePacket.getMessage());
            SecretKey messageKey = new SecretKeySpec(decryptedMessageBytes, "AES");
            SecretKey messageServerHash = Hash.passwordKeyGeneration(new String(messageKey.getEncoded()), 256);
            SecretKey messageClientHash = new SecretKeySpec(messagePacket.getHash(), "AES");

            if (Hash.compareHash(messageClientHash, messageServerHash)) {
                System.out.println("Mensaje recibido con exito: " + new String(decryptedMessageBytes));
                String messageForClient = "DATOS RECIBIDOS CORRECTAMENTE";
                byte[] encryptMesaageForClient = AES_Simetric.encryptData(clientSharedKey, messageForClient.getBytes());
                SecretKey messageForClientHash = Hash.passwordKeyGeneration(messageForClient, 256);
                oos.writeObject(new Packet(encryptMesaageForClient, messageForClientHash.getEncoded()));
                oos.flush();
            }
        }


    } catch (Exception e) {
        IO.println("Ha habido un error con el servidor: " + e.getMessage());
    }
}

