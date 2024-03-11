package HT4;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        ExecutorService clientsExecutor = Executors.newFixedThreadPool(clients);
        for (int i = 0; i < clients; i++) {
            String order = " order" + i;
            String clientName = "Client" + i;
            clientsExecutor.execute(() -> new Client(clientName, bar, order).run());
        }
        clientsExecutor.shutdown();
        ExecutorService bartendersExecutor = Executors.newFixedThreadPool(bartenders);
        for (int i = 0; i < bartenders; i++) {
            String clientName = "Bartender" + i;
            bartendersExecutor.execute(() -> new Bartender(clientName, bar).run());
        }
        bartendersExecutor.shutdown();
    }

    public void makeOrder(String name, String order) throws InterruptedException {
        try {
            orders.put(order);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(name + order + " was made");
    }

    public void receiveOrder(String name, String order, Bar bar) throws InterruptedException {
        System.out.println(name + " " + order + " received");
        bar.orderProcessing(name, order, bar);
    }

    private void orderProcessing(String name, String order, Bar bar) throws InterruptedException {
        System.out.println(name + " " + order + " is in process");
        Thread.sleep(500);
        bar.orderFinish(name, order);
    }

    private void orderFinish(String name, String order) {
        System.out.println(name + " " + order + " is finished");
    }
}
