import generadorclaves.AES_Simetric;
import generadorclaves.Hash;
import generadorclaves.Packet;
import generadorclaves.RSA_Asimetric;

void main() {
    try (Socket socket = new Socket("127.0.0.1", 8888);) {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

        //1. Client: sol·licita la clau publica al servidor.
        PublicKey publicKey = (PublicKey) ois.readObject();
        SecretKey secretKey = AES_Simetric.keygenKeyGeneration(256);
        SecretKey hashKey = Hash.passwordKeyGeneration(new String(secretKey.getEncoded()), 256);

        /*
        3. Client: genera una clau simètrica, genera un hash sobre aquesta clau
        simètrica i ho encripta tot utilitzant la clau pública rebuda del servidor.
        Finalment, el client envia les dades al servidor.
         */
        byte[] encryptedMessage = RSA_Asimetric.encryptData(secretKey.getEncoded(), publicKey);
        oos.writeObject(new Packet(encryptedMessage, hashKey.getEncoded()));
        oos.flush();
        /*
        5. Client, captura paraules per teclat que escriu l'usuari,
        genera un hash sobre la paraula, ho encripta tot amb la clau compartida i ho envia al servidor.
        */
        String message = "";


        while (true) {

            System.out.print("Introduzca un mensaje (exit para salir): ");
            message = new Scanner(System.in).next();

            if (message.equalsIgnoreCase("exit")) {
                break;
            }

            byte[] encryptedMessageBytes =
                    AES_Simetric.encryptData(secretKey, message.getBytes());

            SecretKey messageHashKey =
                    Hash.passwordKeyGeneration(message, 256);

            oos.writeObject(new Packet(encryptedMessageBytes, messageHashKey.getEncoded()));
            oos.flush();

            Packet confirmMessagePacket = (Packet) ois.readObject();

            byte[] decryptedConfirmMessage =
                    AES_Simetric.decryptData(secretKey, confirmMessagePacket.getMessage());

            System.out.println("Servidor: " + new String(decryptedConfirmMessage));
        }
    } catch (Exception e) {
        IO.println("Ha habido un error con el cliente: " + e.getMessage());
    }


}
