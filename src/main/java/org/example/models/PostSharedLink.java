package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.utils.AppConfig;
import org.example.utils.ConfigLoader;
import org.example.utils.DomainConfig;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSharedLink {
    String url;

    public static PostSharedLink generateLink( Integer postId) {
        String baseUrl = ConfigLoader
                .loadConfig("config.yml")
                .getDomainConfig()
                .getLocalDomain();

        return new PostSharedLink(baseUrl + "rest/posts?postId=" + postId);
    }
}
