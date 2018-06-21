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
package org.eclipse.che.selenium.site;

import static org.eclipse.che.selenium.pageobject.dashboard.NewWorkspace.Stack.JAVA;

import com.google.inject.Inject;
import org.eclipse.che.commons.lang.NameGenerator;
import org.eclipse.che.selenium.core.SeleniumWebDriver;
import org.eclipse.che.selenium.core.TestGroup;
import org.eclipse.che.selenium.core.client.TestWorkspaceServiceClient;
import org.eclipse.che.selenium.core.provider.TestDashboardUrlProvider;
import org.eclipse.che.selenium.core.user.DefaultTestUser;
import org.eclipse.che.selenium.pageobject.Ide;
import org.eclipse.che.selenium.pageobject.ToastLoader;
import org.eclipse.che.selenium.pageobject.dashboard.Dashboard;
import org.eclipse.che.selenium.pageobject.dashboard.NewWorkspace;
import org.eclipse.che.selenium.pageobject.dashboard.workspaces.Workspaces;
import org.eclipse.che.selenium.pageobject.ocp.AuthorizeOpenShiftAccessPage;
import org.eclipse.che.selenium.pageobject.ocp.OpenShiftLoginPage;
import org.eclipse.che.selenium.pageobject.site.CheLoginPage;
import org.eclipse.che.selenium.pageobject.site.FirstBrokerLoginPage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

@Test(groups = {TestGroup.OPENSHIFT, TestGroup.MULTIUSER})
public class LoginWithOpenShiftOAuthTest {

  private static final String WORKSPACE_NAME = NameGenerator.generate("workspace", 4);

  @Inject private CheLoginPage cheLoginPage;
  @Inject private OpenShiftLoginPage openShiftLoginPage;
  @Inject private AuthorizeOpenShiftAccessPage authorizeOpenShiftAccessPage;
  @Inject private SeleniumWebDriver seleniumWebDriver;
  @Inject private TestDashboardUrlProvider testDashboardUrlProvider;
  @Inject private DefaultTestUser defaultTestUser;
  @Inject private FirstBrokerLoginPage firstBrokerLoginPage;
  @Inject private Dashboard dashboard;
  @Inject private Workspaces workspaces;
  @Inject private NewWorkspace newWorkspace;
  @Inject private TestWorkspaceServiceClient workspaceServiceClient;
  @Inject private ToastLoader toastLoader;
  @Inject private Ide ide;

  @AfterClass
  private void removeTestWorkspace() throws Exception {
    workspaceServiceClient.delete(WORKSPACE_NAME, defaultTestUser.getName());
  }

  @Test
  public void checkCreationOfWorkspaceUnderOpenShiftAccount() {
    // go to login page
    seleniumWebDriver.get(testDashboardUrlProvider.get().toString());

    // click on button to login with OpenShift OAuth
    cheLoginPage.loginWithOpenShiftOAuth();

    // login at OCP login page with default test user credentials
    openShiftLoginPage.login(defaultTestUser.getName(), defaultTestUser.getPassword());

    // authorize ocp-client to access OpenShift account
    authorizeOpenShiftAccessPage.allowPermissions();

    // fill first broker login page
    firstBrokerLoginPage.submit(defaultTestUser);

    // create and open workspace of java type
    dashboard.selectWorkspacesItemOnDashboard();
    workspaces.clickOnAddWorkspaceBtn();
    newWorkspace.waitToolbar();
    newWorkspace.clickOnAllStacksTab();
    newWorkspace.selectStack(JAVA);
    newWorkspace.typeWorkspaceName(WORKSPACE_NAME);
    newWorkspace.clickOnCreateButtonAndOpenInIDE();

    // switch to the IDE and wait for workspace is ready to use
    toastLoader.waitToastLoaderAndClickStartButton();
    ide.waitOpenedWorkspaceIsReadyToUse();
  }
}
