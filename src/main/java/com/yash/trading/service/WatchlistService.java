package com.yash.trading.service;

import com.yash.trading.model.Coin;
import com.yash.trading.model.User;
import com.yash.trading.model.Watchlist;

public interface WatchlistService {


    Watchlist findUserWatchlist(Long userId) throws Exception;

    Watchlist createWatchlist( User user);

    Watchlist findById(Long id) throws Exception;


    Coin addItemToWatchlist( Coin coin, User user) throws Exception;

}
