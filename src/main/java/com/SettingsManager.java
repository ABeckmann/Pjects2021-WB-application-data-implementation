package com;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.FileReader;
import java.io.FileWriter;

import javaxt.io.Directory;
import javaxt.io.Directory.Event;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

/**
 * SettingsManager class to used to synchronise settings data between a JSON
 * file and an Ethereum blockchain instance
 *
 */
public class SettingsManager extends Thread {

    private String dir = "src\\main\\resources";
    private String file = "src\\main\\resources\\settings.json";

    private String managersPrivKey = "bcdf2812aca2c1b28b8ca74dc667550cf36b9c0f2fb73dd6faea1af2a1138f78";
    private AppSettings appSettings;
    private Web3j web3j;

    SettingsManager() {
        // Attempt to connect to the chain
        try {
            System.out.println("Connecting to server....");
            web3j = Web3j.build(new HttpService("HTTP://127.0.0.1:7545"));
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().send();
            System.out.println("Connected.");
            System.out.println();
        } catch (Exception e) {
            System.out.println("Couldn't connect to network");
        }

        deployContract();

        // Initialise Chain with Setting Values
        JsonObject jsonSettings = getSettingsJson();
        modifyChain(jsonSettings);

        System.out.println("Initialised Values:");
        try {
            List chainKeys = appSettings.getSettingKeys().send();
            Map<String, String[]> chainSettings = (TreeMap<String, String[]>) getChainSettings(chainKeys);
            for (String key : chainSettings.keySet()) {
                System.out.println(key + "," + chainSettings.get(key)[0] + "," + chainSettings.get(key)[1]);
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * Retrieves the settings from the Json file
     * 
     * @return a JsonObject containing the settings
     */
    public JsonObject getSettingsJson() {
        try {
            FileReader fr = new FileReader(file);
            JsonObject settingsObject = JsonParser.parseReader(fr).getAsJsonObject();
            fr.close();
            return settingsObject;

        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Error during parsing of file " + file + " to JsonObject");
        }
    }

    /**
     * Retrieves the setting data from the smart contract
     * 
     * @param keys the keys to mapping containing the settings
     * @return A map of the keys to their relevant setting data
     */
    public Map<String, String[]> getChainSettings(List keys) {
        Map<String, String[]> chainSettings = new TreeMap<String, String[]>();
        for (Object key : keys) {
            try {
                List setting = appSettings.getSetting(key.toString()).send();
                chainSettings.put(key.toString(),
                        new String[] { setting.get(0).toString(), setting.get(1).toString() });
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        return chainSettings;
    }

    /**
     * Starts the process of modifying the chain with the Json data
     * 
     * @param settings JsonObject of settings
     */
    public void modifyChain(JsonObject settings) {
        for (String key : settings.keySet()) {

            if (settings.get(key).isJsonPrimitive()) {
                if (settings.get(key).getAsJsonPrimitive().isBoolean()) {
                    modifyChain(key, "boolean", settings.get(key).getAsString());

                } else if (settings.get(key).getAsJsonPrimitive().isNumber()) {
                    modifyChain(key, "number", settings.get(key).getAsString());

                } else if (settings.get(key).getAsJsonPrimitive().isString()) {
                    modifyChain(key, "string", settings.get(key).getAsString());
                }

            } else if (settings.get(key).isJsonNull()) {
                modifyChain(key, "null", "");

            } else if (settings.get(key).isJsonArray()) {
                JsonArray arr = settings.get(key).getAsJsonArray();
                modifyChainForArray(key, arr);

            } else if (settings.get(key).isJsonObject()) {
                modifyChainForObject(key, settings.get(key).getAsJsonObject());

            }
        }
    }

    /**
     * Modifies the smart contract with setting data
     * 
     * @param key   Key to the setting data
     * @param type  the setting's data type
     * @param value a string representation of the data
     * @return Whether the modification was successful
     */
    public boolean modifyChain(String key, String type, String value) {
        try {
            appSettings.updateSetting(key, type, value).send();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parses JsonObjects for chain modifcation
     * 
     * @param prepend  key pre-pended to the JsonObject
     * @param settings Jsonobject to be parsed
     */
    public void modifyChainForObject(String prepend, JsonObject settings) {
        for (String key : settings.keySet()) {
            String newKey = prepend + "." + key;
            if (settings.get(key).isJsonPrimitive()) {
                if (settings.get(key).getAsJsonPrimitive().isBoolean()) {
                    modifyChain(newKey, "boolean", settings.get(key).getAsString());

                } else if (settings.get(key).getAsJsonPrimitive().isNumber()) {
                    modifyChain(newKey, "number", settings.get(key).getAsString());

                } else if (settings.get(key).getAsJsonPrimitive().isString()) {
                    modifyChain(newKey, "string", settings.get(key).getAsString());
                }

            } else if (settings.get(key).isJsonNull()) {
                modifyChain(newKey, "null", settings.get(key).getAsString());

            } else if (settings.get(key).isJsonArray()) {
                JsonArray arr = settings.get(key).getAsJsonArray();
                modifyChainForArray(key, arr);

            } else if (settings.get(key).isJsonObject()) {
                modifyChainForObject(newKey, settings.get(key).getAsJsonObject());

            }
        }
    }

    /**
     * Parses JsonArrays for chain modification
     * 
     * @param key key of the JsonArray
     * @param arr JsonArray to be parsed
     */
    public void modifyChainForArray(String key, JsonArray arr) {
        for (int i = 0; i < arr.size(); i++) {
            String newKey = key + "[" + i + "]";
            if (arr.get(i).isJsonPrimitive()) {
                if (arr.get(i).getAsJsonPrimitive().isBoolean()) {
                    modifyChain(newKey, "boolean", arr.get(i).getAsString());

                } else if (arr.get(i).getAsJsonPrimitive().isNumber()) {
                    modifyChain(newKey, "number", arr.get(i).getAsString());

                } else if (arr.get(i).getAsJsonPrimitive().isString()) {
                    modifyChain(newKey, "string", arr.get(i).getAsString());
                }

            } else if (arr.get(i).isJsonNull()) {
                modifyChain(newKey, "null", arr.get(i).getAsString());

            } else if (arr.get(i).isJsonArray()) {
                modifyChainForArray(newKey, arr.get(i).getAsJsonArray());

            } else if (arr.get(i).isJsonObject()) {
                modifyChainForObject(newKey, arr.get(i).getAsJsonObject());
            }
        }
    }

    /**
     * Modifies the JSON file with data from the blockchain
     * 
     * @param chainSettings settings data from the blockchain
     */
    public void modifyJson(Map<String, String[]> chainSettings) {
        JsonObject json = new JsonObject();
        for (String key : chainSettings.keySet()) {
            if (key.contains(".") && key.contains("[")) {
                int obj = key.indexOf('.');
                int arr = key.indexOf(']');

                if (obj < arr) {
                    String oldKey = key.substring(0, obj);
                    String newKey = key.substring(obj + 1);
                    if (!json.has(oldKey)) {
                        json.add(oldKey, new JsonObject());
                    }
                    modifyJsonObject(chainSettings, json.get(oldKey).getAsJsonObject(), newKey, key);
                } else {
                    int arrStart = key.indexOf('[');
                    int index = Integer.parseInt(key.substring(arrStart + 1, arr));
                    String oldKey = key.substring(0, arrStart);
                    String newKey = key.substring(arr + 1);
                    if (!json.has(oldKey)) {
                        json.add(oldKey, new JsonArray());
                    }
                    modifyJsonArray(chainSettings, json.get(oldKey).getAsJsonArray(), newKey, key, index);
                }

            } else if (key.contains(".")) {
                int obj = key.indexOf('.');

                String oldKey = key.substring(0, obj);
                String newKey = key.substring(obj + 1);
                if (!json.has(oldKey)) {
                    json.add(oldKey, new JsonObject());
                }
                modifyJsonObject(chainSettings, json.get(oldKey).getAsJsonObject(), newKey, key);

            } else if (key.contains("]")) {
                int arr = key.indexOf(']');
                int arrStart = key.indexOf('[');
                int index = Integer.parseInt(key.substring(arrStart + 1, arr));
                String oldKey = key.substring(0, arrStart);

                String newKey;
                try {
                    newKey = key.substring(arr + 1);
                } catch (NullPointerException e) {
                    newKey = "";
                }

                if (!json.has(oldKey)) {
                    json.add(oldKey, new JsonArray());
                }
                modifyJsonArray(chainSettings, json.get(oldKey).getAsJsonArray(), newKey, key, index);

            } else {
                if (chainSettings.get(key)[0].equals("boolean")) {
                    json.addProperty(key, Boolean.parseBoolean(chainSettings.get(key)[1]));

                } else if (chainSettings.get(key)[0].equals("number")) {
                    json.addProperty(key, Double.parseDouble(chainSettings.get(key)[1]));

                } else if (chainSettings.get(key)[0].equals("string")) {
                    json.addProperty(key, chainSettings.get(key)[1]);

                } else if (chainSettings.get(key)[0].equals("null")) {
                    json.add(key, JsonNull.INSTANCE);
                }
            }
        }

        // Write the updated JsonObject to the Json file
        try {
            FileWriter fWriter = new FileWriter(file);
            fWriter.write(json.toString());
            fWriter.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Creates a JsonArray to be written to the JSON file
     * 
     * @param chainSettings settings data from the blockchain
     * @param json          parent object to be written to file
     * @param key           key of the JsonArray being parsed
     * @param origKey       complete key of the JsonArray being parsed
     * @param index         Index of the element being parsed in the JsonArray
     */
    public void modifyJsonArray(Map<String, String[]> chainSettings, JsonArray json, String key, String origKey,
            int index) {

        if (key.contains(".") && key.contains("]")) {
            int obj = key.indexOf('.');
            int arr = key.indexOf(']');

            if (obj < arr) {
                String newKey = key.substring(obj + 1);

                try {
                    JsonElement element = json.set(index, new JsonObject());
                    json.set(index, element);
                } catch (Exception e) {
                    json.add(new JsonObject());
                }
                modifyJsonObject(chainSettings, json.get(index).getAsJsonObject(), newKey, origKey);
            } else {
                int arrStart = key.indexOf('[');
                int newIndex = Integer.parseInt(key.substring(arrStart + 1, arr));
                String newKey = key.substring(arr + 1);

                try {
                    JsonElement element = json.set(index, new JsonArray());
                    json.set(index, element);
                } catch (Exception e) {
                    json.add(new JsonObject());
                }

                modifyJsonArray(chainSettings, json.get(index).getAsJsonArray(), newKey, origKey, newIndex);
            }

        } else if (key.contains(".")) {
            int obj = key.indexOf('.');
            String newKey = key.substring(obj + 1);

            try {
                JsonElement element = json.set(index, new JsonObject());
                json.set(index, element);
            } catch (Exception e) {
                json.add(new JsonObject());
            }

            modifyJsonObject(chainSettings, json.get(index).getAsJsonObject(), newKey, origKey);

        } else if (key.contains("]")) {
            int arr = key.indexOf(']');
            int arrStart = key.indexOf('[');
            int newIndex = Integer.parseInt(key.substring(arrStart + 1, arr));
            String newKey = key.substring(arr + 1);

            try {
                JsonElement element = json.set(index, new JsonArray());
                json.set(index, element);
            } catch (Exception e) {
                json.add(new JsonArray());
            }

            modifyJsonArray(chainSettings, json.get(index).getAsJsonArray(), newKey, origKey, newIndex);

        } else {
            if (chainSettings.get(origKey)[0].equals("boolean")) {
                json.add(new JsonPrimitive(Boolean.parseBoolean(chainSettings.get(origKey)[1])));

            } else if (chainSettings.get(origKey)[0].equals("number")) {
                json.add(new JsonPrimitive(Double.parseDouble(chainSettings.get(origKey)[1])));

            } else if (chainSettings.get(origKey)[0].equals("string")) {
                json.add(new JsonPrimitive(chainSettings.get(origKey)[1]));

            } else if (chainSettings.get(origKey)[0].equals("null")) {
                json.add(JsonNull.INSTANCE);
            } else {
                System.out.println("Nothing created for:" + chainSettings.get(origKey)[0]);
            }
        }
    }

    /**
     * Creates a JsonObject to be written to the JSON file
     * 
     * @param chainSettings settings data from the blockchain
     * @param json          parent object to be written to file
     * @param key           key of the JsonObject being parsed
     * @param origKey       complete key of the JsonObject being parsed
     */
    public void modifyJsonObject(Map<String, String[]> chainSettings, JsonObject json, String key, String origKey) {
        if (key.contains(".") && key.contains("]")) {
            int obj = key.indexOf('.');
            int arr = key.indexOf(']');

            if (obj < arr) {
                String oldKey = key.substring(0, obj);
                String newKey = key.substring(obj + 1);
                if (!json.has(oldKey)) {
                    json.add(oldKey, new JsonObject());
                }

                modifyJsonObject(chainSettings, json.get(oldKey).getAsJsonObject(), newKey, origKey);
            } else {
                int arrStart = key.indexOf('[');
                int index = Integer.parseInt(key.substring(arrStart + 1, arr));
                String oldKey = key.substring(0, arrStart);
                String newKey = key.substring(arr + 1);
                if (!json.has(oldKey)) {
                    json.add(oldKey, new JsonArray());
                }

                modifyJsonArray(chainSettings, json.get(oldKey).getAsJsonArray(), newKey, origKey, index);
            }

        } else if (key.contains(".")) {
            int obj = key.indexOf('.');

            String oldKey = key.substring(0, obj);
            String newKey = key.substring(obj + 1);
            if (!json.has(oldKey)) {
                json.add(oldKey, new JsonObject());
            }
            modifyJsonObject(chainSettings, json.get(oldKey).getAsJsonObject(), newKey, origKey);

        } else if (key.contains("]")) {
            int arr = key.indexOf(']');
            int arrStart = key.indexOf('[');
            int index = Integer.parseInt(key.substring(arrStart + 1, arr));
            String oldKey = key.substring(0, arrStart);
            String newKey = key.substring(arr + 1);
            if (!json.has(oldKey)) {
                json.add(oldKey, new JsonArray());
            }
            modifyJsonArray(chainSettings, json.get(oldKey).getAsJsonArray(), newKey, origKey, index);

        } else {
            if (chainSettings.get(origKey)[0].equals("boolean")) {
                json.addProperty(key, Boolean.parseBoolean(chainSettings.get(origKey)[1]));

            } else if (chainSettings.get(origKey)[0].equals("number")) {
                json.addProperty(key, Double.parseDouble(chainSettings.get(origKey)[1]));

            } else if (chainSettings.get(origKey)[0].equals("string")) {
                json.addProperty(key, chainSettings.get(origKey)[1]);

            } else if (chainSettings.get(origKey)[0].equals("null")) {
                json.add(key, JsonNull.INSTANCE);
            }
        }
    }

    /**
     * Deploys the AppSettings contract on the blockchain
     */
    public void deployContract() {
        Credentials contractCred = Credentials.create(managersPrivKey);
        MyGasProvider provider = new MyGasProvider();

        try {
            System.out.println("Deploying contract");
            appSettings = AppSettings.deploy(web3j, contractCred, provider, "appSettings").send();
            System.out.println("Contract Deployed");
            System.out.println();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * runs the applications main loop once the Thread has joined
     */
    @Override
    public void run() {
        mainLoop();
    }

    /**
     * Start the main loop of the application which detects changes made to both the
     * Json file containing the application settings and the blockchain used for
     * synchronsing these settings with other nodes
     */
    public void mainLoop() {

        Directory d = new Directory(dir);

        BigInteger count = BigInteger.valueOf(0);
        try {
            count = appSettings.getUpdatesMade().send();
        } catch (Exception e) {
            System.out.println("Coudn't retrieve updatesMade");
        }

        // Retrieve any events
        List events = new ArrayList();
        try {
            events = d.getEvents();
        } catch (Exception e) {
            System.out.println(e);
        }

        Event event = null;
        BigInteger newCount = BigInteger.valueOf(0);

        while (true) {
            try {
                newCount = appSettings.getUpdatesMade().send();
            } catch (Exception e) {
                System.out.println("Coudn't retrieve updatesMade");
            }

            synchronized (events) {
                while (events.isEmpty() && newCount.equals(count)) {
                    try {
                        events.wait(1000);
                        newCount = appSettings.getUpdatesMade().send();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

                if (!events.isEmpty()) {
                    event = (Event) events.remove(0);
                }
            }

            // If a modification of the file, update the chain
            if (event != null && event.getFile().equals(file)) {
                if (event.getAction() != "Modify") {
                    System.out.println("Json modified, updating chain\n");
                    modifyChain(getSettingsJson());

                    // Set the count to the new transaction, so as not to detect the changes
                    try {
                        count = appSettings.getUpdatesMade().send();
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                    // Uncomment to print the updated chain data on file update

                    try {
                        List chainKeys = appSettings.getSettingKeys().send();
                        Map<String, String[]> chainSettings = (TreeMap<String, String[]>) getChainSettings(chainKeys);
                        for (String key : chainSettings.keySet()) {
                            System.out.println(key + "," + chainSettings.get(key)[0] + "," + chainSettings.get(key)[1]);
                        }
                        System.out.println();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

                // If a modification of the chain, update the file
            } else if (!newCount.equals(count)) {
                System.out.println("Chain modified, updating Json\n");

                try {
                    List chainKeys = appSettings.getSettingKeys().send();
                    Map<String, String[]> chainSettings = getChainSettings(chainKeys);
                    modifyJson(chainSettings);
                } catch (Exception e) {
                    System.out.println(e);
                }

                // Uncomment to print the updated JSON data on chain update

                // try {
                // JsonObject jsonSettings = getSettingsJson();
                // for (String key : jsonSettings.keySet()) {
                // System.out.println(key + "," + jsonSettings.get(key));
                // }
                // System.out.println();
                // } catch (Exception e) {
                // System.out.println(e);
                // }

                // Allow time for the newly created file modification event to occur and be
                // removed
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e);
                }

                events.remove(events.size() - 1);

                count = newCount;

            } else {
                System.out.println("Didn't detect");
                System.out.println("Event:" + event.toString());
            }

            // Set event back to null
            event = null;

        }

    }

    // For testing purposes, returns the address of the AppSettings contract
    public String getContractAddress() {
        return this.appSettings.getContractAddress();
    }
}
