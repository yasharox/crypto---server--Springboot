package com.yash.trading.service;

import com.yash.trading.model.Asset;
import com.yash.trading.model.Coin;
import com.yash.trading.model.User;

import java.util.List;

public interface AssetService {

    Asset createAsset (User user, Coin coin, double quantity);

    Asset getAssetById ( Long assetId) throws Exception;

    Asset getAssetByUserIdAndId(Long userId, Long assetId);

    List<Asset> getUsersAssets(Long userId);

    Asset updateAsset (Long assetId, double quantity) throws Exception;

    Asset findAssetByUserIdAndCoinId(Long userId, String coinId);

    void deleteAsset(Long assetId);


}
