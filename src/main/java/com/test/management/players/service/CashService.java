package com.test.management.players.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashService {
    private static CashService cashService;
    private static HashMap<Integer, Map> cashMap = new HashMap<>();

    CashService() {
        cashMap = new HashMap<>();
    }

    void checkSizeCash() {
        if (cashMap.size() > 1000) {
            List<Integer> keys = cashMap.keySet().stream().toList();
            while(keys.size() > 700 ) {
                cashMap.remove(keys.get(0));
            }
        }
    }
    public void getInstance() {
        if (cashMap == null) {
            cashService = new CashService();
            System.out.println("cashMap is null");
        }
    }

    public void addToCashMap(List<Map> addToCash) {
        System.out.println("befor add to cash map" + cashMap);
        getInstance();
        checkSizeCash();
        addToCash.stream().filter(x -> x.containsKey("id")).map((x) -> cashMap.put((Integer)x.get("id") ,x)).toList();
        System.out.println( "after add to cash map" + cashMap);
    }

    public Boolean isUserInTheCash(Integer user) {
        getInstance();
        System.out.println("is user in the cash?" + cashMap.containsKey(user) + cashMap);
        return cashMap.containsKey(user);
    }

    public List<Integer> GetNotInTheCashIds(List<Integer> ids) {
        getInstance();
        System.out.println( "not user in the cash" + ids.stream().filter(x -> !cashMap.containsKey(x)).toList());
        return ids.stream().filter(x -> !cashMap.containsKey(x)).toList();
    }
    public List<Map> getFromCash(List<Integer> ids) {
        getInstance();
        System.out.println( "get ids from cash" + ids.stream().filter(
                x -> cashMap.containsKey(x)).map(x -> cashMap.get(x)).toList());
        return ids.stream().filter(x -> cashMap.containsKey(x)).map(x -> cashMap.get(x)).toList();
    }
}