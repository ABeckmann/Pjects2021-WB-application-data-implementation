package com;

import java.math.BigInteger;

import org.web3j.tx.gas.StaticGasProvider;

/**
 * Determines the Gas price and limit for transactions on the Ethereum
 * blockchain
 */
public class MyGasProvider extends StaticGasProvider {
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(6_721_975);
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(2_100_000_000L);

    public MyGasProvider() {
        super(GAS_PRICE, GAS_LIMIT);
    }
}
