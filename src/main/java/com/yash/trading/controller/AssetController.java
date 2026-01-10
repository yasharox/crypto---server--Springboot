package com.yash.trading.controller;

import com.yash.trading.model.Asset;
import com.yash.trading.model.User;
import com.yash.trading.service.AssetService;
import com.yash.trading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")   //  FIXED: Added base mapping to avoid "/" conflicts
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserService userService;

    @GetMapping("/{assetId}")    // Now maps to /api/assets/{assetId}
    public ResponseEntity<Asset> getAssetById(@PathVariable Long assetId) throws Exception {
        Asset asset = assetService.getAssetById(assetId);
        return ResponseEntity.ok().body(asset);
    }

    @GetMapping("/coin/{coinId}/user")  // /api/assets/coin/{coinId}/user
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(
            @PathVariable String coinId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        if ( !jwt.startsWith("Bearer ")){
            throw new RuntimeException("Invalid Authorization header");
        }

        String token = jwt.substring(7).trim();
        User user = userService.findUserProfileByJwt(token);
        Asset asset = assetService.findAssetByUserIdAndCoinId(user.getId(), coinId);
        return ResponseEntity.ok().body(asset);
    }



    @GetMapping()    // /api/assets
    public ResponseEntity<List<Asset>> getAssetsForUser(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        if ( !jwt.startsWith("Bearer ")){
            throw new RuntimeException("Invalid Authorization header");
        }

        String token = jwt.substring(7).trim();
        User user = userService.findUserProfileByJwt(token);
        List<Asset> assets = assetService.getUsersAssets(user.getId());
        return ResponseEntity.ok().body(assets);
    }
}
