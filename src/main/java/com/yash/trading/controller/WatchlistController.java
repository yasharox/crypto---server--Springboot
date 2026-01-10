package com.yash.trading.controller;

import com.yash.trading.model.Coin;
import com.yash.trading.model.User;
import com.yash.trading.model.Watchlist;
import com.yash.trading.service.CoinService;
import com.yash.trading.service.UserService;
import com.yash.trading.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/api/watchlist")
public class WatchlistController {

    @Autowired
    private  WatchlistService watchlistService;

    @Autowired
    private  UserService userService;

    @Autowired
    private CoinService coinService;


    @GetMapping("/user")
    public ResponseEntity<Watchlist> getUserWatchlist(
            @RequestHeader("Authorization") String jwt) throws Exception {

        if ( jwt.startsWith("Bearer ")){
            jwt =jwt.substring(7).trim();
        }

        User user=userService.findUserProfileByJwt(jwt);
        Watchlist watchlist = watchlistService.findUserWatchlist(user.getId());
        return ResponseEntity.ok(watchlist);

    }


    @GetMapping("/{watchlistId}")
    public ResponseEntity<Watchlist> getWatchlistById(
            @PathVariable Long watchlistId) throws Exception {

        Watchlist watchlist = watchlistService.findById(watchlistId);
        return ResponseEntity.ok(watchlist);

    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchlist(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String coinId) throws Exception {

        if ( jwt.startsWith("Bearer ")){
            jwt = jwt.substring(7).trim();
        }


        User user=userService.findUserProfileByJwt(jwt);
        Coin coin=coinService.findById(coinId);
        Coin addedCoin = watchlistService.addItemToWatchlist(coin, user);
        return ResponseEntity.ok(addedCoin);

    }
}


