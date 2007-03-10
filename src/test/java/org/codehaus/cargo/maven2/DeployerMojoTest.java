/*
 * ========================================================================
 *
 * Copyright 2006 Vincent Massol.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ========================================================================
 */
package org.codehaus.cargo.maven2;

import org.codehaus.cargo.container.stub.InstalledLocalContainerStub;
import org.codehaus.cargo.container.stub.InstalledLocalDeployerStub;
import org.codehaus.cargo.container.stub.RemoteContainerStub;
import org.codehaus.cargo.container.stub.RemoteDeployerStub;
import org.codehaus.cargo.container.deployer.DeployerType;
import org.codehaus.cargo.maven2.configuration.Deployer;

import java.net.URL;

import junit.framework.TestCase;

/**
 * Unit tests for {@link AbstractDeployerMojo}.
 *
 * @version $Id$
 */
public class DeployerMojoTest extends TestCase
{
    private class TestableDeployerMojo extends AbstractDeployerMojo
    {
        protected void performDeployerActionOnSingleDeployable(
            org.codehaus.cargo.container.deployer.Deployer deployer,
            org.codehaus.cargo.container.deployable.Deployable deployable, URL pingURL,
            Long pingTimeout)
        {
            // Do nothing voluntarily for testing
        }
    }

    public void testCreateDeployerWhenNoDeployerElementSpecified() throws Exception
    {
        TestableDeployerMojo mojo = new TestableDeployerMojo();
        mojo.getDeployerFactory().registerDeployer(RemoteContainerStub.ID, DeployerType.REMOTE,
            RemoteDeployerStub.class);

        org.codehaus.cargo.container.deployer.Deployer deployer = mojo.createDeployer(
            new RemoteContainerStub());

        assertEquals(RemoteDeployerStub.class.getName(), deployer.getClass().getName());
        assertEquals(DeployerType.REMOTE, deployer.getType());
    }

    public void testCreateDeployerWhenDeployerElementSpecified() throws Exception
    {
        TestableDeployerMojo mojo = new TestableDeployerMojo();

        Deployer deployerElement = new Deployer();
        deployerElement.setImplementation(InstalledLocalDeployerStub.class.getName());
        deployerElement.setType(DeployerType.INSTALLED.getType());
        mojo.setDeployerElement(deployerElement);

        org.codehaus.cargo.container.deployer.Deployer deployer = mojo.createDeployer(
            new InstalledLocalContainerStub());

        assertEquals(InstalledLocalDeployerStub.class.getName(), deployer.getClass().getName());
    }
}
