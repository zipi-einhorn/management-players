package com.test.management.players.service;

import com.test.management.players.client.PlayersClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class PlayersService {

    @Autowired
    PlayersClient playersClient;

    CashService cashService = new CashService();

    public InputStreamResource getPlayersDetails(List<Integer> ids) throws URISyntaxException, IOException, InterruptedException {
        List<Map> listObject = new ArrayList<>();
        listObject.addAll(cashService.getFromCash(ids));
        List<Map> listFromTheClient = playersClient.players(cashService.GetNotInTheCashIds(ids));
        if (listFromTheClient == null || listFromTheClient.isEmpty()) {
            return null;
        }
        cashService.addToCashMap(listFromTheClient);
        listObject.addAll(listFromTheClient);
        return new InputStreamResource(CSVHelper.tutorialsToCSV(getDetailsPlayersWithUseCash(ids)));
   }

  public void pullsThePlayerDetailsEvery15minutes(Integer id) {
      Timer t = new Timer();
      t.schedule(new TimerTask() {
          @Override
          public void run() {
              try {
                  getDetailsPlayersWithUseCash(List.of(id));
                  System.out.println("Succeed");
              } catch (Exception e) {
                  System.out.println("not succeed" + e.getMessage());
              }
          }
      }, 0, 15 * 60 * 1000);
  }

  private List<Map> getDetailsPlayersWithUseCash(List<Integer> ids ) throws URISyntaxException, IOException, InterruptedException {
      List<Map> listObject = new ArrayList<>();
      listObject.addAll(cashService.getFromCash(ids));
      List<Map> listFromTheClient = playersClient.players(cashService.GetNotInTheCashIds(ids));
      cashService.addToCashMap(listFromTheClient);
      listObject.addAll(listFromTheClient);
      return listObject;
  }

  public boolean isUserInTheCash(Integer id) {
      return cashService.isUserInTheCash(id);
  }
}
