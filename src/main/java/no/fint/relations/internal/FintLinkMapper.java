package no.fint.relations.internal;

import jakarta.annotation.PostConstruct;
import no.fint.model.relation.Relation;
import no.fint.relations.config.FintRelationsProps;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.hateoas.Link;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FintLinkMapper {

    @Autowired
    private Environment environment;

    @Autowired
    private FintRelationsProps props;

    @Qualifier("linkMapper")
    @Autowired(required = false)
    private Map<String, String> links = new HashMap<>();

    @Value("${server.context-path:}")
    private String contextPath;

    private StringSubstitutor strSubstitutor;

    @PostConstruct
    public void init() {
        Map<String, String> shortnameLinks = links.entrySet().stream().collect(Collectors.toMap(
                e -> Relation.createType(e.getKey()),
                Map.Entry::getValue
        ));

        Map<String, String> stringMap = new HashMap<>();
        stringMap.putAll(shortnameLinks);
        stringMap.putAll(links);
        strSubstitutor = new StringSubstitutor(stringMap);
    }

    public String getLink(String link) {
        if (link == null) {
            return null;
        }
        String defaultLink = getConfiguredLink();
        if (link.startsWith("${") && link.contains("}")) {
            link = link.replace("}", ":-%s}".formatted(defaultLink));
            link = strSubstitutor.replace(link);
        }
        if (link.startsWith("/")) {
            return defaultLink + link;
        }

        return populateProtocol(link);
    }

    private String getConfiguredLink() {
        return props.getRelationBase();
    }

    public Link populateProtocol(Link link) {
        String href = populateProtocol(link.getHref());
        return Link.of(href, link.getRel());
    }

    public String populateProtocol(String href) {
        if (Boolean.valueOf(props.getForceHttps())) {
            return href.replace("http://", "https://");
        } else {
            return href;
        }
    }

    public static String getName(Class<?> clazz) {
        return no.fint.model.resource.Link.getHrefPlaceholder(clazz);
    }

}
