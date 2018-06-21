/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.selenium.pageobject.site;

import static java.util.Arrays.asList;
import static org.eclipse.che.selenium.pageobject.site.CheLoginPage.Locators.LOGIN_BUTTON_NAME;
import static org.eclipse.che.selenium.pageobject.site.CheLoginPage.Locators.OPEN_SHIFT_OAUTH_LINK_ID;
import static org.eclipse.che.selenium.pageobject.site.CheLoginPage.Locators.PASSWORD_FIELD_NAME;
import static org.eclipse.che.selenium.pageobject.site.CheLoginPage.Locators.USERNAME_FIELD_NAME;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.selenium.core.SeleniumWebDriver;
import org.eclipse.che.selenium.core.webdriver.SeleniumWebDriverHelper;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/** @author Dmytro Nochevnov */
@Singleton
public class CheLoginPage implements LoginPage {
  private final SeleniumWebDriverHelper seleniumWebDriverHelper;

  protected interface Locators {
    String USERNAME_FIELD_NAME = "username";
    String PASSWORD_FIELD_NAME = "password";
    String LOGIN_BUTTON_NAME = "login";
    String OPEN_SHIFT_OAUTH_LINK_ID = "zocial-openshift-v3";
  }

  @FindBy(name = USERNAME_FIELD_NAME)
  private WebElement usernameInput;

  @FindBy(name = PASSWORD_FIELD_NAME)
  private WebElement passwordInput;

  @FindBy(name = LOGIN_BUTTON_NAME)
  private WebElement loginButton;

  @FindBy(id = OPEN_SHIFT_OAUTH_LINK_ID)
  private WebElement openShiftOAuthLink;

  @Inject
  public CheLoginPage(
      SeleniumWebDriver seleniumWebDriver, SeleniumWebDriverHelper seleniumWebDriverHelper) {
    this.seleniumWebDriverHelper = seleniumWebDriverHelper;

    PageFactory.initElements(seleniumWebDriver, this);
  }

  @Override
  public void login(String username, String password) {
    waitOnOpen();

    seleniumWebDriverHelper.setValue(usernameInput, username);
    seleniumWebDriverHelper.setValue(passwordInput, password);
    seleniumWebDriverHelper.waitAndClick(loginButton);

    waitOnClose();
  }

  public void loginWithOpenShiftOAuth() {
    waitOnOpen();
    seleniumWebDriverHelper.waitAndClick(openShiftOAuthLink);
  }

  @Override
  public boolean isOpened() {
    try {
      waitOnOpen();
    } catch (TimeoutException e) {
      return false;
    }

    return true;
  }

  private void waitOnOpen() {
    seleniumWebDriverHelper.waitAllVisibility(asList(usernameInput, passwordInput, loginButton));
  }

  private void waitOnClose() {
    seleniumWebDriverHelper.waitAllInvisibility(asList(usernameInput, passwordInput, loginButton));
  }
}
