package sample;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import static org.springframework.messaging.simp.SimpMessageType.*;
/*
 * WebSocket configuration.
 *
 * @Author Jay Sridhar
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer
{
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config)
    {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry)
    {
        registry.addEndpoint("/chat").setAllowedOrigins("*").withSockJS();
    }

    // TODO should be removed and add CSRF protection
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                //.nullDestMatcher().authenticated()
                //.simpSubscribeDestMatchers("/user/queue/errors").permitAll()
                //.simpDestMatchers("/app/**").hasRole("USER")
                //.simpSubscribeDestMatchers("/user/**", "/topic/friends/*").hasRole("USER")
                // TODO block MESSAGE in future
        			// TODO following does not seem to be effective. Check why
                .simpTypeMatchers(CONNECT, SUBSCRIBE, MESSAGE).authenticated()
                .simpSubscribeDestMatchers("/topic/**").denyAll()
                .simpSubscribeDestMatchers("/queue/**").denyAll()
                .anyMessage().denyAll();

    }
}
