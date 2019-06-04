package com.faforever.client.teammatchmaking;

import com.faforever.client.config.ClientProperties;
import com.faforever.client.fx.AbstractViewController;
import com.faforever.client.fx.JavaFxUtil;
import com.faforever.client.game.GameService;
import com.faforever.client.i18n.I18n;
import com.faforever.client.leaderboard.LeaderboardService;
import com.faforever.client.main.event.ShowLadderMapsEvent;
import com.faforever.client.player.PlayerService;
import com.faforever.client.preferences.PreferencesService;
import com.faforever.client.preferences.event.MissingGamePathEvent;
import com.faforever.client.theme.UiService;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.eventbus.EventBus;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.lang.invoke.MethodHandles;

@Component
@Lazy
public class TeamMatchmakingController extends AbstractViewController<Node> {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final GameService gameService;
  private final PreferencesService preferencesService;
  private final PlayerService playerService;
  private final LeaderboardService leaderboardService;
  private final I18n i18n;
  private final ClientProperties clientProperties;
  private final UiService uiService;
  private EventBus eventBus;

  @FXML
  public GridPane teamMatchmakingRoot;
  @FXML
  public JFXListView playerListView;

  private ObservableList<PartyPlayerItem> playerItems;

  @Inject
  public TeamMatchmakingController(GameService gameService,
                                   PreferencesService preferencesService,
                                   PlayerService playerService,
                                   LeaderboardService leaderboardService,
                                   I18n i18n, ClientProperties clientProperties,
                                   UiService uiService, EventBus eventBus) {
    this.gameService = gameService;
    this.preferencesService = preferencesService;
    this.playerService = playerService;
    this.leaderboardService = leaderboardService;
    this.i18n = i18n;
    this.clientProperties = clientProperties;
    this.uiService = uiService;
    this.eventBus = eventBus;

    playerItems = FXCollections.observableArrayList();


  }

  @Override
  public void initialize() {
    super.initialize();

    //TODO
    setSearching(false);
    JavaFxUtil.addListener(gameService.searching1v1Property(), (observable, oldValue, newValue) -> setSearching(newValue));

    playerItems.add(new PartyPlayerItem(playerService.getCurrentPlayer().get()));
    playerItems.add(new PartyPlayerItem(playerService.getPlayerForUsername(playerService.getPlayerNames().stream().findAny().get()).get()));
    playerItems.add(new PartyPlayerItem(playerService.getPlayerForUsername(playerService.getPlayerNames().stream().findAny().get()).get()));
    playerItems.add(new PartyPlayerItem(playerService.getPlayerForUsername(playerService.getPlayerNames().stream().findAny().get()).get()));

    playerListView.setCellFactory(listView -> new PartyPlayerItemListCell(uiService));
    playerListView.setItems(playerItems);
  }

  @VisibleForTesting
  void setSearching(boolean searching) {

  }

  @Override
  public Node getRoot() {
    return teamMatchmakingRoot;
  }

  public void showMatchmakingMaps(ActionEvent actionEvent) {
    eventBus.post(new ShowLadderMapsEvent());//TODO show team matchmaking maps and not ladder maps
  }

  public void onEnterQueueButtonClicked(ActionEvent actionEvent) {
    //TODO
    if (preferencesService.getPreferences().getForgedAlliance().getPath() == null) {
      //TODO: check on party join
      eventBus.post(new MissingGamePathEvent(true));
      return;
    }
  }

  public void onLeavePartyButtonClicked(ActionEvent actionEvent) {
  }
}
