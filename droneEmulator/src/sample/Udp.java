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
    Controller control;

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

            //if command is end, the server stops
            if (received.equals("end")) {
                running = false;
                continue;
            }
            try {
                //removes any spaces in received ("hallo   " becomes "hallo"
                received = received.trim();
                System.out.println(received);
                udpReceivedSomething();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    //send received command to Controller
    public void udpReceivedSomething() throws FileNotFoundException, InterruptedException {
        Controller.controlDrone(received);
    }
}