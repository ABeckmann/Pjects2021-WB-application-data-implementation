pragma solidity ^0.7.4;
pragma experimental ABIEncoderV2;

// Smart contract storing applicaton's setting data
contract AppSettings {
    struct Setting {
        string dataType;
        string value;
    }

    string name;
    string[] keyList;
    uint256 updatesMade;
    mapping(string => Setting) settings;

    constructor(string memory _name) {
        name = _name;
        updatesMade = 0;
    }

    modifier incrementUpdates() {
        updatesMade++;
        _;
    }

    function updateSetting(
        string calldata key,
        string calldata dataType,
        string calldata value
    ) public incrementUpdates {
        settings[key] = Setting(dataType, value);
        keyList.push(key);
    }

    function deleteSetting(string calldata key) public incrementUpdates {
        settings[key].dataType = "null";
        delete settings[key].value;
    }

    function getSetting(string calldata key)
        public
        view
        returns (string[2] memory)
    {
        return [settings[key].dataType, settings[key].value];
    }

    function getSettingKeys() public view returns (string[] memory) {
        return keyList;
    }

    function getUpdatesMade() public view returns (uint256) {
        return updatesMade;
    }
}
