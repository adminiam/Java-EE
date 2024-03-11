package HT4;

public class Bartender implements Runnable {
    private String name;
    private Bar bar;

    public Bartender(String name, Bar bar) {
        this.name = name;
        this.bar = bar;
    }

    @Override
    public void run() {
        try {
            while (true) {
                bar.receiveOrder(name, bar.getOrders().take(), bar);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
