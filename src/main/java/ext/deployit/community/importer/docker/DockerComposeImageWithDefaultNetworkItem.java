/**
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS
 * FOR A PARTICULAR PURPOSE. THIS CODE AND INFORMATION ARE NOT SUPPORTED BY XEBIALABS.
 */
package ext.deployit.community.importer.docker;

import com.google.common.collect.Lists;
import com.xebialabs.deployit.plugin.api.udm.ConfigurationItem;

import java.util.Map;

public class DockerComposeImageWithDefaultNetworkItem extends DockerComposeImageItem {
    private final DockerComposeNetworkItem network;

    public DockerComposeImageWithDefaultNetworkItem(String name, Map properties, DockerComposeNetworkItem network) {
        super(name, properties);
        this.network = network;
    }

    @Override
    public ConfigurationItem toConfigurationItem(String id, RepositoryService service) {
        ConfigurationItem configurationItem = super.toConfigurationItem(id, service);
        configurationItem.setProperty("networks", Lists.newArrayList(this.network.getName()));
        return configurationItem;
    }
}
