package com.faforever.client.teammatchmaking;

import com.faforever.client.chat.CountryFlagService;
import com.faforever.client.chat.avatar.AvatarService;
import com.faforever.client.fx.Controller;
import com.faforever.client.util.IdenticonUtil;
import com.google.common.base.Strings;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PartyPlayerItemController implements Controller<Node> {

  private final CountryFlagService countryFlagService;
  private final AvatarService avatarService;

  @FXML
  public Node playerItemRoot;

  @FXML
  public ImageView userImageView;
  @FXML
  public ImageView avatarImageView;
  @FXML
  public ImageView countryImageView;
  @FXML
  public Label clanLabel;
  @FXML
  public Label usernameLabel;

  public PartyPlayerItemController(CountryFlagService countryFlagService, AvatarService avatarService) {
    this.countryFlagService = countryFlagService;
    this.avatarService = avatarService;
  }

  @Override
  public void initialize() {
    clanLabel.managedProperty().bind(clanLabel.visibleProperty());
  }

  @Override
  public Node getRoot() {
    return playerItemRoot;
  }

  void setPlayerItem(PartyPlayerItem item) {
    userImageView.setImage(IdenticonUtil.createIdenticon(item.getPlayer().getId()));

    //TODO: duplicate code
    setCountry(item.getPlayer().getCountry());

    setAvatar(item.getPlayer().getAvatarUrl(), item.getPlayer().avatarTooltipProperty());
    setClanTag(item.getPlayer().getClan());

    usernameLabel.setText(item.getPlayer().getUsername());
    //TODO: change Listeners?
  }

  private void setCountry(String country) {
    if (StringUtils.isEmpty(country)) {
      countryImageView.setVisible(false);
    } else {
      countryFlagService.loadCountryFlag(country).ifPresent(image -> {
        countryImageView.setImage(image);
        countryImageView.setVisible(true);
      });
    }
  }

  private void setAvatar(@Nullable String avatarUrl, StringProperty avatarTooltipProperty) {
    if (Strings.isNullOrEmpty(avatarUrl)) {
      avatarImageView.setVisible(false);
    } else {
      // Loading the avatar image asynchronously would be better but asynchronous list cell updates don't work well
      avatarImageView.setImage(avatarService.loadAvatar(avatarUrl));
      avatarImageView.setVisible(true);
    }
  }

  private void setClanTag(String clanTag) {
    if (Strings.isNullOrEmpty(clanTag)) {
      clanLabel.setVisible(false);
      return;
    }

    clanLabel.setText(String.format("[%s]", clanTag));
    clanLabel.setVisible(true);
  }
}
