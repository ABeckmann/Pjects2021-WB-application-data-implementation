package com;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.StaticArray2;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class AppSettings extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50604051610a9b380380610a9b83398101604081905261002f916100ef565b805161004290600090602084019061004e565b505060006002556101a5565b828054600181600116156101000203166002900490600052602060002090601f01602090048101928261008457600085556100ca565b82601f1061009d57805160ff19168380011785556100ca565b828001600101855582156100ca579182015b828111156100ca5782518255916020019190600101906100af565b506100d69291506100da565b5090565b5b808211156100d657600081556001016100db565b60006020808385031215610101578182fd5b82516001600160401b0380821115610117578384fd5b818501915085601f83011261012a578384fd5b81518181111561013657fe5b604051601f8201601f191681018501838111828210171561015357fe5b6040528181528382018501881015610169578586fd5b8592505b8183101561018a578383018501518184018601529184019161016d565b8183111561019a57858583830101525b979650505050505050565b6108e7806101b46000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c8063528241321461005c57806366dfa1921461007a578063aa6b631a1461008f578063e1e3b3a4146100af578063e2a8acd3146100c4575b600080fd5b6100646100d7565b60405161007191906108a8565b60405180910390f35b6100826100dd565b6040516100719190610811565b6100a261009d3660046106a7565b6101b5565b604051610071919061078d565b6100c26100bd3660046106a7565b610329565b005b6100c26100d23660046106e7565b6103b6565b60025490565b60606001805480602002602001604051908101604052809291908181526020016000905b828210156101ac5760008481526020908190208301805460408051601f60026000196101006001871615020190941693909304928301859004850281018501909152818152928301828280156101985780601f1061016d57610100808354040283529160200191610198565b820191906000526020600020905b81548152906001019060200180831161017b57829003601f168201915b505050505081526020019060010190610101565b50505050905090565b6101bd6104d5565b6040518060400160405280600385856040516101da92919061077d565b9081526040805160209281900383018120805460026001821615610100026000190190911604601f810185900485028301850190935282825290929091908301828280156102695780601f1061023e57610100808354040283529160200191610269565b820191906000526020600020905b81548152906001019060200180831161024c57829003601f168201915b505050505081526020016003858560405161028592919061077d565b90815260408051602092819003830181206001908101805460029281161561010002600019011691909104601f810185900485028301850190935282825290929091908301828280156103195780601f106102ee57610100808354040283529160200191610319565b820191906000526020600020905b8154815290600101906020018083116102fc57829003601f168201915b5050505050815250905092915050565b60028054600101905560408051808201825260048152631b9d5b1b60e21b6020820152905160039061035e908590859061077d565b908152602001604051809103902060000190805190602001906103829291906104fc565b506003828260405161039592919061077d565b908152602001604051809103902060010160006103b29190610588565b5050565b6002805460010190556040805160606020601f87018190040282018101835291810185815290918291908790879081908501838280828437600092019190915250505090825250604080516020601f860181900481028201810190925284815291810191908590859081908401838280828437600092019190915250505091525060405160039061044a908990899061077d565b908152602001604051809103902060008201518160000190805190602001906104749291906104fc565b50602082810151805161048d92600185019201906104fc565b50506001805480820182556000919091526104cc91507fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf60187876105cf565b50505050505050565b60405180604001604052806002905b60608152602001906001900390816104e45790505090565b828054600181600116156101000203166002900490600052602060002090601f0160209004810192826105325760008555610578565b82601f1061054b57805160ff1916838001178555610578565b82800160010185558215610578579182015b8281111561057857825182559160200191906001019061055d565b5061058492915061064b565b5090565b50805460018160011615610100020316600290046000825580601f106105ae57506105cc565b601f0160209004906000526020600020908101906105cc919061064b565b50565b828054600181600116156101000203166002900490600052602060002090601f0160209004810192826106055760008555610578565b82601f1061061e5782800160ff19823516178555610578565b82800160010185558215610578579182015b82811115610578578235825591602001919060010190610630565b5b80821115610584576000815560010161064c565b60008083601f840112610671578182fd5b50813567ffffffffffffffff811115610688578182fd5b6020830191508360208285010111156106a057600080fd5b9250929050565b600080602083850312156106b9578182fd5b823567ffffffffffffffff8111156106cf578283fd5b6106db85828601610660565b90969095509350505050565b600080600080600080606087890312156106ff578182fd5b863567ffffffffffffffff80821115610716578384fd5b6107228a838b01610660565b9098509650602089013591508082111561073a578384fd5b6107468a838b01610660565b9096509450604089013591508082111561075e578384fd5b5061076b89828a01610660565b979a9699509497509295939492505050565b6000828483379101908152919050565b60208082526000906060830183820185845b600281101561080557601f198088860301845282518051808752885b818110156107d6578281018901518882018a015288016107bb565b818111156107e6578989838a0101525b50601f019091169490940185019350918401919084019060010161079f565b50919695505050505050565b6000602080830181845280855180835260408601915060408482028701019250838701855b8281101561089b57878503603f1901845281518051808752885b8181101561086b578281018901518882018a01528801610850565b8181111561087b578989838a0101525b50601f01601f191695909501860194509285019290850190600101610836565b5092979650505050505050565b9081526020019056fea2646970667358221220d34f78b25dd79fc104c2dba1d821708e6c016810166deacb4dad0887f3ccd74164736f6c63430007040033";

    public static final String FUNC_DELETESETTING = "deleteSetting";

    public static final String FUNC_GETSETTING = "getSetting";

    public static final String FUNC_GETSETTINGKEYS = "getSettingKeys";

    public static final String FUNC_GETUPDATESMADE = "getUpdatesMade";

    public static final String FUNC_UPDATESETTING = "updateSetting";

    @Deprecated
    protected AppSettings(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected AppSettings(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected AppSettings(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected AppSettings(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> deleteSetting(String key) {
        final Function function = new Function(
                FUNC_DELETESETTING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(key)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> getSetting(String key) {
        final Function function = new Function(FUNC_GETSETTING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(key)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<StaticArray2<Utf8String>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> getSettingKeys() {
        final Function function = new Function(FUNC_GETSETTINGKEYS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getUpdatesMade() {
        final Function function = new Function(FUNC_GETUPDATESMADE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> updateSetting(String key, String dataType, String value) {
        final Function function = new Function(
                FUNC_UPDATESETTING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(key), 
                new org.web3j.abi.datatypes.Utf8String(dataType), 
                new org.web3j.abi.datatypes.Utf8String(value)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static AppSettings load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new AppSettings(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static AppSettings load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AppSettings(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static AppSettings load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new AppSettings(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static AppSettings load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new AppSettings(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<AppSettings> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _name) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name)));
        return deployRemoteCall(AppSettings.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<AppSettings> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _name) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name)));
        return deployRemoteCall(AppSettings.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<AppSettings> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _name) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name)));
        return deployRemoteCall(AppSettings.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<AppSettings> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _name) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name)));
        return deployRemoteCall(AppSettings.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
