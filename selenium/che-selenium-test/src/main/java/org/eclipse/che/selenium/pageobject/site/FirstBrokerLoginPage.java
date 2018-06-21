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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.selenium.core.SeleniumWebDriver;
import org.eclipse.che.selenium.core.user.TestUser;
import org.eclipse.che.selenium.core.webdriver.SeleniumWebDriverHelper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/** @author Dmytro Nochevnov */
@Singleton
public class FirstBrokerLoginPage {

  private static final String FIRST_NAME = "first name";
  private static final String LAST_NAME = "last name";

  private interface Locators {
    String USERNAME_FIELD_ID = "username";
    String EMAIL_FIELD_ID = "email";
    String FIRST_NAME_FIELD_ID = "firstName";
    String LAST_NAME_FIELD_ID = "lastName";
    String SUBMIT_BUTTON_XPATH = "//input[@value='Submit']";
  }

  private SeleniumWebDriverHelper seleniumWebDriverHelper;

  @FindBy(id = Locators.USERNAME_FIELD_ID)
  private WebElement usernameField;

  @FindBy(id = Locators.EMAIL_FIELD_ID)
  private WebElement emailField;

  @FindBy(id = Locators.FIRST_NAME_FIELD_ID)
  private WebElement firstNameField;

  @FindBy(id = Locators.LAST_NAME_FIELD_ID)
  private WebElement lastNameField;

  @FindBy(xpath = Locators.SUBMIT_BUTTON_XPATH)
  private WebElement submitButton;

  @Inject
  public FirstBrokerLoginPage(
      SeleniumWebDriver seleniumWebDriver, SeleniumWebDriverHelper seleniumWebDriverHelper) {
    this.seleniumWebDriverHelper = seleniumWebDriverHelper;

    PageFactory.initElements(seleniumWebDriver, this);
  }

  public void submit(TestUser user) {
    waitOnOpen();

    seleniumWebDriverHelper.setValue(usernameField, user.getName());
    seleniumWebDriverHelper.setValue(emailField, user.getEmail());
    seleniumWebDriverHelper.setValue(firstNameField, FIRST_NAME);
    seleniumWebDriverHelper.setValue(lastNameField, LAST_NAME);

    seleniumWebDriverHelper.waitAndClick(submitButton);

    waitOnClose();
  }

  private void waitOnOpen() {
    seleniumWebDriverHelper.waitAllVisibility(
        asList(usernameField, emailField, lastNameField, submitButton));
  }

  private void waitOnClose() {
    seleniumWebDriverHelper.waitAllInvisibility(
        asList(usernameField, emailField, lastNameField, submitButton));
  }
}
