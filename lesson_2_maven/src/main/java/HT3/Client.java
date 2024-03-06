package HT3;

public class Client implements Runnable {

    private String name;
    private Bar bar;

    private String order;

    public Client(String name, Bar bar, String order) {
        this.name = name;
        this.bar = bar;
        this.order = order;
    }

    @Override
    public void run() {
        try {
            bar.makeOrder(name, order);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
