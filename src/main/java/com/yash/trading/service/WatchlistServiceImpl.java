package com.yash.trading.service;

import com.yash.trading.model.Coin;
import com.yash.trading.model.User;
import com.yash.trading.model.Watchlist;
import com.yash.trading.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class WatchlistServiceImpl implements WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;


    @Override
    public Watchlist findUserWatchlist(Long userId) throws Exception {
//        Watchlist watchlist = watchlistRepository.findByUserId(userId);
//        if (watchlist == null){
//            throw new Exception("watchlist not found");
//
//             }
//        return watchlist;

        return watchlistRepository.findByUserId(userId);
    }

    @Override
    public Watchlist createWatchlist(User user) {

        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);


        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist findById(Long id) throws Exception {

        Optional<Watchlist> watchlistOptional= watchlistRepository.findById(id);
        if (watchlistOptional.isEmpty()){
            throw new Exception("watchlist not found");
        }
        return watchlistOptional.get();
    }


    @Override
    public Coin addItemToWatchlist(Coin coin,User user) throws Exception {
//        Watchlist watchlist=findUserWatchlist(user.getId());
//
//        if(watchlist.getCoins().contains(coin)){
//            watchlist.getCoins().remove(coin);
//        }
//        else watchlist.getCoins().add(coin);
//        watchlistRepository.save(watchlist);
//        return coin;

        // 🔴 CHANGED: safely fetch watchlist
        Watchlist watchlist = watchlistRepository.findByUserId(user.getId());

        // 🔴 CHANGED: auto-create watchlist if not present (VERY IMPORTANT)
        if (watchlist == null) {
            watchlist = new Watchlist();
            watchlist.setUser(user);
            watchlist = watchlistRepository.save(watchlist);
        }

        // Toggle add/remove coin
        if (watchlist.getCoins().contains(coin)) {
            watchlist.getCoins().remove(coin);
        } else {
            watchlist.getCoins().add(coin);
        }

        watchlistRepository.save(watchlist);
        return coin;
    }
}
