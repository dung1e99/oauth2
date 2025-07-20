package server.bff.config;


import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2RefreshTokenGrantRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockingRefreshTokenResponseClient implements
        OAuth2AccessTokenResponseClient<OAuth2RefreshTokenGrantRequest> {

    private final OAuth2AccessTokenResponseClient<OAuth2RefreshTokenGrantRequest> delegate;
    private final ConcurrentMap<String, Lock> reentrantLockMap;

    public LockingRefreshTokenResponseClient(
            OAuth2AccessTokenResponseClient<OAuth2RefreshTokenGrantRequest> delegate) {
        this.delegate = delegate;
        this.reentrantLockMap = new ConcurrentHashMap<>();
    }

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2RefreshTokenGrantRequest request) {

        var clientId = request.getClientRegistration().getRegistrationId();
        var lock = reentrantLockMap.computeIfAbsent(clientId, k -> new ReentrantLock());
        lock.lock();
        try {
            return delegate.getTokenResponse(request);
        } finally {
            lock.unlock();
        }
    }
}
