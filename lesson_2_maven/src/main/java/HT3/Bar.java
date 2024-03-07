package HT3;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Bar {

    BlockingQueue<String> orders = new LinkedBlockingQueue<>();

    public BlockingQueue<String> getOrders() {
        return orders;
    }

    public static void main(String[] args) {
        Bar bar = new Bar();
        int clients = 3;
        int bartenders = 2;
        for (int i = 0; i < clients; i++) {
            String order = " order" + i;
            String clientName = "Client" + i;
            new Thread(new Client(clientName, bar, order)).start();
        }
        for (int i = 0; i < bartenders; i++) {
            String clientName = "Bartender" + i;
            new Thread(new Bartender(clientName, bar)).start();
        }
    }

    void makeOrder(String name, String order) throws InterruptedException {
        try {
            orders.put(order);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(name + order + " was made");
    }

    void receiveOrder(String name, String order, Bar bar) throws InterruptedException {
        System.out.println(name + " " + order + " received");
        bar.orderProcessing(name, order, bar);
    }

    void orderProcessing(String name, String order, Bar bar) throws InterruptedException {
        System.out.println(name + " " + order + " is in process");
        Thread.sleep(500);
        bar.orderFinish(name, order);
    }

    void orderFinish(String name, String order) {
        System.out.println(name + " " + order + " is finished");
    }
}
