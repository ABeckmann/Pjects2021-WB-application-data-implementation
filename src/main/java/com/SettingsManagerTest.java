package com;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.util.List;

import org.web3j.crypto.Credentials;

/**
 * A Test class used to perform actions on the Ethereum blockchain to test the
 * functionality of SettingsManager
 */
public class SettingsManagerTest extends Thread {
    private String testPrivKey = "e9fdcb92e805a558b7b9ba01ef6e1323acf99c59cd1a54a35977bff0934d8989";
    private String contractAddress;

    private AppSettings appSettings;
    private Web3j web3j;

    SettingsManagerTest(String contractAddress) {
        try {
            System.out.println("Connecting Tester....");
            web3j = Web3j.build(new HttpService("HTTP://127.0.0.1:7545"));
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().send();
            System.out.println("Tester Connected.");
        } catch (Exception e) {
            throw new RuntimeException("Tester couldn't connect to network");
        }

        this.contractAddress = contractAddress;
    }

    @Override
    public void run() {
        System.out.println();
        loadContract();

        // Uncomment test to run them, some need to be run together to
        // test certain show functionilities (e.g test9 with test1)

        // try {
        // Thread.sleep(5500);
        // } catch (Exception e) {
        // System.out.println(e);
        // }
        // test1();

        // try {
        // Thread.sleep(2000);
        // } catch (Exception e) {
        // System.out.println(e);
        // }
        // test2();

        // try {
        // Thread.sleep(2000);
        // } catch (Exception e) {
        // System.out.println(e);
        // }
        // test3();

        // try {
        // Thread.sleep(2000);
        // } catch (Exception e) {
        // System.out.println(e);
        // }
        // test4();

        // try {
        // Thread.sleep(2000);
        // } catch (Exception e) {
        // System.out.println(e);
        // }
        // test5();

        // try {
        // Thread.sleep(2000);
        // } catch (Exception e) {
        // System.out.println(e);
        // }
        // test6();

        // try {
        // Thread.sleep(2000);
        // } catch (Exception e) {
        // System.out.println(e);
        // }
        // test7();

        // try {
        // Thread.sleep(2000);
        // } catch (Exception e) {
        // System.out.println(e);
        // }
        // test8();

        // try {
        // Thread.sleep(2000);
        // } catch (Exception e) {
        // System.out.println(e);
        // }
        // test9();
    }

    public void loadContract() {
        Credentials contractCred = Credentials.create(testPrivKey);
        MyGasProvider provider = new MyGasProvider();

        try {
            System.out.println("Tester loading contract");
            appSettings = AppSettings.load(contractAddress, web3j, contractCred, provider);
            System.out.println("Tester loaded contract");
        } catch (Exception e) {
            System.out.println("Error in deployment: ");
            System.out.println(e.getMessage());
        }
    }

    // String test
    public void test1() {
        System.out.println("Test 1:");
        try {
            appSettings.updateSetting("backgroundColour", "string", "blue").send();
            System.out.println("Tester updated setting backgroundColour");
        } catch (Exception e) {
            System.out.println("Error in update: ");
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    // Number test
    public void test2() {
        System.out.println("Test 2:");
        try {
            appSettings.updateSetting("faveNumber", "number", "2").send();
            System.out.println("Tester updated setting faveNumber");
        } catch (Exception e) {
            System.out.println("Error in update: ");
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    // Null value test
    public void test3() {
        System.out.println("Test 3:");
        try {
            appSettings.updateSetting("faveNumber", "null", "").send();
            System.out.println("Tester updated setting faveNumber");
        } catch (Exception e) {
            System.out.println("Error in update: ");
            System.out.println(e.getMessage());
        }
    }

    // Object test
    public void test4() {
        System.out.println("Test 4:");
        try {
            appSettings.updateSetting("colours.favourite", "string", "orange").send();
            System.out.println("Tester updated setting colours.favourite");
        } catch (Exception e) {
            System.out.println("Error in update: ");
            System.out.println(e.getMessage());
        }
    }

    // Array test
    public void test5() {
        System.out.println("Test 5:");
        try {
            appSettings.updateSetting("shapes[0]", "string", "square").send();
            appSettings.updateSetting("shapes[1]", "string", "triangle").send();
            System.out.println("Tester updated setting shapes[]");
        } catch (Exception e) {
            System.out.println("Error in update: ");
            System.out.println(e.getMessage());
        }
    }

    // Test for array nested in an object
    public void test6() {
        System.out.println("Test 6:");
        try {
            appSettings.updateSetting("colours.blend[0]", "string", "red").send();
            appSettings.updateSetting("colours.blend[1]", "string", "yellow").send();
            System.out.println("Tester called deleteSetting on faveNum");
        } catch (Exception e) {
            System.out.println("Error in update: ");
            System.out.println(e.getMessage());
        }
    }

    // Test for object nested in array
    public void test7() {
        System.out.println("Test 7:");
        try {
            appSettings.updateSetting("shapes[2].name", "string", "rectangle").send();
            appSettings.updateSetting("shapes[2].height", "number", "5").send();
            appSettings.updateSetting("shapes[2].width", "number", "2").send();
            System.out.println("Tester updated setting backgroundColour");
        } catch (Exception e) {
            System.out.println("Error in update: ");
            System.out.println(e.getMessage());
        }
    }

    // Test for incorrect input data
    public void test8() {
        System.out.println("Test 8:");
        try {
            appSettings.updateSetting("backgroundColour", "number", "Five").send();
            System.out.println("Incorret data");
        } catch (Exception e) {
            System.out.println("Error in update: ");
            System.out.println(e.getMessage());
        }
    }

    // Test for setting deletion
    public void test9() {
        System.out.println("Test 9:");
        try {
            appSettings.deleteSetting("backgroundColour").send();
            System.out.println("Tester called deleteSetting on faveNum");
        } catch (Exception e) {
            System.out.println("Error in update: ");
            System.out.println(e.getMessage());
        }
    }

    // Used for viewing changes to blockchain made from the JSON file in manual
    // testing
    public void manualTest(String key) {
        System.out.println("Manual Test:");
        try {
            List setting = appSettings.getSetting(key).send();
            System.out.println("Tester retrieving setting " + key);
            System.out.println(setting);
        } catch (Exception e) {
            System.out.println("Error in update: ");
            System.out.println(e.getMessage());
        }
    }
}
