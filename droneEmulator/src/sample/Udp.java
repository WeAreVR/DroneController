/*
https://www.baeldung.com/udp-in-java
 */

package sample;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Udp extends Thread {
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
    public String received = "000";


    public Udp() throws SocketException {
        socket = new DatagramSocket(4001);
    }

    public static void main(String[] args) throws SocketException {
        Udp server = new Udp();
        server.start();
    }

    public void run() {
        System.out.println("Running server on port " + this.socket.getLocalPort());
        running = true;

        while (running) {
            buf = new byte[256];
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            received
                    = new String(packet.getData(), 0, packet.getLength());

            if (received.equals("end")) {
                running = false;
                continue;
            }
            try {
                received = received.trim();
                String contents = "HAllO. Du skrev: " + received;
                DatagramPacket p = new DatagramPacket(contents.getBytes(), contents.length(), address, port);
                //socket.send(p);
                System.out.println(received);
                iGotSomething();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    public String getReceived() {
        return received;
    }

    public void iGotSomething() throws FileNotFoundException {
        Controller.doStuff(received);
    }
}