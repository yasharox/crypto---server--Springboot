package com.yash.trading.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.trading.model.Coin;
import com.yash.trading.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = {"http://localhost:5173",
        "https://crypto-fullstack-apz6.vercel.app"}
)
@RestController
//@RequestMapping("/coins")
public class CoinController {

    @Autowired
    private CoinService coinService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/coins")
    ResponseEntity<List<Coin>> getCoinList (
            @RequestParam(
//                    required = false,
                    defaultValue = "1",
                    name ="page")
            int page) throws Exception {
        List<Coin> coins = coinService.getCoinList(page);
        return  new ResponseEntity<>(coins, HttpStatus.ACCEPTED);

    }

    @GetMapping("/coins/{coinId}/chart")
    ResponseEntity<JsonNode> getMarketChart (
            @PathVariable String coinId,
            @RequestParam("days") int days
            ) throws Exception {
        String res = coinService.getMarketChart(coinId, days);
        JsonNode jsonNode = objectMapper.readTree(res);
        return  new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);

    }

    @GetMapping("/coins/search")
    ResponseEntity<JsonNode> searchCoin(@RequestParam("q") String keyword) throws Exception {
        String coin=coinService.searchCoin(keyword);
        JsonNode jsonNode = objectMapper.readTree(coin);

        return ResponseEntity.ok(jsonNode);

    }
    @GetMapping("/coins/top50")
    ResponseEntity<JsonNode> getTop50CoinByMarketCapRank() throws Exception {
        String coin=coinService.getTop50CoinsByMarketCapRank();
        JsonNode jsonNode = objectMapper.readTree(coin);

        return ResponseEntity.ok(jsonNode);

    }

    @GetMapping("/coins/trading")
    ResponseEntity<JsonNode> getTradingCoins() throws Exception {
        String coin=coinService.getTradingCoins();
        JsonNode jsonNode = objectMapper.readTree(coin);
        return ResponseEntity.ok(jsonNode);

    }

    @GetMapping("/coins/details/{coinId}")
    ResponseEntity<JsonNode> getCoinDetails(@PathVariable String coinId) throws Exception {
        String coin = coinService.getCoinDetails(coinId);
        JsonNode jsonNode = objectMapper.readTree(coin);

        return ResponseEntity.ok(jsonNode);

    }

}
