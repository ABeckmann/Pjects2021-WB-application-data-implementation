package com;

/**
 * Main class to instantiate and run the SettingsManager along with it's tester
 */
public class App {

    public static void main(String[] args) {
        SettingsManager main = new SettingsManager();
        SettingsManagerTest tester = new SettingsManagerTest(main.getContractAddress());
        Thread t1 = new Thread(main);
        Thread t2 = new Thread(tester);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (Exception exception_name) {
            System.out.println(exception_name);
        }

    }
}
