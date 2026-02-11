# 🛡️ Práctica Final - Unidad 5
## Programación de Servicios y Procesos
**Curso 25/26 – Grupo S2P Data**

---

## 📌 Descripción del Proyecto

Este proyecto implementa una simulación del protocolo SSL utilizando un modelo cliente/servidor en Java.

El objetivo principal es permitir que un cliente envíe datos cifrados mediante AES a un servidor utilizando una clave simétrica compartida. Para que dicha clave sea segura, se intercambia previamente utilizando criptografía asimétrica (RSA).

Durante toda la comunicación se debe garantizar:

- 🔐 Confidencialidad (cifrado)
- 🧾 Integridad (uso de Hash)
- 🔑 Intercambio seguro de claves
- 📜 (Opcional) Autenticación mediante certificado digital autofirmado

---

## 🎯 Objetivos

1. Implementar comunicación cliente/servidor en Java.
2. Simular el intercambio de claves como en SSL/TLS.
3. Garantizar la integridad de los datos mediante SHA-256.
4. Implementar cifrado híbrido (RSA + AES).
5. Implementar certificados digitales autofirmados (ejercicio avanzado).

---

## 🔄 Flujo de Comunicación

### 1️⃣ Solicitud de clave pública
El cliente solicita la clave pública al servidor.

### 2️⃣ Generación de claves RSA
El servidor:
- Genera un par de claves (pública y privada).
- Envía la clave pública al cliente.

### 3️⃣ Envío de clave simétrica
El cliente:
- Genera una clave simétrica AES.
- Genera un hash sobre esa clave.
- Cifra la clave y el hash con la clave pública del servidor.
- Envía el paquete al servidor.

### 4️⃣ Validación de la clave compartida
El servidor:
- Descifra el mensaje con su clave privada.
- Genera su propio hash.
- Compara ambos hashes.
- Si coinciden, guarda la clave compartida en una variable `SecretKey`.

---

### 5️⃣ Envío de mensajes cifrados
El cliente:
- Captura palabras por teclado.
- Genera hash del mensaje.
- Cifra mensaje y hash con la clave compartida.
- Envía el paquete al servidor.

### 6️⃣ Validación y acuse de recibo
El servidor:
- Descifra el mensaje con AES.
- Genera hash del mensaje recibido.
- Compara hashes.
- Si coinciden:
  - Imprime el mensaje por pantalla.
  - Envía un acuse de recibo cifrado.

Mensaje de acuse: DataRecived


Este mensaje también se envía cifrado con AES y acompañado de su hash.

---

## 🔐 Algoritmos Utilizados

### RSA
- Algoritmo: `RSA/ECB/PKCS1Padding`
- Tamaño de clave: 2048 bits
- Uso: Intercambio seguro de clave simétrica.

### AES
- Modo: `AES/CBC/PKCS5Padding`
- IV fijo definido en la clase.
- Uso: Cifrado de mensajes.

### Hash
- Algoritmo: `SHA-256`
- Uso:
  - Verificación de integridad de la clave simétrica.
  - Verificación de integridad de los mensajes.

---

## 📜 Ejercicio 7 – Certificado Digital Autofirmado

Para simular HTTPS y añadir autenticidad al servidor:

### Pasos recomendados:

1. Generar un certificado autofirmado utilizando:
   - Java KeyStore (JKS)
   - Herramienta `keytool`

2. Enviar el certificado al cliente en lugar de solo la clave pública.

3. Validar el certificado en el cliente antes de usar la clave pública.

4. Simular ataques con certificados falsos para comprobar la verificación.

---

## 📂 Estructura del Proyecto

Clases principales:

- `Packet`
  - Contiene:
    - `byte[] message`
    - `byte[] hash`

- `Hash`
  - Generación de claves a partir de SHA-256.
  - Comparación de hashes.

- `AES_Simetric`
  - Generación de claves AES.
  - Cifrado y descifrado con AES/CBC.

- `RSA_Asimetric`
  - Generación de par de claves RSA.
  - Cifrado y descifrado RSA.

---

## 📊 Criterios de Evaluación

| Apartado | Peso |
|----------|------|
| Funcionamiento (pasos 1–6) | 50% |
| Ejercicio 7 (certificado) | 20% |
| Video demostración | 30% |

⚠️ Si el código no compila o no funciona correctamente, la práctica se considerará suspensa con una puntuación de 0.

---

## 🎥 Entrega

Se debe entregar:

- Código completo del proyecto.
- Video corto demostrando:
  - Funcionamiento.
  - Ejecución.
  - Explicación del flujo de comunicación.

---

## 🚀 Resultado Esperado

Una simulación funcional del protocolo SSL que garantice:

- Confidencialidad.
- Integridad.
- Intercambio seguro de claves.
- Autenticación (opcional con certificado).

---

