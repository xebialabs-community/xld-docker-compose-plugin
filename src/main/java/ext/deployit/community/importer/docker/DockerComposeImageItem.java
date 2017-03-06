/**
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS
 * FOR A PARTICULAR PURPOSE. THIS CODE AND INFORMATION ARE NOT SUPPORTED BY XEBIALABS.
 */
package ext.deployit.community.importer.docker;

import com.xebialabs.deployit.plugin.api.udm.ConfigurationItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static com.google.common.collect.Sets.newHashSet;

public class DockerComposeImageItem extends BaseDockerConfigurationItem {

    public DockerComposeImageItem(final String name, final Map properties) {
        super("docker.ContainerSpec", name, properties);
    }

    public String getImage() {
        return (String) properties.get("image");
    }

    public String getName() {
        return name;
    }

    public List<String> getPorts() {
        return (List<String>) (properties.containsKey("ports") ? properties.get("ports") : Collections.emptyList());

    }

    public List<String> getLinks() {
        return (List<String>) (properties.containsKey("links") ? properties.get("links") : Collections.emptyList());
    }

    public Map<String, String> getEnvironments() {
        if (!properties.containsKey("environment"))
            return Collections.emptyMap();

        if (properties.get("environment") instanceof Map) {
            Map environment = (Map) properties.get("environment");
            return environment;
        }
        if (properties.get("environment") instanceof List) {
            List<String> environment = (List) properties.get("environment");
            Map<String, String> env = new HashMap<>();
            for (String line : environment) {
                if (line.contains("=")) {
                    String[] split = line.split("=");
                    env.put(split[0].trim(), split[1].trim());
                }

            }
            return env;
        }

        throw new RuntimeException("Shoud not be there " + properties.get("environment"));
    }

    public List<String> getVolumes() {
        return (List<String>) (properties.containsKey("volumes") ? properties.get("volumes") : Collections.emptyList());

    }

    public List<String> getNetworks() {
        return (List<String>) (properties.containsKey("networks") ? properties.get("networks") : Collections.emptyList());
    }

    public String getType() {
        return type;
    }

    @Override
    public ConfigurationItem toConfigurationItem(final String id, final RepositoryService service) {
        ConfigurationItem ci = service.newCI(getType(), id);
        DockerComposeImageItem item = this;
        final String imageId = ci.getId();
        ci.setProperty("image", translateToPropertyPlaceholder(item.getImage()));
        ci.setProperty("command", translateToPropertyPlaceholder(item.getCommand()));
        ci.setProperty("portBindings", newArrayList(transform(item.getPorts(), s -> {
            final String id1 = String.format("%s/%s", imageId, toCiName(s));
            ConfigurationItem ci1 = service.newCI("docker.PortSpec", id1);
            ci1.setProperty("hostPort", translateToPropertyPlaceholder(s.split(":")[0]));
            ci1.setProperty("containerPort", translateToPropertyPlaceholder(s.split(":")[1]));
            return ci1;
        })));

        Map<String, String> links = new HashMap<String, String>();
        for (String s : item.getLinks()) {
            if (s.contains(":")) {
                links.put(s.split(":")[0].trim(), s.split(":")[1].trim());
            } else {
                links.put(s, s);
            }

        }
        ci.setProperty("links", links);

        Map<String,String> tmap = new HashMap<>();
        for (Map.Entry<String, String> e : item.getEnvironments().entrySet()) {
            tmap.put(e.getKey(), translateToPropertyPlaceholder(e.getValue()));
        }
        ci.setProperty("environment", tmap);

        ci.setProperty("volumeBindings", newHashSet(transform(item.getVolumes(), s -> {
            if (s.contains(":")) {
                final String[] split = s.split(":");
                String name = translateToPropertyPlaceholder(split[0].replace('/', '_'));
                final String id12 = String.format("%s/%s", imageId, name);
                ConfigurationItem ci12 = service.newCI("docker.MountedVolumeSpec", id12);
                ci12.setProperty("source", translateToPropertyPlaceholder(split[0]));
                ci12.setProperty("destination", translateToPropertyPlaceholder(split[1]));
                return ci12;
            } else {
                throw new RuntimeException("Cannot convert to VolumeSpec " + s);
            }
        })));

        return ci;

    }

    public String getCommand() {
        return (String) (properties.containsKey("command") ? properties.get("command") : "");
    }
}
