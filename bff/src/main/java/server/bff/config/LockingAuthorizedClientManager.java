package server.bff.config;

import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

public class LockingAuthorizedClientManager implements OAuth2AuthorizedClientManager {

    private final DefaultOAuth2AuthorizedClientManager delegate;
    private final ConcurrentMap<String, ReentrantLock> reentrantLockMap;

    public LockingAuthorizedClientManager(DefaultOAuth2AuthorizedClientManager delegate) {
        this.delegate = delegate;
        this.reentrantLockMap = new ConcurrentHashMap<>();
    }

    @Override
    public OAuth2AuthorizedClient authorize(OAuth2AuthorizeRequest authorizeRequest) {

        var client = delegate.authorize(authorizeRequest);
        if (client != null && isAboutToExpire(client)) {
            return client;
        }

        var clientId = authorizeRequest.getClientRegistrationId();
        var lock = reentrantLockMap.computeIfAbsent(clientId, k -> new ReentrantLock());
        lock.lock();
        try {
            return delegate.authorize(authorizeRequest);
        } finally {
            lock.unlock();
        }
    }

    private boolean isAboutToExpire(OAuth2AuthorizedClient client) {

        var exp = client.getAccessToken().getExpiresAt();
        return exp != null && Instant.now().isAfter(exp.minusSeconds(5));
    }
}
